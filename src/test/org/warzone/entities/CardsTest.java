package org.warzone.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CardsTest {
    private static Cards d_cards;
    private static String[] d_cardTypes = {"bomb", "blockade", "airlift", "negotiate"};
    @BeforeEach
    public void setUp() {
        d_cards = new Cards();
    }

    @Test
    public void testSetDeck() {
        HashMap<String, Integer> l_deck = new HashMap<String, Integer>();
        l_deck.put(d_cardTypes[0], 2);
        l_deck.put(d_cardTypes[1], 2);
        d_cards.setD_deck(l_deck);

        assertTrue(d_cards.getD_deck().containsKey(d_cardTypes[0]));
        assertTrue(d_cards.getD_deck().containsKey(d_cardTypes[1]));
    }

    @Test
    public void testAddToDeck() {
        assertTrue(d_cards.getD_deck().isEmpty());
        d_cards.addCardToDeck();
        assertTrue(d_cards.getD_deck().size() == 1);
    }

    @Test
    public void testValid() {
        assertFalse(d_cards.valid(d_cardTypes[0]));
        HashMap<String, Integer> l_deck = new HashMap<String, Integer>();
        l_deck.put(d_cardTypes[0], 2);
        d_cards.setD_deck(l_deck);

        assertTrue(d_cards.valid(d_cardTypes[0]));
    }
}