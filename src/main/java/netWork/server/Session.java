package netWork.server;

import netWork.chatServiceUser.User;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;

public interface Session extends AutoCloseable {
    User getUser();
    InetAddress getClientAddress();
    BufferedReader getReader();
    PrintWriter getWriter();
    void disconnect();      // закроет всё
}