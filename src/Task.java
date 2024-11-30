import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private int id;
    private Status status;

    public Task (String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task (String title, String description, int id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public Status getStatus () {
        return status;
    }

    public void setStatus (Status status){
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
