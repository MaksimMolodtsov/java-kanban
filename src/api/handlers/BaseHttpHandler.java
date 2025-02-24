package api.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {

    protected TaskManager taskManager;
    protected String response;
    protected Gson gson = Managers.getGson();

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                getHandle(httpExchange, path);
            case "POST":
                postHandle(httpExchange);
            case "DELETE":
                deleteHandle(httpExchange, path);
            default:
                sendNotFound(httpExchange);
        }
    }

    protected void getHandle(HttpExchange httpExchange, String[] path) throws IOException {}

    protected void postHandle(HttpExchange httpExchange) throws IOException {}

    protected void deleteHandle(HttpExchange httpExchange, String[] path) throws IOException {}

    protected void sendText(HttpExchange httpExchange, String response, int statusCode) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Not Found", 404);
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Not Acceptable", 406);
    }

}