package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.CLI.Color;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;

import java.util.ArrayList;

public class Printer {
    private final Game model;
    private final DevCardPrinter devCardPrinter;
    private final FaithTrackPrinter faithTrackPrinter;
    private final LeaderCardPrinter leaderCardPrinter;
    private final LobbyPrinter lobbyPrinter;
    private final MarketPrinter marketPrinter;
    private final ShelvesPrinter shelvesPrinter;
    private final StrongBoxPrinter strongBoxPrinter;

    public Printer(Game model){
        this.model = model;
        this.devCardPrinter = new DevCardPrinter();
        this.faithTrackPrinter = new FaithTrackPrinter();
        this.leaderCardPrinter = new LeaderCardPrinter();
        this.lobbyPrinter = new LobbyPrinter();
        this.marketPrinter = new MarketPrinter();
        this.shelvesPrinter = new ShelvesPrinter();
        this.strongBoxPrinter = new StrongBoxPrinter();
    }

    public synchronized void inGamePrintRequest(String request){
        if (request.equals("USERNAMES")){
            System.out.println("\nHere are all the players' usernames of the game:");
            for (int id : model.getPLayersID()){
                System.out.println("\t"+
                        "USER NAME: " + Color.colourText(model.getPlayerFromId(id).getUsername(), "YELLOW") +
                        "\t\t" + "ID:" + id);
            }
            return;
        }

        if (request.startsWith("MARKET")){
            if (request.equals("MARKET LEGEND")){
                marketPrinter.printLegend();
                return;
            }

            marketPrinter.printMarket(model.getGrid(), model.getSlide());
            return;
        }

        if (request.equals("DEV CARDS ON TABLE")){
            devCardPrinter.printDevDecks(model.getDevDecks());
            return;
        }

        if (request.startsWith("FAITH TRACK")){
            switch(request){
                case "FAITH TRACK":
                    faithTrackPrinter.printTrack();
                    return;
                case "FAITH TRACK POINTS":
                    faithTrackPrinter.printPointsForPosition();
                    return;
                case "FAITH TRACK VATICAN RELATIONS":
                    faithTrackPrinter.printVaticanRelations();
                    return;
                default:
                    return;
            }
        }

        SimplifiedPlayer player = model.getPlayerFromId(model.getLocalPlayerId());

        if (request.contains("@")){
            if (model.getNumberOfPlayers() == 1){
                printError("You cannot use this function in single player, please use the correct command");
                return;
            }

            String[] requestParts = request.split("@");
            int playerID = Integer.parseInt(requestParts[1]);

            if (!model.getPLayersID().contains(playerID)){
                printError("There is no player with such an id ("+ playerID +"), please, retry...");
                return;
            }

            if(model.getLocalPlayerId() == playerID){
                printError("You cannot use this function on yourself, please enter the correct command!");
                return;
            }

            player = model.getPlayerFromId(playerID);
            request = requestParts[0].trim();
        }

        if (request.equals("SHELVES")){
            shelvesPrinter.printShelves(player.getShelf(0), player.getShelf(1), player.getShelf(2));
            return;
        }

        if (request.equals("STRONGBOX")){
            strongBoxPrinter.printStrongBox(player.getStrongbox());
            return;
        }

        if (request.equals("SUPPORT CONTAINER")){
            strongBoxPrinter.printSupportContainer(player.getSupportContainer());
            return;
        }

        if (request.startsWith("DEV SLOT ")){
            request = request.replace("DEV SLOT ","");
            boolean topCardOnly;
            if (request.contains(" CONTENT")){
                request = request.replace(" CONTENT","");
                topCardOnly = false;
            } else {
                request = request.replace(" TOP","");
                topCardOnly = true;
            }
            devCardPrinter.printSingleDevSlot(player.getDevSlots()[Integer.parseInt(request)+1], topCardOnly);
            return;
        }

        if (request.startsWith("DEV SLOTS")){
            boolean topCardOnly = request.contains("TOP");
            devCardPrinter.printDevSlots(player.getDevSlots(), topCardOnly);
            return;
        }

        if (request.equals("LEADER CARDS")){
            ArrayList<Integer> leaderCardList = player.getLeaderCards();
            switch(leaderCardList.size()){
                case 0:
                    if (player.equals(model.getPlayerFromId(model.getLocalPlayerId())))
                        System.out.println("\n" + Color.colourText("You do not own any leader card anymore!","YELLOW"));
                    else
                        System.out.println("\n" + Color.colourText(player.getUsername()+" has not played any leader card yet!","YELLOW"));
                    return;
                case 1:
                    if (player.equals(model.getPlayerFromId(model.getLocalPlayerId())))
                        System.out.println("\n" + "Here is the only leader card you own:" + "\n");
                    else
                        System.out.println("\n" + Color.colourText(player.getUsername()+" has played only this card:","YELLOW"));
                    break;
                case 2:
                    if (player.equals(model.getPlayerFromId(model.getLocalPlayerId())))
                        System.out.println("\n" + "Here are the leader cards you own:" + "\n");
                    else
                        System.out.println("\n" + Color.colourText(player.getUsername()+" has played this two cards:","YELLOW"));
                    break;
                default:
                    return;
            }
            for (int id : leaderCardList)
                leaderCardPrinter.printFromID(id,player.getLeaderStorage(id));
            return;
        }
    }

