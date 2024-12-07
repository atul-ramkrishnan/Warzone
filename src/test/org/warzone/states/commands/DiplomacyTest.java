package org.warzone.states.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.warzone.GameEngine;
import org.warzone.entities.Cards;
import org.warzone.operations.DominationMapIO;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This {@code DiplomacyTest} class contains tests for the Diplomacy command in the game.
 * It sets up a game scenario with multiple players and tests the Diplomacy command execution,
 * ensuring that diplomacy is correctly established and terminated according to game rules.
 */
public class DiplomacyTest {
    static ArrayList<Player> d_playerList;
    static GameEngine d_gameEngine;

    /**
     * Sets up the game environment before all the test methods run.
     * Initializes players and the game engine, loads a game map, and assigns countries to the players.
     *
     * @throws IOException if there is an error loading the map.
     */
    @BeforeAll
    static void setUp() throws IOException {
        d_gameEngine = new GameEngine();
        d_playerList = new ArrayList<>();
        Player initiator = new Player("Initiator");

        Cards cards = new Cards();
        Map<String,Integer> d_deck = new HashMap<String,Integer>();
        d_deck.put("negotiate", 5);

        cards.setD_deck(d_deck);
        initiator.setD_cardList(cards);

        d_playerList.add(initiator);

        Player receiver = new Player("Initiator");
        receiver.setD_cardList(cards);
        d_playerList.add(receiver);

        Player nonParticipant = new Player("NonParticipant");
        nonParticipant.setD_cardList(cards);
        d_playerList.add(nonParticipant);


        GameMap l_gameMap = GameMap.getInstance();
        l_gameMap.setFileName("artic");
        DominationMapIO l_dominationMapIO = new DominationMapIO();
        l_dominationMapIO.loadMap("artic");
        //Assigning Countries
        Player.assignCountries(d_playerList);
    }

    /**
     * Tests the execution of the Diplomacy command.
     * Verifies that the command correctly establishes and terminates diplomacy
     * between players over the course of game rounds.
     */
    @Test
    public void testExecute() {



        Diplomacy diplomacyCommand = new Diplomacy(d_playerList.get(0), d_playerList.get(1));
        diplomacyCommand.execute(d_gameEngine);

        // Round 1: All diplomacies are inactive until next round.
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(1)));
        assertFalse(d_playerList.get(1).hasActiveDiplomacyWith(d_playerList.get(0)));
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(2)));
        assertFalse(d_playerList.get(2).hasActiveDiplomacyWith(d_playerList.get(0)));


        // Round 2: Diplomacies have been activated.
        GameEngine.incrementRoundNumber();
        assertTrue(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(1)));
        assertTrue(d_playerList.get(1).hasActiveDiplomacyWith(d_playerList.get(0)));
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(2)));
        assertFalse(d_playerList.get(2).hasActiveDiplomacyWith(d_playerList.get(0)));

        // Round 3: Diplomacies are still active.
        GameEngine.incrementRoundNumber();
        assertTrue(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(1)));
        assertTrue(d_playerList.get(1).hasActiveDiplomacyWith(d_playerList.get(0)));
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(2)));
        assertFalse(d_playerList.get(2).hasActiveDiplomacyWith(d_playerList.get(0)));

        // Round 4: Diplomacies are terminated.
        GameEngine.incrementRoundNumber();
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(1)));
        assertFalse(d_playerList.get(1).hasActiveDiplomacyWith(d_playerList.get(0)));
        assertFalse(d_playerList.get(0).hasActiveDiplomacyWith(d_playerList.get(2)));
        assertFalse(d_playerList.get(2).hasActiveDiplomacyWith(d_playerList.get(0)));

    }
}
