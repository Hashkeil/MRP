package at.technikum.router;
import at.technikum.server.SimpleApplication;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class Handler implements HttpHandler {


    private final SimpleApplication application;

    public Handler(SimpleApplication app) {
        this.application = app;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = convertToRequest(exchange);
        Response response = application.handle(request);
        sendResponse(exchange, response);
    }

    private Request convertToRequest(HttpExchange exchange) throws IOException {
        Request request = new Request();
        request.setMethod(exchange.getRequestMethod());
        request.setPath(exchange.getRequestURI().getPath());

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            request.setBody(body);
        }

        exchange.getRequestHeaders().forEach((key, values) -> {
            if (!values.isEmpty()) {
                request.addHeader(key.toLowerCase(), values.get(0));
            }
        });

        return request;
    }

    private void sendResponse(HttpExchange exchange, Response response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", response.getContentType());
        byte[] bytes = response.getBody().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(response.getStatus(), bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}