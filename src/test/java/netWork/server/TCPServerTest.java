package netWork.server;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TCPServerTest {

    @Test
    void testServerAcceptAndGreeting() throws Exception {
        int port = 0;  // 0 = автоматический выбор свободного порта
        TCPServer server = new TCPServer(port);
        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        TimeUnit.MILLISECONDS.sleep(300);

    }
    @Test
    void testClientCanConnectAndLogin() throws IOException {
        int port = 15999;   // нестандартный порт для теста
        TCPServer server = new TCPServer(port);
        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        // Ждём запуск сервера
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line = in.readLine();
            assertEquals("Login:", line);

            out.println("testUser");

            line = in.readLine();
            assertEquals("STATUS OK", line);

            line = in.readLine();
            assertTrue(line.contains("Добро пожаловать"));
        }
    }
}