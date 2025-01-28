package tests.tasks;

import utils.Status;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    Task task = new Task("Задача №5", "Отдохнуть", 1, Status.IN_PROGRESS);
    Task task1 = new Task("Задача №1", "Поспать", 2, Status.NEW);
    Task task2 = new Task("Задача №1", "Поспать", 2, Status.NEW);

    @Test
    void getAndSetStatus() {
        Assertions.assertEquals(Status.IN_PROGRESS, task.getStatus());
        task.setStatus(Status.NEW);
        Assertions.assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Assertions.assertEquals(task1, task2);
    }

}