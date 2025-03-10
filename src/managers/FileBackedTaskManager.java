package managers;

import exceptions.ManagerReadException;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.Status;
import utils.TypeOfTasks;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic,duration,startTime\n");
            List<String> allTasks = new ArrayList<>();

            List<Task> tasks = super.getTasks();
            for (Task task: tasks) {
                String taskToString = task.toStringForFile();
                allTasks.add(taskToString);
            }

            List<Epic> epics = super.getEpics();
            for (Epic epic: epics) {
                String epicToString = epic.toStringForFile();
                allTasks.add(epicToString);
            }

            List<Subtask> subtasks = super.getSubtasks();
            for (Subtask subtask: subtasks) {
                String subtaskToString = subtask.toStringForFile();
                allTasks.add(subtaskToString);
            }

            for (String s: allTasks) {
                fileWriter.write(String.format("%s\n", s));
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Невозможно записать файл.");
        }
    }

    private static Task fromString(String value) {
        String[] taskToArray = value.split(",");
        int id = Integer.parseInt(taskToArray[0]);
        TypeOfTasks type = TypeOfTasks.valueOf(taskToArray[1]);
        String title = taskToArray[2];
        Status status = Status.valueOf(taskToArray[3]);
        String description = taskToArray[4];
        String epicId = "";
        if (taskToArray.length > 5) {
            epicId = taskToArray[5];
        }
        Duration duration = Duration.ofMinutes(Integer.parseInt(taskToArray[6]));
        LocalDateTime startTime = LocalDateTime.parse(taskToArray[7], FORMATTER);

        switch (type) {
            case TASK:
                return new Task(title, description, id, status, duration, startTime);
            case EPIC:
                return new Epic(title, description, status, id, duration, startTime);
            case SUBTASK:
                return new Subtask(title, description, id, status, Integer.parseInt(epicId), duration, startTime);
            default:
                throw new IllegalArgumentException("Нулевой тип.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader((file)))) {
            String string;
            while (reader.ready()) {
                string = reader.readLine();
                if (string.isBlank()) {
                    continue;
                }
                if (string.contains("id")) {
                    continue;
                }
                Task task = fromString(string);
                if (task.getType().equals(TypeOfTasks.EPIC)) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task.getType().equals(TypeOfTasks.SUBTASK)) {
                    manager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerReadException("Невозможно прочитать файл.");
        }
        return manager;
    }

}
