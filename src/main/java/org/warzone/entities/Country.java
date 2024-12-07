package org.warzone.entities;

import org.warzone.strategy.Player;

import java.util.Map;

/**
 * The Country class represents a country or territory in a game map.
 */

public class Country {
    private int d_index;
    private String d_name;
    private Map<String, Country> d_adjacentCountries;
    private Continent d_continent;
    private int d_noOfArmies;
    private int d_x;
    private int d_y;

    /**
     * Gets the owner of the country in the game.
     *
     * @return The player who owns the country.
     */

    public Player getCountryOwner() {
        return countryOwner;
    }

    /**
     * Sets the owner of the country in the game.
     *
     * @param countryOwner The player who owns the country.
     */

    public void setCountryOwner(Player countryOwner) {
        this.countryOwner = countryOwner;
    }

    private Player countryOwner;

    /**
     * Constructs a new Country object.
     */

    public Country() {
        d_noOfArmies = 0;
    }

    /**
     * Gets the index of the country.
     *
     * @return The index of the country.
     */

    public int getIndex() {
        return d_index;
    }

    /**
     * Returns the name associated with this instance.
     *
     * @return the name
     */
    public String getName() {
        return d_name;
    }

    /**
     * Gets a map of adjacent countries.
     *
     * @return A map of adjacent countries.
     */

    public Map<String, Country> getAdjacentCountries() {
        return d_adjacentCountries;
    }

    /**
     * Gets the continent to which the country belongs.
     *
     * @return The continent of the country.
     */

    public Continent getContinent() {
        return d_continent;
    }

    /**
     * Gets the X-coordinate of the country on a map.
     *
     * @return The X-coordinate of the country.
     */

    public int getX() {
        return d_x;
    }

    /**
     * Gets the Y-coordinate of the country on a map.
     *
     * @return The Y-coordinate of the country.
     */

    public int getY() {
        return d_y;
    }

    /**
     * Sets the index of the country.
     *
     * @param index The index of the country.
     * @return The modified Country object.
     */

    public Country setIndex(int index) {
        this.d_index = index;
        return this;
    }

    /**
     * Sets the name of the country.
     *
     * @param name The name of the country.
     * @return The modified Country object.
     */

    public Country setName(String name) {
        this.d_name = name;
        return this;
    }

    /**
     * Sets the map of adjacent countries.
     *
     * @param adjacentCountries A map of adjacent countries.
     * @return The modified Country object.
     */

    public Country setAdjacentCountries(Map<String, Country> adjacentCountries) {
        this.d_adjacentCountries = adjacentCountries;
        return this;
    }

    /**
     * Sets the continent to which the country belongs.
     *
     * @param continent The continent of the country.
     * @return The modified Country object.
     */

    public Country setContinent(Continent continent) {
        this.d_continent = continent;
        return this;
    }

    /**
     * Sets the X-coordinate of the country on a map.
     *
     * @param x The X-coordinate of the country.
     * @return The modified Country object.
     */

    public Country setX(int x) {
        this.d_x = x;
        return this;
    }

    /**
     * Sets the Y-coordinate of the country on a map.
     *
     * @param y The Y-coordinate of the country.
     * @return The modified Country object.
     */

    public Country setY(int y) {
        this.d_y = y;
        return this;
    }

    /**
     * Gets the number of armies present in the country.
     *
     * @return The number of armies in the country.
     */

    public int getD_noOfArmies() {
        return d_noOfArmies;
    }

    /**
     * Sets the number of armies in the country.
     *
     * @param d_noOfArmies The number of armies to set.
     * @return The modified Country object.
     */

    public Country setD_noOfArmies(int d_noOfArmies) {
        this.d_noOfArmies = d_noOfArmies;
        return this;
    }
}
