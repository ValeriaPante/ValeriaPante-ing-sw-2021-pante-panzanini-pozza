package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.ActionTokenType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Cards.ActionToken;
import it.polimi.ingsw.View.CLI.Color;
import it.polimi.ingsw.View.CLI.SimplifiedFaithTrack;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;

import java.util.ArrayList;

/**
 * This class is replacing "System.out.print..." and contains the logic
 * to print all the elements of the game. Each public method is "synchronized"
 */
public class Printer {
    private final Game model;
    private final SimplifiedFaithTrack simplifiedFaithTrack;
    private final DevCardPrinter devCardPrinter;
    private final FaithTrackPrinter faithTrackPrinter;
    private final LeaderCardPrinter leaderCardPrinter;
    private final LobbyPrinter lobbyPrinter;
    private final MarketPrinter marketPrinter;
    private final ShelvesPrinter shelvesPrinter;
    private final StrongBoxPrinter strongBoxPrinter;

    /**
     * @param model the model used in the game
     * @param simplifiedFaithTrack the faith track used in the game
     */
    public Printer(Game model, SimplifiedFaithTrack simplifiedFaithTrack){
        this.model = model;
        this.simplifiedFaithTrack = simplifiedFaithTrack;
        this.devCardPrinter = new DevCardPrinter();
        this.faithTrackPrinter = new FaithTrackPrinter();
        this.leaderCardPrinter = new LeaderCardPrinter();
        this.lobbyPrinter = new LobbyPrinter();
        this.marketPrinter = new MarketPrinter();
        this.shelvesPrinter = new ShelvesPrinter();
        this.strongBoxPrinter = new StrongBoxPrinter();
    }

    /**
     * Interprets the requests coming from the player (console)
     * when the game is started. To each request correspond a printing
     * on the terminal: if the request is illegal it will be directly printed
     * a message error on the terminal, otherwise it will be printed what requested
     * @param request it is a string respecting a particular format (this
     *                property must be checked before calling this method,
     *                with an "InputManager" for example)
     */
    public synchronized void inGamePrintRequest(String request){
        if (request.equals("USERNAMES")){
            if(model.getPLayersID().size() == 1){
                printError("You are playing a single player game");
            } else {
                System.out.println("\nHere are all the players' usernames of the game:");
                for (int id : model.getPLayersID()){
                    System.out.println("\t"+
                            "USER NAME: " + Color.colourText(model.getPlayerFromId(id).getUsername(), "YELLOW") +
                            "\t\t" + "ID:" + id);
                }
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
                    faithTrackPrinter.printTrack(model.getPLayersID(), model.getUsernames(), simplifiedFaithTrack);
                    return;
                case "FAITH TRACK POINTS":
                    faithTrackPrinter.printPointsForPosition();
                    return;
                case "FAITH TRACK VATICAN RELATIONS":
                    faithTrackPrinter.printVaticanRelations(model.getPLayersID(), model.getUsernames(), simplifiedFaithTrack);
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
            devCardPrinter.printSingleDevSlot(player.getDevSlots()[Integer.parseInt(request)-1], topCardOnly);
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
                        System.out.println("\n" + Color.colourText(player.getUsername(),"YELLOW")+" has not played any leader card yet!");
                    return;
                case 1:
                    if (player.equals(model.getPlayerFromId(model.getLocalPlayerId())))
                        System.out.print("\n" + "Here is the only leader card you own:" + "\n");
                    else
                        System.out.print("\n" + Color.colourText(player.getUsername(),"YELLOW")+" has played only this card:");
                    break;
                case 2:
                    if (player.equals(model.getPlayerFromId(model.getLocalPlayerId())))
                        System.out.print("\n" + "Here are the leader cards you own:" + "\n");
                    else
                        System.out.print("\n" + Color.colourText(player.getUsername(),"YELLOW")+" has played this two cards:");
                    break;
                default:
                    return;
            }
            for (int id : leaderCardList)
                leaderCardPrinter.printFromID(id,player.getLeaderStorage(id));
            return;
        }
    }

    /**
     * Interprets the requests coming from the player (console)
     * when the game is not started yet (while still in lobbies).
     * To each request correspond a printing on the terminal:
     * if the request is illegal it will be directly printed a message error on the terminal,
     * otherwise it will be printed what requested
     * @param request it is a string respecting a particular format (this
     *                property must be checked before calling this method,
     *                with an "InputManager" for example)
     */
    public synchronized void preGamePrintRequest(String request){
        switch(request){
            case "MY LOBBY":
                if (model.getLocalPlayerLobbyId() == 0)
                    printError("You are not inside a lobby yet, please pick one");
                else {
                    System.out.println("Here is your lobby:");
                    lobbyPrinter.printLobby(model.getLocalPlayerLobbyId(), model.getLobbies().get(model.getLocalPlayerLobbyId()));
                }
                break;

            case "ALL LOBBIES":
                if (!model.getLobbies().isEmpty()){
                    System.out.println("Here are all the lobbies:");
                    lobbyPrinter.printAllLobbies(model.getLobbies());
                } else {
                    System.out.println("There are " + Color.colourText("NO LOBBIES", "RED") +
                            " available right now! If you want to play, " + Color.colourText("create one!", "GREEN"));
                }
                break;

            default:
                break;
        }
    }

