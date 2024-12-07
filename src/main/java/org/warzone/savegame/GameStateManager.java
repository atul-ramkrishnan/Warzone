package org.warzone.savegame;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.warzone.GameEngine;
import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.operations.DominationMapIO;
import org.warzone.operations.GameMapIO;
import org.warzone.strategy.*;
import org.warzone.states.commands.*;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameStateManager {
    private ObjectMapper objectMapper;
    private String saveFilePath;

    public GameStateManager(String saveFilePath) {
        this.objectMapper = new ObjectMapper();
        this.saveFilePath = saveFilePath;
    }

    /**
     * Saves game
     * @param p_gameEngine takes current gameplay
     * @throws IOException
     */
    public void saveGameState(GameEngine p_gameEngine) throws IOException {
        GameMap l_gameMap = GameMap.getInstance();
        Map<String, Object> l_gameState = new HashMap<>();
        l_gameState.put("d_roundNumber", p_gameEngine.getRoundNumber());
        l_gameState.put("d_map", l_gameMap.getFileName());

        //Adding Player Variables
        List<Map<String, Object>> l_playerStates = new ArrayList<>();
        for (Player l_player : p_gameEngine.getD_playerList()) {
            Map<String, Object> playerData = new HashMap<>();
            playerData.put("d_name", l_player.getD_name());
            playerData.put("d_unDeployedArmies", l_player.getD_unDeployedArmies());
            playerData.put("d_playerType", l_player.getPlayerType());
//            playerData.put("d_playerStrategy", l_player.getPlayerStrategy());
            //Cards and number
            List<Map<String, Object>> l_playerCardList = new ArrayList<>();
            for (String key : l_player.getD_cardList().getD_deck().keySet()){
                Map<String, Object> l_card = new HashMap<>();
                l_card.put("Type", key);
                l_card.put("Number", l_player.getD_cardList().getD_deck().get(key));
                l_playerCardList.add(l_card);
            }
            playerData.put("d_cards", l_playerCardList);
            //OrderList
            List<String> l_playerOrderList = new ArrayList<>();
            for (Order order : l_player.getOrderList()){
                l_playerOrderList.add(order.getD_orderParameters());
            }
            playerData.put("d_orderList", l_playerOrderList);
            //Number of armies per territory
            List<Map<String, Object>> l_playerCountryList = new ArrayList<>();
            for (String key : l_player.getD_listCountries().keySet()){
                Map<String, Object> playerCountry = new HashMap<>();
                playerCountry.put("Country", key);
//                int numArmies;
//                if (Objects.equals(l_player.getD_listCountries().get(key).getD_noOfArmies(), null)){
//                    numArmies = 0;
//                }
//                else{
//                    numArmies = l_player.getD_listCountries().get(key).getD_noOfArmies();
//                }
                playerCountry.put("numArmies", l_player.getD_listCountries().get(key).getD_noOfArmies());
                l_playerCountryList.add(playerCountry);
            }
            playerData.put("d_listCountries", l_playerCountryList);

            l_playerStates.add(playerData);
        }
        l_gameState.put("players", l_playerStates);

        //Adding neutral territories
        List<Map<String, Object>> l_neutralCountryList = new ArrayList<>();
        for (Country key : l_gameMap.getD_NeutralTerritory()) {
            Map<String, Object> neutralCountry = new HashMap<>();
            neutralCountry.put("Country", key.getIndex());
            neutralCountry.put("numArmies", key.getD_noOfArmies());
            l_neutralCountryList.add(neutralCountry);
        }
        l_gameState.put("d_neutralCountries", l_neutralCountryList);

        // Write to file
        objectMapper.writeValue(new File(saveFilePath), l_gameState);
    }

    /**
     * Loads gameplay
     * @param p_gameEngine uses current game environment to load game
     * @throws IOException
     */
    public void loadGameState(GameEngine p_gameEngine) throws IOException {
        JsonNode root = objectMapper.readTree(new File(saveFilePath));
        int roundNumber = root.at("/d_roundNumber").asInt();
        p_gameEngine.setD_roundNumber(roundNumber);
        int size = root.at("/players").size();
        ArrayList<Player> d_playerList = new ArrayList<>();
        String l_fileName = root.at("/d_map").asText();
        //loading map
        GameMapIO d_dominationMapIO = new DominationMapIO();
        StringBuilder l_fileContents = d_dominationMapIO.loadFile(l_fileName);
        GameMap l_gameMap = d_dominationMapIO.loadGameMap(l_fileContents);
        //unpack using root
        for (int i = 0; i < size; i++){
            String d_name = root.at("/players").get(i).at("/d_name").asText();
            Player player = new Player(d_name);
            int d_undeployedArmies = root.at("/players").get(i).at("/d_unDeployedArmies").asInt();
            player.setD_unDeployedArmies(d_undeployedArmies);
            String l_playerType = root.at("/players").get(i).at("/d_playerType").asText();
            player.setPlayerType(l_playerType);
//            String l_playerStrategy = root.at("/players").get(i).at("/d_playerStrategy").asText();


            //cards
            int size1 = root.at("/players").get(i).at("/d_cards").size();
            Map<String,Integer> l_listCards = new HashMap<>();
            for (int j = 0; j < size1; j++){
                String l_type = root.at("/players").get(i).at("/d_cards").get(j).at("/Type").asText();
                int l_amount = root.at("/players").get(i).at("/d_cards").get(j).at("/Number").asInt();
                l_listCards.put(l_type, l_amount);
            }
            player.getD_cardList().setD_deck(l_listCards);

            //d_list countries
            int size2 = root.at("/players").get(i).at("/d_listCountries").size();
            Map<String,Country> l_listCountries = new HashMap<>();
            for (int j = 0; j < size2; j++){
                String index = root.at("/players").get(i).at("/d_listCountries").get(j).at("/Country").asText();
                int armies = root.at("/players").get(i).at("/d_listCountries").get(j).at("/numArmies").asInt();
                l_gameMap.getCountryMap().get(index).setD_noOfArmies(armies);
                l_gameMap.getCountryMap().get(index).setCountryOwner(player);
                l_listCountries.put(index, l_gameMap.getCountryMap().get(index));
            }
            player.setD_listCountries(l_listCountries);
            d_playerList.add(player);
        }

        for (int i = 0; i < size; i++) {
            String d_name = root.at("/players").get(i).at("/d_name").asText();
            //Will need to add orders since game can be interrupted midway - suggest using setD_orderParameters
            int size12 = root.at("/players").get(i).at("/d_orderList").size();
            Queue<Order> l_orders = new LinkedList<>();
            for (int j = 0; j < size12; j++) {
                String order = root.at("/players").get(i).at("/d_orderList").get(j).asText();
                String[] orderSplit = order.split(" ");
                switch (orderSplit[0]) {
                    case "deploy":
                        Deploy l_deploy = new Deploy(returnPlayer(d_playerList, orderSplit[1]), Integer.valueOf(orderSplit[2]), Integer.valueOf(orderSplit[3]));
                        l_orders.add(l_deploy);
                        break;
                    case "advance":
                        Advance l_advance = new Advance(Integer.valueOf(orderSplit[1]), Integer.valueOf(orderSplit[2]), Integer.valueOf(orderSplit[3]));
                        l_orders.add(l_advance);
                        break;
                    case "airlift":
                        Airlift l_airlift = new Airlift(Integer.valueOf(orderSplit[1]), Integer.valueOf(orderSplit[2]), Integer.valueOf(orderSplit[3]));
                        l_orders.add(l_airlift);
                        break;
                    case "negotiate":
                        Diplomacy l_negotiate = new Diplomacy(returnPlayer(d_playerList, orderSplit[1]), returnPlayer(d_playerList, orderSplit[2]));
                        l_orders.add(l_negotiate);
                        break;
                    case "bomb":
                        Bomb l_bomb = new Bomb(Integer.valueOf(orderSplit[1]));
                        l_orders.add(l_bomb);
                        break;
                    case "blockade":
                        Blockade l_blockade = new Blockade(Integer.valueOf(orderSplit[1]));
                        l_orders.add(l_blockade);
                        break;
                }
            }
            d_playerList.get(i).setOrderList(l_orders);
        }
        p_gameEngine.setD_playerList(d_playerList);

        //setting player strategy
        d_playerList.forEach(player -> {
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

        //neutral countries
        int size3 = root.at("/d_neutralCountries").size();
        List<Country> l_listNeutral = new ArrayList<>();
        for (int j = 0; j < size3; j++){
            String index = root.at("/d_neutralCountries").at("/d_listCountries").get(j).at("/Country").asText();
            int armies = root.at("/d_neutralCountries").at("/d_listCountries").get(j).at("/numArmies").asInt();
            l_gameMap.getCountryMap().get(index).setD_noOfArmies(armies);
            l_listNeutral.add(l_gameMap.getCountryMap().get(index));
        }
        l_gameMap.setD_NeutralTerritory(l_listNeutral);
    }

    /**
     * Helper Method to return player
     * @param p_players list of players
     * @param p_name player to be returned
     * @return
     */
    private Player returnPlayer(ArrayList<Player> p_players, String p_name){
        for (Player l_player : p_players){
            if (l_player.getD_name().compareTo(p_name) == 0){
                return l_player;
            }
        }
        return null;
    }
}