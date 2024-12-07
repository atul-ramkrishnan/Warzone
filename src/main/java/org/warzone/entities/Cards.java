package org.warzone.entities;
import java.util.*;

/**
 * The Cards class stores the cards owned by a Player and updates the card deck
 * based on the player's moves.
 */
public class Cards {
    private String[] d_cardTypes = {"bomb", "blockade", "airlift", "negotiate"};
    private Map<String,Integer> d_deck = new HashMap<String,Integer>();
    /**
     * Sets the deck of cards.
     * @param d_deck The map representing the deck of cards with card types as keys and counts as values.
     */
    public void setD_deck(Map<String, Integer> d_deck) {
        this.d_deck = d_deck;
    }
    /**
     * Retrieves the current deck of cards.
     * @return The map representing the deck of cards.
     */
    public Map<String, Integer> getD_deck() {
        return d_deck;
    }
    /**
     * Adds a random card to the deck.
     */
    public void addCardToDeck(){
        Random l_rand = new Random();
        int l_randNum = l_rand.nextInt(4);
        String p_cardType = d_cardTypes[l_randNum];
        if(d_deck.containsKey(p_cardType))
            d_deck.put(p_cardType,d_deck.get(p_cardType)+1);
        else
            d_deck.put(p_cardType,1);
        System.out.println("Added card to deck. Currently deck is: "+ d_deck);
    }
    /**
     * Validates the availability of a card type in the deck and reduces its count if available.
     * @param p_cardType The type of card to validate and potentially reduce its count.
     * @return true if the card is available and its count is reduced, false if the card does not exist.
     */
    public boolean valid(String p_cardType){
            if(d_deck.containsKey(p_cardType)) {
                return true;
            }
            else {
                return false;
            }
    }
}
