package netWork;

import netWork.config.Settings;
import netWork.server.TCPServer;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {

        Settings settings = new Settings();

        TCPServer server = new TCPServer(settings.getPort());
        server.start();
    }
}