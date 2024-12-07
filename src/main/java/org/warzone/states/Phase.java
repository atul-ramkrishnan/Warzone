package org.warzone.states;

import org.warzone.GameEngine;
import org.warzone.strategy.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Phase} abstract class represents a phase in the game lifecycle.
 * It defines a set of abstract methods that are to be implemented by concrete phase classes
 * to perform various actions appropriate to the current phase of the game.
 */
public abstract class Phase {
    /**
     * Constructs a {@code Phase} and initializes the game engine.
     */
    GameEngine gameEngine = new GameEngine();

    /**
     * Displays the current state of the map.
     */
    abstract public void showMap();

    /**
     * Displays the map with the provided list of players.
     *
     * @param p_players the list of players to be displayed on the map
     * @throws IOException if an I/O error occurs
     */
    abstract public void showMapWithPlayers(List<Player> p_players) throws IOException;

    /**
     * Edits the map.
     * @param p_fileName The map file.
     */
    abstract public void editMap(String p_fileName);

    /**
     * Edits the continents.
     * @param p_mapEditCommands The edit commands.
     */
    abstract public void editContinent(String[] p_mapEditCommands);

    /**
     * Edits the countries.
     * @param p_mapEditCommands The edit commands.
     */
    abstract public void editCountry(String[] p_mapEditCommands);

    /**
     * Edits the neighbors.
     * @param p_mapEditCommands The edit commands.
     */
    abstract public void editNeighbor(String[] p_mapEditCommands);

    /**
     * Saves the map.
     * @param p_mapEditCommands The edit commands.
     * @throws IOException if commands are incorrect
     */
    abstract public void saveMap(String[] p_mapEditCommands) throws IOException;

    /**
     * Validates the map.
     * @param p_fileName The map file.
     * @throws IOException if filename is incorrect
     */
    abstract public void validateMap(String p_fileName) throws IOException;

    /**
     * Loads the map.
     * @param p_fileName The map file.
     */
    abstract public void loadMap(String p_fileName) throws IOException;

    /**
     * Saves the game at any point
     */
    abstract public void saveGame(String p_fileName) throws IOException;

    /**
     * Loads the game at any point
     */
    abstract public void loadGame(String p_fileName) throws IOException;

    /**
     * Assigns the countries to players.
     *
     * @param l_PlayerList The list of players.
     * @throws IOException if PlayerList is incorrect
     */
    abstract public void assignCountries(ArrayList<Player> l_PlayerList) throws IOException;
    /**
     * gamePlayer
     * @param setUpCommands The commands.
     * @param l_PlayerList The list of players.
     */
    abstract public void gamePlayer(String[] setUpCommands, ArrayList<Player> l_PlayerList);

    /**
     * Deploys armies
     * @param p_player The player object.
     * @param p_countryIndex country index.
     * @param p_armyAmount Number of armies.
     */
    abstract public void deploy(Player p_player, int p_countryIndex, int p_armyAmount);

    /**
     * Advances armies from source to destination
     * @param p_countryOrigin The source country.
     * @param p_countryDestination The destination country.
     * @param p_numArmiesToMove Number of armies.
     */
    abstract public void advance(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove);

    /**
     * Bombs a specified country
     * @param p_countryID The target country.
     */
    abstract public void bomb(int p_countryID);

    /**
     * Blockades a specified country
     * @param p_countryID The target country.
     */
    abstract public void blockade(int p_countryID);

    /**
     * Airlifts armies from source to destination
     * @param p_countryOrigin The source country.
     * @param p_countryDestination The destination country.
     * @param p_numArmiesToMove Number of armies.
     */
    abstract public void airlift(int p_countryOrigin, int p_countryDestination, int p_numArmiesToMove);

    /**
     * Negotiates between two players
     * @param p_initiator The initiator.
     * @param p_receiver The receiver.
     */
    abstract public void negotiate(Player p_initiator, Player p_receiver);

    /**
     * Reinforce
     */
    abstract public void reinforce();

    /**
     * Fortify
     */
    abstract public void fortify();

    /**
     * Executes the orders in a round robin fashion.
     * @param p_playerList The list of players
     */
    abstract public void executeOrdersRoundRobin(ArrayList<Player> p_playerList);

    /**
     * Signifies the end of the game
     */
    abstract public void endGame();

    /**
     * Prints the reason why a command is invalid in a phase.
     */
    public void printInvalidCommandMessage() {
        System.out.println("Invalid Command in State "
                + this.getClass().getSimpleName());
    }
}
