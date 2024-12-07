package org.warzone.states;

import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.operations.ConquestMapIO;
import org.warzone.operations.GameMapIO;
import org.warzone.savegame.GameStateManager;
import org.warzone.strategy.Player;
import org.warzone.operations.DominationMapIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The {@code Play} abstract class represents a phase in the game lifecycle.
 * It defines a set of abstract methods that are to be implemented by concrete play classes
 * to perform various actions appropriate to the current phase of the game.
 */
public abstract class Play extends Phase {
    /**
     * {@inheritDoc}
     */
    @Override
    public void showMap() {
        GameMap l_gameMap = GameMap.getInstance();
        System.out.println("### Loading Map ###");

        System.out.println("\n[continents]");
        l_gameMap.getContinentMap().forEach((l_key, l_continent) -> {
            System.out.println(l_continent.getD_name() + " " + l_continent.getValue() + " " + l_continent.getColor());
        });

        System.out.println("\n[countries]");
        l_gameMap.getCountryMap().forEach((l_key, l_country) -> {
            System.out.println(l_key + " " + l_country.getName() + " " + l_country.getContinent().getIndex());
        });

        System.out.println("[borders]");
        l_gameMap.getBorderMap().forEach((l_key, l_borderList) -> {
            System.out.print("\n" + l_key);
            for (String border : l_borderList) {
                System.out.print(" " + border);
            }
        });
        System.out.println();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMapWithPlayers(List<Player> p_players) throws IOException {
        GameMap l_gameMap = GameMap.getInstance();
        GameMapIO l_gameMapIO;
        if (GameMapIO.isDominationMap(l_gameMap.getFileName())) {
            l_gameMapIO = new DominationMapIO();
        } else {
            l_gameMapIO = new ConquestMapIO();
        }
        Map<String, Country> l_countryList = l_gameMapIO.loadMap(l_gameMap.getFileName());

        p_players.forEach(l_player -> {
            System.out.println("\n\nPlayer Name : " + l_player.getD_name() + "\n");
            System.out.format("%s%20s%32s%40s%100s", "Country No", "Continent", "Country", "No. of armies", "Neighboring Countries");
            System.out.println();
            l_player.getD_listCountries().forEach((l_key, l_country) -> {
                StringBuilder l_adjacentCountries = new StringBuilder();
                for (String countryIndex : l_country.getAdjacentCountries().keySet()) {
                    l_adjacentCountries.append(l_country.getAdjacentCountries().get(countryIndex).getName())
                            .append("(")
                            .append(l_country.getAdjacentCountries().get(countryIndex).getIndex())
                            .append(")")
                            .append(", ");
                }
                System.out.format("%s%32s%32s%32s%100s", l_key, l_country.getContinent().getD_name(), l_country.getName(),
                        l_country.getD_noOfArmies(), l_adjacentCountries);
                System.out.println();
            });
        });
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
    @Override
    public void editCountry(String[] p_mapEditCommands) {
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
    public void editMap(String p_fileName) {
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
    public void endGame() {
        System.out.println("in end game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeOrdersRoundRobin(ArrayList<Player> p_playerList) {
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
}
