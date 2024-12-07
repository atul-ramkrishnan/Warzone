package org.warzone.states.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains tests for the Airlift command.
 */
public class AirliftTest {
    GameEngine gameEngine = new GameEngine();
    //no duplicate assignments
    //all countries assigned
    static ArrayList<Player> playerList;

    /**
     * Initialize PlayersList and countryList to be used in tests
     *
     * @throws IOException if map is incorrectly loaded
     */
    @BeforeAll
    static void setUp() throws IOException {
        playerList = new ArrayList<>();
        playerList.add(new Player("Abhijit"));
        playerList.add(new Player("Atul"));
        playerList.add(new Player("Domenico"));
        playerList.add(new Player("Muskan"));
        playerList.add(new Player("Saheb"));

        GameMapIO l_dominationMapIO = new DominationMapIO();
        StringBuilder l_fileContents = l_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = l_dominationMapIO.loadGameMap(l_fileContents);
        //Assigning Countries
        Player.assignCountries(playerList);
    }

    /**
     * Test the Airlift functionality.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void Airlift() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        GameMap d_gameMap = GameMap.getInstance();
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 50;
        playerList.get(0).setArmies(l_playerZeroArmies);
        //Adding to some countries
        int l_PlayerCountry0 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_PlayerCountry1 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(1));
        Deploy deploy1 = new Deploy(playerList.get(0), l_PlayerCountry0, 20);
        deploy1.execute(gameEngine);
        Deploy deploy2 = new Deploy(playerList.get(0), l_PlayerCountry1, 30);
        deploy2.execute(gameEngine);
        int l_Country0Armies = d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies();
        int l_Country1Armies = d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry1)).getD_noOfArmies();
        System.out.println("\nMoving Armies with Airlift");
        System.out.println("Before Airlift");
        System.out.println("Armies at Origin: " + l_Country0Armies);
        System.out.println("Armies at Target: " + l_Country1Armies);
        Airlift airlift = new Airlift(l_PlayerCountry0, l_PlayerCountry1, 20);
        airlift.execute(gameEngine);
        System.out.println("\nAfter Airlift");
        System.out.println("Armies at Origin " + d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies());
        System.out.println("Armies at Target " + d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry1)).getD_noOfArmies());
        assertEquals(l_playerZeroArmies, l_Country0Armies + l_Country1Armies);
    }
}
