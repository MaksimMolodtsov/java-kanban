package Tests.Tasks;

import Statuses.Status;
import Tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void beforeEach() {
        epic1 = new Epic("Эпик №1", "Съездить в отпуск", Status.NEW, 2);
        epic2 = new Epic("Эпик №1", "Съездить в отпуск", Status.NEW, 2);
    }

    @Test
    void shouldReturnFalseWhenAddiEpicAsSubtaskToItself() {
        int subtasksSize = epic1.getSubtasksIds().size();
        epic1.addSubtask(2);
        Assertions.assertEquals(subtasksSize, epic1.getSubtasksIds().size());
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Assertions.assertEquals(epic1, epic2);
    }

}