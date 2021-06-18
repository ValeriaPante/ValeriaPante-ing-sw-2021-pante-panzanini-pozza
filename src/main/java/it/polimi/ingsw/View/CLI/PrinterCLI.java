package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.View.ClientModel.Game;

public class PrinterCLI {
    private final Game model;
    private final LeaderCardPrinter leaderCardPrinter;
    private final DevCardPrinter devCardPrinter;
    private final FaithTrackPrinter faithTrackPrinter;
    private final MarketPrinter marketPrinter;

    public PrinterCLI(Game model){
        this.model = model;
        leaderCardPrinter = new LeaderCardPrinter();
        devCardPrinter = new DevCardPrinter();
        faithTrackPrinter = new FaithTrackPrinter();
        marketPrinter = new MarketPrinter();
    }

    public synchronized void printError(String errorText){
        System.out.println(Color.colourText("ERROR! "+ errorText, "RED"));
    }

    public synchronized void printRequest(String request){
        switch(request){
            case "LEADER CARDS":
                for (int leaderCardID : model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards())
                    leaderCardPrinter.printFromID(leaderCardID, model.getPlayerFromId(model.getLocalPlayerId()).getLeaderStorage(leaderCardID));
                break;

            case "MARKET":
                marketPrinter.printMarket(model.getGrid(), model.getSlide());
                break;

            case "MARKET LEGEND":
                marketPrinter.printLegend();
                break;

            case "DEVELOPMENT SLOTS":
                printDevSlots();
                break;

            case "SHELVES":
                printShelves();
                break;

            case "STRONGBOX":
                printStrongBox();
                break;

            case "SUPPORT CONTAINER":
                printSupportContainer();
                break;

            case "FAITH TRACK ONLY":
                faithTrackPrinter.printTrack();
                break;

            case "FAITH TRACK":
                faithTrackPrinter.printTrack();
                faithTrackPrinter.printVaticanRelations();
                faithTrackPrinter.printVaticanRelations();
                break;

            case "VATICAN RELATIONS":
                faithTrackPrinter.printVaticanRelations();
                break;

            case "POSITIONS POINTS":
                faithTrackPrinter.printPointsForPosition();
                break;

            case "DEVELOPMENT CARD GRILL":
                break;

            case "PLAYING ORDER":
                break;

            case "NICKNAMES":
                System.out.print("\n");
                for (String nickname : model.getUsernames())
                    System.out.println(nickname);
                break;

            //print one player property
            default:
                break;
        }
    }

    private void printDevSlots(){

    }

    private void printShelves(){

    }

    private void printStrongBox(){

    }

    private void printSupportContainer(){

    }
}
