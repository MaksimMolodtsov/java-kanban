package managers;

import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> nodes = new HashMap<>();
    private Node<Task> tail;
    private Node<Task> head;

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
        nodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    // ForLinkedList

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        nodes.put(task.getId(), newNode);
        if (oldTail != null) {
            oldTail.next = newNode;
        } else {
            head = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> recievedTasks = new ArrayList<>();
        Node<Task> curNode = head;
        while (curNode != null) {
            recievedTasks.add(curNode.data);
            curNode = curNode.next;
        }
        return recievedTasks;
    }
}