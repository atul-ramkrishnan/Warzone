package org.warzone.states;

import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.operations.ConquestMapIO;
import org.warzone.operations.GameMapIO;
import org.warzone.savegame.GameStateManager;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;
import org.warzone.operations.DominationMapIO;
import org.warzone.operations.ValidateMap;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * The {@code IssueOrderPhase} class represents the phase when the map is loaded and edited.
 */
public class PreLoadPhase extends StartUpPhase {

    private static final String USER_DIR = "user.dir";
    private static final String SRC = "src";
    private static final String MAIN = "main";
    private static final String RESOURCES = "resources";

    /**
     * Constructor for {@code PreLoadPhase}
     */
    public PreLoadPhase() {
        System.out.println("Phase changed to pre-load.");
        System.out.println("Available commands in this phase : ");
        System.out.println("1. editmap\n2. loadmap\n3. loadgame");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMap() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMapWithPlayers(List<Player> p_players) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editMap(String p_fileName) {
        GameMap l_gameMap = GameMap.getInstance();
        File l_mapFile = new File(Paths.get(System.getProperty(USER_DIR), SRC, MAIN, RESOURCES, p_fileName + ".map").toString());

        if (l_mapFile.exists()) {
            try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(l_mapFile))) {
                StringBuilder l_fileContents = new StringBuilder();

                String l_currentLine = l_bufferedReader.readLine();

                while (l_currentLine != null) {
                    l_fileContents.append(l_currentLine).append(System.lineSeparator());
                    l_currentLine = l_bufferedReader.readLine();
                }
                GameMapIO l_gameMapIO;
                if (GameMapIO.isDominationMap(l_mapFile)) {
                    l_gameMapIO = new DominationMapIO();
                } else {
                    l_gameMapIO = new ConquestMapIO();
                }

                l_gameMap = l_gameMapIO.loadGameMapToEdit(l_fileContents);
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("File Not Found");
            } catch (IOException ioException) {
                System.out.println("Internal Error Occurred");
            }

        }

        System.out.println("\nGame phase changed to EditMapPhase\n");
        gameEngine.setGamePhase(new EditMapPhase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editContinent(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editCountry(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editNeighbor(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMap(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(String p_fileName) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMap(String p_fileName) throws IOException {
        File file = new File(Paths.get(System.getProperty(USER_DIR), SRC, MAIN, RESOURCES, p_fileName + ".map").toString());
        if (!file.exists()) {
            System.out.println("Map not found!");
            return;
        }

        try {
            if (!ValidateMap.isMapValid(p_fileName)) {
                System.out.println("Invalid map cannot be loaded");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameMapIO l_gameMapIO;
        if (GameMapIO.isDominationMap(file)) {
            l_gameMapIO = new DominationMapIO();
        }
        else {
            l_gameMapIO = new ConquestMapIO();
        }
        StringBuilder l_fileContents = l_gameMapIO.loadFile(p_fileName);
        GameMap gameMap = l_gameMapIO.loadGameMap(l_fileContents);
        System.out.println("\nMap Loaded\n");
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.PRELOAD, p_fileName + " map loaded."));
        gameEngine.setGamePhase(new PlaySetupPhase());
    }

    @Override
    public void saveGame(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java","org","warzone","savegame", p_fileName + ".json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.saveGameState(gameEngine);
    }

    @Override
    public void loadGame(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java","org","warzone","savegame", p_fileName + ".json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.loadGameState(gameEngine);
    }
}
