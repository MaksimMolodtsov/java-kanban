package tests.managers;

import managers.FileBackedTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.Status;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @BeforeEach
    public void beforeEach() throws IOException {

        File newFile = File.createTempFile("text", ".temp", new File("/Users/Maksim"));

        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(newFile);

        Task task1 = new Task("Задача №1", "Поспать", Status.IN_PROGRESS);
        backedTaskManager.addTask(task1);
        Task task2 = new Task("Задача №2", "Еще поспать", Status.NEW);
        backedTaskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        backedTaskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, epic1.getId());
        backedTaskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, epic1.getId());
        backedTaskManager.addSubtask(subtask12);

        Epic epic2 = new Epic("Эпик №2", "Подумать о смысле жизни");
        backedTaskManager.addEpic(epic2);
        Subtask subtask21 = new Subtask("Подзадача №1", "Думать", Status.IN_PROGRESS, epic2.getId());
        backedTaskManager.addSubtask(subtask21);

    }

    @Test
    void loadFromFile() {

        TaskManager fileBackedManager = FileBackedTaskManager.loadFromFile(new File("/Users/Maksim/text.temp"));

        assertEquals("Задача №2", fileBackedManager.getTaskById(2).getTitle());
        assertEquals("Эпик №1", fileBackedManager.getEpicById(3).getTitle());
        assertEquals("Подзадача №1", fileBackedManager.getSubtaskById(4).getTitle());
    }
}