    /**
     *Prints a message as an error on the terminal
     * @param errorText the text to be displayed on terminal as error
     */
    public synchronized void printError(String errorText){
        System.out.println(Color.colourText("ERROR! "+ errorText, "RED"));
    }

    /**
     *Prints, on the terminal, the state of the notification: if they will be shown or hidden, from now on
     * @param showNotifications if true the notification will be shown on terminal, else, if false,
     *                          they wil be hidden and not printed (this class is not implementing that logic, it only
     *                          prints on the terminal this information)
     */
    public synchronized void printNotificationState(boolean showNotifications){
        System.out.println("Notifications are now " +
                (showNotifications ? Color.colourText("ACTIVATED","GREEN") : Color.colourText("DEACTIVATED","RED")));
    }

    /**
     *Prints on the terminal that something changed in a lobby
     * @param lobbyId unique identifier of the lobby that has something new:
     *                the players inside are changed or it is a new lobby
     */
    public synchronized void notifyLobbyChange(int lobbyId){
        if (lobbyId == model.getLocalPlayerLobbyId()){
            System.out.println("\nYour lobby has "+ Color.colourText("changed", "YELLOW"));
        } else {
            System.out.println("\nThere is something new with lobby number "+ Color.colourInt(lobbyId, "YELLOW"));
        }
    }

    /**
     *Prints on the terminal that a lobby has been removed from the ones available on the server
     * @param lobbyID unique identifier of the lobby that was removed from server
     */
    public synchronized void notifyLobbyRemoval (int lobbyID){
        System.out.println("Lobby number " + Color.colourInt(lobbyID,"RED") + " has been "+ Color.colourText("removed","RED") +" so it is no longer available");
    }

    /**
     *Prints on the terminal that the player has received
     * the leader cards an has to pick and choose which to keep
     * (initialization of the game)
     */
    public synchronized void notifyChooseLeaderCards(){
        System.out.println("You have just received your four leader cards! Here they are:");
        for (Integer i : model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards())
            leaderCardPrinter.printFromID(i, null);
        System.out.println("Pick and discard two of them before starting. \n" +
                "Write down their ids, one by one:");
    }

    /**
     *Prints on terminal that the player
     * has to decide which resources he wants and in which shelf to put them
     * (initialization of the game)
     */
    public synchronized void notifyChooseInitialRes(){
        System.out.print("\n"+"Setting up is almost done, you only have to " + Color.colourText("choose ", "YELLOW"));
        int resRequired;
        if (model.getLocalPlayerIndex() == 3){
            resRequired = 2;
            System.out.print(Color.colourText(resRequired + " resources ", "YELLOW"));
        } else {
            resRequired = 1;
            System.out.print(Color.colourText(resRequired + " resource ","YELLOW"));
        }
        System.out.println("(between " + Color.colourText(stringLegalResources(), "YELLOW") + ") and decide in which shelf you want to put them. \n" +
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

    /**
     *Prints on the terminal that the player of turn has changed
     * @param playerID unique identifier of the player that is now in turn
     */
    public synchronized void notifyTurnChanged(int playerID){
        if (playerID == model.getLocalPlayerId()){
            System.out.println(Color.colourText("\n\n=========================== Now it's your turn! ===========================\n", "RED"));
        } else {
            System.out.println(Color.colourText("\n\n*************************** Now it's " +
                    model.getPlayerFromId(playerID).getUsername() +
                    " turn! ***************************\n", "RED"));
        }
    }

    /**
     *Prints on the terminal that a player moved on the faith track
     * @param playerID unique identifier of the player that moved on the track
     */
    public synchronized void notifyFaithTrackPositionChange(int playerID){
        System.out.println(Color.colourText(model.getPlayerFromId(playerID).getUsername(),"CYAN") +
                "'s position on the faith track is changed");
    }

    /**
     *Prints on the terminal that the state of some Pope favor card of a player is changed
     * @param playerID unique identifier of the player whose Pope favor cards
     *                 state is changed
     */
    public synchronized void notifyPopeFavorCardChange(int playerID){
        System.out.println("There is something new with " +
                Color.colourText(model.getPlayerFromId(playerID).getUsername(),"CYAN") +
                "'s pope favor cards");
    }

    /**
     *Prints on the terminal that the content of the strongbox of a player is changed
     * @param playerId unique identifier of the player whose strongbox content is changed
     */
    public synchronized void notifyStrongBoxChange(int playerId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the "+ Color.colourText("strongbox","YELLOW") +" has changed");
    }

    /**
     *Prints on the terminal that the content of a shelf of a player is changed
     * @param playerId unique identifier of the player whose shelf content is changed
     * @param numShelf capacity of the shelf whose content is changed
     */
    public synchronized void notifyShelvesChange(int playerId, int numShelf){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the "+ Color.colourText("shelf", "YELLOW") +
                " of capacity "+ Color.colourInt(numShelf, "YELLOW")+" has changed");
    }

    /**
     *Prints on the terminal that the content of the support container of a player is changed
     * @param playerId unique identifier of the player whose support container content is changed
     */
    public synchronized void notifySupportContainerChange(int playerId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the " + Color.colourText("support container","YELLOW") + " has changed");
    }

    /**
     *Prints on the terminal that the content of a leader card with storage ability of a player is changed
     * @param playerId unique identifier of the player whose leader card, with storage ability, content is changed
     * @param cardId unique identifier of the leader card, with storage ability, whose content is changed
     */
    public synchronized void notifyChangeInLCStorage(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources! The content of the leader card with id number " +
                Color.colourInt(cardId, "YELLOW") + " has changed");
    }

