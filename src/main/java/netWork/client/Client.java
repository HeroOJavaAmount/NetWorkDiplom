package netWork.client;

import netWork.config.Settings;
import netWork.fileUtil.Logger;
import netWork.protocol.TCProtocol;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Settings settings = new Settings();
        new Client().start(settings.getPort());
    }

    public void start(int port) {
        clientStart(port);
    }

    private void clientStart(int port) {
        Logger clientLogger = new Logger("clientFile.log");

        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключен к серверу на порту " + port);

            while (true) {
                String line = in.readLine();
                if (line == null) {
                    System.out.println("Сервер закрыл соединение.");
                    return;
                }

                if (TCProtocol.REQUEST_LOGIN.equals(line)) {
                    System.out.print("Login: ");
                    out.println(scanner.nextLine());

                } else if (TCProtocol.REQUEST_PASSWORD.equals(line)) {
                    System.out.print("Password: ");
                    out.println(scanner.nextLine());

                } else if (TCProtocol.STATUS_OK.equals(line)) {
                    System.out.println("Аутентификация успешна!");
                    break;

                } else if (TCProtocol.STATUS_WRONG.equals(line)) {
                    System.out.println("Попробуйте снова.");
                } else {
                    System.out.println(line);
                }
            }
            System.out.println("Добро пожаловать в чат! Введите '/exit' для выхода.");
            // Поток для чтения сообщений от сервера
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                        if (msg.startsWith("[")) {
                            clientLogger.log(msg);
                        }
                    }
                } catch (IOException e) {
                    // соединение закрыто
                }
            }).start();
            // Главный поток для отправки сообщений
            String userInput;
            while ((userInput = scanner.nextLine()) != null) {
                if (TCProtocol.isExitCommand(userInput)) {
                    out.println(TCProtocol.EXIT_COMMAND);
                    break;
                }
                clientLogger.log("Me: " + userInput);
                out.println(userInput);
            }

        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }
}
