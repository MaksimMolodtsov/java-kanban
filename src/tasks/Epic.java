package tasks;
import utils.Status;
import utils.TypeOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subtasksIds;
    LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, null);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    public Epic(String title, String description, Integer id, Status status) {
        super(title, description, id, status);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    public Epic(String title, String description, Status status, Integer id) {
        super(title, description, id, status);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    public Epic(String title, String description, Status status, Integer id, Duration duration,
                LocalDateTime startTime) {
        super(title, description, id, status, duration, startTime);
        subtasksIds = new ArrayList<>();
        this.type = TypeOfTasks.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    @Override
    public TypeOfTasks getType() {
        return TypeOfTasks.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubtasksIds(int id) {
        subtasksIds.add(id);
    }

    public void setSubtasksIds(List<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}
