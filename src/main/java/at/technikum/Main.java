package at.technikum;

import at.technikum.server.Server;
import at.technikum.server.SimpleApplication;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        SimpleApplication application = new SimpleApplication();
        Server server = new Server(PORT, application);

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
