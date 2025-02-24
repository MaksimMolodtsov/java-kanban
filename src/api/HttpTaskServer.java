package api;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    //private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();

        //File newFile = File.createTempFile("text", ".temp", new File("/Users/Maksim"));
        //FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(newFile);

        Task task1 = new Task("Задача №1", "Поспать", Status.IN_PROGRESS);
        task1.setStartTime(LocalDateTime.of(2025, 2, 10, 10, 5));
        task1.setDuration(Duration.ofHours(2));
        manager.addTask(task1);

        Task task2 = new Task("Задача №2", "Еще поспать", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2025, 2, 10, 16, 0));
        task2.setDuration(Duration.ofHours(1));
        manager.addTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, epic1.getId());
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, epic1.getId());
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 15, 0));
        subtask12.setDuration(Duration.ofHours(1));
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Эпик №2", "Подумать о смысле жизни");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Подзадача №1", "Думать", Status.IN_PROGRESS, epic2.getId());
        subtask21.setStartTime(LocalDateTime.of(2025, 2, 12, 12, 50));
        subtask21.setDuration(Duration.ofHours(10));
        manager.addSubtask(subtask21);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        task1.setStatus(Status.DONE);
        task2.setStatus(Status.IN_PROGRESS);
        subtask11.setStatus(Status.IN_PROGRESS);
        subtask12.setStatus(Status.DONE);
        subtask21.setStatus(Status.DONE);

        manager.updateTask(task1);
        manager.updateTask(task2);
        manager.updateSubtask(subtask11);
        manager.updateSubtask(subtask12);
        manager.updateSubtask(subtask21);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic2.getId());

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(4));

        System.out.println(manager.getHistory());
        System.out.println(manager.getPrioritizedTasks());

        //FileBackedTaskManager fileBackedManager = FileBackedTaskManager.loadFromFile(newFile);

        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        System.out.println("HTTP-сервер запущен на " + 8080 + " порту!");

        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

    }

}