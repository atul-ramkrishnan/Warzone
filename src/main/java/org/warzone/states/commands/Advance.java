package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;

import static org.warzone.operations.BinomialDistributionPicker.getNumberOfKills;

/**
 * The {@code Advance} class implements the {@code Order} interface
 * and encapsulates an order to advance armies from one country to another.
 */
public class Advance implements Order {
    private int d_countryOrigin;
    private int d_countryDestination;
    private int d_numArmiesToMove;
    private int d_totalOriginArmies;

    /**
     * Constructs an {@code Advance} order with the specified origin country, destination country,
     * and number of armies to move.
     *
     * @param p_countryOrigin      The ID of the country where the armies are advancing from.
     * @param p_countryDestination The ID of the country where the armies are advancing to.
     * @param p_numArmiesToMove    The number of armies to be moved.
     */
    public Advance(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove){
        this.d_countryOrigin = p_countryOrigin;
        this.d_countryDestination = p_countryDestination;
        this.d_numArmiesToMove = p_numArmiesToMove;
    }
    /**
     * Executes an attack from one country to another if countries have different owners.
     * Otherwise simply moves players
     */
    public void execute(GameEngine gameEngine) {
        if (valid(gameEngine)){
            System.out.println("Advancing " + d_numArmiesToMove + " from " + d_countryOrigin + " to " + d_countryDestination);
            GameMap gameMap = GameMap.getInstance();
            Player l_attacker = gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).getCountryOwner();
            Player l_defender = gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).getCountryOwner();
            int p_defendingArmies = gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).getD_noOfArmies();
            if (l_attacker.equals(l_defender)){
                gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).setD_noOfArmies(p_defendingArmies+d_numArmiesToMove);
                gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).setD_noOfArmies(d_totalOriginArmies-d_numArmiesToMove);
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Advance order executed: " +
                        d_numArmiesToMove + " moved to " + gameMap.getCountryMap().get(Integer.toString(d_countryDestination)).getName() + "."));
            }
            else if (l_attacker.hasActiveDiplomacyWith(l_defender)) {
                System.out.println("Cannot advance due to negotiate card");
            }
            else {
                // Calculating number of successful attacks - 60% chance of attacker killing one unit
                int l_SuccessfulOffensiveAttacks = getNumberOfKills(d_numArmiesToMove, 0.6);

                //Calculating number of defensive attacks - 70% chance of defender killing one unit
                int l_SuccessfulDefensiveAttacks = getNumberOfKills(p_defendingArmies, 0.7);

                // Allow attack
                int l_numAttackingArmiesRemaining = d_numArmiesToMove - l_SuccessfulDefensiveAttacks; //defensive attacks performed on moved armies only
                if (l_numAttackingArmiesRemaining < 0) {
                    l_numAttackingArmiesRemaining = 0;
                }
                p_defendingArmies = p_defendingArmies - l_SuccessfulOffensiveAttacks;
                if (p_defendingArmies < 0) {
                    p_defendingArmies = 0;
                }

                //transfer country if all defending armies killed
                if (p_defendingArmies == 0) {

                    //changing counts
                    gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).setD_noOfArmies(l_numAttackingArmiesRemaining);
                    gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).setD_noOfArmies(d_totalOriginArmies - d_numArmiesToMove);

                    //changing owners in D_List
                    Country l_removedCountry = l_defender.getD_listCountries().remove(String.valueOf(d_countryDestination));
                    l_attacker.getD_listCountries().put(String.valueOf(d_countryDestination), l_removedCountry);

                    //changing owners from Singleton
                    gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).setCountryOwner(l_attacker);
                    GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Advance order executed: " +
                            gameMap.getCountryMap().get(Integer.toString(d_countryDestination)).getName() +
                            "captured by " + l_attacker.getD_name() + " with " + l_numAttackingArmiesRemaining +
                            " armies remaining."));
                    l_attacker.getD_cardList().addCardToDeck();
                }
                //else just reassign army counts
                else {
                    gameMap.getCountryMap().get(String.valueOf(d_countryDestination)).setD_noOfArmies(p_defendingArmies);
                    gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).setD_noOfArmies(d_totalOriginArmies - d_numArmiesToMove + l_numAttackingArmiesRemaining);
                    GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Advance order executed: " +
                            gameMap.getCountryMap().get(Integer.toString(d_countryDestination)).getName() +
                            "failed to be captured by " + l_attacker.getD_name() + ". " + p_defendingArmies +
                            " armies remain."));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean valid(GameEngine gameEngine) {
        GameMap gameMap = GameMap.getInstance();
        //checking attacks are not greater than armies on site
        d_totalOriginArmies = gameMap.getCountryMap().get(String.valueOf(d_countryOrigin)).getD_noOfArmies();
        if (d_numArmiesToMove > d_totalOriginArmies) {
            System.out.println("There are not enough armies to advance");
            System.out.println("Deployed armies : " + d_totalOriginArmies + "\nArmies ordered to move : " + d_numArmiesToMove + "\n");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getOrdertype() {
        return "advance";
    }

    /**
     * {@inheritDoc}
     */
    public void setD_player(Player player) {

    }

    public String getD_orderParameters() {
        return "advance " + String.valueOf(d_countryOrigin) + " " + String.valueOf(d_countryDestination) + " " + String.valueOf(d_numArmiesToMove);
    }

    /**
     * {@inheritDoc}
     */
    public void setD_orderParameters(ArrayList<String> lOrderParams) {

    }
}
