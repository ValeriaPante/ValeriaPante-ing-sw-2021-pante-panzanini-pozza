package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.CLI.Color;

/**
 * This class is used to print on terminal the market (with the slide too) and the legend
 * useful to interpret what was printed with the business logic of the game
 */
public class MarketPrinter {
    /**
     * Prints the market, with the slide too
     * @param grill it is a 3x4 matrix representing the content of the market
     *              (actually represents the resources correspondent to the marbles that are in the grill of market)
     * @param slide it is the resource correspondent to the marble in the slide of the market
     */
    public void printMarket(Resource[][] grill, Resource slide){
        System.out.print("\n" +
                "|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n" +
                "|                     " + intoMarble(slide) + " |\n" +
                "|    ___________________|\n" +
                "|   ||¯¯¯||¯¯¯||¯¯¯||¯¯¯|\n" +
                "|   || " + intoMarble(grill[0][0]) + " || " + intoMarble(grill[0][1]) + " || " + intoMarble(grill[0][2]) + " || " + intoMarble(grill[0][3]) + " | 0\n" +
                "|   ||___||___||___||___|\n" +
                "|   ||¯¯¯||¯¯¯||¯¯¯||¯¯¯|\n" +
                "|   || " + intoMarble(grill[1][0]) + " || " + intoMarble(grill[1][1]) + " || " + intoMarble(grill[1][2]) + " || " + intoMarble(grill[1][3]) + " | 1\n" +
                "|   ||___||___||___||___|\n" +
                "|   ||¯¯¯||¯¯¯||¯¯¯||¯¯¯|\n" +
                "|   || " + intoMarble(grill[2][0]) + " || " + intoMarble(grill[2][1]) + " || " + intoMarble(grill[2][2]) + " || " + intoMarble(grill[2][3]) + " | 2\n" +
                "|___||___||___||___||___|\n" +
                "       0    1    2    3 \n" +
                "\n");

    }

    private String intoMarble(Resource resource){
        switch (resource){
            case FAITH:
                return Color.colourText("R", "RED");
            case SHIELD:
                return Color.colourText("B", "BLUE");
            case SERVANT:
                return Color.colourText("P", "PURPLE");
            case COIN:
                return Color.colourText("Y", "YELLOW");
            case STONE:
                return Color.colourText("G", "CYAN");
            case WHITE:
                return Color.colourText("W", "WHITE");
            default:
                return " ";
        }
    }

    /**
     * Prints the legend to interpret the representation of the market printed with the command "printMarket(...)"
     * with the business logic of the game
     */
    public void printLegend(){
        System.out.println("\n"+ "MARBLE LEGEND:\n" +
                "- " + Color.colourText("R", "RED") + " stands for " + Color.colourText("RED", "RED") + "\n" +
                "- " + Color.colourText("B", "BLUE") + " stands for " + Color.colourText("BLUE", "BLUE") + "\n" +
                "- " + Color.colourText("P", "PURPLE") + " stands for " + Color.colourText("PURPLE", "PURPLE") + "\n" +
                "- " + Color.colourText("Y", "YELLOW") + " stands for " + Color.colourText("YELLOW", "YELLOW") + "\n" +
                "- " + Color.colourText("G", "CYAN") + " stands for " + Color.colourText("GREY", "CYAN") + "\n" +
                "- " + Color.colourText("W", "WHITE") + " stands for " + Color.colourText("WHITE", "WHITE"));
    }
}
