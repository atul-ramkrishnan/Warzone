package org.warzone.states;

import org.warzone.GameEngine;
import org.warzone.entities.Continent;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.operations.ConquestMapIO;
import org.warzone.operations.DominationMapIO;
import org.warzone.operations.GameMapIO;
import org.warzone.savegame.GameStateManager;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;
import org.warzone.operations.ValidateMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code EditMapPhase}  class represents the phase where the user can edit the map.
 */
public class EditMapPhase extends StartUpPhase {

    private static final String USER_DIR = "user.dir";
    private static final String SRC = "src";
    private static final String MAIN = "main";
    private static final String RESOURCES = "resources";
    private static final String COUNTRY_NOT_EXISTS = "Provided country number does not exist";
    private static final String VALID_MAP = "The map is valid";
    private static final String INVALID_MAP = "The map is in-valid";


    /**
     * Constructor for EditMapPhase.
     */
    public EditMapPhase() {
        System.out.println("Phase changed to edit-map.");
        System.out.println("Available commands in this phase : ");
        System.out.println("1. showmap\n2. editcontinent\n3. editcountry\n4. editneighbor\n5. validatemap\n6. savemap");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMap() {
        GameMap l_gameMap = GameMap.getInstance();
        System.out.println("### Loading Map ###");

        System.out.println("\n[continents]");
        l_gameMap.getContinentMap().forEach((l_key, l_continent) -> {
            System.out.println(l_continent.getD_name() + " " + l_continent.getValue() + " " + l_continent.getColor());
        });

        System.out.println("\n[countries]");
        l_gameMap.getCountryMap().forEach((l_key, l_country) -> {
            System.out.println(l_key + " " + l_country.getName() + " " + l_country.getContinent().getIndex());
        });

        System.out.println("[borders]");
        l_gameMap.getBorderMap().forEach((l_key, l_borderList) -> {
            System.out.print("\n" + l_key);
            for (String border : l_borderList) {
                System.out.print(" " + border);
            }
        });
        System.out.println();
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
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editContinent(String[] p_mapEditCommands) {
        GameMap l_gameMap = GameMap.getInstance();
        for (int i = 1; i < p_mapEditCommands.length; i++) {
            if (p_mapEditCommands[i].equals("-add") && i < p_mapEditCommands.length - 2) {
                l_gameMap.getContinentMap().put(p_mapEditCommands[i + 1],
                        new Continent()
                                .setIndex(Integer.parseInt(p_mapEditCommands[i + 1]))
                                .setD_name("Continent" + p_mapEditCommands[i + 1])
                                .setValue(p_mapEditCommands[i + 1])
                                .setColor("white")
                );
                System.out.println("\nContinent Added\n");
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EDIT_MAP,
                                        p_mapEditCommands[i + 1] + " added as a continent."));
            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                l_gameMap.getContinentMap().remove(p_mapEditCommands[i + 1]);
                System.out.println("\nContinent Removed\n");
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EDIT_MAP,
                        p_mapEditCommands[i + 1] + " removed from continents."));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editCountry(String[] p_mapEditCommands) {
        GameMap l_gameMap = GameMap.getInstance();
        for (int i = 1; i < p_mapEditCommands.length; i++) {
            if (p_mapEditCommands[i].equals("-add") && i < p_mapEditCommands.length - 2) {

                if (l_gameMap.getContinentMap().get(p_mapEditCommands[i + 2]) == null) {
                    System.out.println("Continent Not Found");
                    break;
                } else {
                    l_gameMap.getCountryMap().put(p_mapEditCommands[i + 1],
                            new Country()
                                    .setIndex(Integer.parseInt(p_mapEditCommands[i + 1]))
                                    .setName("Country" + p_mapEditCommands[i + 1])
                                    .setContinent(l_gameMap.getContinentMap().get(p_mapEditCommands[i + 2]))
                    );
                    System.out.println("\nCountry Added\n");
                    GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EDIT_MAP,
                            p_mapEditCommands[i + 1] + " added as a country."));
                }

            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                l_gameMap.getCountryMap().remove(p_mapEditCommands[i + 1]);
                System.out.println("\nCountry Removed\n");
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EDIT_MAP,
                        p_mapEditCommands[i + 1] + " removed from countries."));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editNeighbor(String[] p_mapEditCommands) {
        GameMap l_gameMap = GameMap.getInstance();
        for (int i = 1; i < p_mapEditCommands.length; i++) {
            if (p_mapEditCommands[i].equals("-add") && i < p_mapEditCommands.length - 2) {
                if (l_gameMap.getCountryMap().get(p_mapEditCommands[i + 1]) == null) {
                    System.out.println(COUNTRY_NOT_EXISTS);
                    continue;
                }
                if (l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]) != null) {
                    l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]).add(p_mapEditCommands[i + 2]);
                    l_gameMap.getCountryMap()
                            .get(p_mapEditCommands[i + 1])
                            .getAdjacentCountries()
                            .put(p_mapEditCommands[i + 2], l_gameMap.getCountryMap().get(p_mapEditCommands[i + 2]));
                    System.out.println("\nNeighbor Added\n");
                } else {
                    List<String> l_countryList = new ArrayList<>();
                    l_countryList.add(p_mapEditCommands[i + 2]);
                    l_gameMap.getBorderMap().put(p_mapEditCommands[i + 1], l_countryList);
                    l_gameMap.getCountryMap()
                            .get(p_mapEditCommands[i + 1])
                            .setAdjacentCountries(new HashMap<>());
                    l_gameMap.getCountryMap()
                            .get(p_mapEditCommands[i + 1])
                            .getAdjacentCountries()
                            .put(p_mapEditCommands[i + 2], l_gameMap.getCountryMap().get(p_mapEditCommands[i + 2]));
                    System.out.println("\nNeighbor Added\n");
                }
            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                if (l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]) != null) {
                    l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]).remove(p_mapEditCommands[i + 2]);
                    System.out.println("\nNeighbor Removed\n");
                } else {
                    System.out.println(COUNTRY_NOT_EXISTS);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMap(String[] p_mapEditCommands) throws IOException {
        String l_format = "domination";
        if (p_mapEditCommands.length != 4) {
            System.out.println("savemap should be of the form savemap <filename> -format <format>");
        }
        if (p_mapEditCommands[2].equals("-format")) {
            GameMap gameMap = GameMap.getInstance();
            if (ValidateMap.isMapValid(gameMap.getCountryMap())) {
                writeMapToText(p_mapEditCommands[1], p_mapEditCommands[3]);
                System.out.println("\nMap Saved\n");
            } else {
                System.out.println(INVALID_MAP);
            }

            gameEngine.setGamePhase(new PreLoadPhase());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(String p_fileName) throws IOException {
        GameMap gameMap = GameMap.getInstance();
        if (ValidateMap.isMapValid(gameMap.getCountryMap())) {
            System.out.println(VALID_MAP);
        } else {
            System.out.println(INVALID_MAP);
        }
    }

    @Override
    public void loadMap(String p_fileName) throws IOException {

    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public static void writeMapToText(String p_fileName, String p_format) throws IOException {
        GameMapIO l_gameMapIO;
        if (p_format.equalsIgnoreCase("domination")) {
            l_gameMapIO = new DominationMapIO();

        } else if (p_format.equalsIgnoreCase("conquest")) {
            l_gameMapIO = new ConquestMapIO();

        } else {
            System.out.println("Wrong format provided.");
            return;
        }
        l_gameMapIO.saveGameMap(p_fileName);
    }
}
