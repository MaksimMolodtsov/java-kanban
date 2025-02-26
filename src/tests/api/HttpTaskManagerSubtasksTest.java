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
import tasks.Epic;
import tasks.Subtask;
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

class HttpTaskManagerSubtasksTest {

    private TaskManager taskManager = new InMemoryTaskManager();
    private HttpTaskServer taskServer;
    private Gson gson = Managers.getGson();
    private Type subtaskType = new TypeToken<List<Subtask>>() {
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
    public void testAddSubtask() throws IOException, InterruptedException {
        taskManager.addEpic(new Epic("Эпик №1", "Съездить в отпуск"));

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, 1);
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, 1);
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        String subtaskJson11 = gson.toJson(subtask11);
        String subtaskJson12 = gson.toJson(subtask12);

        URI url = URI.create("http://localhost:8080/subtasks");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson11))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson12))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request1, handler);
        HttpResponse<String> response = client.send(request2, handler);

        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = taskManager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Подзадача №1", subtasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {

        taskManager.addEpic(new Epic("Эпик №1", "Съездить в отпуск"));

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, 1);
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, 1);
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        String subtaskJson11 = gson.toJson(subtask11);
        String subtaskJson12 = gson.toJson(subtask12);

        URI url = URI.create("http://localhost:8080/subtasks");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson11))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson12))
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
        List<Subtask> epicsFromManager = gson.fromJson(responseGet.body(), subtaskType);
        assertNotNull(epicsFromManager);
        assertEquals(2, epicsFromManager.size());
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        taskManager.addEpic(new Epic("Эпик №1", "Съездить в отпуск"));

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, 1);
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, 1);
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        String subtaskJson11 = gson.toJson(subtask11);
        String subtaskJson12 = gson.toJson(subtask12);

        URI url = URI.create("http://localhost:8080/subtasks");
        URI url2 = URI.create("http://localhost:8080/subtasks/2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson11))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson12))
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
        Subtask responseSubTask = gson.fromJson(responseGet.body(), Subtask.class);
        assertEquals(taskManager.getSubtaskById(2).getTitle(), responseSubTask.getTitle());
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        taskManager.addEpic(new Epic("Эпик №1", "Съездить в отпуск"));

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, 1);
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, 1);
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        String subtaskJson11 = gson.toJson(subtask11);
        String subtaskJson12 = gson.toJson(subtask12);

        URI url = URI.create("http://localhost:8080/subtasks");
        URI url2 = URI.create("http://localhost:8080/subtasks/2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson11))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson12))
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

        Task responseSubtask = gson.fromJson(responseGet.body(), Subtask.class);
        responseSubtask.setStatus(Status.DONE);

        String responseSubtaskToJson = gson.toJson(responseSubtask);

        HttpRequest requestUpdateTask = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(responseSubtaskToJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> responseUpdateSubtask = client.send(requestUpdateTask, handler);

        assertEquals(201, responseUpdateSubtask.statusCode());

        HttpResponse<String> responseAfterUpdate = client.send(requestGet, handler);

        assertEquals(200, responseAfterUpdate.statusCode());

        Subtask responseSubtaskAfterUpdate = gson.fromJson(responseAfterUpdate.body(), Subtask.class);

        assertEquals(200, responseAfterUpdate.statusCode());
        assertEquals(responseSubtask.getStatus(), responseSubtaskAfterUpdate.getStatus());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        taskManager.addEpic(new Epic("Эпик №1", "Съездить в отпуск"));

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, 1);
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, 1);
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        String subtaskJson11 = gson.toJson(subtask11);
        String subtaskJson12 = gson.toJson(subtask12);

        URI url = URI.create("http://localhost:8080/subtasks");
        URI url2 = URI.create("http://localhost:8080/subtasks/2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson11))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson12))
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
        HttpResponse<String> responseGet = client.send(requestGet, handler);

        assertEquals(200, responseDelete.statusCode());
        assertNull(taskManager.getTaskById(2));

        assertEquals(404, responseGet.statusCode());
    }

}