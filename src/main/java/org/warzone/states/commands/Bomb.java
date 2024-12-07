package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;
import java.util.Map;

/**
 * "Bomb" is a type of order that the user can enter. It is implemented by the Bomb Class
 * Destroys half of the armies located on an opponent’s territory that is adjacent to one of the current player’s territories
 */

public class Bomb implements Order{

    private int d_countryID;

    /**
     * The list of order parameters.
     */
    public ArrayList<String> d_orderParameters;
    private Player d_player;
    private int p_countryIndex;

    /**
     * Creates a new Bomb order with the specified target country ID.
     * @param p_countryID The ID of the country to be bombed.
     */
    public Bomb(int p_countryID){
        this.d_countryID = p_countryID;
    }

    /**
     * Executes the Bomb order, reducing the number of armies in the targeted country by half.
     */
    @Override
    public void execute(GameEngine gameEngine) {
        if (valid(gameEngine)) {
            System.out.println("Bombing country index " + p_countryIndex);
            GameMap gameMap = GameMap.getInstance();
            int l_defendingArmies = gameMap.getCountryMap().get(String.valueOf(d_countryID)).getD_noOfArmies();
            int l_armiesAfterBombing = 0;
            Player d_bombedPlayer = gameMap.getCountryMap().get(Integer.toString(d_countryID)).getCountryOwner();
            if (gameEngine.getD_currentPlayer().hasActiveDiplomacyWith(d_bombedPlayer)) {
                System.out.println("Cannot Bomb due to negotiate card");
            }
            else {
                try {
                    l_armiesAfterBombing = (int) Math.ceil(l_defendingArmies / 2);
                } catch (Exception e) {
                    System.out.println("Issue : " + e);
                }
                gameMap.getCountryMap().get(String.valueOf(d_countryID)).setD_noOfArmies(l_armiesAfterBombing);
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Bomb order executed: " +
                        l_armiesAfterBombing + " remain in " +
                        gameMap.getCountryMap().get(Integer.toString(d_countryID)).getName() + "."));
            }

            gameEngine.getD_currentPlayer().getD_cardList().getD_deck().put("bomb",gameEngine.getD_currentPlayer().getD_cardList().getD_deck().get("bomb")-1);
            if(gameEngine.getD_currentPlayer().getD_cardList().getD_deck().get("bomb") == 0){
                gameEngine.getD_currentPlayer().getD_cardList().getD_deck().remove("bomb");
            }
            System.out.println("Removed card from deck. Currently deck is: "+ gameEngine.getD_currentPlayer().getD_cardList().getD_deck());
        }
    }
    /**
     * Validation check to see if the "bomb" order can be executed
     * @return True if the order is valid; else, false.
     */
    @Override
    public boolean valid(GameEngine gameEngine) {
        GameMap gameMap = GameMap.getInstance();
        //checking if card is available
        if (!gameEngine.getD_currentPlayer().getD_cardList().valid("bomb")){
            System.out.println(gameEngine.getD_currentPlayer().getD_name() + " does not have bomb card");
            return false;
        }
        //get the target's neighbours
        System.out.println("Bombed country : "+d_countryID);
        Map<String, Country> neighbours = gameMap.getCountryMap().get(Integer.toString(d_countryID)).getAdjacentCountries();
        for (Map.Entry<String, Country> entry1 : neighbours.entrySet()) {
            Country neighbour = entry1.getValue();
            String index = entry1.getKey();
            Player l_bomber = gameMap.getCountryMap().get(String.valueOf(index)).getCountryOwner();
            if(l_bomber != null && l_bomber.getD_name().equals(gameEngine.getD_currentPlayer().getD_name())){
                System.out.println("Bombing country found to be a neighbour : "+neighbour.getName()+", index "+neighbour.getIndex());
                System.out.println("Bombing will be executed");
                return true;
            }

        }
        System.out.println("Entered ID is not an adjacent country");

        return false;
    }

    /**
     * Get the type of this order, which is "bomb".
     * @return The order type as string.
     */
    @Override
    public String getOrdertype() {
        return "bomb";
    }

    /**
     * Sets the player who issues the Bomb order.
     * @param player The player issuing the order.
     */
    @Override
    public void setD_player(Player player) {
        this.d_player = player;
    }

    /**
     * Used to set parameters that are needed for the order
     * @param lOrderParams The list of order parameters.
     */
    @Override
    public void setD_orderParameters(ArrayList<String> lOrderParams) {
        this.d_orderParameters = lOrderParams;
        p_countryIndex = Integer.parseInt(d_orderParameters.get(0));
    }

    public String getD_orderParameters() {
        return "bomb " + String.valueOf(d_countryID);
    }
}
