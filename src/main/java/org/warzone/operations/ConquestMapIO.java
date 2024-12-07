package org.warzone.operations;

import org.warzone.entities.Continent;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The DominationMapIO class provides methods for loading and processing game maps of Domination format from files.
 */
public class ConquestMapIO extends GameMapIO{

    private static StringBuilder convertConquestToDomination(StringBuilder conquestData) {
        StringBuilder dominationData = new StringBuilder();
        Map<String, Integer> continentValues = new HashMap<>();
        Map<String, Integer> countryIdMap = new LinkedHashMap<>();
        List<String> bordersData = new ArrayList<>();

        String[] lines = conquestData.toString().split("\n");
        boolean processContinents = false, processTerritories = false;
        int countryIdCounter = 1;

        for (String line : lines) {
            if (line.startsWith("[Continents]")) {
                dominationData.append("[continents]\n");
                processContinents = true;
                processTerritories = false;
            } else if (line.startsWith("[Territories]")) {
                dominationData.append("\n[countries]\n");
                processContinents = false;
                processTerritories = true;
                continue;
            }

            if (processContinents && !line.trim().isEmpty() && !line.startsWith("[")) {
                // Process continent line
                String[] parts = line.split("=");
                dominationData.append(parts[0].trim().replace(" ", "_"))
                        .append(" ")
                        .append(parts[1].trim())
                        .append(" white\n"); // Adding default color "white"
                continentValues.put(parts[0].trim(), continentValues.size() + 1);
            } else if (processTerritories && !line.trim().isEmpty() && !line.startsWith("[")) {
                // Process territory line
                String[] parts = line.split(",");
                // Replace spaces with underscores in country names
                String countryName = parts[0].trim().replace(" ", "_");
                int x = Integer.parseInt(parts[1].trim());
                int y = Integer.parseInt(parts[2].trim());
                String continentName = parts[3].trim();
                Integer continentId = continentValues.get(continentName);
                countryIdMap.put(countryName, countryIdCounter);

                dominationData.append(countryIdCounter)
                        .append(" ")
                        .append(countryName)
                        .append(" ")
                        .append(continentId)
                        .append(" ")
                        .append(x)
                        .append(" ")
                        .append(y)
                        .append("\n");
                countryIdCounter++;

                // Prepare borders data
                StringBuilder borderLine = new StringBuilder(countryIdMap.size() + " ");
                for (int i = 4; i < parts.length; i++) {
                    String adjacentCountry = parts[i].trim().replace(" ", "_");
                    borderLine.append("{" + adjacentCountry + "} ");
                }
                bordersData.add(borderLine.toString());
            }
        }

        // Add borders section
        dominationData.append("\n[borders]\n");
        for (String border : bordersData) {
            String[] borderParts = border.split(" ");
            dominationData.append(borderParts[0]); // Country ID
            for (int i = 1; i < borderParts.length; i++) {
                String adjacentCountry = borderParts[i].replace("{", "").replace("}", "");
                if (countryIdMap.containsKey(adjacentCountry)) {
                    dominationData.append(" ").append(countryIdMap.get(adjacentCountry));
                }
            }
            dominationData.append("\n");
        }

        return dominationData;
    }

