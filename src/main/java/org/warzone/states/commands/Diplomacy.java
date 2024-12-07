package org.warzone.states.commands;

import org.warzone.GameEngine;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;

import java.util.ArrayList;

/**
 * The {@code Diplomacy} class implements the {@code Order} interface and
 * represents an application of the diplomacy card in the game. This order, when executed,
 * establishes a temporary truce between the initiator and the receiver for
 * a predefined number of rounds.
 */
public class Diplomacy implements Order{
    /**
     * The player who initiates the diplomacy order.
     */
    private final Player d_initiator;
    /**
     * The player who is the target of the diplomacy order.
     */
    private final Player d_receiver;

    /**
     * The constant number of rounds for which the diplomacy will be active.
     */
    private static final int DIPLOMACY_ACTIVE_FOR_ROUNDS = 2;

    /**
     * Constructs a {@code Diplomacy} order with the specified initiator and receiver.
     *
     * @param p_initiator The player initiating the diplomacy.
     * @param p_receiver  The player who is the target of the diplomacy.
     */
    public Diplomacy(Player p_initiator, Player p_receiver){
        this.d_initiator = p_initiator;
        this.d_receiver = p_receiver;
    }

    /**
     * Executes the diplomacy order, establishing a truce between the initiator and the receiver.
     *
     * @param p_gameEngine The game engine.
     */
    @Override
    public void execute(GameEngine p_gameEngine) {
        if (valid(p_gameEngine)) {
            getReceiver().applyDiplomacyCard(new DiplomacyDetails(getInitiator(),
                    p_gameEngine.getRoundNumber() + 1,
                    p_gameEngine.getRoundNumber() + 1 + DIPLOMACY_ACTIVE_FOR_ROUNDS));
            getInitiator().applyDiplomacyCard(new DiplomacyDetails(getReceiver(),
                    p_gameEngine.getRoundNumber() + 1,
                    p_gameEngine.getRoundNumber() + 1 + DIPLOMACY_ACTIVE_FOR_ROUNDS));
            GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.EXECUTE_ORDER, "Diplomacy card executed: " +
                    "Diplomacy enforced between " + getInitiator().getD_name() + " and " + getReceiver().getD_name() +
                    " for the next " + DIPLOMACY_ACTIVE_FOR_ROUNDS + " rounds."));


            d_initiator.getD_cardList().getD_deck().put("negotiate",d_initiator.getD_cardList().getD_deck().get("negotiate")-1);
            if(d_initiator.getD_cardList().getD_deck().get("negotiate") == 0){
                d_initiator.getD_cardList().getD_deck().remove("negotiate");
            }
            System.out.println("Removed card from deck. Currently deck is: "+ d_initiator.getD_cardList().getD_deck());
        }
    }

    /**
     * Validates the diplomacy order to ensure that it is being applied correctly within the game rules.
     * Specifically, it checks that a player is not attempting to declare diplomacy with themselves.
     *
     * @param gameEngine The game engine.
     * @return {@code true} if the order is valid; {@code false} otherwise.
     */
    @Override
    public boolean valid(GameEngine gameEngine) {
        if (!d_initiator.getD_cardList().valid("negotiate")){
            System.out.println(d_initiator.getD_name() + " does not have negotiate card");
            return false;
        }
        return d_initiator != d_receiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrdertype() {
        return "negotiate";
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
    public Player getInitiator() {
        return d_initiator;
    }

    /**
     * {@inheritDoc}
     */
    public Player getReceiver() {
        return d_receiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setD_orderParameters(ArrayList<String> lOrderParams) {

    }

    public String getD_orderParameters() {
        return "negotiate " + d_initiator.getD_name() + " " + d_receiver.getD_name();
    }

}
