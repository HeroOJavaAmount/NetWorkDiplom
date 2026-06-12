package netWork.fileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filePath;

    public Logger(String filePath) {
        this.filePath = filePath;
    }

    public synchronized void log(String message) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String timestamp = LocalDateTime.now().format(DT_FORMAT);
            pw.println("[" + timestamp + "] " + message);
        } catch (IOException e) {
            System.err.println("Ошибка логирования: " + e.getMessage());
        }
    }
}