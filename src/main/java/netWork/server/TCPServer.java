package netWork.server;

import netWork.config.Settings;
import netWork.fileUtil.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements ServerIntf {
    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final Logger serverLogger = new Logger("serverFile.log");

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        Settings settings = new Settings();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту - " + port);
            serverLogger.log("Сервер запущен.");
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(() -> {
                    Connection conn = new Connection(socket, settings);
                    conn.run();
                    if (conn.isAuthenticated()) {
                        ClientHandler handler = new ClientHandler(this, conn, serverLogger);
                        addClient(handler);
                        executor.execute(handler);
                    } else {
                        conn.disconnect();
                        serverLogger.log("Аутентификация не пройдена, соединение закрыто");
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void broadcastMessage(String msg) {
        for (ClientHandler c : clients) {
            c.sendMessage(msg);
        }
    }

    @Override
    public void unSenderClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    @Override
    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }
}