    /**
     * {@inheritDoc}
     */
    private static StringBuilder convertDominationToConquest(StringBuilder dominationData) {
        StringBuilder conquestData = new StringBuilder();
        Map<Integer, String> countryIdMap = new LinkedHashMap<>();
        Map<String, String> continentNames = new HashMap<>(); // Continent name mapping
        Map<Integer, String> countryToContinentMap = new HashMap<>(); // Country ID to continent name mapping
        Map<Integer, List<Integer>> borderMap = new LinkedHashMap<>();
        Map<Integer, String> countryCoordinates = new LinkedHashMap<>();
        Map<String, String> continentValues = new HashMap<>(); // Continent values

        String[] lines = dominationData.toString().split("\n");
        boolean processContinents = false, processCountries = false, processBorders = false;

        for (String line : lines) {
            if (line.startsWith("[continents]")) {
                conquestData.append("[Continents]\n");
                processContinents = true;
                processCountries = false;
                processBorders = false;
            } else if (line.startsWith("[countries]")) {
                processContinents = false;
                processCountries = true;
                processBorders = false;
            } else if (line.startsWith("[borders]")) {
                processContinents = false;
                processCountries = false;
                processBorders = true;
            } else if (processContinents && !line.trim().isEmpty()) {
                // Process continent line
                String[] parts = line.split(" ");
                continentValues.put(parts[0], parts[1]);
                continentNames.put(parts[0], parts[0]);
            } else if (processCountries && !line.trim().isEmpty()) {
                // Process country line
                String[] parts = line.split(" ");
                int countryId = Integer.parseInt(parts[0].trim());
                String countryName = parts[1].trim();
                String continentId = parts[2].trim();
                String coordinates = parts.length > 4 ? parts[3].trim() + "," + parts[4].trim() : "0,0";
                countryIdMap.put(countryId, countryName);
                countryCoordinates.put(countryId, coordinates);
                countryToContinentMap.put(countryId, continentId);
            } else if (processBorders && !line.trim().isEmpty()) {
                // Process border line
                String[] parts = line.split(" ");
                int countryId = Integer.parseInt(parts[0].trim());
                List<Integer> borders = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    borders.add(Integer.parseInt(parts[i].trim()));
                }
                borderMap.put(countryId, borders);
            }
        }

        // Continents section
        for (Map.Entry<String, String> entry : continentValues.entrySet()) {
            conquestData.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        conquestData.append("[Territories]\n");
        // Territories section
        for (Map.Entry<Integer, String> entry : countryIdMap.entrySet()) {
            Integer countryId = entry.getKey();
            String countryName = entry.getValue();
            String continentName = continentNames.get(countryToContinentMap.get(countryId));
            String coordinates = countryCoordinates.get(countryId);
            conquestData.append(countryName).append(",").append(coordinates).append(",").append(continentName);

            // Add borders
            List<Integer> borders = borderMap.get(countryId);
            if (borders != null) {
                for (Integer borderId : borders) {
                    conquestData.append(",").append(countryIdMap.get(borderId));
                }
            }
            conquestData.append("\n");
        }

        return conquestData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameMap loadGameMapToEdit(StringBuilder p_fileContents) {
        p_fileContents = convertConquestToDomination(p_fileContents);
        String[] l_fileContentsArray = p_fileContents.toString().replaceAll("\r", "").split("\n");
        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setContinentMap(getContinentMap(l_fileContentsArray));
        l_gameMap.setCountryMap(getCountryMap(l_fileContentsArray, l_gameMap.getContinentMap()));
        l_gameMap.setBorderMap(getBorderMap(l_fileContentsArray));
        System.out.println("\nMap ready to edit\n");
        return l_gameMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameMap loadGameMap(StringBuilder p_fileContents) throws IOException {
        p_fileContents = convertConquestToDomination(p_fileContents);
        String[] l_fileContentsArray = p_fileContents.toString().replaceAll("\r", "").split("\n");
        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setContinentMap(getContinentMap(l_fileContentsArray));
        l_gameMap.setCountryMap(loadMap(l_gameMap.getFileName()));
        l_gameMap.setBorderMap(getBorderMap(l_fileContentsArray));
        return l_gameMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Country> loadMap(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", p_fileName + ".map");
        File l_file = new File(l_filePath.toString());
        BufferedReader l_br = new BufferedReader(new FileReader(l_file));
        StringBuilder l_fileContents = new StringBuilder();

        String l_currentLine;

        while ((l_currentLine = l_br.readLine()) != null) {
            l_fileContents.append(l_currentLine).append(System.lineSeparator());
        }

        l_fileContents = convertConquestToDomination(l_fileContents);

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
     * {@inheritDoc}
     */
    @Override
    public void saveGameMap(String p_fileName) throws IOException {
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

        l_mapFileWriter.write(String.valueOf(convertDominationToConquest(l_fileContents)));
        l_mapFileWriter.close();
    }
}
