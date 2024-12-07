import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // TASK
    ArrayList<Task> getTasks();

    void deleteTasks();

    Task getTaskById(int id);

    Task addTask(Task task);

    Task updateTask(Task task);

    Object deleteTaskById(Integer id);

    // EPIC
    ArrayList<Epic> getEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    Epic addEpic(Epic epic);

    Epic updateEpic(Epic epic);

    void deleteEpicById(Integer id);

    //SUBTASK
    ArrayList<Subtask> getSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    Subtask addSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    Object deleteSubtaskById(int id);

    ArrayList<Subtask> getSubtasksOfEpic(int id);

    //
    List<Task> getHistory();

}