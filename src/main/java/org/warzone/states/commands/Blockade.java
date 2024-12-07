package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;

/**
 * The {@code Blockade} class implements the {@code Order} interface
 * and encapsulates an order to triple the number of armies in a country and
 * convert it to a neutral territory, effectively creating a blockade.
 */
public class Blockade implements Order{

    private int d_countryID;

    /**
     * Constructs a {@code Blockade} order for a specified country.
     *
     * @param p_countryID The ID of the country to be blockaded.
     */
    public Blockade(int p_countryID){
        this.d_countryID = p_countryID;
    }

    /**
     * Executes the blockade order within the context of the game engine.
     * Triples the armies in the specified country and transfers its ownership to neutral,
     * effectively creating a blockade.
     *
     * @param gameEngine The game engine that provides the context in which the order is executed.
     */
    @Override
    public void execute(GameEngine gameEngine) {
        if(valid(gameEngine)){
            System.out.println("Blockade on country index : " + d_countryID);
            GameMap gameMap = GameMap.getInstance();
            Player l_callingPlayer = gameEngine.getD_currentPlayer();
            int l_newArmySize = gameMap.getCountryMap().get(String.valueOf(d_countryID)).getD_noOfArmies()*3;
            gameMap.getCountryMap().get(String.valueOf(d_countryID)).setD_noOfArmies(l_newArmySize);

            //adding the country to neutral territory list
            gameMap.getD_NeutralTerritory().add(l_callingPlayer.getD_listCountries().get(String.valueOf(d_countryID)));

            //Removing country from the Player's country list
            l_callingPlayer.getD_listCountries().remove(String.valueOf(d_countryID));
            GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Blockade order executed: " +
                    l_newArmySize + "armies placed in " +
                    gameMap.getCountryMap().get(Integer.toString(d_countryID)).getName() +
                    " and changed to a neutral territory."));


            l_callingPlayer.getD_cardList().getD_deck().put("blockade",l_callingPlayer.getD_cardList().getD_deck().get("blockade")-1);
            if(l_callingPlayer.getD_cardList().getD_deck().get("blockade") == 0){
                l_callingPlayer.getD_cardList().getD_deck().remove("blockade");
            }
            System.out.println("Removed card from deck. Currently deck is: "+ l_callingPlayer.getD_cardList().getD_deck());
        }
    }

    /**
     * Validates the blockade order to ensure that it can be executed.
     * Checks if the player has a blockade card and if the specified country is owned by the player.
     *
     * @param gameEngine The game engine that provides the context for validation.
     * @return {@code true} if the order is valid; {@code false} otherwise.
     */

    @Override
    public boolean valid(GameEngine gameEngine) {
        GameMap gameMap = GameMap.getInstance();
        Player l_callingPlayer = gameEngine.getD_currentPlayer();
        //checking if card is available
        if (!l_callingPlayer.getD_cardList().valid("blockade")){
            System.out.println(l_callingPlayer.getD_name() + " does not have blockade card");
            return false;
        }
        if (!(l_callingPlayer.getD_listCountries().containsKey(String.valueOf(d_countryID)))){
            System.out.println("This Country is not owned by you ! Please choose country owned by you");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrdertype() {
        return "blockade";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setD_player(Player p_player) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setD_orderParameters(ArrayList<String> lOrderParams) {

    }
    public String getD_orderParameters() {
        return "blockade " + String.valueOf(d_countryID);
    }
}
