package org.warzone.operations;

import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.strategy.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit class to check the removeLostPlayers method in main
 */
public class RemovePlayerTest {

    /**
     * Test the removal of players.
     */
    @Test
    public void RemovePlayersTest() {
        ArrayList<Player> l_player_list = new ArrayList<Player>();
        /*
         * Initializing values for test
         */
        Player player1 = new Player("player 1");
        Player player2 = new Player("player 2");
        Player player3 = new Player("player 3");


        player1.setArmies(0);
        player2.setArmies(1);
        player3.setArmies(0);


        l_player_list.add(player1);
        l_player_list.add(player2);
        l_player_list.add(player3);

        GameEngine gameEngine = new GameEngine();
        GameEngine.setD_playerList(l_player_list);
        GameEngine.removeLostPlayers();
        ArrayList<Player> result_list = GameEngine.getD_playerList();


                assertEquals(l_player_list.size(), result_list.size());
        System.out.println("Size : " + l_player_list.size() + " " + result_list.size());
        for (int i = 0; i < l_player_list.size(); i++) {
            System.out.println("Expected Winner : " + l_player_list.get(i).getD_name() + " Result Winner : " + result_list.get(i).getD_name());
            assertEquals(l_player_list.get(i), result_list.get(i));
        }


    }
}
