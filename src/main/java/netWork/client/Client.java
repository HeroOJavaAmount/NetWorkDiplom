package netWork.client;

import netWork.config.Settings;
import netWork.fileUtil.Logger;

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

                if ("Login:".equals(line)) {
                    System.out.print("Login: ");
                    String login = scanner.nextLine();
                    out.println(login);

                } else if ("Password:".equals(line)) {
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    out.println(password);

                } else if ("STATUS OK".equals(line)) {
                    System.out.println("Аутентификация успешна!");
                    break;

                } else if ("STATUS WRONG".equals(line)) {
                    System.out.println("Неверный логин или пароль. Попробуйте снова.");

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
                if ("/exit".equalsIgnoreCase(userInput)) {
                    out.println("/exit");
                    break;
                }
                out.println(userInput);
            }

        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }
}
