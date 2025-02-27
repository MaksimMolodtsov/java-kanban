package api.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getHandle(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            response = gson.toJson(taskManager.getEpics());
            sendText(httpExchange, response, 200);
        } else if (path.length == 3) {
            try {
                int id = Integer.parseInt(path[2]);
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    response = gson.toJson(epic);
                    sendText(httpExchange, response, 200);
                } else {
                    sendNotFound(httpExchange);
                }
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                sendNotFound(httpExchange);
            }
        } else if (path.length == 4 && path[3].equals("subtasks")) {
            try {
                int id = Integer.parseInt(path[2]);
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    response = gson.toJson(taskManager.getSubtasksOfEpic(id));
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
            Epic epic = gson.fromJson(bodyRequest, Epic.class);
                taskManager.addEpic(epic);
                sendText(httpExchange, "Success", 201);
        } catch (JsonSyntaxException e) {
            sendNotFound(httpExchange);
        }
    }

    @Override
    protected void deleteHandle(HttpExchange httpExchange, String[] path) throws IOException {
        try {
            int id = Integer.parseInt(path[2]);
            taskManager.deleteEpicById(id);
            sendText(httpExchange, "Success", 200);
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            sendNotFound(httpExchange);
        }
    }

}
