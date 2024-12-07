package org.warzone.operations;

import org.warzone.entities.Country;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for validating the game map.
 */
public class ValidateMap {
    /**
     * Private constructor to prevent instantiation.
     */
    private ValidateMap() {
    }

    /**
     * Extracts and returns continent subgraphs from the given world map.
     *
     * @param p_worldMap The world map with countries.
     * @return A map with continent indices as keys and their corresponding countries as values.
     */
    public static Map<Integer, Map<String, Country>> getContinentSubgraphs(Map<String, Country> p_worldMap) {
        Map<Integer, Map<String, Country>> d_continentMap = p_worldMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getContinent().getIndex(),
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));
        return d_continentMap;
    }

    /**
     * Validates the given game map by checking its connectivity, continent subgraph connectivity,
     * and bidirectionality of borders.
     *
     * @param p_fileName The game map to validate.
     * @return true if the map is valid, false otherwise.
     * @throws IOException if there's an issue processing the map.
     */
    public static boolean isMapValid(String p_fileName) throws IOException {
        GameMapIO l_gameMapIO;
        System.out.println(p_fileName);
        if (GameMapIO.isDominationMap(p_fileName)) {
            l_gameMapIO = new DominationMapIO();
        } else {
            l_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
        }
        Map<String, Country> d_worldMap = l_gameMapIO.loadMap(p_fileName);
        Map<Integer, Map<String, Country>> d_continentMap = getContinentSubgraphs(d_worldMap);
        boolean l_isMapValid = true;
        if (!isWorldConnected(d_worldMap)) {
            l_isMapValid = false;
        }
        if (!areAllContinentsConnected(d_continentMap)) {
            l_isMapValid = false;
        }
        if (!areBordersBidirectional(d_worldMap)) {
            l_isMapValid = false;
        }
        return l_isMapValid;
    }

    /**
     * Checks if the provided map is valid.
     *
     * @param p_worldMap the map to be checked
     * @return true if the map is valid, otherwise false
     * @throws IOException if an I/O error occurs
     */
    public static boolean isMapValid(Map<String, Country> p_worldMap) throws IOException {
        Map<Integer, Map<String, Country>> d_continentMap = getContinentSubgraphs(p_worldMap);
        boolean l_isMapValid = true;
        if (!isWorldConnected(p_worldMap)) {
            l_isMapValid = false;
        }
        if (!areAllContinentsConnected(d_continentMap)) {
            l_isMapValid = false;
        }
        if (!areBordersBidirectional(p_worldMap)) {
            l_isMapValid = false;
        }
        return l_isMapValid;
    }

    /**
     * Recursively traverses the graph to identify connected components using DFS.
     *
     * @param p_country     The country to start the DFS from.
     * @param p_countryList The list of countries to consider.
     * @param p_visited     Map to track which countries have been visited.
     * @throws IOException if there's an issue processing the map.
     */
    private static void dfs(String p_country, Map<String, Country> p_countryList, Map<String, Boolean> p_visited) {
        if (Boolean.TRUE.equals(p_visited.get(p_country))) {
            return;
        }
        p_visited.put(p_country, Boolean.TRUE);
        if (p_countryList.get(p_country) != null) {
            for (Map.Entry<String, Country> l_adjacentEntry : p_countryList.get(p_country).getAdjacentCountries().entrySet()) {
                String l_adjacentCountryName = l_adjacentEntry.getKey();
                Boolean l_isVisited = p_visited.get(l_adjacentCountryName);
                if (l_isVisited == null) {
                    dfs(l_adjacentCountryName, p_countryList, p_visited);
                }
            }
        }
    }

    /**
     * Checks if the provided country graph is fully connected.
     *
     * @param p_countryList The list of countries to verify connectivity.
     * @return true if the graph is connected, false otherwise.
     * @throws IOException if there's an issue processing the map.
     */
    private static boolean isGraphConnected(Map<String, Country> p_countryList) {
        if (!p_countryList.isEmpty()) {
            String l_sourceCountry = p_countryList.keySet().iterator().next();
            Map<String, Boolean> l_visited = new HashMap<>();
            dfs(l_sourceCountry, p_countryList, l_visited);
            for (String l_countryName : p_countryList.keySet()) {
                if (l_visited.get(l_countryName) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the entire world map is connected.
     * <p>
     * The world map is considered connected if all its countries are interconnected,
     * allowing traversal from any one country to any other country in the world.
     * </p>
     *
     * @param d_worldMap The world map with countries.
     * @return true if the entire world map is connected, false otherwise.
     */
    public static boolean isWorldConnected(Map<String, Country> d_worldMap) {
        if (!isGraphConnected(d_worldMap)) {
            System.out.println("INVALID MAP. The world is not connected");
            return false;
        }
        return true;
    }

    /**
     * Checks if all continents in the given map are connected.
     * <p>
     * A continent is considered connected if all its countries are interconnected
     * and can be traversed from any one country to any other country within the same continent.
     * </p>
     *
     * @param d_continentMap A map with continent indices as keys and their corresponding countries as values.
     * @return true if all continents are connected, false otherwise.
     */
    public static boolean areAllContinentsConnected(Map<Integer, Map<String, Country>> d_continentMap) {
        for (Map.Entry<Integer, Map<String, Country>> entry : d_continentMap.entrySet()) {
            Integer l_continentKey = entry.getKey();
            Map<String, Country> l_continentCountries = entry.getValue();
            if (!isGraphConnected(l_continentCountries)) {
                System.out.println("INVALID MAP. Continent " +
                        l_continentCountries.values().iterator().next().getContinent().getD_name() +
                        " is not connected.");
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that borders between countries are bidirectional.
     * <p>
     * This ensures that if country A lists country B as adjacent, then country B
     * should also list country A as adjacent.
     * </p>
     *
     * @param d_worldMap The world map with countries.
     * @return true if all borders are bidirectional, false otherwise.
     */
    public static boolean areBordersBidirectional(Map<String, Country> d_worldMap) {
        for (Map.Entry<String, Country> l_entry : d_worldMap.entrySet()) {
            String l_countryName = l_entry.getKey();
            Country l_country = l_entry.getValue();

            for (String l_adjacentCountryName : l_country.getAdjacentCountries().keySet()) {
                Country l_adjacentCountry = d_worldMap.get(l_adjacentCountryName);

                // If the adjacent country is not in world map or doesn't list the original country as adjacent
                if (l_adjacentCountry == null || !l_adjacentCountry.getAdjacentCountries().containsKey(l_countryName)) {
                    System.out.println(("INVALID MAP. " + d_worldMap.get(l_countryName).getName() +
                            " and " + d_worldMap.get(l_adjacentCountryName).getName() +
                            " do not have bidirectional adjacency."));
                    return false;
                }
            }
        }
        // If loop completes without failing, the bidirectionality is maintained for all countries
        return true;
    }
}
