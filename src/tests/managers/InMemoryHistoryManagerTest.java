package tests.managers;

import managers.TaskManager;
import managers.Managers;
import utils.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryShouldReturnListOfUniqueTasksIfDeleteOneOfThem() {

        for (int i = 1; i < 11; i++) {
            taskManager.addTask(new Task("Задача №" + i, "Поспать", 1, Status.NEW,
                    Duration.ofHours(1), LocalDateTime.of(2026,i,10,10,5)));
        }

        List<Task> tasks = taskManager.getTasks();
        for (int i = 0; i < 10; i++) {
            taskManager.getTaskById(tasks.get(i).getId());
        }

        for (int i = 0; i < 6; i++) {
            taskManager.deleteTaskById(tasks.get(i).getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(4, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryShouldReturnListOfUniqueTasks() {

        taskManager.addTask(new Task("Задача №1", "Поспать", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.of(2026,1,10,10,5)));
        taskManager.addTask(new Task("Задача №2", "Поспать", 2, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2026,2,10,10,5)));

        List<Task> tasks = taskManager.getTasks();
        for (int i = 0; i < 10; i++) {
            taskManager.getTaskById(tasks.get(0).getId());
            taskManager.getTaskById(tasks.get(1).getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(2, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task = new Task("Задача №1", "Описание", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.of(2026,1,10,10,5));
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.updateTask(new Task("Обновление Задачи №1",
                "Обновление описания", task.getId(), Status.IN_PROGRESS, Duration.ofHours(1),
                LocalDateTime.of(2026,1,10,10,5)));
        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        Assertions.assertEquals(task.getTitle(), oldTask.getTitle(), "В истории не сохранилась " +
                "старая версия задачи");
        Assertions.assertEquals(task.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.updateEpic(new Epic("Новый Эпик", "Новое описание", Status.IN_PROGRESS,
                epic.getId()));
        List<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        Assertions.assertEquals(epic.getTitle(), oldEpic.getTitle(),
                "В истории не сохранилась старая версия эпика");
        Assertions.assertEquals(epic.getDescription(), oldEpic.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic epic = new Epic("Эпик №1", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача №1", "Описание", Status.NEW, epic.getId());
        subtask.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(subtask);
        taskManager.getSubtaskById(subtask.getId());
        taskManager.updateSubtask(new Subtask("Новая подзадача", "Новое описание", Status.IN_PROGRESS,
                epic.getId()));
        List<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        Assertions.assertEquals(subtask.getTitle(), oldSubtask.getTitle(),
                "В истории не сохранилась старая версия эпика");
        Assertions.assertEquals(subtask.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnEmptyListAfterDeleteAllTasks() {

        taskManager.addTask(new Task("Задача №1", "Поспать", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.of(2026,1,10,10,5)));
        taskManager.addTask(new Task("Задача №2", "Поспать", 2, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2026,2,10,10,5)));

        List<Task> tasks = taskManager.getTasks();
        for (int i = 0; i < 10; i++) {
            taskManager.getTaskById(tasks.get(0).getId());
            taskManager.getTaskById(tasks.get(1).getId());
        }

        taskManager.deleteTasks();

        List<Task> list = taskManager.getHistory();
        assertEquals(0, list.size(), "Неверное количество элементов в истории ");
    }

}
