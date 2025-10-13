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
            this.httpServer = HttpServer.create(
                    new InetSocketAddress("localhost", this.port), 0
            );

            // Pass SimpleApplication directly to Handler
            this.httpServer.createContext("/", new Handler(this.application));
            this.httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

