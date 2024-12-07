package org.warzone.logging;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@code LogEntryBuffer}.
 * Validates that the log entry buffer can add and retrieve log entries correctly.
 */
public class LogEntryBufferTest {
    private static LogEntryBuffer d_logEntryBuffer;

    /**
     * Initializes a new {@code LogEntryBuffer} object before any tests are run.
     */
    @BeforeAll
    public static void setUp() {
        d_logEntryBuffer = new LogEntryBuffer();
    }

    /**
     * Tests the addition of a {@code LogEntry} to the {@code LogEntryBuffer}.
     * Ensures that a log entry is correctly added to the buffer and can be retrieved.
     */
    @Test
    public void testLogAddition() {
        assertNull(d_logEntryBuffer.getCurrentLogEntry(), "Log entry buffer is not null");
        d_logEntryBuffer.addLogEntry(new LogEntry(LogEntry.GamePhase.TEST, "Test"));
        assertNotNull(d_logEntryBuffer.getCurrentLogEntry(), "Log entry buffer is null");
    }
}