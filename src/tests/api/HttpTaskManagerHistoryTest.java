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

class HttpTaskManagerHistoryTest {

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
    void shouldGetHistory() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик №1", "Съездить в отпуск");

        Subtask subtask11 = new Subtask("Подзадача №1", "Найти тур", Status.DONE, epic.getId());
        subtask11.setStartTime(LocalDateTime.of(2025, 2, 11, 12, 0));
        subtask11.setDuration(Duration.ofHours(3));

        Subtask subtask12 = new Subtask("Подзадача №2", "Оплата", Status.IN_PROGRESS, epic.getId());
        subtask12.setStartTime(LocalDateTime.of(2025, 2, 11, 16, 0));
        subtask12.setDuration(Duration.ofHours(2));

        URI url = URI.create("http://localhost:8080/history");

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

        List<Task> history = gson.fromJson(response.body(), taskType);
        assertNotNull(history, "История не возвращается");
    }
}