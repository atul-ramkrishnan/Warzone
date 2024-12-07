package org.warzone.states.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.Cards;
import org.warzone.entities.GameMap;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;
import org.warzone.states.IssueOrderPhase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains tests for the Blockade command.
 */
public class BlockadeTest {

    static ArrayList<Player> playerList;
    static GameEngine gameEngine;

    @BeforeAll
    static void setUp() throws IOException {
        gameEngine = new GameEngine();
        playerList = new ArrayList<>();
        playerList.add(new Player("Abhijit"));
        playerList.add(new Player("Atul"));
        playerList.add(new Player("Domenico"));
        playerList.add(new Player("Muskan"));
        playerList.add(new Player("Saheb"));

        GameMapIO l_dominationMapIO = new DominationMapIO();
        StringBuilder l_fileContents = l_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = l_dominationMapIO.loadGameMap(l_fileContents);
        gameEngine.setGamePhase(new IssueOrderPhase());
        //Assigning Countries
        Player.assignCountries(playerList);
    }

    /**
     * Test the Blockade functionality.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void Blockade() throws IOException {
        playerList.clear();
        Player player = new Player("Player0");
        Cards cards = new Cards();
        Map<String,Integer> d_deck = new HashMap<String,Integer>();
        d_deck.put("blockade", 5);

        cards.setD_deck(d_deck);
        player.setD_cardList(cards);
        playerList.add(player);
        gameEngine.setD_currentPlayer(playerList.get(0));
        GameMap d_gameMap = GameMap.getInstance();
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 50;
        playerList.get(0).setArmies(l_playerZeroArmies);
        //Adding to some countries
        int l_PlayerCountry0 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        Deploy deploy1 = new Deploy(playerList.get(0), l_PlayerCountry0, 20);
        deploy1.execute(gameEngine);
        int l_Country0Armies = d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies();
        int countriesOwnedBeforeBlockade = playerList.get(0).getD_listCountries().size();
        System.out.println("Before Blockade");
        System.out.println("No of armies at the target country " + l_Country0Armies);
        System.out.println("Total Countries Owned by Player " + playerList.get(0).getD_listCountries().size());

        Blockade blockade = new Blockade(44);
        blockade.execute(gameEngine);
        System.out.println("After Blockade");
        System.out.println("No of Armies at the target country " + d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies());
        System.out.println("Total Countries Owned by Player " + playerList.get(0).getD_listCountries().size());
        assertEquals(l_Country0Armies * 3, d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies());
        assertEquals(countriesOwnedBeforeBlockade - 1, playerList.get(0).getD_listCountries().size());
    }
}
