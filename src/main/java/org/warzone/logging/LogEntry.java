package org.warzone.logging;

import java.time.LocalDateTime;

/**
 * The {@code LogEntry} class captures details such as the game phase and a message.
 * Each log entry is timestamped when it is created.
 */
public class LogEntry {
    /**
     * Enumerates the various game phases that can be associated with a log entry.
     */
    public static enum GamePhase {
        /**
         * The start of the game phase.
         */
        START_OF_GAME,

        /**
         * The preload phase.
         */
        PRELOAD,

        /**
         * The map editing phase.
         */
        EDIT_MAP,

        /**
         * The play setup phase.
         */
        PLAY_SETUP,
        /**
         * The issue order phase.
         */
        ISSUE_ORDER,

        /**
         * The execute order phase.
         */
        EXECUTE_ORDER,

        /**
         * The end of the game phase.
         */
        END_OF_GAME,

        /**
         * The test phase.
         */
        TEST
    }
    private GamePhase d_gamePhase;
    private LocalDateTime d_timestamp;
    private String d_message;

    /**
     * Constructs a new LogEntry with the specified game phase and message.
     * The timestamp is set to the current date and time.
     *
     * @param p_gamePhase The game phase to associate with the log entry.
     * @param p_message   The message to be logged.
     */
    public LogEntry(GamePhase p_gamePhase, String p_message) {
        this.d_gamePhase = p_gamePhase;
        this.d_timestamp = LocalDateTime.now();
        this.d_message = p_message;
    }

    /**
     * Returns a string representation of the log entry, including the timestamp, game phase, and message.
     *
     * @return A string containing the timestamp, game phase, and message of the log entry.
     */
    @Override
    public String toString() {
        return String.format("Timestamp: %s, Phase: %s, Message: %s", d_timestamp, d_gamePhase, d_message);
    }
}
