package Tasks;

import Statuses.Status;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }
}