package tests.managers;

import exceptions.ManagerCrossingTimeException;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import tasks.Task;
import tasks.Epic;
import tasks.Subtask;
import utils.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    void addNewTaskAndFindById() {
        final Task task = taskManager.addTask(new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,1,10,10,5)));
        final Task savedTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    void addNewEpicAndSubtasksAndFindById() {
        final Epic epic = taskManager.addEpic(new Epic("Эпик №1", "Описание"));
        final Subtask epicSubtask1 = new Subtask("Подзадача №1",
                "Описание", Status.NEW, epic.getId());
        epicSubtask1.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        epicSubtask1.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask1);
        final Subtask epicSubtask2 = new Subtask("Подзадача №2",
                "Описание 2", Status.NEW, epic.getId());
        epicSubtask2.setStartTime(LocalDateTime.of(2025, 2, 11, 18, 0));
        epicSubtask2.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask2);
        final Subtask epicSubtask3 = new Subtask("Подзадача №3",
                "Описание 3", Status.NEW, epic.getId());
        epicSubtask3.setStartTime(LocalDateTime.of(2025, 2, 11, 18, 0));
        epicSubtask3.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask3);
        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskById(epicSubtask1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskById(epicSubtask2.getId());
        final Subtask savedSubtask3 = taskManager.getSubtaskById(epicSubtask3.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(epicSubtask1, savedSubtask1, "Подзадачи не совпадают.");
        assertEquals(epicSubtask3, savedSubtask3, "Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    public void updateTaskShouldReturnTaskWithTheSameId() {
        final Task expected = new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,1,10,10,5));
        taskManager.addTask(expected);
        final Task updatedTask = new Task("Задача №1", "Описание 2", expected.getId(), Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,1,10,10,5));
        final Task actual = taskManager.updateTask(updatedTask);
        Assertions.assertEquals(expected.getId(), actual.getId(), "Вернулась задачи с другим id");
    }

    public void updateEpicShouldReturnEpicWithTheSameId() {
        final Epic expected = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(expected);
        final Epic updatedEpic = new Epic("Эпик №1", "Описание", Status.DONE, expected.getId());
        final Epic actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected.getId(), actual.getId(), "Вернулся эпик с другим id");
    }

    public void deleteTasksShouldReturnEmptyList() {
        taskManager.addTask(new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,1,10,10,5)));
        taskManager.addTask(new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,2,10,10,5)));
        taskManager.deleteTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "После удаления задач список должен быть пуст.");
    }

    public void deleteEpicsShouldReturnEmptyList() {
        taskManager.addEpic(new Epic("Эпик №1", "Описание"));
        taskManager.deleteEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "После удаления эпиков список должен быть пуст.");
    }

    public void deleteSubtasksShouldReturnEmptyList() {
        Epic epic = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(epic);
        final Subtask epicSubtask1 = new Subtask("Подзадача №1", "Описание", Status.NEW, epic.getId());
        epicSubtask1.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        epicSubtask1.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask1);
        final Subtask epicSubtask2 = new Subtask("Подзадача №2", "Описание 2", Status.NEW, epic.getId());
        epicSubtask2.setStartTime(LocalDateTime.of(2025, 3, 11, 16, 0));
        epicSubtask2.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask2);

        taskManager.deleteSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "После удаления подзадач список должен быть пуст.");
    }

    void createdAndAddedTaskShouldHaveSameVariables() {
        Task expected = new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026,1,10,10,5));
        taskManager.addTask(expected);
        List<Task> list = taskManager.getTasks();
        Task actual = list.getFirst();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
    }

    void tasksShouldBeAddWithoutCrossingInTime() {
        final Task task1 = new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2025,1,10,10,5));
        final Task task2 = new Task("Задача №2", "Описание 2", 2, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2025,1,10,13,0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assertions.assertEquals(2, taskManager.getTasks().size());
    }

    void catchExceptionWhenTasksCrossingInTime() {
        final Task task1 = new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2025,1,10,10,5));
        final Task task2 = new Task("Задача №2", "Описание 2", 2, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2025,1,10,10,10));
        taskManager.addTask(task1);
        assertThrows(ManagerCrossingTimeException.class, () -> {
            taskManager.addTask(task2);} , "Наложение по времени с другой задачей.");
    }

}