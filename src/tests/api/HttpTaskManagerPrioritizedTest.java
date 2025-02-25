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

class HttpTaskManagerPrioritizedTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = Managers.getGson();
    Type taskType = new TypeToken<List<Task>>() {
    }.getType();

    public HttpTaskManagerPrioritizedTest() throws IOException {
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
    void shouldGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("Задача №1", "Описание 1", 1, Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2025, 1, 10, 10, 5));
        Task task2 = new Task("Задача №2", "Описание 2", 2, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2025, 1, 9, 13, 0));
        Task task3 = new Task("Задача №3", "Описание 3", 3, Status.IN_PROGRESS,
                Duration.ofHours(1), LocalDateTime.of(2025, 1, 11, 13, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        URI url = URI.create("http://localhost:8080/prioritized");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());

        List<Task> prioritized = gson.fromJson(response.body(), taskType);
        assertNotNull(prioritized, "История не возвращается");

        assertEquals(3, prioritized.size(), "Неверное количество задач");
        assertEquals(task2.getDescription(), prioritized.getFirst().getDescription(), "Задачи не совпадают");
    }
}