package org.warzone.operations;

import org.warzone.entities.Country;
import org.warzone.entities.GameMap;
import org.warzone.strategy.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Show Map Class
 */
public class ShowMap {

    /**
     * The ShowMap class provides methods for displaying game maps and related information.
     *
     * @throws IOException if map is incorrectly loaded
     */

    public static void showGraphicMap() throws IOException {
        BufferedImage l_myPicture = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);

        GameMap l_gameMap = GameMap.getInstance();
        GameMapIO l_gameMapIO;
        if (GameMapIO.isDominationMap(l_gameMap.getFileName())) {
            l_gameMapIO = new DominationMapIO();
        } else {
            l_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
        }

        Map<String, Country> l_countryList = l_gameMapIO.loadMap(l_gameMap.getFileName());

        Graphics2D l_graphics = (Graphics2D) l_myPicture.getGraphics();
        l_graphics.setStroke(new BasicStroke(1));
        l_graphics.setColor(Color.WHITE);

        l_countryList.forEach((l_key, l_country) -> {
            l_graphics.setColor(Color.WHITE);
            l_country.getAdjacentCountries().forEach((l_adjacentKey, l_adjacentCountry) -> {
                l_graphics.drawLine(l_country.getX(), l_country.getY(), l_adjacentCountry.getX(), l_adjacentCountry.getY());
            });
        });

        l_graphics.setStroke(new BasicStroke(3));

        l_countryList.forEach((key, l_country) -> {
            l_graphics.setColor(getContinentColor(l_graphics, l_country));
            l_graphics.fillOval(l_country.getX() - 17, l_country.getY() - 15, 30, 30);
            l_graphics.setColor(Color.RED);
            l_graphics.drawString(String.valueOf(l_country.getIndex() + "(" + l_country.getD_noOfArmies() + ")"), l_country.getX() - 14, l_country.getY() + 5);
        });

        JLabel l_picLabel = new JLabel(new ImageIcon(l_myPicture));

        JPanel l_jPanel = new JPanel();
        l_jPanel.add(l_picLabel);

        JFrame l_jFrame = new JFrame();
        l_jFrame.setSize(new Dimension(l_myPicture.getWidth() + 50, l_myPicture.getHeight() + 50));
        l_jFrame.add(l_jPanel);
        l_jFrame.setVisible(true);
    }

    /**
     * Retrieves the initials of a country's name for labeling on the map.
     *
     * @param p_country The country for which initials are needed.
     * @return The initials of the country's name.
     */

    private static String getCountryInitials(Country p_country) {
        if (p_country.getName().contains(" ")) {
            return (p_country.getName().charAt(0) + "" + p_country.getName().split(" ")[1].charAt(0)).toUpperCase();
        } else {
            return (p_country.getName().charAt(0) + "" + p_country.getName().charAt(p_country.getName().length() - 1)).toUpperCase();
        }
    }

    /**
     * Retrieves the continent color for displaying countries on the map.
     *
     * @param p_graphics The Graphics2D object for drawing.
     * @param p_country  The country for which to get the continent color.
     * @return The color corresponding to the continent.
     */

    private static Color getContinentColor(Graphics2D p_graphics, Country p_country) {
        p_graphics.setColor(Color.getColor(p_country.getContinent().getColor().toUpperCase()));

        return switch (p_country.getContinent().getColor().toUpperCase()) {
            case "YELLOW" -> Color.YELLOW;
            case "WHITE" -> Color.WHITE;
            case "GREEN" -> Color.GREEN;
            case "BLUE" -> Color.BLUE;
            case "ORANGE" -> Color.ORANGE;
            case "CYAN" -> Color.CYAN;
            default -> Color.BLACK;
        };
    }

    /**
     * Displays the game map with player-specific information.
     *
     * @param p_players A list of player objects representing players in the game.
     * @throws IOException If an I/O error occurs while displaying the map.
     */

    public static void showMapWithPlayers(List<Player> p_players) throws IOException {
        GameMap l_gameMap = GameMap.getInstance();
        GameMapIO l_gameMapIO;
        if (GameMapIO.isDominationMap(l_gameMap.getFileName())) {
            l_gameMapIO = new DominationMapIO();
        } else {
            l_gameMapIO = new ConquestMapIOAdapter(new ConquestMapIO());
        }
        Map<String, Country> l_countryList = l_gameMapIO.loadMap(l_gameMap.getFileName());

        p_players.forEach(l_player -> {
            System.out.println("\n\nPlayer Name : " + l_player.getD_name() + "\n");
            if (l_player.getD_cardList().getD_deck().keySet().isEmpty()) {
                System.out.println("No Cards available!");
            } else {
                System.out.print("Cards available : ");
                l_player.getD_cardList().getD_deck().forEach((s, integer) -> System.out.print(s + " -> " + integer));
                System.out.println();
            }
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

        System.out.println("Neutral territories : ");
        System.out.format("%s%20s%32s%40s%100s", "Country No", "Continent", "Country", "No. of armies", "Neighboring Countries");
        System.out.println();
        l_gameMap.getD_NeutralTerritory().forEach(l_country -> {
            StringBuilder l_adjacentCountries = new StringBuilder();
            for (String countryIndex : l_country.getAdjacentCountries().keySet()) {
                l_adjacentCountries.append(l_country.getAdjacentCountries().get(countryIndex).getName())
                        .append("(")
                        .append(l_country.getAdjacentCountries().get(countryIndex).getIndex())
                        .append(")")
                        .append(", ");
            }
            System.out.format("%s%32s%32s%32s%100s", l_country.getIndex(), l_country.getContinent().getD_name(), l_country.getName(),
                    l_country.getD_noOfArmies(), l_adjacentCountries);
            System.out.println();
        });

        System.out.println("\n\n");
    }

    /**
     * Displays the game map's basic information, including continents, countries, and borders.
     */

    public static void showMap() {
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


}
