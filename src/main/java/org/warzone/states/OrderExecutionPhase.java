package org.warzone.states;

import org.warzone.savegame.GameStateManager;
import org.warzone.strategy.Player;
import org.warzone.states.commands.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code IssueOrderPhase} class represents the phase where the orders are executed.
 */
public class OrderExecutionPhase extends Phase {

    private GameStateManager l_gameStateManager;

    /**
     * Constructor for {@code OrderExecutionPhase}
     */
    public OrderExecutionPhase() {
        System.out.println("Phase changed to order execution.");
        System.out.println("\nExecuting all orders.");
    }

    String[] orderType = {"deploy", "airlift", "blockade", "bomb", "advance", "negotiate"};

    /**
     * {@inheritDoc}
     */
    public void executeOrdersRoundRobin(ArrayList<Player> p_playerList) {

        ArrayList<Player> l_playerList = new ArrayList<Player>(p_playerList);
        for (String s : orderType) {
            for (Player player : l_playerList) {
                gameEngine.setD_currentPlayer(player);
                List<Order> orderListCopy = new ArrayList<>(player.getOrderList());
                for (Order order : orderListCopy) {
                    if (s.equals(order.getOrdertype())) {
                        order.execute(gameEngine);
//                        System.out.println("Executing order :" + order.getOrdertype());
                        player.getOrderList().remove(order);
                    }

                }
            }
        }

        gameEngine.setGamePhase(new IssueOrderPhase());
//        while (l_playerList.size() > 0) {
//            for (int i = 0; i < l_playerList.size(); i++) {
//                if (l_playerList.get(i).getOrderList().size() != 0) {
//                    Order order = p_playerList.get(i).next_order();
//                    if(order.getOrdertype().equals("deploy")){
//                        order.execute();
//
//                    }
//                    System.out.println("Player " + l_playerList.get(i).getD_name() + " Executing order "+order.getOrdertype());
//                    order.execute();
//                } else {
//                    l_playerList.remove(i);
//                    --i;
//                }
//            }
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMap() {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMapWithPlayers(List<Player> p_players) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editMap(String p_fileName) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editContinent(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editCountry(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editNeighbor(String[] p_mapEditCommands) {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMap(String[] p_mapEditCommands) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(String p_fileName) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMap(String p_fileName) {
        printInvalidCommandMessage();
    }

    @Override
    public void saveGame(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java","org","warzone","savegame", p_fileName + ".json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.saveGameState(gameEngine);
    }

    @Override
    public void loadGame(String p_fileName) throws IOException {
        Path l_filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java","org","warzone","savegame", p_fileName + ".json");
        File l_file = new File(l_filePath.toString());
        GameStateManager l_gameStateManager = new GameStateManager(l_file.toString());
        l_gameStateManager.loadGameState(gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignCountries(ArrayList<Player> l_PlayerList) throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gamePlayer(String[] setUpCommands, ArrayList<Player> l_PlayerList) {
        printInvalidCommandMessage();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {
        printInvalidCommandMessage();
    }
}
