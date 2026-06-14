package netWork.server;

import netWork.chatServiceUser.User;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SessionImpl implements Session {
    private final InetAddress clientAddress;
    private final User user;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Socket socket;

    public SessionImpl(InetAddress clientAddress, User user, Socket socket,
                       BufferedReader reader, PrintWriter writer) {
        this.clientAddress = clientAddress;
        this.user = user;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public User getUser() { return user; }

    @Override
    public InetAddress getClientAddress() { return clientAddress; }

    @Override
    public BufferedReader getReader() { return reader; }

    @Override
    public PrintWriter getWriter() { return writer; }

    @Override
    public void disconnect() {
        try { reader.close(); } catch (IOException ignored) {}
        try { writer.close(); } catch (Exception ignored) {}
        try { socket.close(); } catch (IOException ignored) {}
    }

    @Override
    public void close() { disconnect(); }   // совместимость с AutoCloseable
}