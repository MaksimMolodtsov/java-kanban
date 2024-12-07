import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description, null);
        subtasksIds = new ArrayList<>();
    }

    public Epic(String title, String description, Status status, Integer id) {
        super(title, description, id, status);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
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
