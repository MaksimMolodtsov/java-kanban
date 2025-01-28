package tests.managers;

import managers.Managers;
import utils.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach

    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTaskAndFindById() {
        final Task task = taskManager.addTask(new Task("Задача №1", "Описание", Status.NEW));
        final Task savedTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicAndSubtasksAndFindById() {
        final Epic epic = taskManager.addEpic(new Epic("Эпик №1", "Описание"));
        final Subtask epicSubtask1 = taskManager.addSubtask(new Subtask("Подзадача №1",
                "Описание", Status.NEW, epic.getId()));
        final Subtask epicSubtask2 = taskManager.addSubtask(new Subtask("Подзадача №2",
                "Описание 2", Status.NEW, epic.getId()));
        final Subtask epicSubtask3 = taskManager.addSubtask(new Subtask("Подзадача №2",
                "Описание 3", Status.NEW, epic.getId()));
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

    @Test
    public void updateTaskShouldReturnTaskWithTheSameId() {
        final Task expected = new Task("Задача №1", "Описание", Status.NEW);
        taskManager.addTask(expected);
        final Task updatedTask = new Task("Задача №1", "Описание", expected.getId(), Status.NEW);
        final Task actual = taskManager.updateTask(updatedTask);
        Assertions.assertEquals(expected, actual, "Вернулась задачи с другим id");
    }

    @Test
    public void updateEpicShouldReturnEpicWithTheSameId() {
        final Epic expected = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(expected);
        final Epic updatedEpic = new Epic("Эпик №1", "Описание", Status.DONE, expected.getId());
        final Epic actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual, "Вернулся эпик с другим id");
    }

    @Test
    public void deleteTasksShouldReturnEmptyList() {
        taskManager.addTask(new Task("Задача №1", "Описание", Status.NEW));
        taskManager.addTask(new Task("Задача №2", "Описание 2", Status.NEW));
        taskManager.deleteTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "После удаления задач список должен быть пуст.");
    }

    @Test
    public void deleteEpicsShouldReturnEmptyList() {
        taskManager.addEpic(new Epic("Эпик №1", "Описание"));
        taskManager.deleteEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "После удаления эпиков список должен быть пуст.");
    }

    @Test
    public void deleteSubtasksShouldReturnEmptyList() {
        Epic epic = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(epic);
        taskManager.addSubtask(new Subtask("Подзадача №1", "Описание", Status.NEW, epic.getId()));
        taskManager.addSubtask(new Subtask("Подзадача №1", "Описание", Status.NEW, epic.getId()));
        taskManager.addSubtask(new Subtask("Подзадача №1", "Описание", Status.NEW, epic.getId()));

        taskManager.deleteSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "После удаления подзадач список должен быть пуст.");
    }

    @Test
    void createdAndAddedTaskShouldHaveSameVariables() {
        Task expected = new Task("Задача №1", "Описание", 1, Status.DONE);
        taskManager.addTask(expected);
        List<Task> list = taskManager.getTasks();
        Task actual = list.getFirst();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
    }

}