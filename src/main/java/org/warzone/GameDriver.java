package org.warzone;

import java.io.IOException;

/**
 * The {@code GameDriver} class serves as the entry point for the game application.
 * It initializes the {@code GameEngine} and starts the game process.
 */
public class GameDriver {
    /**
     * The main method that serves as the entry point of the game application.
     * It creates an instance of {@code GameEngine} and calls the start method to begin the game.
     *
     * @param args Command-line arguments passed to the application (not used).
     * @throws IOException If an I/O error occurs during game initialization or execution.
     */
    public static void main(String[] args) throws IOException {
        GameEngine gameEngine = new GameEngine();
        gameEngine.start();
    }
}
