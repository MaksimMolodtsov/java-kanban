package tests.tasks;

import managers.Managers;
import managers.TaskManager;
import tasks.Subtask;
import utils.Status;
import tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    private Epic epic1;
    private Epic epic2;
    private Epic epic3;
    private Epic epic4;
    private Epic epic5;
    private Epic epic6;

    @BeforeEach
    void beforeEach() {

        TaskManager manager = Managers.getDefault();

        epic1 = new Epic("Эпик №1", "Тест");
        manager.addEpic(epic1);
        epic2 = new Epic("Эпик №2", "Тест", Status.NEW, 2);
        epic3 = new Epic("Эпик №3", "Тест", Status.NEW, 2);
        epic4 = new Epic("Эпик №4", "Тест");
        manager.addEpic(epic4);
        epic5 = new Epic("Эпик №5", "Тест");
        manager.addEpic(epic5);
        epic6 = new Epic("Эпик №6", "Тест");
        manager.addEpic(epic6);

        Subtask subtask11 = new Subtask("Подзадача №1", "Тест", Status.NEW, epic1.getId());
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача №2", "Тест", Status.NEW, epic1.getId());
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(1));
        manager.addSubtask(subtask12);

        Subtask subtask41 = new Subtask("Подзадача №3", "Тест", Status.DONE, epic4.getId());
        subtask41.setStartTime(LocalDateTime.of(2025, 4, 11, 12, 0));
        subtask41.setDuration(Duration.ofHours(3));
        manager.addSubtask(subtask41);

        Subtask subtask42 = new Subtask("Подзадача №4", "Тест", Status.DONE, epic4.getId());
        subtask42.setStartTime(LocalDateTime.of(2025, 4, 11, 16, 0));
        subtask42.setDuration(Duration.ofHours(1));
        manager.addSubtask(subtask42);

        Subtask subtask51 = new Subtask("Подзадача №5", "Тест", Status.NEW, epic5.getId());
        subtask51.setStartTime(LocalDateTime.of(2025, 5, 11, 12, 0));
        subtask51.setDuration(Duration.ofHours(3));
        manager.addSubtask(subtask51);

        Subtask subtask52 = new Subtask("Подзадача №6", "Тест", Status.DONE, epic5.getId());
        subtask52.setStartTime(LocalDateTime.of(2025, 5, 11, 16, 0));
        subtask52.setDuration(Duration.ofHours(1));
        manager.addSubtask(subtask52);

        Subtask subtask61 = new Subtask("Подзадача №7", "Тест", Status.IN_PROGRESS, epic6.getId());
        subtask61.setStartTime(LocalDateTime.of(2025, 6, 11, 12, 0));
        subtask61.setDuration(Duration.ofHours(3));
        manager.addSubtask(subtask61);

        Subtask subtask62 = new Subtask("Подзадача №8", "Тест", Status.IN_PROGRESS, epic6.getId());
        subtask62.setStartTime(LocalDateTime.of(2025, 5, 11, 16, 0));
        subtask62.setDuration(Duration.ofHours(1));
        manager.addSubtask(subtask62);

    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Assertions.assertEquals(epic2.getId(), epic3.getId());
    }

    @Test
    void shouldReturnStatusOfEpicNewIfAllStatusesOfSubtasksAreNew() {
        Assertions.assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void shouldReturnStatusOfEpicDoneIfAllStatusesOfSubtasksAreDone() {
        Assertions.assertEquals(Status.DONE, epic4.getStatus());
    }

    @Test
    void shouldReturnStatusOfEpicNewIfOneStatusOfSubtaskIsNew() {
        Assertions.assertEquals(Status.IN_PROGRESS, epic5.getStatus());
    }

    @Test
    void shouldReturnStatusOfEpicInProgressIfAllStatusesOfSubtasksAreInProgress() {
        Assertions.assertEquals(Status.IN_PROGRESS, epic6.getStatus());
    }

}