    public synchronized void preGamePrintRequest(String request){
        switch(request){
            case "MY LOBBY":
                if (model.getLocalPlayerLobbyId() == 0)
                    printError("You are not inside a lobby yet, please pick one");
                else
                    lobbyPrinter.printLobby(model.getLocalPlayerLobbyId(), model.getLobbies().get(model.getLocalPlayerLobbyId()));
                break;

            case "ALL LOBBIES":
                lobbyPrinter.printAllLobbies(model.getLobbies());
                break;

            default:
                break;
        }
    }

    public synchronized void printError(String errorText){
        System.out.println(Color.colourText("ERROR! "+ errorText, "RED"));
    }

    public synchronized void printNotificationState(boolean showNotifications){
        System.out.println("Notifications are now " +
                (showNotifications ? Color.colourText("ACTIVATED","GREEN") : Color.colourText("DEACTIVATED","RED")));
    }

    public synchronized void notifyLobbyChange(int lobbyId){
        if (lobbyId == model.getLocalPlayerLobbyId()){
            System.out.println("\nYour lobby has "+ Color.colourText("changed", "YELLOW"));
        } else {
            System.out.println("\nThere is something new with lobby number "+ Color.colourInt(lobbyId, "YELLOW"));
        }
    }

    public synchronized void notifyLobbyRemoval (int lobbyID){
        System.out.println("Lobby number " + lobbyID + " has been removed so it is no longer available");
    }

    public synchronized void notifyChooseLeaderCards(){
        System.out.println("You have just received your four leader cards! Here they are:");
        for (Integer i : model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards())
            leaderCardPrinter.printFromID(i, null);
        System.out.println("Pick and discard two of them before starting. \n " +
                "Write down their ids, one by one:");
    }

    public synchronized void notifyChooseInitialRes(){
        System.out.print("\n"+"Setting up is almost done, you only have to choose ");
        int resRequired;
        if (model.getLocalPlayerIndex() == 3){
            resRequired = 2;
            System.out.print(resRequired + " resources ");
        } else {
            resRequired = 1;
            System.out.print(resRequired + " resource ");
        }
        System.out.print("(between " + stringLegalResources() + ") and decide in which shelf you want to put them. \n" +
                "Please list them one by one with their full name:");
    }

    private String stringLegalResources(){
        boolean printedOne = false;
        StringBuilder listResources = new StringBuilder();
        for (Resource r : Resource.values()) {
            if (r != Resource.ANY && r != Resource.FAITH && r != Resource.WHITE) {
                if (printedOne)
                    listResources.append(", ");
                else
                    printedOne = true;
                listResources.append(r.toString());
            }
        }
        return listResources.toString();
    }

    public synchronized void notifyTurnChanged(int playerID){
        if (playerID == model.getLocalPlayerId()){
            System.out.println("========= " + Color.colourText("Now it's your turn!", "RED")+" =========");
        } else {
            System.out.println("========= " + Color.colourText("Now it's " + model.getPlayerFromId(playerID).getUsername() + " turn!", "RED")+ " =========");
        }
    }

    public synchronized void notifyStrongBoxChange(int playerId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the strongbox has changed");
    }

    public synchronized void notifyShelvesChange(int playerId, int numShelf){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the shelf of capacity "+ numShelf+" has changed");
    }

    public synchronized void notifySupportContainerChange(int playerId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the support container has changed");
    }

