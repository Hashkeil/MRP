package at.technikum;

import at.technikum.router.Handler;
import at.technikum.server.SimpleApplication;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        SimpleApplication application = new SimpleApplication();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new Handler(application));
        server.start();

        System.out.println("Server started on http://localhost:8080");
        System.out.println("API endpoints available at http://localhost:8080/api/");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(0);
            System.out.println("Server stopped.");
        }));

    }}

