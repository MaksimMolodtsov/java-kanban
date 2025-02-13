package tasks;

import utils.Status;
import utils.TypeOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this.type = TypeOfTasks.SUBTASK;
    }

    public Subtask(String title, String description, Integer id, Status status, int epicId, Duration duration,
                   LocalDateTime startTime) {
        super(title, description, id, status, duration, startTime);
        this.epicId = epicId;
        this.type = TypeOfTasks.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (id > 0) {
            this.epicId = epicId;
        }
    }

    @Override
    public TypeOfTasks getType() {
        return TypeOfTasks.SUBTASK;
    }

    @Override
    public String toStringForFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, type, title, status, description, epicId,
                duration.toMinutes(), startTime.format(FORMATTER));
    }

}
