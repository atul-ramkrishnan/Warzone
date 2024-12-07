package org.warzone.states.commands;

import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.Cards;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
BombTest is the Junit test class to test the Bomb Class
 */

public class BombTest {

    /**
     * Tests the "bomb" order
     * The method creates a test player, assigns a country to it and bombs one of the neighbouring countries
     * It verifies if the "bomb" order is valid
     * If the "bomb" order is valid it checks if the armies of the bombed country are reduced
     *
     * @throws IOException if an I/O error occurs.
     */
    GameEngine gameEngine = new GameEngine();

    /**
     * Test the Bomb functionality.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void Bomb() throws IOException {
        //create new player
        Player player = new Player("Test Player");

        gameEngine.setD_currentPlayer(player);
        Cards cards = new Cards();
        Map<String,Integer> d_deck = new HashMap<String,Integer>();
        d_deck.put("bomb", 5);

        cards.setD_deck(d_deck);
        player.setD_cardList(cards);

        //instantiate the map
        GameMap gameMap = GameMap.getInstance();
        gameMap.setFileName("artic");
        GameMapIO l_dominationMapIO = new DominationMapIO();
        StringBuilder l_fileContents = l_dominationMapIO.loadFile("artic");
        gameMap = l_dominationMapIO.loadGameMap(l_fileContents);

        //assign a country to the test player
        Map<String, Country> countries = new HashMap<String, Country>();
        System.out.println(gameMap.getCountryMap().get(String.valueOf(1)).getName());
        gameMap.getCountryMap().get(String.valueOf(1)).setCountryOwner(player);
        countries.put("1", gameMap.getCountryMap().get(String.valueOf(1)));
        player.setD_listCountries(countries);

        //get one neighbour of any one of the countries owned by the player
        Country neighbour = null;
        boolean got_neighbour = false;
        List<Integer> l_neighbourIDs = new ArrayList<Integer>();

        //Select a country to bomb such that it passes the valid test
        for (Map.Entry<String, Country> entry : player.getD_listCountries().entrySet()) {
            System.out.println(entry.getKey());
            String key = entry.getKey();
            Country country = entry.getValue();
            System.out.println("Get neighbours for country : " + country.getName());
            Map<String, Country> neighbours = country.getAdjacentCountries();

            for (Map.Entry<String, Country> entry1 : neighbours.entrySet()) {
                neighbour = entry1.getValue();
                neighbour.setD_noOfArmies(11);
                System.out.println("Bomb : " + neighbour.getName() + ", index : " + neighbour.getIndex() + ", number of armies : " + neighbour.getD_noOfArmies());
                got_neighbour = true;
                l_neighbourIDs.add(neighbour.getIndex());
            }

        }

        /* since one of the neighbours of the player is being sent for bombing,
        the action should be valid
         */
        Bomb l_bombOrder = new Bomb(neighbour.getIndex());
        l_bombOrder.setD_player(player);
        boolean l_isValid = false;
        int armies_after_bombing = (int) Math.ceil(neighbour.getD_noOfArmies() / 2);
        l_isValid = l_bombOrder.valid(gameEngine);

        //Check for a bomb order true validity and execution results
        assertEquals(true, l_isValid);
        if (l_isValid) {
            l_bombOrder.execute(gameEngine);
            System.out.println("Armies after bombing : " + neighbour.getD_noOfArmies());
            assertEquals(armies_after_bombing, neighbour.getD_noOfArmies());
        }

        //Check for a bomb order false validity
        Bomb l_bombOrder2 = new Bomb(53);
        l_bombOrder2.setD_player(player);
        l_isValid = l_bombOrder2.valid(gameEngine);
        assertEquals(false, l_isValid);


    }

}
