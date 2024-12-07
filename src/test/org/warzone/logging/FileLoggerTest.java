package org.warzone.logging;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@code FileLogger}.
 * Validates that log entries are written correctly to a file.
 */
public class FileLoggerTest {

    private static FileLogger d_fileLogger;
    private static LogEntryBuffer d_logEntryBuffer;

    private static String d_testLogPath = "testLog.txt";

    /**
     * Sets up the test environment before all tests are run.
     * Initializes a {@code FileLogger} and a {@code LogEntryBuffer},
     * and registers the logger as an observer to the buffer.
     */
    @BeforeAll
    public static void setUp() {
        d_fileLogger = new FileLogger(d_testLogPath);
        d_logEntryBuffer = new LogEntryBuffer();
        d_logEntryBuffer.addObserver(d_fileLogger);
    }

    /**
     * Cleans up the test environment after all tests have been run.
     * Deletes the test log file created during the testing process.
     */
    @AfterAll
    public static void tearDown() {
        try {
            // Delete the file if it exists
            boolean deleted = Files.deleteIfExists(Paths.get(d_testLogPath));
            if (deleted) {
                System.out.println("Test log file deleted successfully.");
            } else {
                System.out.println("Test log file does not exist or could not be deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the {@code FileLogger}'s ability to write log entries to a file.
     * Ensures that the contents of the file include the expected phase and message
     * after a log entry is added to the {@code LogEntryBuffer}.
     *
     * @throws IOException if reading from the file fails
     */
    @Test
    public void testWrite() throws IOException {
        LogEntry.GamePhase l_expectedPhase = LogEntry.GamePhase.TEST;
        String l_expectedMessage = "Test Message";
        LogEntry l_logEntry = new LogEntry(l_expectedPhase, l_expectedMessage);

        d_logEntryBuffer.addLogEntry(l_logEntry);
        String content = new String(Files.readAllBytes(Paths.get(d_testLogPath)));
        assertTrue(content.contains(l_expectedPhase.toString()), "Log does not contain the expected phase.");
        assertTrue(content.contains(l_expectedMessage.toString()), "Log does not contain the expected message.");
    }
}