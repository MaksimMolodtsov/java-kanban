package tests.managers;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void addNewTaskAndFindById() {
        super.addNewTaskAndFindById();
    }

    @Test
    void addNewEpicAndSubtasksAndFindById() {
        super.addNewEpicAndSubtasksAndFindById();
    }

    @Test
    public void updateTaskShouldReturnTaskWithTheSameId() {
        super.updateTaskShouldReturnTaskWithTheSameId();
    }

    @Test
    public void updateEpicShouldReturnEpicWithTheSameId() {
        super.updateEpicShouldReturnEpicWithTheSameId();
    }

    @Test
    public void deleteTasksShouldReturnEmptyList() {
        super.deleteTasksShouldReturnEmptyList();
    }

    @Test
    public void deleteEpicsShouldReturnEmptyList() {
        super.deleteEpicsShouldReturnEmptyList();
    }

    @Test
    public void deleteSubtasksShouldReturnEmptyList() {
        super.deleteSubtasksShouldReturnEmptyList();
    }

    @Test
    void createdAndAddedTaskShouldHaveSameVariables() {
        super.createdAndAddedTaskShouldHaveSameVariables();
    }

    @Test
    void tasksShouldBeAddWithoutCrossingInTime() {
        super.tasksShouldBeAddWithoutCrossingInTime();
    }

    @Test
    void catchExceptionWhenTasksCrossingInTime() {
        super.catchExceptionWhenTasksCrossingInTime();
    }

}