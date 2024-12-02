import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    final private HashMap<Integer, Task> tasks = new HashMap<>();
    final private HashMap<Integer, Epic> epics = new HashMap<>();
    final private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    // TASK
    public ArrayList<Task> getTasks(){
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    public void deleteTasks(){
        tasks.clear();
    }

    public Task getTaskById(int id){
        return tasks.get(id);
    }

    public void addTask(Task task){
        task.setId(nextId++);
        tasks.put (task.getId(), task);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(Integer id){
        tasks.remove(id);
    }

    // EPIC
    public ArrayList<Epic> getEpics(){
        ArrayList<Epic> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }

    public void deleteEpics(){
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public void addEpic(Epic epic){
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epics.put (epic.getId(), epic);
    }

    public void updateEpic(Epic epic){
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatus(epic);
        }
    }

    public void deleteEpicById(Integer id){
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    //SUBTASK
    public ArrayList<Subtask> getSubtasks(){
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
        return allSubtasks;
    }

    public void deleteSubtasks(){
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
            epic.getSubtasksIds().clear();
            updateStatus(epic);
        }
    }

    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtasksIds(subtask.getId());
        updateStatus(epic);
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        updateStatus(epic);
    }

    public void deleteSubtaskById (int id){
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(id);
            epic.getSubtasksIds().remove(id);
            updateStatus(epic);
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpic (int id){
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

    private void updateStatus (Epic epic){
        boolean isNew = true;
        boolean isDone = true;
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
}