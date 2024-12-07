package org.warzone.operations;

import org.warzone.entities.Continent;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The GameMapIO class provides methods for loading and processing game maps of format from files.
 */
public abstract class GameMapIO {

    static final String USER_DIR = "user.dir";
    static final String SRC = "src";
    static final String MAIN = "main";
    static final String RESOURCES = "resources";
    /**
     * Loads a game map from a file and returns a map of countries.
     *
     * @param p_fileName The name of the map file to load.
     * @return A map of countries loaded from the map file.
     * @throws IOException If an I/O error occurs while loading the map.
     */

    public Map<String, Country> loadMap(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", p_fileName + ".map");
        File l_file = new File(l_filePath.toString());
        BufferedReader l_br = new BufferedReader(new FileReader(l_file));
        StringBuilder l_fileContents = new StringBuilder();

        String l_currentLine;

        while ((l_currentLine = l_br.readLine()) != null) {
            l_fileContents.append(l_currentLine).append(System.lineSeparator());
        }

        String[] l_fileContentsArray = l_fileContents.toString().replaceAll("\r", "").split("\n");
        int l_lineOffSetCounter = 0;

        Map<String, Continent> l_continentMap = getContinentMap(l_fileContentsArray);

        Map<String, Country> l_countryList = getCountryMap(l_fileContentsArray, l_continentMap);

        // finding [borders] identifier
        while (l_lineOffSetCounter < l_fileContentsArray.length && (l_currentLine = l_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.contains("[borders]")) {
                break;
            }
        }

        // saving adjacent country information
        while (l_lineOffSetCounter < l_fileContentsArray.length && (l_currentLine = l_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.strip().isBlank()) {
                break;
            }

            String[] l_borderElements = l_currentLine.split(" ");

            Map<String, Country> l_adjacentCountries = new HashMap<>();

            for (int i = 1; i < l_borderElements.length; i++) {
                l_adjacentCountries.put(l_borderElements[i], l_countryList.get(l_borderElements[i]));
            }


            l_countryList.get(l_borderElements[0]).setAdjacentCountries(l_adjacentCountries);
        }

        return l_countryList;
    }

    /**
     * Loads the contents of a file.
     *
     * @param p_fileName Name of file
     * @return A StringBuilder containing the file contents.
     * @throws IOException If an I/O error occurs while loading the file.
     */

    public StringBuilder loadFile(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", p_fileName + ".map");
        GameMap.getInstance().setFileName(p_fileName);
        File file = new File(l_filePath.toString());
        BufferedReader l_br = new BufferedReader(new FileReader(file));
        StringBuilder l_fileContents = new StringBuilder();

        String l_currentLine;

        while ((l_currentLine = l_br.readLine()) != null) {
            l_fileContents.append(l_currentLine).append(System.lineSeparator());
        }
        return l_fileContents;
    }

    /**
     * Retrieves a map of countries from the loaded map file contents.
     *
     * @param p_fileContentsArray The array of lines from the map file contents.
     * @param p_continentMap      A map of continents for reference.
     * @return A map of countries parsed from the map file.
     */

    protected Map<String, Country> getCountryMap(String[] p_fileContentsArray, Map<String, Continent> p_continentMap) {
        Map<String, Country> l_countryMap = new HashMap<>();
        String l_currentLine;
        int l_lineOffSetCounter = 0;

        // finding [countries] identifier
        while (l_lineOffSetCounter < p_fileContentsArray.length && (l_currentLine = p_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.contains("[countries]")) {
                break;
            }
        }

        // saving country details in a hashmap
        while (l_lineOffSetCounter < p_fileContentsArray.length && (l_currentLine = p_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.strip().isBlank()) {
                break;
            }

            String[] l_countryElements = l_currentLine.split(" ");

            Country l_country = new Country();
            l_country.setIndex(Integer.parseInt(l_countryElements[0]))
                    .setName(l_countryElements[1])
                    .setContinent(p_continentMap.get(l_countryElements[2]));
            if (l_countryElements.length == 5) {
                l_country.setX(Integer.parseInt(l_countryElements[3]));
                l_country.setY(Integer.parseInt(l_countryElements[4]));
            }

            l_countryMap.put(l_countryElements[0], l_country);
        }
        return l_countryMap;
    }

    /**
     * Retrieves a map of continents from the loaded map file contents.
     *
     * @param p_FileContentsArray The array of lines from the map file contents.
     * @return A map of continents parsed from the map file.
     */

    protected Map<String, Continent> getContinentMap(String[] p_FileContentsArray) {
        int l_lineOffSetCounter = 0;
        String l_currentLine;
        Map<String, Continent> l_continentMap = new HashMap<>();

        // finding [continent] identifier
        while (l_lineOffSetCounter < p_FileContentsArray.length && (l_currentLine = p_FileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.contains("[continents]")) {
                break;
            }
        }

        // saving continent details in a hashmap
        int l_counter = 1;
        while (l_lineOffSetCounter < p_FileContentsArray.length && (l_currentLine = p_FileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.strip().isBlank()) {
                break;
            }

            String[] l_continentElements = l_currentLine.split(" ");

            Continent l_continent = new Continent();
            l_continent.setIndex(l_counter);
            l_continent.setD_name(l_continentElements[0]);
            l_continent.setValue(l_continentElements[1]);
            l_continent.setColor(l_continentElements[2]);
            l_continentMap.put(String.valueOf(l_counter), l_continent);
            l_counter++;
        }

        return l_continentMap;
    }

    /**
     * Retrieves a map of borders (adjacent countries) from the loaded map file contents.
     *
     * @param l_fileContentsArray The array of lines from the map file contents.
     * @return A map of borders parsed from the map file.
     */