    public synchronized void notifyChangeInLCStorage(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the leader card with id number " +
                Color.colourInt(cardId, "YELLOW") + " has changed");
    }

    public synchronized void notifyLeaderCardActivation(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just played the leader card with id:" + cardId);
    }

    public synchronized void notifyLeaderCardDiscard(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just discarded the leader card with id:" + cardId);
    }

    public synchronized void notifyDevCardPurchase(int playerId, int cardId, int slot){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just bought the development card with id:" + cardId +
                " and put it in his development slot number " + (slot+1));
    }

    public synchronized void waitingInitialisation(int playerId){
        System.out.println("Please wait, "+ Color.colourText(model.getPlayerFromId(playerId).getUsername(), "CYAN") + "is deciding!");
    }

    public synchronized void printWinner(int winnerId){
        if (model.getLocalPlayerId() == winnerId) {
            System.out.println(Color.colourText("\n" +
                    "▄██   ▄    ▄██████▄  ███    █▄        ▄█     █▄   ▄█  ███▄▄▄▄   \n" +
                    "███   ██▄ ███    ███ ███    ███      ███     ███ ███  ███▀▀▀██▄ \n" +
                    "███▄▄▄███ ███    ███ ███    ███      ███     ███ ███▌ ███   ███ \n" +
                    "▀▀▀▀▀▀███ ███    ███ ███    ███      ███     ███ ███▌ ███   ███ \n" +
                    "▄██   ███ ███    ███ ███    ███      ███     ███ ███▌ ███   ███ \n" +
                    "███   ███ ███    ███ ███    ███      ███     ███ ███  ███   ███ \n" +
                    "███   ███ ███    ███ ███    ███      ███ ▄█▄ ███ ███  ███   ███ \n" +
                    " ▀█████▀   ▀██████▀  ████████▀        ▀███▀███▀  █▀    ▀█   █▀  \n" +
                    "                                                                \n", "GREEN"));
        } else {
            System.out.println(Color.colourText("\n" +
                    "▄██   ▄    ▄██████▄  ███    █▄        ▄█        ▄██████▄     ▄████████     ███     \n" +
                    "███   ██▄ ███    ███ ███    ███      ███       ███    ███   ███    ███ ▀█████████▄ \n" +
                    "███▄▄▄███ ███    ███ ███    ███      ███       ███    ███   ███    █▀     ▀███▀▀██ \n" +
                    "▀▀▀▀▀▀███ ███    ███ ███    ███      ███       ███    ███   ███            ███   ▀ \n" +
                    "▄██   ███ ███    ███ ███    ███      ███       ███    ███ ▀███████████     ███     \n" +
                    "███   ███ ███    ███ ███    ███      ███       ███    ███          ███     ███     \n" +
                    "███   ███ ███    ███ ███    ███      ███▌    ▄ ███    ███    ▄█    ███     ███     \n" +
                    " ▀█████▀   ▀██████▀  ████████▀       █████▄▄██  ▀██████▀   ▄████████▀     ▄████▀   \n" +
                    "                                     ▀                                             \n\n","RED"));
            System.out.println("The winner is: " + Color.colourText("model.getPlayerFromId(winnerId)","GREEN") +" (with id number: "+ winnerId +")");
        }
    }

    public synchronized void gameStarted(){
        System.out.println(Color.colourText("\n" +
                "   ▄██████▄     ▄████████   ▄▄▄▄███▄▄▄▄      ▄████████         ▄████████     ███        ▄████████    ▄████████     ███        ▄████████ ████████▄  \n" +
                "  ███    ███   ███    ███ ▄██▀▀▀███▀▀▀██▄   ███    ███        ███    ███ ▀█████████▄   ███    ███   ███    ███ ▀█████████▄   ███    ███ ███   ▀███ \n" +
                "  ███    █▀    ███    ███ ███   ███   ███   ███    █▀         ███    █▀     ▀███▀▀██   ███    ███   ███    ███    ▀███▀▀██   ███    █▀  ███    ███ \n" +
                " ▄███          ███    ███ ███   ███   ███  ▄███▄▄▄            ███            ███   ▀   ███    ███  ▄███▄▄▄▄██▀     ███   ▀  ▄███▄▄▄     ███    ███ \n" +
                "▀▀███ ████▄  ▀███████████ ███   ███   ███ ▀▀███▀▀▀          ▀███████████     ███     ▀███████████ ▀▀███▀▀▀▀▀       ███     ▀▀███▀▀▀     ███    ███ \n" +
                "  ███    ███   ███    ███ ███   ███   ███   ███    █▄                ███     ███       ███    ███ ▀███████████     ███       ███    █▄  ███    ███ \n" +
                "  ███    ███   ███    ███ ███   ███   ███   ███    ███         ▄█    ███     ███       ███    ███   ███    ███     ███       ███    ███ ███   ▄███ \n" +
                "  ████████▀    ███    █▀   ▀█   ███   █▀    ██████████       ▄████████▀     ▄████▀     ███    █▀    ███    ███    ▄████▀     ██████████ ████████▀  \n" +
                "                                                                                                    ███    ███                                     \n\n\n","CYAN"));
    }
}
