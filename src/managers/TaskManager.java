package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    // TASK
    List<Task> getTasks();

    void deleteTasks();

    Task getTaskById(int id);

    Task addTask(Task task);

    Task updateTask(Task task);

    void deleteTaskById(int id);

    // EPIC
    List<Epic> getEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    Epic addEpic(Epic epic);

    Epic updateEpic(Epic epic);

    void deleteEpicById(int id);

    //SUBTASK
    List<Subtask> getSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    Subtask addSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);

    List<Subtask> getSubtasksOfEpic(int id);

    //
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}