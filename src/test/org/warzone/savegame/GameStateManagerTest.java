package org.warzone.savegame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.Cards;
import org.warzone.entities.GameMap;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;
import org.warzone.states.commands.Bomb;
import org.warzone.states.commands.Deploy;
import org.warzone.states.commands.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateManagerTest {

    static ArrayList<Player> d_playerList;
    static GameEngine d_gameEngine;

    /**
     * Sets up the game environment before all the test methods run.
     * Initializes players and the game engine, loads a game map, and assigns countries to the players.
     *
     * @throws IOException if there is an error loading the map.
     */
    @BeforeAll
    public static void setUp() throws IOException {
        d_gameEngine = new GameEngine();
        d_playerList = new ArrayList<>();
        Player initiator = new Player("Initiator");

        Cards cards = new Cards();
        Map<String,Integer> d_deck = new HashMap<String,Integer>();
        d_deck.put("negotiate", 5);
        initiator.getD_cardList().setD_deck(d_deck);

        Queue<Order> orders = new LinkedList<>();
        Order order = (Order) new Bomb(5);
        orders.add(order);
        Order order2 = (Order) new Deploy(initiator, 6, 7);
        orders.add(order2);
        initiator.setOrderList(orders);
        d_playerList.add(initiator);

        Player receiver = new Player("Initiator2");
        d_playerList.add(receiver);

        Player nonParticipant = new Player("NonParticipant");
        d_playerList.add(nonParticipant);

        GameMapIO l_dominationMapIO = new DominationMapIO();
        StringBuilder l_fileContents = l_dominationMapIO.loadFile("artic");
        GameMap l_gameMap = l_dominationMapIO.loadGameMap(l_fileContents);

        //Assigning Countries
        Player.assignCountries(d_playerList);
        d_gameEngine.setD_playerList(d_playerList);
    }

    /**
     * Tests the save and load game methods.
     * @throws IOException
     */
    @Test
    public void testSaveGame() throws IOException {
        System.out.println(d_gameEngine.getD_playerList());
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "test", "org","warzone","resources", "test_save.json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.saveGameState(d_gameEngine);
        l_gameStateManager.loadGameState(d_gameEngine);

        String l_name = d_gameEngine.getD_playerList().get(0).getD_name();
        int l_numCountries = d_gameEngine.getD_playerList().get(0).getD_listCountries().size();
        int l_size = d_gameEngine.getD_playerList().get(0).getOrderList().size();
        String l_firstOrder = d_gameEngine.getD_playerList().get(0).getOrderList().remove().getOrdertype();
        String l_secondOrder = d_gameEngine.getD_playerList().get(0).getOrderList().remove().getOrdertype();
        int l_cardAmount = d_gameEngine.getD_playerList().get(0).getD_cardList().getD_deck().remove("negotiate");
        assertEquals("Initiator", l_name);
        assertEquals(18, l_numCountries);
        assertEquals(2, l_size);
        assertEquals("bomb", l_firstOrder);
        assertEquals("deploy", l_secondOrder);
        assertEquals(5, l_cardAmount);
        System.out.println("Player name is Initiator: " + l_name);
        System.out.println("Number of countries is 18: " + l_numCountries);
        System.out.println("Order list size is 2: " + l_size);
        System.out.println("First order is bomb: " + l_firstOrder);
        System.out.println("Second order is deploy: " + l_secondOrder);
        System.out.println("Amount of negotiate cards is 5: " + l_cardAmount);
    }
}