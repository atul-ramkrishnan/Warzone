package org.warzone.logging;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code FileLogger} class implements the {@code Observer} interface
 * and is responsible for writing log entries to a file when notified by an
 * {@code Observable} object, specifically a {@code LogEntryBuffer}.
 */
public class FileLogger implements Observer {

    private String d_filePath;

    /**
     * Creates a {@code FileLogger} instance with a specified file path for the log.
     *
     * @param p_filePath The path to the file where log entries should be written.
     */
    public FileLogger(String p_filePath) {
        this.d_filePath = p_filePath;
    }

    /**
     * Called by the {@code Observable}, typically a {@code LogEntryBuffer},
     * whenever a log entry is added to the buffer. Writes the log entry to a file.
     *
     * @param p_observable The observable object.
     */
    @Override
    public void update(Observable p_observable) {
        if (p_observable instanceof LogEntryBuffer) {
            LogEntryBuffer l_buffer = (LogEntryBuffer) p_observable;
            LogEntry l_entry = l_buffer.getCurrentLogEntry();
            writeToFile(l_entry);
        }
    }

    /**
     * Writes the given log entry to the file specified by {@code d_filePath}.
     *
     * @param p_entry The log entry to write to the file.
     */
    private void writeToFile(LogEntry p_entry) {
        try (FileWriter l_writer = new FileWriter(d_filePath, true)) {
            l_writer.write(p_entry.toString() + "\n");
        } catch (IOException l_e) {
            l_e.printStackTrace();
        }
    }
}
