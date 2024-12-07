import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryShouldReturnListOf10Tasks() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Задача №1", "Поспать", Status.NEW));
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskById(task.getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(20, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task washFloor = new Task("Помыть полы", "С новым средством", Status.NEW);
        taskManager.addTask(washFloor);
        taskManager.getTaskById(washFloor.getId());
        taskManager.updateTask(new Task("Не забыть помыть полы",
                "Можно и без средства", washFloor.getId(), Status.IN_PROGRESS));
        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertEquals(washFloor.getTitle(), oldTask.getTitle(), "В истории не сохранилась старая версия задачи");
        assertEquals(washFloor.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic flatRenovation = new Epic("Сделать ремонт", "За 2 дня");
        taskManager.addEpic(flatRenovation);
        taskManager.getEpicById(flatRenovation.getId());
        taskManager.updateEpic(new Epic("Новое имя", "Новое описание", Status.IN_PROGRESS,
                flatRenovation.getId()));
        List<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        assertEquals(flatRenovation.getTitle(), oldEpic.getTitle(),
                "В истории не сохранилась старая версия эпика");
        assertEquals(flatRenovation.getDescription(), oldEpic.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic flatRenovation = new Epic("Сделать ремонт", "За 2 дня");
        taskManager.addEpic(flatRenovation);
        Subtask flatRenovationSubtask3 = new Subtask("Заказать книжный шкаф", "Из темного дерева",
                Status.NEW, flatRenovation.getId());
        taskManager.addSubtask(flatRenovationSubtask3);
        taskManager.getSubtaskById(flatRenovationSubtask3.getId());
        taskManager.updateSubtask(new Subtask("Новое имя", "новое описание", Status.IN_PROGRESS,
                flatRenovation.getId()));
        List<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        assertEquals(flatRenovationSubtask3.getTitle(), oldSubtask.getTitle(),
                "В истории не сохранилась старая версия эпика");
        assertEquals(flatRenovationSubtask3.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

}
