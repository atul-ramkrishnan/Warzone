package org.warzone.strategy;

import org.warzone.GameEngine;
import org.warzone.entities.Cards;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.operations.DominationMapIO;
import org.warzone.states.commands.DiplomacyDetails;
import org.warzone.states.commands.Order;
import org.warzone.states.Phase;

import java.io.IOException;
import java.util.*;

/**
 * The {@code Player} class represent a Player within a game. It stores fields such as the player name,
 * the player's cards, etc.
 */
public class Player {

    private int d_unDeployedArmies;
    private int d_armiesInDeployQueue;
    private String d_name;
    private PlayerStrategy playerStrategy;
    private boolean isHuman;
    private String playerType;

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public PlayerStrategy getPlayerStrategy() {
        return playerStrategy;
    }

    public void setPlayerStrategy(PlayerStrategy playerStrategy) {
        this.playerStrategy = playerStrategy;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    /**
     * Gets the number of armies in the player's deploy queue.
     *
     * @return the number of armies in the deploy queue
     */
    public int getD_armiesInDeployQueue() {
        return d_armiesInDeployQueue;
    }

    /**
     * Sets the number of armies in the player's deploy queue.
     *
     * @param d_armiesInDeployQueue the number of armies to set in the deploy queue
     */
    public void setD_armiesInDeployQueue(int d_armiesInDeployQueue) {
        this.d_armiesInDeployQueue = d_armiesInDeployQueue;
    }

    /**
     * Gets the number of undeployed armies of the player.
     *
     * @return the number of undeployed armies
     */
    public int getD_unDeployedArmies() {
        return d_unDeployedArmies;
    }

    /**
     * Sets the number of undeployed armies of the player.
     *
     * @param d_unDeployedArmies the number of undeployed armies to set
     */
    public void setD_unDeployedArmies(int d_unDeployedArmies) {
        this.d_unDeployedArmies = d_unDeployedArmies;
    }

    private Map<String, Country> d_listCountries;
    private Queue<Order> orderList = new LinkedList<>();

    /**
     * Sets the number of armies.
     *
     * @param armies the number of armies to set
     */
    public void setArmies(int armies) {
        this.d_unDeployedArmies = armies;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player
     */
    public String getD_name() {
        return d_name;
    }

    /**
     * Sets the name of the player.
     *
     * @param d_name the name of the player to set
     */
    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    /**
     * Gets the countries owned by the player.
     *
     * @return the map of countries owned by the player
     */
    public Map<String, Country> getD_listCountries() {
        return d_listCountries;
    }

    /**
     * Sets the countries owned by the player.
     *
     * @param d_listCountries the map of countries owned by the player to set
     */
    public void setD_listCountries(Map<String, Country> d_listCountries) {
        this.d_listCountries = d_listCountries;
    }

    /**
     * Gets the order list of the player.
     *
     * @return the order list of the player
     */
    public Queue<Order> getOrderList() {
        return orderList;
    }

    /**
     * Sets the order list of the player.
     *
     * @param orderList the order list of the player to set
     */
    public void setOrderList(Queue<Order> orderList) {
        this.orderList = orderList;
    }

    private List<DiplomacyDetails> d_activeDiplomacies;

    /**
     * Gets the card list of the player.
     *
     * @return the card list of the player
     */
    public Cards getD_cardList() {
        return d_cardList;
    }

    /**
     * Sets the card list of the player.
     *
     * @param d_cardList the card list of the player to set
     */
    public void setD_cardList(Cards d_cardList) {
        this.d_cardList = d_cardList;
    }

    private Cards d_cardList;

    /**
     * Creates a new player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        d_name = name;
        d_listCountries = new HashMap<>();
        d_activeDiplomacies = new ArrayList<>();
        d_unDeployedArmies = 0;
        d_cardList = new Cards();
    }

    /**
     * Evenly distributes countries in random order among players. The remainder resulting from the division of Number of countries
     * by the number of players will also be distributed by giving the player at most one country from this remainder.
     *
     * @param playerList List of Players
     * @throws IOException if Map is loaded incorrectly
     */
    public static void assignCountries(List<Player> playerList) throws IOException {
        GameMap gameMap = GameMap.getInstance();
        int p_playersSize = playerList.size();
        int p_countriesSize = gameMap.getCountryMap().size();
        int p_playerIndex;
        int[] randArray = new Random().ints(1, p_countriesSize + 1).distinct().limit(p_countriesSize).toArray();
        for (int i = 0; i < p_countriesSize; i++) {
            p_playerIndex = i % p_playersSize;
            playerList.get(p_playerIndex).d_listCountries.put(String.valueOf(randArray[i]), gameMap.getCountryMap().get(Integer.toString(randArray[i])));
            gameMap.getCountryMap().get(Integer.toString(randArray[i])).setCountryOwner(playerList.get(p_playerIndex));
        }
    }

    /**
     * Applies a diplomacy card to the player.
     *
     * @param p_diplomacy the diplomacy details to apply
     */
    public void applyDiplomacyCard(DiplomacyDetails p_diplomacy) {
        d_activeDiplomacies.add(p_diplomacy);
    }

    private List<DiplomacyDetails> getActiveDiplomacies() {
        return d_activeDiplomacies;
    }

    /**
     * Updates the active diplomacies for a new round.
     */
    public void updateDiplomaciesForNewRound() {
        Iterator<DiplomacyDetails> l_iterator = d_activeDiplomacies.iterator();
        while (l_iterator.hasNext()) {
            DiplomacyDetails diplomacy = l_iterator.next();
            if (diplomacy.getEndRound() < GameEngine.getRoundNumber()) {
                l_iterator.remove();
            }
        }
    }

    /**
     * Checks if the player has an active diplomacy with another player.
     *
     * @param p_otherPlayer the other player to check for active diplomacy
     * @return true if the player has an active diplomacy with the other player, otherwise false
     */
    public boolean hasActiveDiplomacyWith(Player p_otherPlayer) {
        for (DiplomacyDetails l_diplomacy : getActiveDiplomacies()) {
            if (l_diplomacy.getReceiver().equals(p_otherPlayer) &&
                    l_diplomacy.getStartRound() <= GameEngine.getRoundNumber() &&
                    l_diplomacy.getEndRound() > GameEngine.getRoundNumber()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Allows a player to issue orders based on user input.
     *
     * @throws IOException If an I/O error occurs during input.
     */
    public void issue_order() throws IOException {
        Scanner sc = new Scanner(System.in);
        if (isHuman) {
            humanPlayerInput(sc);
        } else {
            List<Country> l_map = new ArrayList<>();
            d_listCountries.forEach((s, country) -> l_map.add(country));
            playerStrategy.setD_map(l_map);
            playerStrategy.createOrder();
        }
    }
    /**
     * Handles user input for a human player, parsing and executing commands accordingly.
     * The method takes a Scanner object as a parameter to read user input from the console.
     *
     * @param sc The Scanner object for reading user input.
     * @throws IOException If an I/O error occurs.
     */
    private void humanPlayerInput(Scanner sc) throws IOException {
        System.out.println("Player " + d_name + "\nEnter Your Orders");
        String l_userInput = sc.nextLine();
        String[] l_userInputArray = l_userInput.split(" ");

        if (!executeCommands(l_userInputArray, new ArrayList<>(), GameEngine.getGamePhase())) {
            switch (l_userInputArray[0]) {
                case "savegame"->{
                    GameEngine.getGamePhase().saveGame(l_userInputArray[1]);
                }
                case "showmap" -> {
                    GameEngine.getGamePhase().showMapWithPlayers(GameEngine.getD_playerList());
                }
                case "deploy" -> {
                    GameEngine.getGamePhase().deploy(this, Integer.parseInt(l_userInputArray[1]), Integer.parseInt(l_userInputArray[2]));
                }
                case "advance" -> {
                    GameEngine.getGamePhase().advance(Integer.parseInt(l_userInputArray[1]), Integer.parseInt(l_userInputArray[2]), Integer.parseInt(l_userInputArray[3]));
                }
                case "bomb" -> {
                    GameEngine.getGamePhase().bomb(Integer.parseInt(l_userInputArray[1]));
                }
                case "blockade" -> {
                    GameEngine.getGamePhase().blockade(Integer.parseInt(l_userInputArray[1]));
                }
                case "airlift" -> {
                    GameEngine.getGamePhase().airlift(Integer.parseInt(l_userInputArray[1]),Integer.parseInt(l_userInputArray[2]),Integer.parseInt(l_userInputArray[3]));
                }
                case "negotiate" -> {
                    Optional<Player> player = GameEngine.getD_playerList().stream().filter(player1 -> player1.getD_name().equals(l_userInputArray[1])).findFirst();
                    if (player.isEmpty()) {
                        System.out.println("Player not found");
                    } else {
                        GameEngine.getGamePhase().negotiate(this, player.get());
                    }
                }
                case "reinforce" -> {
                    GameEngine.getGamePhase().reinforce();
                }
                case "fortify" -> {
                    GameEngine.getGamePhase().fortify();
                }
                default -> {
                    System.out.println("\nUnrecognised command\n");
                }
            }
        }
    }

    /**
     * Checks if there are any orders in Players list of orders. If orders are
     * present, it will return first order in Players list of orders
     *
     * @return First order in Players list of orders
     */
    public Order next_order() {
        if (orderList != null && !orderList.isEmpty()) {
            Order nextOrder = orderList.peek();
            orderList.remove();
            return nextOrder;
        } else {
            return null;
        }
    }

    /**
     * Calculates the total number of armies owned by the player.
     *
     * @return the total number of armies owned by the player
     */
    public int getTotalArmies() {
        int l_totalArmies = 0;
        for (String countryName : this.getD_listCountries().keySet()) {
            l_totalArmies += this.getD_listCountries().get(countryName).getD_noOfArmies();
        }

        return l_totalArmies;
    }

    /**
     * Executes the provided setup commands based on the game phase.
     *
     * @param p_setUpCommands the array of setup commands to execute
     * @param p_PlayerList the list of players in the game
     * @param p_gamePhase the current game phase
     * @return true if the command is executed successfully, false otherwise
     * @throws IOException if an I/O error occurs during the process
     */
    public static boolean executeCommands(String[] p_setUpCommands, ArrayList<Player> p_PlayerList, Phase p_gamePhase) throws IOException {
        switch (p_setUpCommands[0]) {
            case "editmap" -> {
                p_gamePhase.editMap(p_setUpCommands[1]);
                return true;
            }
            case "editcountry" -> {
                p_gamePhase.editCountry(p_setUpCommands);
                return true;
            }
            case "editcontinent" -> {
                p_gamePhase.editContinent(p_setUpCommands);
                return true;
            }
            case "editneighbor" -> {
                p_gamePhase.editNeighbor(p_setUpCommands);
                return true;
            }
            case "savemap" -> {
                p_gamePhase.saveMap(p_setUpCommands);
                return true;
            }
            case "validatemap" -> {
                p_gamePhase.validateMap(p_setUpCommands[1]);
                return true;
            }
            case "loadmap" -> {
                if (p_setUpCommands.length == 2) {
                    p_gamePhase.loadMap(p_setUpCommands[1]);
                } else {
                    System.out.println("\nInvalid command!\n");
                }
                return true;
            }
            case "gamePlayer" -> {
                p_gamePhase.gamePlayer(p_setUpCommands, p_PlayerList);
                return true;
            }
            case "assigncountries" -> {
                p_gamePhase.assignCountries(p_PlayerList);
                return true;
            }

            default -> {
                return false;
            }
        }
    }

}
