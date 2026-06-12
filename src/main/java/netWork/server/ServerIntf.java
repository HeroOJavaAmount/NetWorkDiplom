package netWork.server;

public interface ServerIntf {
    void start();
    void broadcastMessage(String msg);
    void unSenderClient(ClientHandler clientHandler);
    void addClient(ClientHandler clientHandler);
}