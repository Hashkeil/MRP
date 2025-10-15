package at.technikum.server;

import at.technikum.router.Handler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private HttpServer httpServer;
    private final int port;
    private final SimpleApplication application;

    public Server(int port, SimpleApplication application) {
        this.port = port;
        this.application = application;
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            httpServer.createContext("/", new Handler(application));
            httpServer.start();
            System.out.println("Server started on http://localhost:" + port);
            System.out.println("API endpoints available at http://localhost:" + port + "/api/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("Server stopped.");
        }
    }
}
