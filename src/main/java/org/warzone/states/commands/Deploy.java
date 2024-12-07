package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;

/**
 * The {@code Deploy} class implements the {@code Order} interface
 * and encapsulates an order to deploy armies to a specified country.
 */
public class Deploy implements Order {

    /**
     * The list of order parameters.
     */
    public ArrayList<String> d_orderParameters;
    private int d_orderNo;
    private Player d_player;
    private int p_countryIndex;
    private int p_armyAmount;

    /**
     * {@inheritDoc}
     */
    public String getD_orderParameters() {
        return "deploy " + d_player.getD_name() + " " + String.valueOf(p_countryIndex) + " " + String.valueOf(p_armyAmount);
    }

    /**
     * {@inheritDoc}
     */
    public void setD_orderParameters(ArrayList<String> p_orderParameters) {
        this.d_orderParameters = p_orderParameters;
        p_countryIndex = Integer.parseInt(d_orderParameters.get(0));
        p_armyAmount = Integer.parseInt(d_orderParameters.get(1));
    }

    /**
     * Returns the player associated with this instance.
     *
     * @return the player associated with this instance
     */
    public Player getD_player() {
        return d_player;
    }

    /**
     * {@inheritDoc}
     */
    public void setD_player(Player p_player) {
        this.d_player = p_player;
    }

    /**
     * Creates a deploy order with a given order number.
     * @param p_number order number.
     */
    public Deploy(int p_number) {
        this.d_orderNo = p_number;
    }

    /**
     * Constructs a {@code Deploy} order with the specified player, country index, and army amount.
     *
     * @param p_player        The player who issues the deploy order.
     * @param p_countryIndex  The index of the country to which armies are deployed.
     * @param p_armyAmount    The number of armies to deploy.
     */
    public Deploy(Player p_player, int p_countryIndex, int p_armyAmount){
        this.d_player = p_player;
        this.p_countryIndex = p_countryIndex;
        this.p_armyAmount = p_armyAmount;
    }

    /**
     * {@inheritDoc}
     */
    public String getOrdertype() {
        return "deploy";
    }

    /**
     * Retrieves the order number associated with this instance.
     *
     * @return the order number
     */
    public int getOrderNo() {
        return d_orderNo;
    }

    /**
     * Deploys army units in countries
     * @param gameEngine The game engine.
     */
    public void execute(GameEngine gameEngine) {
        if (valid(gameEngine)){
            System.out.println("Deploying " + p_armyAmount + " armies in country index " + p_countryIndex + ". " + d_player.getD_listCountries().get(String.valueOf(p_countryIndex)).getName());
            GameMap gameMap = GameMap.getInstance();
            int l_newSize = gameMap.getCountryMap().get(String.valueOf(p_countryIndex)).getD_noOfArmies() + p_armyAmount;
            gameMap.getCountryMap().get(String.valueOf(p_countryIndex)).setD_noOfArmies(l_newSize);
            d_player.setD_unDeployedArmies(d_player.getD_unDeployedArmies() - p_armyAmount);
            GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Deploy order of executed: "
                    + p_armyAmount + " deployed to " + gameMap.getCountryMap().get(Integer.toString(p_countryIndex)).getName() + "."));
        }
    }

    /**
     * Validates if the deploy order can be executed based on the current game state.
     * It checks two conditions: whether the player has enough undeployed armies,
     * and whether the country index specified belongs to the player issuing the order.
     *
     * @param gameEngine The game engine that provides the context for validation.
     * @return true if the deploy order is valid; false otherwise.
     */
    @Override
    public boolean valid(GameEngine gameEngine) {
        //if deployment count greater than armies available
        if (d_player.getD_unDeployedArmies() < p_armyAmount) {
            System.out.println("Cannot deploy armies greater than the armies available");
            System.out.println("Armies available " + p_armyAmount + ".\nUndeployed armies : " + d_player.getD_unDeployedArmies() + "\n");
            return false;
        }
        //if country not in the player's list of countries
        if (!(d_player.getD_listCountries().containsKey(String.valueOf(p_countryIndex)))){
            System.out.println("Cannot deploy armies in country " + p_countryIndex);
            System.out.println("Country index " + p_countryIndex + " does not belong to player " + gameEngine.getD_currentPlayer().getD_name());
            return false;
        }
        return true;
    }
}