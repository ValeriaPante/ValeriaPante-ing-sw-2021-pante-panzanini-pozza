package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Deposit.Market;

public class SomeWritings {

    public void printTitle2(){

    }

    public void printMarket(){
        Market market = new Market();
        Resource[][] grill = market.getState();

        System.out.println("\n" +
                "|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n" +
                "|                     " + intoMarble(market.getSlide()) + " |\n" +
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

        System.out.println("\n" +
                "|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n" +
                "|                             " + intoMarble(market.getSlide()) + " |\n" +
                "|                             " + intoMarble(market.getSlide()) + " |\n" +
                "|      ___________________________|\n" +
                "|     ||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯|\n" +
                "|     || " + intoMarble(grill[0][0]) + " || " + intoMarble(grill[0][1]) + " || " + intoMarble(grill[0][2]) + " || " + intoMarble(grill[0][3]) + " |  0\n" +
                "|     || " + intoMarble(grill[0][0]) + " || " + intoMarble(grill[0][1]) + " || " + intoMarble(grill[0][2]) + " || " + intoMarble(grill[0][3]) + " |  \n" +
                "|     ||_____||_____||_____||_____|\n" +
                "|     ||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯|\n" +
                "|     || " + intoMarble(grill[1][0]) + " || " + intoMarble(grill[1][1]) + " || " + intoMarble(grill[1][2]) + " || " + intoMarble(grill[1][3]) + " |  1\n" +
                "|     || " + intoMarble(grill[1][0]) + " || " + intoMarble(grill[1][1]) + " || " + intoMarble(grill[1][2]) + " || " + intoMarble(grill[1][3]) + " |  \n" +
                "|     ||_____||_____||_____||_____|\n" +
                "|     ||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯||¯¯¯¯¯|\n" +
                "|     || " + intoMarble(grill[2][0]) + " || " + intoMarble(grill[2][1]) + " || " + intoMarble(grill[2][2]) + " || " + intoMarble(grill[2][3]) + " |  2\n" +
                "|     || " + intoMarble(grill[2][0]) + " || " + intoMarble(grill[2][1]) + " || " + intoMarble(grill[2][2]) + " || " + intoMarble(grill[2][3]) + " |  \n" +
                "| ____||_____||_____||_____||_____|\n" +
                "\n" +
                "          0      1      2      3");
    }
    private String intoMarble(Resource resource){
        switch (resource){
            case FAITH:
                return Color.colourText("█", "RED");
            case SHIELD:
                return Color.colourText("█", "BLUE");
            case SERVANT:
                return Color.colourText("█", "PURPLE");
            case COIN:
                return Color.colourText("█", "YELLOW");
            case STONE:
                return Color.colourText("█", "CYAN");
            case WHITE:
                return Color.colourText("█", "WHITE");
            default:
                return "██";
        }
    }

    public void printLeaderCard(int id){

    }
}
