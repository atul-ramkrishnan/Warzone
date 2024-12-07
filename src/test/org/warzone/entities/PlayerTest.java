package org.warzone.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;
import org.warzone.states.commands.*;
import org.warzone.strategy.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit class for Player Test
 */

public class PlayerTest {
    GameEngine gameEngine = new GameEngine();
    static GameMapIO d_dominationMapIO = new DominationMapIO();
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

        StringBuilder l_fileContents = d_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = d_dominationMapIO.loadGameMap(l_fileContents);
        //Assigning Countries
        Player.assignCountries(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            System.out.println("\n" + playerList.get(i).getD_name() + "'s countries");
            playerList.get(i).getD_listCountries().forEach((j, country) -> {
                System.out.println(l_gameMap.getCountryMap().get(String.valueOf(j)).getIndex() + " " + l_gameMap.getCountryMap().get(String.valueOf(j)).getName());
                System.out.println("Continent : " + l_gameMap.getCountryMap().get(String.valueOf(j)).getContinent().getD_name());
                System.out.println();
            });
        }
        System.out.println("Country Owners from Singleton");
        l_gameMap.getCountryMap().forEach((s, country) -> {
            System.out.println(country.getName() + "'s owner: " + country.getCountryOwner().getD_name());
        });
    }

    /**
     * Checks if all countries have been assigned
     *
     * @throws IOException if map is incorrectly loaded
     */
    @Test
    public void allCountriesAssigned() throws IOException {
        int p_numCountriesPlayers = 0;
        int p_numCountriesMapArtic = 54;
        int p_playersListSize = playerList.size();
        for (int l_i = 0; l_i < p_playersListSize; l_i++) {
            p_numCountriesPlayers += playerList.get(l_i).getD_listCountries().size();
        }
        assertEquals(p_numCountriesMapArtic, p_numCountriesPlayers);
    }

    /**
     * Test the deployment of units.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void unitsDeployed() throws IOException {
        playerList.clear();
        playerList.add(new Player("Abhijit"));
        Player.assignCountries(playerList);
        playerList.get(0).setArmies(5);
        Deploy deploy = new Deploy(playerList.get(0), 1, 5);
        deploy.execute(gameEngine);
        assertEquals(5, playerList.get(0).getD_listCountries().get(String.valueOf(1)).getD_noOfArmies());
    }

    /**
     * Testing Attack class number of Armies
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void AttackArmyCountChange() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        GameMap d_gameMap = GameMap.getInstance();
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 5;
        int l_playerOneArmies = 50;
        playerList.get(0).setArmies(l_playerZeroArmies);
        playerList.get(1).setArmies(l_playerOneArmies);
        //Adding to some countries
        int l_Player0Country = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_Player1Country = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(0));
        Deploy deploy1 = new Deploy(playerList.get(0), l_Player0Country, l_playerZeroArmies);
        deploy1.execute(gameEngine);
        Deploy deploy2 = new Deploy(playerList.get(1), l_Player1Country, l_playerOneArmies);
        deploy2.execute(gameEngine);
        System.out.println("\nNo Land Change");
        System.out.println("Before Attack");
        System.out.println("Attacking Armies: " + l_playerZeroArmies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneArmies);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());

        Advance attack = new Advance(l_Player0Country, l_Player1Country, 5);
        attack.execute(gameEngine);
        int l_playerZeroRemain = d_gameMap.getCountryMap().get(String.valueOf(l_Player0Country)).getD_noOfArmies();
        int l_playerOneRemain = d_gameMap.getCountryMap().get(String.valueOf(l_Player1Country)).getD_noOfArmies();
        System.out.println("After Attack");
        System.out.println("Attacking Armies: " + l_playerZeroRemain);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneRemain);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());
        assertEquals(0, playerList.get(0).getD_listCountries().size() - playerList.get(1).getD_listCountries().size());
    }

    /**
     * Testing Attack class number of Armies not all used
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void AttackArmyCountChangeNotAllUsed() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        GameMap d_gameMap = GameMap.getInstance();
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 10;
        int l_playerOneArmies = 50;
        playerList.get(0).setArmies(l_playerZeroArmies);
        playerList.get(1).setArmies(l_playerOneArmies);
        //Adding to some countries
        int l_Player0Country = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_Player1Country = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(0));
        Deploy deploy1 = new Deploy(playerList.get(0), l_Player0Country, l_playerZeroArmies);
        deploy1.execute(gameEngine);
        Deploy deploy2 = new Deploy(playerList.get(1), l_Player1Country, l_playerOneArmies);
        deploy2.execute(gameEngine);
        System.out.println("\nNo Land Change, not all armies used (5)");
        System.out.println("Before Attack");
        System.out.println("Attacking Armies: " + l_playerZeroArmies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneArmies);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());

        Advance attack = new Advance(l_Player0Country, l_Player1Country, 5);
        attack.execute(gameEngine);
        int l_playerZeroRemain = d_gameMap.getCountryMap().get(String.valueOf(l_Player0Country)).getD_noOfArmies();
        int l_playerOneRemain = d_gameMap.getCountryMap().get(String.valueOf(l_Player1Country)).getD_noOfArmies();
        System.out.println("After Attack");
        System.out.println("Attacking Armies: " + l_playerZeroRemain);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneRemain);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());
        assertEquals(0, playerList.get(0).getD_listCountries().size() - playerList.get(1).getD_listCountries().size());
    }

    /**
     * Testing army class land changes
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void AttackLandChange() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        StringBuilder l_fileContents = d_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = d_dominationMapIO.loadGameMap(l_fileContents);
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 15;
        int l_playerOneArmies = 4;
        playerList.get(0).setArmies(l_playerZeroArmies);
        playerList.get(1).setArmies(l_playerOneArmies);
        //Adding to some countries
        int l_Player0Country = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_Player1Country = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(0));
        Deploy deploy1 = new Deploy(playerList.get(0), l_Player0Country, l_playerZeroArmies);
        deploy1.execute(gameEngine);
        Deploy deploy2 = new Deploy(playerList.get(1), l_Player1Country, l_playerOneArmies);
        deploy2.execute(gameEngine);
        System.out.println("\nLand Change");
        System.out.println("Before Attack");
        System.out.println("Attacking Armies: " + l_playerZeroArmies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneArmies);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());

        Advance attack = new Advance(l_Player0Country, l_Player1Country, l_playerZeroArmies);
        attack.execute(gameEngine);
        int l_playerZeroRemain = l_gameMap.getCountryMap().get(String.valueOf(l_Player0Country)).getD_noOfArmies();
        int l_playerOneRemain = l_gameMap.getCountryMap().get(String.valueOf(l_Player1Country)).getD_noOfArmies();
        System.out.println("After Attack");
        System.out.println("Attacking Armies: " + l_playerZeroRemain);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Attacking Armies in new Territory: " + l_playerOneRemain);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());
        assertEquals(2, playerList.get(0).getD_listCountries().size() - playerList.get(1).getD_listCountries().size());
    }

    /**
     * Testing army class land changes not all troops used
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void AttackLandChangeNotAllTroopsUsed() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        StringBuilder l_fileContents = d_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = d_dominationMapIO.loadGameMap(l_fileContents);
        Player.assignCountries(playerList);
        int l_playerZeroArmies = 50;
        int l_playerOneArmies = 4;
        playerList.get(0).setArmies(l_playerZeroArmies);
        playerList.get(1).setArmies(l_playerOneArmies);
        //Adding to some countries
        int l_Player0Country = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_Player1Country = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(0));
        Deploy deploy1 = new Deploy(playerList.get(0), l_Player0Country, l_playerZeroArmies);
        deploy1.execute(gameEngine);
        Deploy deploy2 = new Deploy(playerList.get(1), l_Player1Country, l_playerOneArmies);
        deploy2.execute(gameEngine);
        System.out.println("\nLand Change, not all troops used (15)");
        System.out.println("Before Attack");
        System.out.println("Attacking Armies: " + l_playerZeroArmies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_playerOneArmies);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());

        Advance attack = new Advance(l_Player0Country, l_Player1Country, 15);
        attack.execute(gameEngine);
        int l_playerZeroRemain = l_gameMap.getCountryMap().get(String.valueOf(l_Player0Country)).getD_noOfArmies();
        int l_playerOneRemain = l_gameMap.getCountryMap().get(String.valueOf(l_Player1Country)).getD_noOfArmies();
        System.out.println("After Attack");
        System.out.println("Attacking Armies: " + l_playerZeroRemain);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Attacking Armies in new Territory: " + l_playerOneRemain);
        System.out.println("Number of Countries: " + playerList.get(1).getD_listCountries().size());
        assertEquals(2, playerList.get(0).getD_listCountries().size() - playerList.get(1).getD_listCountries().size());
    }

    /**
     * Test the Advance functionality.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void Advance() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        StringBuilder l_fileContents = d_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = d_dominationMapIO.loadGameMap(l_fileContents);
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
        System.out.println("\nMoving Armies with Advance");
        System.out.println("Before Attack");
        System.out.println("Attacking Armies: " + l_Country0Armies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Defending Armies: " + l_Country1Armies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());

        Advance attack = new Advance(l_PlayerCountry0, l_PlayerCountry1, 20);
        attack.execute(gameEngine);
        l_Country0Armies = d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry0)).getD_noOfArmies();
        l_Country1Armies = d_gameMap.getCountryMap().get(String.valueOf(l_PlayerCountry1)).getD_noOfArmies();
        System.out.println("After Attack");
        System.out.println("Attacking Armies: " + l_Country0Armies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        System.out.println("Attacking Armies in new Territory: " + l_Country1Armies);
        System.out.println("Number of Countries: " + playerList.get(0).getD_listCountries().size());
        assertEquals(l_playerZeroArmies, l_Country0Armies + l_Country1Armies);
    }

    /**
     * Test the retrieval of the next order.
     */
    @Test
    public void NextOrderTest() {
        Queue<Order> orderList = new LinkedList<>();
        /*
         * Initializing values for test
         */
        ArrayList<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(new Player("Player A"));
        l_playerList.add(new Player("Player B"));
        String[] l_orders = {"deploy 10 3", "attack 23 5 3"};

        String[] l_array = l_orders[0].split(" ");
        String[] l_array1 = l_orders[1].split(" ");
        Deploy obj = new Deploy(playerList.get(0),Integer.parseInt(l_array[1]),Integer.parseInt(l_array[2]));
        Advance obj1 = new Advance(Integer.parseInt(l_array1[1]),Integer.parseInt(l_array1[2]),Integer.parseInt(l_array1[3]));
        orderList.add(obj);
        orderList.add(obj1);
        l_playerList.get(0).setOrderList(orderList);
        Deploy deploy = (Deploy) l_playerList.get(0).next_order();
        assertEquals("deploy", deploy.getOrdertype());
        System.out.println("Expected : deploy Result : " + deploy.getOrdertype());
    }
    /**
     * Checks block all classes that require cards
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void checkCard() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        gameEngine.setD_currentPlayer(playerList.get(0));
        Player.assignCountries(playerList);
        int l_PlayerCountry0 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_PlayerCountry0_1 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(1));
        int l_PlayerCountry1 = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(1));
        playerList.get(0).setArmies(5);
        Deploy deploy = new Deploy(playerList.get(0), l_PlayerCountry0, 5);
        deploy.execute(gameEngine);
        Bomb bomb = new Bomb(l_PlayerCountry1);
        bomb.execute(gameEngine);
        Airlift airlift = new Airlift(l_PlayerCountry0, l_PlayerCountry0_1, 5);
        airlift.execute(gameEngine);
        Blockade blockade = new Blockade(l_PlayerCountry0);
        blockade.execute(gameEngine);
        Diplomacy diplomacy = new Diplomacy(playerList.get(0), playerList.get(1));
        diplomacy.execute(gameEngine);
    }
    /**
     * Allows moves since card is available
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void checkCardExecute() throws IOException {
        playerList.clear();
        playerList.add(new Player("Player0"));
        playerList.add(new Player("Player1"));
        gameEngine.setD_currentPlayer(playerList.get(0));
        Player.assignCountries(playerList);
        int l_PlayerCountry0 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(0));
        int l_PlayerCountry0_1 = Integer.valueOf(playerList.get(0).getD_listCountries().keySet().stream().toList().get(1));
        int l_PlayerCountry1 = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(1));
        int l_PlayerCountry1_1 = Integer.valueOf(playerList.get(1).getD_listCountries().keySet().stream().toList().get(1));
        playerList.get(0).setArmies(5);
        Deploy deploy = new Deploy(playerList.get(0), l_PlayerCountry0, 5);
        deploy.execute(gameEngine);
        Advance advance = new Advance(l_PlayerCountry0, l_PlayerCountry1, 5);
        advance.execute(gameEngine);
        String command = playerList.get(0).getD_cardList().getD_deck().keySet().stream().toList().get(0);
        switch (command){
            case "bomb":
                Bomb bomb = new Bomb(l_PlayerCountry1_1);
                bomb.execute(gameEngine);
                break;
            case "airlift":
                Airlift airlift = new Airlift(l_PlayerCountry1, l_PlayerCountry0, 5);
                airlift.execute(gameEngine);
                break;
            case "blockade":
                Blockade blockade = new Blockade(l_PlayerCountry1);
                blockade.execute(gameEngine);
                break;
            case "diplomacy":
                Diplomacy diplomacy = new Diplomacy(playerList.get(0), playerList.get(1));
                diplomacy.execute(gameEngine);
                break;
        }
    }
}
