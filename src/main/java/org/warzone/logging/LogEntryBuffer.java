package org.warzone.logging;

/**
 * The {@code LogEntryBuffer} class represents a buffer that stores the most recent log entry.
 * It is observable, allowing observers to be notified when a new log entry is added.
 */
public class LogEntryBuffer implements Observable {

    private LogEntry d_currentLogEntry;

    /**
     * Adds a new log entry to this buffer and notifies all observers that a change has occurred.
     *
     * @param p_logEntry The log entry to add to the buffer.
     */
    public void addLogEntry(LogEntry p_logEntry) {
        this.d_currentLogEntry = p_logEntry;
        notifyObservers();
    }

    /**
     * Retrieves the current log entry from the buffer.
     *
     * @return The most recently added log entry.
     */
    public LogEntry getCurrentLogEntry() {
        return d_currentLogEntry;
    }
}

