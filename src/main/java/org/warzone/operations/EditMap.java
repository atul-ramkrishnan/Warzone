package org.warzone.operations;

import org.warzone.entities.Continent;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The EditMap class provides methods for editing and managing game maps.
 */
public class EditMap {
    private static final String USER_DIR = "user.dir";
    private static final String SRC = "src";
    private static final String MAIN = "main";
    private static final String RESOURCES = "resources";
    private static final String EDIT_CONTINENT = "editcontinent";
    private static final String EDIT_COUNTRY = "editcountry";
    private static final String EDIT_NEIGHBOR = "editneighbor";
    private static final String SHOW_MAP = "showmap";
    private static final String VALIDATE_MAP = "validatemap";
    private static final String VALID_MAP = "The map is valid";
    private static final String INVALID_MAP = "The map is in-valid";
    private static final String SAVE_MAP = "savemap";
    private static final String COUNTRY_NOT_EXISTS = "Provided country number does not exist";


    /**
     * Edits a game map by processing user commands.
     *
     * @param p_fileName The name of the map file to edit.
     * @param p_sc       A Scanner for user input.
     * @throws IOException If an I/O error occurs while editing the map.
     */
    public static void editMap(String p_fileName, Scanner p_sc) throws IOException {

        System.out.println("Map editor commands: \r\n"
                + "editcontinent -add continentID continentvalue -remove continentID\r\n"
                + "editcountry -add countryID continentID -remove countryID\r\n"
                + "editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID");

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
                    l_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
                }
                l_gameMap = l_gameMapIO.loadGameMapToEdit(l_fileContents);
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("File Not Found");
            } catch (IOException ioException) {
                System.out.println("Internal Error Occurred");
            }

        }

        String[] l_mapEditCommands = "Y".split("");
        while (!l_mapEditCommands[0].equalsIgnoreCase("N")) {

            System.out.println("Enter map-edit commands here or press N to exit.");
            l_mapEditCommands = p_sc.nextLine().split(" ");
            if (!l_mapEditCommands[0].equalsIgnoreCase("N")) {
                switch (l_mapEditCommands[0]) {

                    case SHOW_MAP -> {
                        ShowMap.showMap();
                    }

                    case EDIT_COUNTRY -> {
                        editCountry(l_mapEditCommands);
                    }

                    case EDIT_CONTINENT -> {
                        editContinent(l_mapEditCommands);
                    }

                    case EDIT_NEIGHBOR -> {
                        editNeighbor(l_mapEditCommands);
                    }

                    case VALIDATE_MAP -> {
                        validateMap(p_fileName);
                    }
                    case SAVE_MAP -> {
                        if (l_mapEditCommands.length != 4) {
                            System.out.println("Invalid command!");
                            continue;
                        }
                        writeMapToText(l_mapEditCommands[1]);
                        return;
                    }

                    default -> {
                        System.out.println("Invalid command");
                    }
                }
            }
        }
    }

    /**
     * Validates the map based on the provided file name.
     *
     * @param p_fileName the name of the file to validate
     * @throws IOException if an I/O error occurs during the process
     */
    private static void validateMap(String p_fileName) throws IOException {
        if (ValidateMap.isMapValid(p_fileName)) {
            System.out.println(VALID_MAP);
        } else {
            System.out.println(INVALID_MAP);
        }
    }

    /**
     * Edits the neighbors of countries based on the provided map edit commands.
     *
     * @param p_mapEditCommands the array of map edit commands to execute
     */
    private static void editNeighbor(String[] p_mapEditCommands) {
        GameMap l_gameMap = GameMap.getInstance();
        for (int i = 1; i < p_mapEditCommands.length; i++) {
            if (p_mapEditCommands[i].equals("-add") && i < p_mapEditCommands.length - 2) {
                if (l_gameMap.getCountryMap().get(p_mapEditCommands[i + 1]) == null) {
                    System.out.println(COUNTRY_NOT_EXISTS);
                    continue;
                }
                if (l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]) != null) {
                    l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]).add(p_mapEditCommands[i + 2]);
                } else {
                    List<String> l_countryList = new ArrayList<>();
                    l_countryList.add(p_mapEditCommands[i + 2]);
                    l_gameMap.getBorderMap().put(p_mapEditCommands[i + 1], l_countryList);
                }
            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                if (l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]) != null) {
                    l_gameMap.getBorderMap().get(p_mapEditCommands[i + 1]).remove(p_mapEditCommands[i + 2]);
                } else {
                    System.out.println(COUNTRY_NOT_EXISTS);
                }
            }
        }
    }

    /**
     * Edits the continents based on the provided map edit commands.
     *
     * @param p_mapEditCommands the array of map edit commands to execute
     */
    private static void editContinent(String[] p_mapEditCommands) {
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
            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                l_gameMap.getContinentMap().remove(p_mapEditCommands[i + 1]);
            }
        }
    }

    private static void editCountry(String[] p_mapEditCommands) {
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
                }

            } else if (p_mapEditCommands[i].equals("-remove") && i < p_mapEditCommands.length - 1) {
                l_gameMap.getCountryMap().remove(p_mapEditCommands[i + 1]);
            }
        }
    }

    /**
     * Writes the edited game map to a text file.
     *
     * @param p_fileName The name of the output text file.
     * @throws IOException If an I/O error occurs while writing the map to a file.
     */
    public static void writeMapToText(String p_fileName) throws IOException {
        GameMap l_gameMapInstance = GameMap.getInstance();
        GameMapIO l_gameMapIO;
        if (GameMapIO.isDominationMap(l_gameMapInstance.getFileName())) {
            l_gameMapIO = new DominationMapIO();
        } else {
            l_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
        }

        l_gameMapIO.saveGameMap(p_fileName);

    }
}
