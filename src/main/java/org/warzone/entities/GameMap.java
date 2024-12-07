package org.warzone.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GameMap class represents a game map, including countries, continents, and borders.
 */

public class GameMap {

    private String d_fileName;
    private Map<String, Country> d_countryMap;
    private Map<String, Continent> d_continentMap;
    private Map<String, List<String>> d_borderMap;
    private List<Country> d_NeutralTerritory;

    private static GameMap gameMap = null;

    private GameMap() {
        d_fileName = "";
        d_continentMap = new HashMap<>();
        d_countryMap = new HashMap<>();
        d_borderMap = new HashMap<>();
        d_NeutralTerritory = new ArrayList<>();
    }

    /**
     * Get the instance of the GameMap (Singleton pattern).
     *
     * @return The GameMap instance.
     */

    public static GameMap getInstance() {
        if (gameMap == null) {
            gameMap = new GameMap();
        }
        return gameMap;
    }

    /**
     * Get the file name associated with the game map.
     *
     * @return The file name.
     */

    public String getFileName() {
        return d_fileName;
    }

    /**
     * Set the file name associated with the game map.
     *
     * @param fileName The file name to set.
     */

    public void setFileName(String fileName) {
        this.d_fileName = fileName;
    }

    /**
     * Get the map of countries in the game map.
     *
     * @return A map of countries.
     */

    public Map<String, Country> getCountryMap() {
        return d_countryMap;
    }

    /**
     * Set the map of countries in the game map.
     *
     * @param countryMap The map of countries to set.
     */

    public void setCountryMap(Map<String, Country> countryMap) {
        this.d_countryMap = countryMap;
    }

    /**
     * Get the map of continents in the game map.
     *
     * @return A map of continents.
     */

    public Map<String, Continent> getContinentMap() {
        return d_continentMap;
    }

    /**
     * Set the map of continents in the game map.
     *
     * @param continentMap The map of continents to set.
     */

    public void setContinentMap(Map<String, Continent> continentMap) {
        this.d_continentMap = continentMap;
    }

    /**
     * Get the map of borders between countries in the game map.
     *
     * @return A map of borders.
     */

    public Map<String, List<String>> getBorderMap() {
        return d_borderMap;
    }

    /**
     * Set the map of borders between countries in the game map.
     *
     * @param borderMap The map of borders to set.
     */

    public void setBorderMap(Map<String, List<String>> borderMap) {
        this.d_borderMap = borderMap;
    }

    /**
     * Sets neutral territories
     * @param d_NeutralTerritory The countries that are neutral
     */
    public void setD_NeutralTerritory(List<Country> d_NeutralTerritory) {
        this.d_NeutralTerritory = d_NeutralTerritory;
    }

    /**
     * Retrieves the list of neutral territories associated with this instance.
     *
     * @return the list of neutral territories
     */
    public List<Country> getD_NeutralTerritory() {
        return d_NeutralTerritory;
    }
}
