package netWork.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private final Properties properties = new Properties();

    public Settings() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("settings.txt")) {
            if (in == null) {
                throw new IOException("Файл settings.txt не найден в classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки настроек", e);
        }
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("port", "8888"));
    }

    public String getHost() {
        return properties.getProperty("host", "localhost");
    }

    public String getDefaultUsername() {
        // может вернуть null, если параметр отсутствует
        return properties.getProperty("username");
    }

    public String getLoginPass(String login) {
        return properties.getProperty(login);
    }
}