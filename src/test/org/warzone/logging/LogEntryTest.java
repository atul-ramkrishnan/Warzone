package org.warzone.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@code LogEntry}.
 * Validates the correct construction and string representation of log entries.
 */
public class LogEntryTest {
    /**
     * Tests the construction of a {@code LogEntry} object.
     * Ensures that the {@code toString} method of a {@code LogEntry} object
     * returns a string containing the correct phase and message as expected.
     */
    @Test
    public void testLogEntryConstruction() {
        LogEntry.GamePhase l_expectedPhase = LogEntry.GamePhase.TEST;
        String l_expectedMessage = "Test Message";
        LogEntry l_logEntry = new LogEntry(l_expectedPhase, l_expectedMessage);

        assertNotNull(l_logEntry.toString(), "Log is null");
        assertTrue(l_logEntry.toString().contains(l_expectedPhase.toString()), "Log does not contain the expected phase.");
        assertTrue(l_logEntry.toString().contains(l_expectedMessage), "Log does not contain the expected message.");
    }

}