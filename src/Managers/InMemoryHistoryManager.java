package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> receivedTasks = new ArrayList<>();
    private static final int historySize = 10;

    @Override
    public void add(Task task) {
        if (receivedTasks.size() == historySize) {
            receivedTasks.removeFirst();
        }
        receivedTasks.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(receivedTasks);
    }

}



