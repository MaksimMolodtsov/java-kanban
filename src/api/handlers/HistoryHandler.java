package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");

        if (method.equals("GET")) {
            getHandle(httpExchange, path);
        } else {
            sendNotFound(httpExchange);
        }
    }

    @Override
    protected void getHandle(HttpExchange httpExchange, String[] path) throws IOException {
        response = gson.toJson(taskManager.getHistory());
        sendText(httpExchange, response, 200);
    }

}
