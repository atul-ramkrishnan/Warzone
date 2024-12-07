package org.warzone.operations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.entities.GameMap;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConquestMapIOAdapterTest {
    private static GameMap l_gameMap;
    private static GameMapIO d_gameMapIO;

    /**
     * Test the loading of a game map.
     *
     * @throws IOException if an I/O error occurs
     */
    @BeforeAll
    public static void loadGameMapTest() throws IOException {
        l_gameMap = GameMap.getInstance();
        l_gameMap.setFileName("german_empire");
        d_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
        StringBuilder l_fileContents = d_gameMapIO.loadFile("german_empire");
        d_gameMapIO.loadGameMap(l_fileContents);

        System.out.println(l_gameMap);

    }
    @Test
    public void testCountryMap() {
        assertEquals(l_gameMap.getCountryMap().size(), 24);
    }

    @Test
    public void testContinentMap() {
        assertEquals(l_gameMap.getContinentMap().size(), 6);
    }

    @Test
    public void testBorderMap() {
        assertEquals(l_gameMap.getBorderMap().size(), 24);
    }

    @Test
    public void testFormatRecognition() {
        assertFalse(GameMapIO.isDominationMap("german_empire"));
    }

}