package org.warzone;

import org.warzone.entities.GameMap;
import org.warzone.strategy.*;
import org.warzone.logging.FileLogger;
import org.warzone.logging.LogEntry;
import org.warzone.logging.LogEntryBuffer;
import org.warzone.operations.ShowMap;
import org.warzone.states.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The Main class of the Warzone game consists of the main game loop. This class contains the game's user interaction code.
 */

public class GameEngine {

    private static Phase gamePhase;

    private static Player d_currentPlayer;
    private static LogEntryBuffer d_buffer;
    private static FileLogger d_logger;

    /**
     * Constructs a new instance of the GameEngine class.
     * Initializes the logger with a specified log file.
     */
    public GameEngine() {
        initializeLogger("gameLog.txt");
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getD_currentPlayer() {
        return d_currentPlayer;
    }

    /**
     * Sets the current player.
     *
     * @param d_currentPlayer the current player to set
     */
    public void setD_currentPlayer(Player d_currentPlayer) {
        this.d_currentPlayer = d_currentPlayer;
    }

    /**
     * Returns the current game phase.
     *
     * @return the current game phase
     */

    public static Phase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the current game phase.
     *
     * @param gamePhase the game phase to set
     */
    public static void setGamePhase(Phase gamePhase) {
        GameEngine.gamePhase = gamePhase;
    }

    private static int d_roundNumber = 1;

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public static ArrayList<Player> getD_playerList() {
        return d_playerList;
    }

    /**
     * Sets the list of players.
     *
     * @param d_playerList the list of players to set
     */
    public static void setD_playerList(ArrayList<Player> d_playerList) {
        GameEngine.d_playerList = d_playerList;
    }

    private static ArrayList<Player> d_playerList;


    /*
     * Creating constants for map editing commands for cleaner code
     */
    private static final String EDIT_MAP = "editmap";
    private static final String INVALID_COMMAND = "\nInvalid command!\n";

    /**
     * Removes Players with no armies left from the Player list that is the game.
     */

    public static void removeLostPlayers() {
        for (int i = 0; i < d_playerList.size(); i++) {
            int l_armies = d_playerList.get(i).getTotalArmies() + d_playerList.get(i).getD_unDeployedArmies();
            if (l_armies == 0) {
                System.out.println("\n\n=================================================================");
                System.out.println("                     " + d_playerList.get(i).getD_name() + " Lost!!");
                System.out.println("=================================================================\n");
                d_playerList.remove(i);
                --i;
            } else if(d_playerList.get(i).getD_listCountries().isEmpty()) {
                System.out.println("\n\n=================================================================");
                System.out.println("                     " + d_playerList.get(i).getD_name() + " Lost!!");
                System.out.println("=================================================================\n");
                d_playerList.remove(i);
                --i;
            }
        }
    }

    public static void setD_roundNumber(int d_roundNumber) {
        GameEngine.d_roundNumber = d_roundNumber;
    }

    /**
     * Returns the current round number.
     *
     * @return the current round number
     */
    public static int getRoundNumber() {
        return d_roundNumber;
    }

    /**
     * Increments the round number by 1.
     */
    public static void incrementRoundNumber() {
        GameEngine.d_roundNumber += 1;
    }

    public static int d_numLoggers = 0;

    /**
     * Initializes the logger with the specified file path.
     *
     * @param p_filePath the file path for the logger
     */
    public static void initializeLogger(String p_filePath) {
        if (d_numLoggers == 0) {
            d_buffer = new LogEntryBuffer();
            d_logger = new FileLogger(p_filePath);

            d_buffer.addObserver(d_logger);
            d_numLoggers++;
        }
    }

    /**
     * Adds a LogEntry to the log buffer.
     *
     * @param p_logEntry the LogEntry to be added to the log buffer
     */

    public static void addToLog(LogEntry p_logEntry) {
        d_buffer.addLogEntry(p_logEntry);
    }

    /**
     * Starts the game.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void start() throws IOException {
        /*
         * The game starts in the Main method.
         * @param args Command-line arguments (not used in this implementation).
         * @throws IOException If an I/O error.
         */
        addToLog(new LogEntry(LogEntry.GamePhase.START_OF_GAME, "Game started"));

        d_playerList = new ArrayList<Player>();
        Scanner sc = new Scanner(System.in);

//        System.out.println("\nGame phase changed to PreLoad\n");
        setGamePhase(new PreLoadPhase());

        boolean isSetupComplete = false;
        /*
         * Entry point for user commands
         * @param isSetupComplete stores condition for the while loop which keeps on repeating until user is
         * satisfied with the game set up
         */
        while (!(gamePhase instanceof IssueOrderPhase)) {
            System.out.println("Enter " + gamePhase.getClass().getSimpleName() + " commands here:");
            String[] setUpCommands;
            setUpCommands = sc.nextLine().split(" ");

            isSetupComplete = executeCommands(setUpCommands, isSetupComplete);
        }

        System.out.println("### Game Start ###");
        /**
         * l_PlayersWithArmies to store the condition for the while loop which determines the end of
         * the game. It stores how many Players still have armies from the size of the list storing the
         * active Players. If the list size is 1, it means all the other Players lost all their armies
         * and the game should end with this Player as the winner.
         */
        int l_PlayersWithArmies = d_playerList.size();

        ShowMap.showGraphicMap();

        /**
         * continue the loop till all Players except 1 are out of the game/don't have armies to continue
         */
        while (d_playerList.size() > 1) {
            incrementRoundNumber();

            for (Player player : d_playerList) {
                System.out.println("Player " + player.getD_name() + " entering commands");
                setD_currentPlayer(player);
                d_currentPlayer = player;
                d_currentPlayer.updateDiplomaciesForNewRound();

                ShowMap.showMapWithPlayers(d_playerList);

                /*
                 * Calculating the total number of armies the Player should have available before each turn
                 * according to the rules
                 */
                int l_newArmiesAssigned = Math.max((int) (Math.floor((player.getD_listCountries().size()) / 3)), 3);
                player.setArmies(l_newArmiesAssigned);
                player.setD_armiesInDeployQueue(0);

                if (player.getPlayerType().equals("human")) {
                    do {
                        if (player.getD_armiesInDeployQueue() == player.getD_unDeployedArmies()) {
                            System.out.println("All armies deployed");
                        } else {
                            System.out.println("Please deploy " + (player.getD_unDeployedArmies() - player.getD_armiesInDeployQueue()) + " armies");
                        }
                        player.issue_order();
                        System.out.println("Press Y to continue ordering");
                    } while (sc.next().equalsIgnoreCase("Y"));
                } else {
                    player.issue_order();
                }

//                removeLostPlayers();
//                checkForWinner();

            }
            /*
             * Once all the Players have given the orders, execute them in round robin way
             */
//            OrderExecutionPhase l_commit = new OrderExecutionPhase();
//            l_commit.executeOrdersRoundRobin(d_playerList);

            setGamePhase(new OrderExecutionPhase());
            gamePhase.executeOrdersRoundRobin(d_playerList);

            /*
             * check if any Players lost
             */
            removeLostPlayers();
            checkForWinner();

        }
    }

