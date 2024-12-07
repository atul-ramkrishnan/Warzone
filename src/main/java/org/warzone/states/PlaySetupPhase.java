package org.warzone.states;

import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;
import org.warzone.strategy.*;
import org.warzone.logging.LogEntry;
import org.warzone.operations.DominationMapIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The {@code IssueOrderPhase} class represents the phase where the game is being set up.
 */
public class PlaySetupPhase extends Play {
    /**
     * Constructor for {@code PlaySetupPhase}
     */
    public PlaySetupPhase() {
        System.out.println("Phase changed to play-setup.");
        System.out.println("Available commands in this phase : ");
        System.out.println("1. gameplayer\n2. assigncountries\n3. tournament\n4. savegame");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gamePlayer(String[] p_setUpCommands, ArrayList<Player> p_playerList) {
        for (int i = 1; i < p_setUpCommands.length; i++) {
            if (p_setUpCommands[i].equals("-add") && i < p_setUpCommands.length - 1) {
                Player l_player = new Player(p_setUpCommands[i + 1]);
                p_playerList.add(l_player);
                GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.PLAY_SETUP,
                                        p_setUpCommands[i + 1] + " added as a Player."));
            } else if (p_setUpCommands[i].equals("-remove") && i < p_setUpCommands.length - 1) {
                int index = 0;
                for (Player player : p_playerList) {
                    if (p_setUpCommands[2].equals(player.getD_name())) {
                        p_playerList.remove(index);
                        System.out.println("Removed player : " + p_setUpCommands[2]);
                        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.PLAY_SETUP,
                                                p_setUpCommands[2] + " removed from Players."));

                    } else {
                        System.out.println("Player not found, please enter a valid player name");
                    }
                }

            }
        }

        Scanner sc = new Scanner(System.in);

        p_playerList.forEach(player -> {
            System.out.println("Enter player type for " + player.getD_name());
            player.setPlayerType(sc.nextLine());
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignCountries(ArrayList<Player> p_PlayerList) throws IOException {
        if (p_PlayerList.size() < 2) {
            System.out.println("\nThere must be at-least 2 players to start the game.\n");
            return;
        }
        GameMap gameMap = GameMap.getInstance();
        int p_playersSize = p_PlayerList.size();
        int p_countriesSize = gameMap.getCountryMap().size();
        int p_playerIndex;
        int[] randArray = new Random().ints(1, p_countriesSize + 1).distinct().limit(p_countriesSize).toArray();
        for (int i = 0; i < p_countriesSize; i++) {
            p_playerIndex = i % p_playersSize;
            p_PlayerList.get(p_playerIndex).getD_listCountries().put(String.valueOf(randArray[i]), gameMap.getCountryMap().get(Integer.toString(randArray[i])));
            gameMap.getCountryMap().get(Integer.toString(randArray[i])).setCountryOwner(p_PlayerList.get(p_playerIndex));
        }
        p_PlayerList.forEach(player -> {
            List<Country> countryList = new ArrayList<>();
            player.getD_listCountries().forEach((index, country) -> countryList.add(country));
            switch (player.getPlayerType()) {
                case "aggressive" -> {
                    player.setPlayerStrategy(new AggressiveStrategy(countryList, player));
                    player.setHuman(false);
                }
                case "benevolent" -> {
                    player.setPlayerStrategy(new BenevolentStrategy(countryList, player));
                    player.setHuman(false);
                }
                case "cheater" -> {
                    player.setPlayerStrategy(new CheaterStrategy(countryList, player));
                    player.setHuman(false);
                }
                case "random" -> {
                    player.setPlayerStrategy(new RandomStrategy(countryList, player));
                    player.setHuman(false);
                }
                default -> {
                    player.setHuman(true);
                }
            }
        });
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.PLAY_SETUP, "Countries assigned to players."));
        gameEngine.setGamePhase(new IssueOrderPhase());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(Player p_player, int p_countryIndex, int p_armyAmount) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void advance(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bomb(int p_countryID) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void blockade(int p_countryID) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void airlift(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void negotiate(Player p_initiator, Player p_receiver) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() {
        printInvalidCommandMessage();
    }
}
