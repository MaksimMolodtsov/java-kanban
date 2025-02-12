package tests.tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import utils.Status;
import tasks.Epic;
import tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    private static TaskManager taskManager;

    @BeforeEach

    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void getEpicIdFromSubtask() {
        Subtask subtask2 = new Subtask("Подзадача №1", "Идем спать", Status.NEW, 2);
        Assertions.assertEquals(2, subtask2.getEpicId());
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик №1", "Поспать");
        taskManager.addEpic(epic1);
        final Subtask epicSubtask1 = new Subtask("Подзадача №1",
                "Описание", Status.NEW, epic1.getId());
        epicSubtask1.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        epicSubtask1.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask1);
        final Subtask epicSubtask2 = new Subtask("Подзадача №2",
                "Описание 2", Status.NEW, epic1.getId());
        epicSubtask2.setStartTime(LocalDateTime.of(2025, 2, 11, 18, 0));
        epicSubtask2.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask2);
        Assertions.assertEquals(epicSubtask1.getEpicId(), epicSubtask2.getEpicId());
    }

    @Test
    void quantityOfSubtasksShouldBeEqualTheSizeOfListOfSubtasksIds() {
        Epic epic1 = new Epic("Эпик №1", "Поспать");
        taskManager.addEpic(epic1);
        final Subtask epicSubtask1 = new Subtask("Подзадача №1",
                "Описание", Status.NEW, epic1.getId());
        epicSubtask1.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        epicSubtask1.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask1);
        final Subtask epicSubtask2 = new Subtask("Подзадача №2",
                "Описание 2", Status.NEW, epic1.getId());
        epicSubtask2.setStartTime(LocalDateTime.of(2025, 2, 11, 18, 0));
        epicSubtask2.setDuration(Duration.ofHours(1));
        taskManager.addSubtask(epicSubtask2);
        Assertions.assertEquals(2, epic1.getSubtasksIds().size());
    }

}