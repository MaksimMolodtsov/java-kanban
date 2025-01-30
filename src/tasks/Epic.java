package tasks;
import utils.Status;
import utils.TypeOfTasks;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description, null);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    public Epic(String title, String description, Status status, Integer id) {
        super(title, description, id, status);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(int id) {
        subtasksIds.add(id);
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subtasksIds.add(subtaskId);
        }
    }

    @Override
    public TypeOfTasks getType() {
        return TypeOfTasks.EPIC;
    }

}
