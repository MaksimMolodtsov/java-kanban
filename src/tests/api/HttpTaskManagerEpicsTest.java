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
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerEpicsTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = Managers.getGson();
    Type epicType = new TypeToken<List<Epic>>() {
    }.getType();

    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик №1", "Съездить в отпуск");

        String epicJson = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Эпик №1", epicsFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        Epic epic2 = new Epic("Эпик №2", "Съездить в отпуск 2 раза");

        String epicJson1 = gson.toJson(epic1);
        String epicJson2 = gson.toJson(epic2);
        URI url = URI.create("http://localhost:8080/epics");
        URI url2 = URI.create("http://localhost:8080/epics/2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson2))
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

        Task responseEpic = gson.fromJson(responseGet.body(), Task.class);
        assertEquals(taskManager.getEpicById(2).getTitle(), responseEpic.getTitle());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        Epic epic2 = new Epic("Эпик №2", "Съездить в отпуск 2 раза");

        String epicJson1 = gson.toJson(epic1);
        String epicJson2 = gson.toJson(epic2);
        URI url = URI.create("http://localhost:8080/epics");
        URI url2 = URI.create("http://localhost:8080/epics/2");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson2))
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

        assertNull(taskManager.getEpicById(2));

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
    void shouldGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        Epic epic2 = new Epic("Эпик №2", "Съездить в отпуск 2 раза");

        String epicJson1 = gson.toJson(epic1);
        String epicJson2 = gson.toJson(epic2);
        URI url = URI.create("http://localhost:8080/epics");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson2))
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
        List<Task> epicsFromManager = gson.fromJson(responseGet.body(), epicType);
        assertNotNull(epicsFromManager);
        assertEquals(2, epicsFromManager.size());
    }
}