    /**
     *Prints on the terminal that a player played a leader card
     * @param playerId unique identifier of the player that played the leader card
     * @param cardId unique identifier of the leader card played
     */
    public synchronized void notifyLeaderCardActivation(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just played the leader card with id number " + Color.colourInt(cardId,"YELLOW"));
    }

    /**
     *Prints on the terminal that a player discarded a leader card
     * @param playerId unique identifier of the player that discarded the leader card
     * @param cardId unique identifier of the leader card discarded
     */
    public synchronized void notifyLeaderCardDiscard(int playerId, int cardId){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just discarded the leader card with id number " + Color.colourInt(cardId,"YELLOW"));
    }

    /**
     *Prints on the terminal that a player bought a development card from the table
     * @param playerId unique identifier of the player that bought the development card from the table
     * @param cardId unique identifier of the development card bought
     * @param slot number of slot identifying the one in which the player, with "id" equals to "playerId",
     *             put the development card, wit "id" number equals to "cardId", bought.
     *             The slots are considered having a positional number starting from zero and increasing
     *             from left to right on the player's dashboard (so "slot" must be ranging from 0 to 2)
     */
    public synchronized void notifyDevCardPurchase(int playerId, int cardId, int slot){
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                " has just bought the development card with id number " + Color.colourInt(cardId,"YELLOW") +
                " and put it in his development slot number " + Color.colourInt((slot+1),"YELLOW"));
    }

    /**
     *Prints on the terminal that the game is not started yet and a player is deciding on which leader card discard
     * or which resource pick and where to put it
     * @param playerId unique identifier id the player in turn (deciding)
     */
    public synchronized void waitingInitialisation(int playerId){
        System.out.println("Please wait, "+ Color.colourText(model.getPlayerFromId(playerId).getUsername(), "CYAN") + " is deciding!");
    }

    /**
     *Prints on the terminal that the match is started but the game is not started yet
     * since the initialization should be completed first
     */
    public synchronized void notifyInitializationStarted(){
        System.out.println(Color.colourText("Before starting the game let's set everything up! Please wait until it's your turn, now it is "
                + model.getUsernames().get(0)+"'s...", "CYAN"));
    }

    /**
     *Prints on the terminal that a player commit an error while selecting a card (leader or development card)
     * @param error text associated with the error occurred while selecting
     * @param cardId the unique identifier of the card that was not possible to select since an error occurred
     */
    public synchronized void printSelectionError(String error, int cardId){
        printError(error);
        System.out.println(Color.colourText("An error occurred while trying to select the card with id number" + cardId, "RED"));
    }

    public synchronized void notifyActionTokenDraw(ActionTokenType actionTokenType){

    }

    /**
     *Prints on the terminal that the game is over and someone won (and, of course, someone lost)
     * @param winnerId unique identifier of the player that won this match
     */
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
            System.out.println("The winner is: " +
                    Color.colourText(model.getPLayersID().size() == 1 ? "LORENZO IL MAGNIFICO" : model.getPlayerFromId(winnerId).getUsername() ,"GREEN") +
                    (model.getUsernames().size() == 1 ? "" : " (with id number: "+ winnerId +")"));
        }
    }

    /**
     *Prints on the terminal that the game is started so the initialization part is over
     */
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
