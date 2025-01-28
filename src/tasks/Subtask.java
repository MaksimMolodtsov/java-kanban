package tasks;

import utils.Status;
import utils.TypeOfTasks;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this.type = TypeOfTasks.SUBTASK;
    }

    public Subtask(String title, String description, Integer id, Status status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
        this.type = TypeOfTasks.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toStringForFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, type, title, status, description, epicId);
    }

    @Override
    public TypeOfTasks getType() {
        return TypeOfTasks.SUBTASK;
    }

}
