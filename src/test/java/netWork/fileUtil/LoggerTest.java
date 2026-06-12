package netWork.fileUtil;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void testLogWritesMessageWithTimestamp() throws IOException {
        Path tempFile = Files.createTempFile("test", ".log");
        Logger logger = new Logger(tempFile.toString());
        logger.log("test message");

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(1, lines.size());
        String line = lines.get(0);
        assertTrue(line.matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] test message"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLogAppendsToFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".log");
        Logger logger = new Logger(tempFile.toString());
        logger.log("first");
        logger.log("second");

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(2, lines.size());

        Files.deleteIfExists(tempFile);
    }
}
