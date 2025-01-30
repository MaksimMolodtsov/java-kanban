package tests.tasks;

import utils.Status;
import tasks.Epic;
import tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void getEpicIdFromSubtask() {
        Subtask subtask2 = new Subtask("Подзадача №1", "Идем спать", Status.NEW, 2);
        Assertions.assertEquals(2, subtask2.getEpicId());
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик №1", "Поспать");
        Subtask subtask1 = new Subtask("Подзадача №1", "Идем спать", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача №1", "Идем спать", Status.NEW, epic1.getId());
        Assertions.assertEquals(subtask1, subtask2);
    }

}