package tests.managers;

import managers.FileBackedTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.Status;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void loadFromFile() throws IOException {

        File newFile = File.createTempFile("text", ".temp");

        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(newFile);

        Task task1 = new Task("Задача №1", "Поспать", Status.IN_PROGRESS);
        task1.setStartTime(LocalDateTime.of(2025,2,10,10,5));
        task1.setDuration(Duration.ofHours(2));
        backedTaskManager.addTask(task1);
        Task task2 = new Task("Задача №2", "Еще поспать", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2025,2,10,16,0));
        task2.setDuration(Duration.ofHours(1));
        backedTaskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        backedTaskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, epic1.getId());
        subtask11.setStartTime(LocalDateTime.of(2025,2,11,12,0));
        subtask11.setDuration(Duration.ofHours(3));
        backedTaskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, epic1.getId());
        subtask12.setStartTime(LocalDateTime.of(2025,2,11,15,0));
        subtask12.setDuration(Duration.ofHours(1));
        backedTaskManager.addSubtask(subtask12);

        Epic epic2 = new Epic("Эпик №2", "Подумать о смысле жизни");
        backedTaskManager.addEpic(epic2);
        Subtask subtask21 = new Subtask("Подзадача №1", "Думать", Status.IN_PROGRESS, epic2.getId());
        subtask21.setStartTime(LocalDateTime.of(2025,2,12,12,50));
        subtask21.setDuration(Duration.ofHours(10));
        backedTaskManager.addSubtask(subtask21);

        TaskManager fileBackedManager = FileBackedTaskManager.loadFromFile(newFile);

        assertEquals("Задача №2", fileBackedManager.getTaskById(2).getTitle());
        assertEquals("Эпик №1", fileBackedManager.getEpicById(3).getTitle());
        assertEquals("Подзадача №1", fileBackedManager.getSubtaskById(4).getTitle());
    }

}