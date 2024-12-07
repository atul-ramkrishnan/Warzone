package org.warzone.states;

import org.warzone.GameEngine;
import org.warzone.entities.GameMap;
import org.warzone.savegame.GameStateManager;
import org.warzone.strategy.Player;
import org.warzone.logging.LogEntry;
import org.warzone.states.commands.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The {@code IssueOrderPhase} class represents the phase where the orders are issued.
 */
public class IssueOrderPhase extends Play {
    /**
     * Constructor for {@code IssueOrderPhase}
     */
    public IssueOrderPhase() {
        System.out.println("Phase changed to issue order.");
        System.out.println("Available commands in this phase : ");
        System.out.println("1. deploy\n2. advance\n3. bomb\n4. blockade\n5. airlift\n6. negotiate\n7. savegame");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMap(String p_fileName) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    public void assignCountries(ArrayList<Player> l_PlayerList) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(Player p_player, int p_countryIndex, int p_armyAmount) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                " issues a deploy order of " + p_armyAmount + " armies to " +
                        GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryIndex)).getName() + "."));

        Deploy deploy = new Deploy(p_player, p_countryIndex, p_armyAmount);
        if (deploy.valid(gameEngine)) {
            gameEngine.getD_currentPlayer().getOrderList().add(deploy);
            System.out.println("Deploy command added to the queue!");
            int l_ArmiesInDeployQueue = gameEngine.getD_currentPlayer().getD_armiesInDeployQueue();
            gameEngine.getD_currentPlayer().setD_armiesInDeployQueue(l_ArmiesInDeployQueue + p_armyAmount);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void advance(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                        " issues an advance order of " + p_numArmiesToMove + " armies from " +
                        GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryOrigin)).getName() +
                        " to "+ GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryDestination)).getName() + "."));

        Advance advance = new Advance(p_countryOrigin, p_countryDestination, p_numArmiesToMove);
        if (gameEngine.getD_currentPlayer().getD_armiesInDeployQueue() == gameEngine.getD_currentPlayer().getD_unDeployedArmies()) {
            gameEngine.getD_currentPlayer().getOrderList().add(advance);
            System.out.println("Advance command added to the queue!");
        } else {
            System.out.println("Please deploy all armies before advancing");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bomb(int p_countryID) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                        " issues a bomb order on " + GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryID)).getName() + "."));

        Bomb bomb = new Bomb(p_countryID);
        if (bomb.valid(gameEngine)) {
            gameEngine.getD_currentPlayer().getOrderList().add(bomb);
            System.out.println("Bomb command added to the queue!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void blockade(int p_countryID) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                        " issues a blockade order on " + GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryID)).getName() + "."));

        Blockade blockade = new Blockade(p_countryID);
        if (blockade.valid(gameEngine)) {
            gameEngine.getD_currentPlayer().getOrderList().add(blockade);
            System.out.println("Blockade command added to the queue!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void airlift(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                        " issues an airlift order of " + p_numArmiesToMove + " armies to " +
                        GameMap.getInstance().getCountryMap().get(Integer.toString(p_countryDestination)).getName() + "."));

        Airlift airlift = new Airlift(p_countryOrigin, p_countryDestination, p_numArmiesToMove);
        if (airlift.valid(gameEngine)){
            gameEngine.getD_currentPlayer().getOrderList().add(airlift);
            System.out.println("Airlift command added to the queue!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void negotiate(Player p_initiator, Player p_receiver) {
        GameEngine.addToLog(new LogEntry(LogEntry.GamePhase.ISSUE_ORDER,
                gameEngine.getD_currentPlayer().getD_name() +
                        " issues a negotiate order with " +  p_receiver.getD_name() + "."));

        Diplomacy diplomacy = new Diplomacy(p_initiator, p_receiver);
        if (diplomacy.valid(gameEngine)) {
            gameEngine.getD_currentPlayer().getOrderList().add(diplomacy);
            System.out.println("Negotiate command added to the queue!");
        }
    }

    @Override
    public void saveGame(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java","org","warzone","savegame", p_fileName + ".json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.saveGameState(gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() {
        System.out.println("In reinforce");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() {
        System.out.println("In fortify");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gamePlayer(String[] setUpCommands, ArrayList<Player> l_PlayerList) {
        printInvalidCommandMessage();
    }
}
