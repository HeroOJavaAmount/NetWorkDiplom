package netWork.server;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPServerTest {

    @Test
    void testClientCanConnectAndLogin() throws IOException, InterruptedException {
        int port = 15999;
        TCPServer server = new TCPServer(port);
        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(500);

        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            assertEquals("Login:", in.readLine());

            out.println("testUser");

            assertEquals("STATUS OK", in.readLine());

            String welcome = in.readLine();
            assertTrue(welcome.contains("Добро пожаловать"));

            out.println("Привет");
            String echoed = in.readLine();
            assertTrue(echoed.contains("[testUser]:Привет"));

            out.println("/exit");
            String goodbye = in.readLine();
            assertTrue(goodbye.contains("Goodbye"));
        }
    }
}