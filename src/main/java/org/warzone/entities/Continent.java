package org.warzone.entities;

/**
 * The Continent class represents a continent in a game map.
 */

public class Continent {
    private int d_index;
    private String d_name;
    private String d_value;
    private String d_color;

    /**
     * Constructs a new Continent object.
     */

    public Continent() {

    }

    /**
     * Gets the index of the continent.
     *
     * @return The index of the continent.
     */

    public int getIndex() {
        return d_index;
    }

    /**
     * Gets the name of the continent.
     *
     * @return The name of the continent.
     */

    public String getD_name() {
        return d_name;
    }

    /**
     * Gets the color associated with the continent.
     *
     * @return The color of the continent.
     */

    public String getColor() {
        return d_color;
    }

    /**
     * Sets the index of the continent.
     *
     * @param index The index of the continent.
     * @return The modified Continent object.
     */

    public Continent setIndex(int index) {
        this.d_index = index;
        return this;
    }

    /**
     * Sets the name of the continent.
     *
     * @param d_name The name of the continent.
     * @return The modified Continent object.
     */

    public Continent setD_name(String d_name) {
        this.d_name = d_name;
        return this;
    }

    /**
     * Sets the color of the continent.
     *
     * @param color The color of the continent.
     * @return The modified Continent object.
     */

    public Continent setColor(String color) {
        this.d_color = color;
        return this;
    }

    /**
     * Gets the value associated with the continent.
     *
     * @return The value of the continent.
     */

    public String getValue() {
        return d_value;
    }

    /**
     * Sets the value of the continent.
     *
     * @param value The value of the continent.
     * @return The modified Continent object.
     */

    public Continent setValue(String value) {
        this.d_value = value;
        return this;
    }
}
