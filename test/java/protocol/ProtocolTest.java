package protocol;

import netWork.protocol.TCProtocol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolTest {

    @Test
    void testFormatBroadcast() {
        assertEquals("[Alice]:Hello", TCProtocol.formatBroadcast("Alice", "Hello"));
    }

    @Test
    void testIsExitCommand() {
        assertTrue(TCProtocol.isExitCommand("/exit"));
        assertTrue(TCProtocol.isExitCommand("/EXIT"));
        assertFalse(TCProtocol.isExitCommand("exit"));
        assertFalse(TCProtocol.isExitCommand(null));
    }

    @Test
    void testIsServerMessage() {
        assertTrue(TCProtocol.isServerMessage("SERVER:Hello"));
        assertFalse(TCProtocol.isServerMessage("[Alice]:Hello"));
        assertFalse(TCProtocol.isServerMessage(null));
    }

    @Test
    void testIsBroadcastMessage() {
        assertTrue(TCProtocol.isBroadcastMessage("[Alice]:Hello"));
        assertFalse(TCProtocol.isBroadcastMessage("SERVER:Hello"));
        assertFalse(TCProtocol.isBroadcastMessage("Hello"));
        assertFalse(TCProtocol.isBroadcastMessage(null));
    }
}