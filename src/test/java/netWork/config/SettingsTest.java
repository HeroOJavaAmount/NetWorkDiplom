package netWork.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    void testDefaultPort() {
        Settings settings = new Settings();
        assertEquals(8888, settings.getPort());   // из тестового settings.txt
    }

    @Test
    void testHost() {
        Settings settings = new Settings();
        assertEquals("localhost", settings.getHost());
    }

    @Test
    void testGetLoginPass() {
        Settings settings = new Settings();
        assertEquals("123", settings.getLoginPass("admin"));
        assertEquals("pass", settings.getLoginPass("user"));
        assertNull(settings.getLoginPass("unknown"));
    }

    @Test
    void testDefaultUsername() {
        Settings settings = new Settings();
        // если в файле нет username, вернёт null
        assertNull(settings.getDefaultUsername());
    }
}