package org.warzone.operations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.entities.GameMap;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/** This Test class is used to test functionality of LoadMap
 *
 */
public class DominationMapIOTest {
    private static GameMap l_gameMap;

    /**
     * Test the loading of a game map.
     *
     * @throws IOException if an I/O error occurs
     */
    @BeforeAll
    public static void loadGameMapTest() throws IOException {
        l_gameMap = GameMap.getInstance();
        l_gameMap.setFileName("artic");
        GameMapIO l_gameMapIO = new DominationMapIO();
        StringBuilder l_fileContents = l_gameMapIO.loadFile("artic");
        l_gameMapIO.loadGameMap(l_fileContents);

        System.out.println(l_gameMap);

    }
    @Test
    public void testCountryMap() {
        assertEquals(l_gameMap.getCountryMap().size(), 54);
    }

    @Test
    public void testContinentMap() {
        assertEquals(l_gameMap.getContinentMap().size(), 6);
    }

    @Test
    public void testBorderMap() {
        assertEquals(l_gameMap.getBorderMap().size(), 54);
    }

    @Test
    public void testFormatRecognition() {
        assertTrue(GameMapIO.isDominationMap("artic"));
    }

}
