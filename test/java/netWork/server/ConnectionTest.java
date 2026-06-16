//package netWork.server;
//
//import netWork.config.Settings;
//import org.junit.jupiter.api.Test;
//import java.io.*;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ConnectionTest {
//
//    @Test
//    void testSuccessfulAuthenticationAndDetach() throws IOException {
//        PipedOutputStream pos = new PipedOutputStream();
//        PipedInputStream pis = new PipedInputStream(pos);
//        Socket mockSocket = mock(Socket.class);
//        when(mockSocket.getInputStream()).thenReturn(pis);
//        when(mockSocket.getOutputStream()).thenReturn(pos);
//
//        Settings settings = new Settings();
//
//        Connection conn = new Connection(mockSocket, settings);
//        PrintWriter clientOut = new PrintWriter(pos, true);
//
//        clientOut.println("testUser");
//        conn.run();
//
//        assertTrue(conn.isAuthenticated());
//        assertNotNull(conn.getUser());
//        assertEquals("testUser", conn.getUser().getName());
//
//        BufferedReader reader = conn.detachReader();
//        PrintWriter writer = conn.detachWriter();
//        assertNotNull(reader);
//        assertNotNull(writer);
//
//        conn.disconnect();
//    }
//
//    @Test
//    void testFailedAuthenticationDisconnect() throws IOException {
//        PipedOutputStream pos = new PipedOutputStream();
//        PipedInputStream pis = new PipedInputStream(pos);
//        Socket mockSocket = mock(Socket.class);
//        when(mockSocket.getInputStream()).thenReturn(pis);
//        when(mockSocket.getOutputStream()).thenReturn(pos);
//
//        Settings settings = new Settings();
//        Connection conn = new Connection(mockSocket, settings);
//
//        pos.close();
//        conn.run();
//
//        assertFalse(conn.isAuthenticated());
//    }
//}

package netWork.server;

import netWork.config.Settings;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionTest {

    @Test
    void testSuccessfulAuthenticationForNormalUser() throws IOException {

        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(serverOutput);

        ByteArrayInputStream serverInput = new ByteArrayInputStream("testUser\n".getBytes());
        when(mockSocket.getInputStream()).thenReturn(serverInput);

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);

        conn.run();

        assertTrue(conn.isAuthenticated());
        assertEquals("testUser", conn.getUser().getName());

        String output = serverOutput.toString();
        assertTrue(output.contains("Login:"));
        assertTrue(output.contains("STATUS OK"));
        assertFalse(output.contains("Password:"));
    }

    @Test
    void testSuccessfulAuthenticationForAdmin() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(serverOutput);

        ByteArrayInputStream serverInput = new ByteArrayInputStream("admin\n123\n".getBytes());
        when(mockSocket.getInputStream()).thenReturn(serverInput);

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);

        conn.run();

        assertTrue(conn.isAuthenticated());
        assertEquals("admin", conn.getUser().getName());

        String output = serverOutput.toString();
        assertTrue(output.contains("Login:"));
        assertTrue(output.contains("Password:"));
        assertTrue(output.contains("STATUS OK"));
    }

    @Test
    void testFailedAuthenticationForAdmin() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(serverOutput);

        ByteArrayInputStream serverInput = new ByteArrayInputStream("admin\nwrong\n".getBytes());
        when(mockSocket.getInputStream()).thenReturn(serverInput);

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);

        conn.run();

        assertFalse(conn.isAuthenticated());
        String output = serverOutput.toString();
        assertTrue(output.contains("Login:"));
        assertTrue(output.contains("Password:"));
        assertTrue(output.contains("STATUS WRONG"));
    }

    @Test
    void testExitDuringLogin() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream serverOutput = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(serverOutput);

        ByteArrayInputStream serverInput = new ByteArrayInputStream("/exit\n".getBytes());
        when(mockSocket.getInputStream()).thenReturn(serverInput);

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);

        conn.run();

        assertFalse(conn.isAuthenticated());

    }

    @Test
    void testDetachStreams() throws IOException {

        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("test\n".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        Settings settings = new Settings();
        Connection conn = new Connection(mockSocket, settings);
        conn.run();

        assertTrue(conn.isAuthenticated());
        BufferedReader reader = conn.detachReader();
        PrintWriter writer = conn.detachWriter();
        assertNotNull(reader);
        assertNotNull(writer);

        conn.disconnect();

    }
}