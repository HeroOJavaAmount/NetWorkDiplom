package netWork.server;

import netWork.chatServiceUser.User;
import netWork.config.Settings;
import netWork.protocol.TCProtocol;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection implements Runnable {
    private final Settings settings;
    private final Socket netClient;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private volatile boolean authenticated = false;
    private volatile boolean authenticatedChat = false;
    private volatile boolean streamsDetached = false;
    private User user;

    public Connection(Socket client, Settings settings) {
        netClient = client;
        this.settings = settings;
        try {
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            toClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            try {
                netClient.close();
            } catch (IOException ignored) {
            }
            throw new RuntimeException("Cannot open streams", e);
        }
    }

    public void run() {
        try {
            while (true) {
                toClient.println(TCProtocol.REQUEST_LOGIN);
                String login = fromClient.readLine();
                if (login == null || TCProtocol.isExitCommand(login)) break;
                user = new User(login);

                if (login.equalsIgnoreCase("admin")) authenticatedChat = true;

                if (authenticatedChat) {
                    toClient.println(TCProtocol.REQUEST_PASSWORD);
                    String password = fromClient.readLine();
                    if (password == null){
                        break;
                    } else if (settings.getLoginPass(login).equalsIgnoreCase(password)) {
                        toClient.println(TCProtocol.STATUS_OK);
                        authenticated = true;
                        break;
                    } else {
                        toClient.println(TCProtocol.STATUS_WRONG);
                    }
                } else {
                    toClient.println(TCProtocol.STATUS_OK);
                    authenticated = true;
                    break;
                }
            }
        } catch (IOException e) {
            // ошибка связи
        } finally {
            if (!authenticated) {
                disconnect();
            }
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public BufferedReader detachReader() {
        streamsDetached = true;
        return fromClient;
    }

    public PrintWriter detachWriter() {
        streamsDetached = true;
        return toClient;
    }

    public BufferedReader getReader() {
        if (!authenticated) throw new IllegalStateException("Not authenticated");
        return fromClient;
    }

    public PrintWriter getWriter() {
        if (!authenticated) throw new IllegalStateException("Not authenticated");
        return toClient;
    }

    public User getUser() {
        return user;
    }

    public void disconnect() {
        if (streamsDetached) {
            try { if (netClient != null) netClient.close(); } catch (IOException ignored) {}
        } else {
            try { if (fromClient != null) fromClient.close(); } catch (IOException ignored) {}
            try { if (toClient != null) toClient.close(); } catch (Exception ignored) {}
            try { if (netClient != null) netClient.close(); } catch (IOException ignored) {}
        }
    }

    public Socket getNetClient() {
        return netClient;
    }
}
