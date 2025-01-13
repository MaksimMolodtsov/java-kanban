package tasks;

import statuses.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description, null);
        subtasksIds = new ArrayList<>();
    }

    public Epic(String title, String description, Status status, Integer id) {
        super(title, description, id, status);
        subtasksIds = new ArrayList<>();
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

}
