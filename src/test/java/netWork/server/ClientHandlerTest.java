package netWork.server;

import netWork.chatServiceUser.User;
import netWork.fileUtil.Logger;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.mockito.Mockito.*;

class ClientHandlerTest {

    @Test
    void testMessageBroadcast() throws IOException {
        // Мокируем сервер и Connection
        ServerIntf server = mock(ServerIntf.class);
        Connection conn = mock(Connection.class);
        User user = new User("testUser");

        when(conn.getUser()).thenReturn(user);
        when(conn.getReader()).thenReturn(new BufferedReader(new StringReader("Hello\n/exitt\n")));
        when(conn.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        Logger logger = new Logger("testHandler.log");  // временный файл

        ClientHandler handler = new ClientHandler(server, conn, logger);
        handler.run();

        // Проверяем, что broadcast был вызван с правильным сообщением
        verify(server).broadcastMessage("[testUser]: Hello");
        // Проверяем, что при /exit клиент удаляется
        verify(server).unSenderClient(handler);
        // Проверяем, что соединение закрыто
        verify(conn).disconnect();
    }
}