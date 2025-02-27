package api.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerCrossingTimeException;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getHandle(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            response = gson.toJson(taskManager.getSubtasks());
            sendText(httpExchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask != null) {
                    response = gson.toJson(subtask);
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
            Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
            if (taskManager.getSubtaskById(subtask.getId()) != null) {
                taskManager.updateSubtask(subtask);
                sendText(httpExchange, "Success", 201);
            } else {
                taskManager.addSubtask(subtask);
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
        if (path.length == 2) {
            sendNotFound(httpExchange);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                taskManager.deleteSubtaskById(id);
                sendText(httpExchange, "Success", 200);
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                sendNotFound(httpExchange);
            }
        }
    }

}
