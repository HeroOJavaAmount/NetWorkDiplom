package netWork.server;

import netWork.chatServiceUser.User;
import netWork.fileUtil.Logger;
import org.junit.jupiter.api.Test;
import java.io.*;

import static org.mockito.Mockito.*;

class ClientHandlerTest {

    @Test
    void testMessageBroadcastAndExit() throws IOException {
        ServerIntf server = mock(ServerIntf.class);
        Session session = mock(Session.class);
        Logger logger = mock(Logger.class);

        User user = new User("testUser");
        when(session.getUser()).thenReturn(user);
        when(session.getClientAddress()).thenReturn(java.net.InetAddress.getByName("127.0.0.1"));

        BufferedReader reader = new BufferedReader(new StringReader("Hello\n/exitt\n"));
        when(session.getReader()).thenReturn(reader);
        PrintWriter writer = new PrintWriter(new StringWriter());
        when(session.getWriter()).thenReturn(writer);

        ClientHandler handler = new ClientHandler(server, session, logger);
        handler.run();

        verify(server).broadcastMessage("[testUser]:Hello");
        verify(server).unSenderClient(handler);
        verify(session).disconnect();
    }
}