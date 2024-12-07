package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;

/**
 * The {@code Airlift} class implements the {@code Order} interface
 * and encapsulates an order to airlift armies from one country to another.
 * Airlift orders are valid only if the player owns both the origin and destination countries.
 */
public class Airlift implements Order{
    private int d_countryOrigin;
    private int d_countryDestination;
    private int d_numArmiesToMove;
    private int d_totalOriginArmies;

    /**
     * Constructs an {@code Airlift} order with the specified origin country, destination country,
     * and number of armies to move.
     *
     * @param p_countryOrigin      The ID of the country from which the armies are airlifted.
     * @param p_countryDestination The ID of the country to which the armies are airlifted.
     * @param p_numArmiesToMove    The number of armies to be airlifted.
     */
    public Airlift(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove){
        this.d_countryOrigin = p_countryOrigin;
        this.d_countryDestination = p_countryDestination;
        this.d_numArmiesToMove = p_numArmiesToMove;
    }

    /**
     * Executes the airlift order within the context of the game engine, moving armies
     * from the origin country to the destination country, if both are owned by the same player.
     *
     * @param gameEngine The game engine that provides the context in which the order is executed.
     */

    @Override
    public void execute(GameEngine gameEngine) {
        if(valid(gameEngine)){
            GameMap gameMap = GameMap.getInstance();
            Player l_originOwner = gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).getCountryOwner();
            Player l_targetOwner = gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).getCountryOwner();
            int p_armiesOnTarget = gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).getD_noOfArmies();
            if(l_targetOwner.equals(l_originOwner)){
                gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).setD_noOfArmies(p_armiesOnTarget+d_numArmiesToMove);
                gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).setD_noOfArmies(d_totalOriginArmies-d_numArmiesToMove);
                System.out.println("Armies have been successfully Airlifted from " + d_countryOrigin + " to " + d_countryDestination);
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Airlift order executed: " + d_numArmiesToMove + "" +
                        "armies have been airlifted to " + gameMap.getCountryMap().get(Integer.toString(d_countryDestination)).getName()));
            }else{
                System.out.println("You can only airlift armies to YOUR territory");
            }

            l_originOwner.getD_cardList().getD_deck().put("airlift",l_originOwner.getD_cardList().getD_deck().get("airlift")-1);
            if(l_originOwner.getD_cardList().getD_deck().get("airlift") == 0){
                l_originOwner.getD_cardList().getD_deck().remove("airlift");
            }
            System.out.println("Removed card from deck. Currently deck is: "+ l_originOwner.getD_cardList().getD_deck());
        }
    }

    /**
     * Validates the airlift order to ensure that it can be executed.
     * Checks if the player has an airlift card, and if there are sufficient armies in the origin country.
     *
     * @param gameEngine The game engine that provides the context for validation.
     * @return {@code true} if the order is valid; {@code false} otherwise.
     */
    @Override
    public boolean valid(GameEngine gameEngine) {
        GameMap gameMap = GameMap.getInstance();
        Player l_originOwner = gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).getCountryOwner();
        //checking if card is available
        if (!l_originOwner.getD_cardList().valid("airlift")){
            System.out.println(l_originOwner.getD_name() + " does not have airlift card");
            return false;
        }
        //check if there are enough armies to airlift
        d_totalOriginArmies = gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).getD_noOfArmies();
        if (d_numArmiesToMove > d_totalOriginArmies) {
            return false;
        }
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrdertype() {
        return "airlift";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setD_player(Player player) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setD_orderParameters(ArrayList<String> lOrderParams) {

    }

    public String getD_orderParameters() {
        return "airlift " + String.valueOf(d_countryOrigin) + " " + String.valueOf(d_countryDestination) + " " + String.valueOf(d_numArmiesToMove);
    }

}
