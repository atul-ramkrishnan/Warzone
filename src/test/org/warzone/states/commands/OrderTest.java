package org.warzone.states.commands;

import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.operations.DominationMapIO;
import org.warzone.strategy.Player;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit class for Orders class. It initializes a player object and executes an order. The resulting
 * number of un-deployed armies is checked with the expected un-deployed armies to see if order is calling
 * execute function correctly
 */

public class OrderTest {
    GameEngine gameEngine = new GameEngine();

    /**
     * Test the order execution functionality.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void OrderExecutionTest() throws IOException {
        /*
         * Initializing values for test
         */
        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setFileName("artic");
        DominationMapIO l_dominationMapIO = new DominationMapIO();
        l_dominationMapIO.loadMap("artic");
        Player l_player = new Player("player 1");
        ArrayList<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(l_player);
        Player.assignCountries(l_playerList);
        Deploy l_deploy = new Deploy(l_player,22,3);
        l_player.setArmies(4);
        ArrayList<String> l_orderParameters = new ArrayList<String>();
        l_deploy.setD_player(l_player);
        l_orderParameters.add("22");
        l_orderParameters.add("3");
        l_deploy.setD_orderParameters(l_orderParameters);
        l_deploy.execute(gameEngine);
        assertEquals(1, l_player.getD_unDeployedArmies());
        System.out.println("Expected : " + 1 + " Result : " + l_player.getD_unDeployedArmies());
    }
}
