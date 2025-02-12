package managers;

import exceptions.ManagerCrossingTimeException;
import utils.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Comparator<Task> comparator = (t1, t2) -> {
        if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return -1;
        } else if (t1.getStartTime().isAfter(t2.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    };
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator);
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    // TASK
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        if (crossingInTime(task)) {
            throw new ManagerCrossingTimeException("Наложение по времени с другой задачей.");
        } else {
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            prioritizedTasks.remove(oldTask);
            tasks.put(task.getId(), task);
            if (crossingInTime(task)) {
                throw new ManagerCrossingTimeException("Наложение по времени с другой задачей.");
            } else {
                prioritizedTasks.add(task);
            }
        }
        return task;
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    // EPIC
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpics() {
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epic.setStartTime(LocalDateTime.MIN);
        epic.setDuration(Duration.ZERO);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            setSubtasksIdsOfOldEpic(epic, oldEpic);
            updateStatus(epic);
            epics.put(epic.getId(), epic);
        }
        return epic;
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtasksIds()) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    //SUBTASK
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                historyManager.remove(subtaskId);
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
            }
            epic.getSubtasksIds().clear();
            updateStatus(epic);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtasksIds(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        if (crossingInTime(subtask)) {
            throw new ManagerCrossingTimeException("Наложение по времени с другой задачей.");
        } else {
            prioritizedTasks.add(subtask);
        }
        updateStatus(epic);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            Epic epic = epics.get(subtask.getEpicId());
            subtask.setEpicId(oldSubtask.getEpicId());
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.remove(oldSubtask);
            if (crossingInTime(subtask)) {
                throw new ManagerCrossingTimeException("Наложение по времени с другой задачей.");
            } else {
                prioritizedTasks.add(subtask);
            }
            updateStatus(epic);
        }
        return subtask;
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksIds().remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(subtask);
            subtasks.remove(id);
            updateStatus(epic);
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Subtask> newSubtasks = new ArrayList<>();
            for (int i = 0; i < epic.getSubtasksIds().size(); i++) {
                newSubtasks.add(subtasks.get(epic.getSubtasksIds().get(i)));
            }
            return newSubtasks;
        }
        return null;
    }

    //
    private void updateStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        setDurationOfEpic(epic);
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer epicSubtask : epic.getSubtasksIds()) {
            Status status = subtasks.get(epicSubtask).getStatus();
            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }

        if (isNew) {
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void setDurationOfEpic(Epic epic) { //Вычисление длительности происходит при обновлении статуса
        List<Subtask> subtasks = getSubtasksOfEpic(epic.getId());
        Duration durationOfEpic = Duration.ofDays(0);
        LocalDateTime startTimeOfEpic = LocalDateTime.MAX;
        LocalDateTime endTimeOfEpic = LocalDateTime.MIN;

        if (subtasks.isEmpty()) {
            epic.setStartTime(LocalDateTime.MIN);
            epic.setEndTime(LocalDateTime.MAX);
        } else {
            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime().isBefore(startTimeOfEpic)) {
                    startTimeOfEpic = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTimeOfEpic)) {
                    endTimeOfEpic = subtask.getEndTime();
                }
            }
            epic.setStartTime(startTimeOfEpic);
            epic.setEndTime(endTimeOfEpic);
            durationOfEpic = Duration.between(startTimeOfEpic, endTimeOfEpic);
        }
        epic.setDuration(durationOfEpic);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(prioritizedTasks);
    }

    private boolean crossingInTime(Task task) {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        if (!prioritizedTasks.isEmpty()) {
            for (Task priorityTask : prioritizedTasks) {
                if (task.getEndTime().isBefore(priorityTask.getStartTime()) ||
                        task.getStartTime().isAfter(priorityTask.getEndTime())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void setSubtasksIdsOfOldEpic(Epic newEpic, Epic oldEpic) {
        List<Integer> oldEpicSubtasksIds = oldEpic.getSubtasksIds();
        for (Integer id : oldEpicSubtasksIds) {
            newEpic.setSubtasksIds(id);
        }
    }

}