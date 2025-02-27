package api.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerCrossingTimeException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getHandle(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            response = gson.toJson(taskManager.getTasks());
            sendText(httpExchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                Task task = taskManager.getTaskById(id);
                if (task != null) {
                    response = gson.toJson(task);
                    sendText(httpExchange, response, 200);
                } else {
                    sendNotFound(httpExchange);
                }
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                sendNotFound(httpExchange);
            }
        }
    }

    @Override
    protected void postHandle(HttpExchange httpExchange) throws IOException {
        String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (bodyRequest.isEmpty()) {
            sendNotFound(httpExchange);
            return;
        }
        try {
            Task task = gson.fromJson(bodyRequest, Task.class);
            if (taskManager.getTaskById(task.getId()) != null) {
                taskManager.updateTask(task);
                sendText(httpExchange, "Success", 201);
            } else {
                taskManager.addTask(task);
                sendText(httpExchange, "Success", 201);
            }
        } catch (ManagerCrossingTimeException e) {
            sendHasInteractions(httpExchange);
        } catch (JsonSyntaxException e) {
            sendNotFound(httpExchange);
        }
    }

    @Override
    protected void deleteHandle(HttpExchange httpExchange, String[] path) throws IOException {
        try {
            int id = Integer.parseInt(path[2]);
            taskManager.deleteTaskById(id);
            sendText(httpExchange, "Success", 200);
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            sendNotFound(httpExchange);
        }
    }

}