    private void checkForWinner() {
        if (d_playerList.size() == 1) {
            System.out.println("*****************************************************************");
            System.out.println("*****************************************************************");
            System.out.println("\n\n=================================================================");
            System.out.println("                     " + d_playerList.get(0).getD_name() + " won!!");
            System.out.println("=================================================================\n");
            System.out.println("*****************************************************************");
            System.out.println("*****************************************************************");
            addToLog(new LogEntry(LogEntry.GamePhase.END_OF_GAME, "Game ended. " + d_playerList.get(0).getD_name() + " won."));
            System.exit(0);
        }
    }

    /**
     * Executes the specified commands based on the provided input and game state.
     *
     * @param setUpCommands   an array of setup commands to be executed
     * @param isSetupComplete a boolean indicating if the setup is complete
     * @return true if the setup is complete, otherwise false
     * @throws IOException if an I/O error occurs during execution
     */
    public static boolean executeCommands(String[] setUpCommands, boolean isSetupComplete) throws IOException {
        switch (setUpCommands[0]) {
            case "savegame" -> {
                gamePhase.saveGame(setUpCommands[1]);
            }
            case "loadgame" -> {
                setGamePhase(new IssueOrderPhase());
                gamePhase.loadGame(setUpCommands[1]);
            }
            case "showmap" -> {
                gamePhase.showMap();
            }
            case EDIT_MAP -> {
                gamePhase.editMap(setUpCommands[1]);
            }
            case "editcountry" -> {
                gamePhase.editCountry(setUpCommands);
            }
            case "editcontinent" -> {
                gamePhase.editContinent(setUpCommands);
            }
            case "editneighbor" -> {
                gamePhase.editNeighbor(setUpCommands);
            }
            case "savemap" -> {
                gamePhase.saveMap(setUpCommands);
            }
            case "validatemap" -> {
                if (gamePhase instanceof EditMapPhase) {
                    gamePhase.validateMap("");
                } else {
                    gamePhase.validateMap(setUpCommands[1]);
                }
            }
            case "loadmap" -> {
                if (setUpCommands.length == 2) {
                    gamePhase.loadMap(setUpCommands[1]);
                } else {
                    System.out.println(INVALID_COMMAND);
                }
            }
            case "gameplayer" -> {
                gamePhase.gamePlayer(setUpCommands, d_playerList);
            }
            case "assigncountries" -> {
                gamePhase.assignCountries(d_playerList);
                isSetupComplete = true;
            }

            case "deploy" -> {
                gamePhase.deploy(new Player(""), 1, 1);
            }
            case "advance" -> {
                gamePhase.advance(1, 1, 1);
            }
            case "bomb" -> {
                gamePhase.bomb(1);
            }
            case "blockade" -> {
                gamePhase.blockade(1);
            }
            case "airlift" -> {
                gamePhase.airlift(1, 1, 1);
            }
            case "negotiate" -> {
//                    gamePhase.negotiate();
            }
            case "reinforce" -> {
                gamePhase.reinforce();
            }
            case "fortify" -> {
                gamePhase.fortify();
            }
            case "tournament" -> {
                List<String> mapFileList = null;
                List<String> playerStrategyList = null;
                int numberOfGames = 0;
                int maxNumberOfTurns = 0;

                GameMap l_gameMap = GameMap.getInstance();

                for (int i = 1; i < setUpCommands.length; i++) {
                    if (setUpCommands[i].equals("-M")) {
                        mapFileList = Arrays.stream(setUpCommands[i + 1].split(",")).toList();
                    } else if (setUpCommands[i].equals("-P")) {
                        playerStrategyList = Arrays.stream(setUpCommands[i + 1].split(",")).toList();
                    } else if (setUpCommands[i].equals("-G")) {
                        numberOfGames = Integer.parseInt(setUpCommands[i + 1]);
                    } else if (setUpCommands[i].equals("-D")) {
                        maxNumberOfTurns = Integer.parseInt(setUpCommands[i + 1]);
                    }
                }

                if (mapFileList == null || playerStrategyList == null || numberOfGames == 0 || maxNumberOfTurns == 0) {
                    System.out.println("Syntactical error in tournament command");
                } else {

                    String[][] resultmatrix = new String[mapFileList.size()][numberOfGames];

                    for (int i = 0; i < numberOfGames; i++) {
                        setGamePhase(new PreLoadPhase());
                        int mapIndex = 0;
                        for (String map : mapFileList) {
                            //loading map
                            setGamePhase(new PreLoadPhase());
                            gamePhase.loadMap(map);

                            //setting up all players
                            ArrayList<Player> playerList = new ArrayList<>();
                            int playerCount = 1;
                            for (String strategy : playerStrategyList) {
                                Player player = getPlayer(strategy, playerCount);
                                playerList.add(player);
                                playerCount++;
                            }

                            gamePhase.assignCountries(playerList);

                            int turnCount = 0;
                            d_playerList = new ArrayList<>(playerList);
                            while (d_playerList.size() > 1 && turnCount < maxNumberOfTurns) {
                                incrementRoundNumber();

                                for (Player player : playerList) {

                                    System.out.println("Player " + player.getD_name() + " entering commands");

                                    d_currentPlayer = player;
                                    d_currentPlayer.updateDiplomaciesForNewRound();

                                    ShowMap.showMapWithPlayers(d_playerList);

                                    int l_newArmiesAssigned = Math.max((int) (Math.floor((player.getD_listCountries().size()) / 3)), 3);
                                    player.setArmies(l_newArmiesAssigned);
                                    player.setD_armiesInDeployQueue(0);

                                    player.issue_order();

//                                    if (turnCount != 0) {
//                                        removeLostPlayers();
//                                    }

                                    if (player.getD_listCountries().size() == l_gameMap.getCountryMap().keySet().size()) {
                                        setD_playerList(new ArrayList<>(List.of(player)));
                                        break;
                                    }

                                }

                                setGamePhase(new OrderExecutionPhase());
                                gamePhase.executeOrdersRoundRobin(d_playerList);

                                /*
                                 * check if any Players lost
                                 */
                                removeLostPlayers();
                                playerList = new ArrayList<>(d_playerList);
                                turnCount++;
                            }

                            if (d_playerList.size() != 1) {
                                resultmatrix[mapIndex][i] = "draw";
                            } else {
                                resultmatrix[mapIndex][i] = d_playerList.get(0).getD_name();
                            }
                            mapIndex++;

                        }
                    }

                    System.out.println("\n\n\n\n\n\n\n\n");

                    for (int j = 0; j < numberOfGames; j++) {
                        System.out.print("\t\t\t\tGame" + (j + 1));
                    }

                    System.out.println();

                    for (int i = 0; i < mapFileList.size(); i++) {
                        System.out.print(mapFileList.get(i) + "\t\t\t\t");
                        for (int j = 0; j < numberOfGames; j++) {
                            System.out.print(resultmatrix[i][j] + "\t\t\t\t");
                        }
                        System.out.println();
                    }

                    System.exit(0);

                }


            }

            default -> {
                System.out.println(INVALID_COMMAND);
            }
        }
        return isSetupComplete;
    }

    private static Player getPlayer(String strategy, int playerCount) {
        Player player = new Player("Player" + playerCount);
        switch (strategy) {
            case "aggressive" -> {
                player.setPlayerStrategy(new AggressiveStrategy(new ArrayList<>(player.getD_listCountries().values()), player));
                player.setHuman(false);
            }
            case "benevolent" -> {
                player.setPlayerStrategy(new BenevolentStrategy(new ArrayList<>(player.getD_listCountries().values()), player));
                player.setHuman(false);
            }
            case "cheater" -> {
                player.setPlayerStrategy(new CheaterStrategy(new ArrayList<>(player.getD_listCountries().values()), player));
                player.setHuman(false);
            }
            case "random" -> {
                player.setPlayerStrategy(new RandomStrategy(new ArrayList<>(player.getD_listCountries().values()), player));
                player.setHuman(false);
            }
            default -> {
                player.setHuman(true);
            }
        }

        player.setPlayerType(strategy);
        return player;
    }


}
