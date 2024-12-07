package org.warzone.states.commands;

import org.junit.jupiter.api.Test;
import org.warzone.states.OrderExecutionPhase;
import org.warzone.strategy.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit class for Commit class. It initializes some player objects and calls the order.execute()
 * method. At the end it checks if the list containing the active players contains the
 * correct number of players after the orders are executed.
 *
 */
public class OrderExecutionPhaseTest {
    /**
     * Test the removal of players from the game.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void RemovePlayersTest() throws IOException {
        ArrayList<Player> l_player_list = new ArrayList<Player>();

        Player player1 = new Player("player 1");
        Player player2 = new Player("player 2");
        Player player3 = new Player("player 3");

        Deploy deploy = new Deploy(player1,1,20);
        Advance advance = new Advance(1,1,1);
        ArrayList<String> l_orderParams = new ArrayList<String>();
        l_orderParams.add("22");
        l_orderParams.add("1");
        deploy.setD_orderParameters(l_orderParams);
        deploy.setD_player(player1);

        Deploy deploy2 = new Deploy(player2,2,20);
        Deploy deploy3 = new Deploy(player2,2,20);
        Deploy deploy4 = new Deploy(player2,2,20);
        ArrayList<String> l_orderParams2 = new ArrayList<String>();
        l_orderParams2.add("22");
        l_orderParams2.add("1");
        deploy2.setD_orderParameters(l_orderParams2);
        deploy2.setD_player(player1);

        Deploy deploy5 = new Deploy(player3,3,10);
        Deploy deploy6 = new Deploy(player3,4,10);
        ArrayList<String> l_orderParams3 = new ArrayList<String>();
        l_orderParams3.add("22");
        l_orderParams3.add("1");
        deploy3.setD_orderParameters(l_orderParams3);
        deploy3.setD_player(player1);

        Queue<Order> orderList = new LinkedList<>();
        orderList.add(deploy);
        orderList.add(advance);


        player1.setOrderList(orderList);

        Queue<Order> orderList2 = new LinkedList<>();
        orderList2.add(deploy2);
        orderList2.add(deploy3);
        orderList2.add(deploy4);

        player2.setOrderList(orderList2);

        Queue<Order> orderList3 = new LinkedList<>();
        orderList3.add(deploy5);
        orderList3.add(deploy6);
        player3.setOrderList(orderList3);

        l_player_list.add(player1);
        l_player_list.add(player2);
        l_player_list.add(player3);

        OrderExecutionPhase commit = new OrderExecutionPhase();
        commit.executeOrdersRoundRobin(l_player_list);

        for (Player player : l_player_list)
            System.out.println(player.getD_name());

        assertEquals(3, l_player_list.size());

    }

}
