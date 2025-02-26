package tests.api;

import api.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import utils.Status;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class HttpTaskManagerTasksTest {

    private TaskManager taskManager = new InMemoryTaskManager();
    private HttpTaskServer taskServer;
    private Gson gson = Managers.getGson();
    private Type taskType = new TypeToken<List<Task>>() {
    }.getType();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Задача №1", "Описание", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 10, 10, 5));

        String taskJson = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача №1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 10, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request1, handler);
        client.send(request2, handler);
        HttpResponse<String> responseGet = client.send(requestGet, handler);

        assertEquals(200, responseGet.statusCode());
        List<Task> tasksFromManager = gson.fromJson(responseGet.body(), taskType);
        assertNotNull(tasksFromManager);
        assertEquals(2, tasksFromManager.size());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 10, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        URI url2 = URI.create("http://localhost:8080/tasks/2");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request1, handler);
        client.send(request2, handler);
        HttpResponse<String> responseGet = client.send(requestGet, handler);

        assertEquals(200, responseGet.statusCode());

        Task responseTask = gson.fromJson(responseGet.body(), Task.class);
        assertEquals(taskManager.getTaskById(2).getTitle(), responseTask.getTitle());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 10, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        URI url2 = URI.create("http://localhost:8080/tasks/2");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request1, handler);
        client.send(request2, handler);
        HttpResponse<String> responseDelete = client.send(requestDelete, handler);

        assertEquals(200, responseDelete.statusCode());
        assertNull(taskManager.getTaskById(2));

        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> responseGet = client.send(requestGet, handler);

        assertEquals(404, responseGet.statusCode());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 10, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        URI url2 = URI.create("http://localhost:8080/tasks/2");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        client.send(request1, handler);
        client.send(request2, handler);
        HttpResponse<String> responseGet = client.send(requestGet, handler);

        Task responseTask = gson.fromJson(responseGet.body(), Task.class);
        responseTask.setStatus(Status.DONE);
        String responseTaskToJson = gson.toJson(responseTask);

        HttpRequest requestUpdateTask = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(responseTaskToJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> responseUpdateTask = client.send(requestUpdateTask, handler);

        assertEquals(201, responseUpdateTask.statusCode());

        HttpResponse<String> responseAfterUpdate = client.send(requestGet, handler);

        assertEquals(200, responseAfterUpdate.statusCode());

        Task responseTaskAfterUpdate = gson.fromJson(responseAfterUpdate.body(), Task.class);

        assertEquals(200, responseAfterUpdate.statusCode());
        assertEquals(responseTask.getStatus(), responseTaskAfterUpdate.getStatus());
    }

    @Test
    void shouldCreateOverlappingTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2026, 1, 11, 10, 5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client.send(request1, handler);
        HttpResponse<String> response2 = client.send(request2, handler);

        assertEquals(201, response1.statusCode());
        assertEquals(406, response2.statusCode());
    }

}