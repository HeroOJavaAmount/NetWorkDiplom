package netWork.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import netWork.fileUtil.Logger;

public class ClientHandler implements Runnable {
    private final ServerIntf server;
    private final Session session;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Logger logger;

    public ClientHandler(ServerIntf server, Session session, Logger logger) {
        this.server = server;
        this.session = session;
        this.in = session.getReader();
        this.out = session.getWriter();
        this.logger = logger;
    }


    @Override
    public void run() {
        try {
            out.println("SERVER:Добро пожаловать в чат!");
            String clientName = session.getUser().getName();
            logger.log("Клиент " + clientName + " вошёл в чат");
            String msg;
            while ((msg = in.readLine()) != null) {
                logger.log(clientName + ": " + msg);
                System.out.println(clientName + ": " + msg);
                server.broadcastMessage("[" + clientName + "]:" + msg);
                if ("/exit".equalsIgnoreCase(msg)) {
                    out.println("SERVER: Goodbye!");
                    logger.log("Клиент " + clientName + " вышел из чата");
                    break;
                }
            }
        } catch (IOException e) {
            logger.log("Ошибка соединения с клиентом: " + e.getMessage());
        } finally {
            server.unSenderClient(this);
            session.disconnect();
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public Session getConnection() {
        return session;
    }
}









