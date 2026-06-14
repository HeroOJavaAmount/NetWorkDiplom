package netWork.server;

import netWork.config.Settings;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionTest {

    @Test
    void testSuccessfulAuthenticationAndDetach() throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getInputStream()).thenReturn(pis);
        when(mockSocket.getOutputStream()).thenReturn(pos);

        Settings settings = new Settings();

        Connection conn = new Connection(mockSocket, settings);
        PrintWriter clientOut = new PrintWriter(pos, true);

        clientOut.println("testUser");
        conn.run();

        assertTrue(conn.isAuthenticated());
        assertNotNull(conn.getUser());
        assertEquals("testUser", conn.getUser().getName());

        BufferedReader reader = conn.detachReader();
        PrintWriter writer = conn.detachWriter();
        assertNotNull(reader);
        assertNotNull(writer);

        conn.disconnect();
    }

    @Test
    void testFailedAuthenticationDisconnect() throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getInputStream()).thenReturn(pis);
        when(mockSocket.getOutputStream()).thenReturn(pos);

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);

        pos.close();
        conn.run();

        assertFalse(conn.isAuthenticated());
    }
}