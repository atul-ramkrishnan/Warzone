package org.warzone.additional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.warzone.entities.Country;
import org.warzone.operations.DominationMapIO;
import org.warzone.operations.ValidateMap;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * JUnit test class for validating various aspects of a loaded game map.
 * This includes checking the connectivity of the world, individual continents,
 * and the bidirectionality of country borders.
 */
public class ValidateMapTest {
    /** Represents the world map with countries as keys. */
    Map<String, Country> d_worldMap;

    /** Represents the continent map with continent indices as keys and their corresponding countries as values. */
    Map<Integer, Map<String, Country>> d_continentMap;

    /**
     * Initializes the world and continent maps before each test run.
     * Loads the map and populates the world and continent maps accordingly.
     *
     * @throws IOException if there's an issue loading the map.
     */
    @BeforeEach
    public void initializeMap() throws IOException {
        DominationMapIO l_dominationMapIO = new DominationMapIO();
        d_worldMap = l_dominationMapIO.loadMap("us");
        d_continentMap = ValidateMap.getContinentSubgraphs(d_worldMap);
    }

    /**
     * Clears the world and continent maps after each test run.
     */
    @AfterEach
    public void clearMaps() {
        d_worldMap = null;
        d_continentMap = null;
    }

    /**
     * This method tests the connectivity of the entire world map.
     * It checks whether the world map is connected.
     */
    @Test
    public void testWorldConnectivity() {
        assertTrue(ValidateMap.isWorldConnected(d_worldMap));
    }

    /**
     * This method tests the connectivity of all continents in the continent map.
     * It checks whether all continents are connected.
     */
    @Test
    public void testContinentConnectivity() {
        assertTrue(ValidateMap.areAllContinentsConnected(d_continentMap));
    }

    /**
     * This method tests the bidirectionality of borders in the world map.
     * It checks whether the borders are bidirectional.
     */
    @Test
    public void testBorderBidirectionality() {
        assertTrue(ValidateMap.areBordersBidirectional(d_worldMap));
    }
}