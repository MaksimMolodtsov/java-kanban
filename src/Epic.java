import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description, null);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(int id) {
        subtasksIds.add(id);
    }
}