    protected Map<String, List<String>> getBorderMap(String[] l_fileContentsArray) {
        Map<String, List<String>> l_borderMap = new HashMap<>();

        String l_currentLine;
        int l_lineOffSetCounter = 0;
        while (l_lineOffSetCounter < l_fileContentsArray.length && (l_currentLine = l_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.contains("[borders]")) {
                break;
            }
        }

        // saving adjacent country information
        while (l_lineOffSetCounter < l_fileContentsArray.length && (l_currentLine = l_fileContentsArray[l_lineOffSetCounter++]) != null) {
            if (l_currentLine.strip().isBlank()) {
                break;
            }

            String[] l_borderElements = l_currentLine.split(" ");

            List<String> l_countryList = new ArrayList<>();

            l_countryList.addAll(Arrays.asList(l_borderElements).subList(1, l_borderElements.length));

            l_borderMap.put(l_borderElements[0], l_countryList);

        }
        GameMap l_gameMap = GameMap.getInstance();
        Map<String, Country> l_countryMap = l_gameMap.getCountryMap();
        l_countryMap.forEach((key, country) -> {
            Map<String, Country> l_adjacentCountriesMap = new HashMap<>();
            List<String> adjacentCountriesList = l_borderMap.get(key);
            for (String adjacentCountry: adjacentCountriesList) {
                l_adjacentCountriesMap.put(adjacentCountry, l_countryMap.get(adjacentCountry));
            }
            l_countryMap.get(key).setAdjacentCountries(l_adjacentCountriesMap);
        });

        return l_borderMap;

    }

    /**
     * Loads a game map for editing from the provided file contents and returns a GameMap object.
     *
     * @param p_fileContents The StringBuilder containing the map file contents.
     * @return A GameMap object representing the loaded map for editing.
     */

    public GameMap loadGameMapToEdit(StringBuilder p_fileContents) {
        String[] l_fileContentsArray = p_fileContents.toString().replaceAll("\r", "").split("\n");
        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setContinentMap(getContinentMap(l_fileContentsArray));
        l_gameMap.setCountryMap(getCountryMap(l_fileContentsArray, l_gameMap.getContinentMap()));
        l_gameMap.setBorderMap(getBorderMap(l_fileContentsArray));
        System.out.println("\nMap ready to edit\n");
        return l_gameMap;
    }

    /**
     * Loads a game map from the provided file contents and returns a GameMap object.
     *
     * @param p_fileContents The StringBuilder containing the map file contents.
     * @return A GameMap object representing the loaded map.
     * @throws IOException If an I/O error occurs while loading the map.
     */

    public GameMap loadGameMap(StringBuilder p_fileContents) throws IOException {
        String[] l_fileContentsArray = p_fileContents.toString().replaceAll("\r", "").split("\n");
        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setContinentMap(getContinentMap(l_fileContentsArray));
        l_gameMap.setCountryMap(loadMap(l_gameMap.getFileName()));
        l_gameMap.setBorderMap(getBorderMap(l_fileContentsArray));
        return l_gameMap;
    }

    /**
     * Loads a game map from the provided file contents and returns a GameMap object.
     *
     * @param p_fileName The file path of the save file.
     * @throws IOException If an I/O error occurs while opening the file.
     */
    public void saveGameMap(String p_fileName) throws IOException{
        GameMap l_gameMap = GameMap.getInstance();
        File l_textFile = new File(Paths.get(System.getProperty(USER_DIR), SRC, MAIN, RESOURCES, p_fileName + ".txt").toString());
        FileWriter l_writer = new FileWriter(l_textFile);
        StringBuilder l_fileContents = new StringBuilder();
        l_fileContents.append(System.lineSeparator()).append("[continents]").append(System.lineSeparator());

        l_gameMap.getContinentMap().forEach((key, continent) -> {
            l_fileContents.append(continent.getD_name()).append(" ").append(continent.getValue()).append(" ").append(continent.getColor()).append(System.lineSeparator());
        });

        l_fileContents.append(System.lineSeparator()).append("[countries]").append(System.lineSeparator());

        l_gameMap.getCountryMap().forEach((key, country) -> {
            l_fileContents.append(key).append(" ").append(country.getName()).append(" ").append(country.getContinent().getIndex()).append(System.lineSeparator());
        });

        l_fileContents.append(System.lineSeparator()).append("[borders]").append(System.lineSeparator());

        l_gameMap.getBorderMap().forEach((key, countryList) -> {
            l_fileContents.append(key);
            countryList.forEach(country -> {
                l_fileContents.append(" ").append(country);
            });
            l_fileContents.append(System.lineSeparator());
        });

        l_writer.write(String.valueOf(l_fileContents));
        l_writer.close();

        File l_mapFile = new File(Paths.get(System.getProperty(USER_DIR), SRC, MAIN, RESOURCES, p_fileName + ".map").toString());
        FileWriter l_mapFileWriter = new FileWriter(l_mapFile);

        l_mapFileWriter.write(String.valueOf(l_fileContents));
        l_mapFileWriter.close();

    }

    /**
     * Checks if the map file is of the "domination" format
     * @param p_mapFile The map file
     * @return a boolean that checks if the map file is of the "domination" format
     */
    public static boolean isDominationMap(File p_mapFile) {
        System.out.println(p_mapFile.toString());
        try (BufferedReader reader = new BufferedReader(new FileReader(p_mapFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("[countries]")) {
                    return true; // String found in the file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // String not found in the file
    }

    /**
     * Checks if the map file is of the "domination" format
     * @param p_fileName file name that is string
     * @return
     * a boolean that checks if the map file is of the "domination" format
     */
    public static boolean isDominationMap(String p_fileName) {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", p_fileName + ".map");
        File l_file = new File(l_filePath.toString());

        return isDominationMap(l_file);
    }
}
