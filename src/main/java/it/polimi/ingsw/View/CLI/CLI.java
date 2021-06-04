package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.Observable;
import it.polimi.ingsw.View.View;

import java.util.Scanner;

public class CLI extends Observable implements View, Runnable{
    private final Game model;
    private final Client client;

    private final Scanner input;

    private boolean someoneIsWaitingForInput;
    private boolean newInputStringReady;
    private String inputString;

    private final LeaderCardPrinter leaderCardPrinter;
    private final DevCardPrinter devCardPrinter;
    private final FaithTrackPrinter faithTrackPrinter;
    private final InputManager inputManager;

    public CLI(){
        this.model = new Game();
        this.client = new Client(this);
        this.input = new Scanner(System.in);
        this.leaderCardPrinter = new LeaderCardPrinter();
        this.devCardPrinter = new DevCardPrinter();
        this.faithTrackPrinter = new FaithTrackPrinter();
        this.inputManager = new InputManager();
    }

    @Override
    public void run() {
        System.out.println("\n" +
                Color.colourText("        ▄▄▄▄███▄▄▄▄      ▄████████    ▄████████     ███        ▄████████    ▄████████    ▄████████       ▄██████▄     ▄████████ \n","YELLOW") +
                Color.colourText("      ▄██▀▀▀███▀▀▀██▄   ███    ███   ███    ███ ▀█████████▄   ███    ███   ███    ███   ███    ███      ███    ███   ███    ███ \n", "YELLOW") +
                Color.colourText("      ███   ███   ███   ███    ███   ███    █▀     ▀███▀▀██   ███    █▀    ███    ███   ███    █▀       ███    ███   ███    █▀  \n", "YELLOW") +
                Color.colourText("      ███   ███   ███   ███    ███   ███            ███   ▀  ▄███▄▄▄      ▄███▄▄▄▄██▀   ███             ███    ███  ▄███▄▄▄     \n", "YELLOW") +
                Color.colourText("      ███   ███   ███ ▀███████████ ▀███████████     ███     ▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ▀███████████      ███    ███ ▀▀███▀▀▀     \n", "YELLOW") +
                Color.colourText("      ███   ███   ███   ███    ███          ███     ███       ███    █▄  ▀███████████          ███      ███    ███   ███        \n", "YELLOW") +
                Color.colourText("      ███   ███   ███   ███    ███    ▄█    ███     ███       ███    ███   ███    ███    ▄█    ███      ███    ███   ███        \n", "YELLOW") +
                Color.colourText("       ▀█   ███   █▀    ███    █▀   ▄████████▀     ▄████▀     ██████████   ███    ███  ▄████████▀        ▀██████▀    ███        \n", "YELLOW") +
                Color.colourText("                                                                           ███    ███                                           \n", "YELLOW") +
                Color.colourText("    ▄████████    ▄████████ ███▄▄▄▄      ▄████████  ▄█     ▄████████    ▄████████    ▄████████ ███▄▄▄▄    ▄████████    ▄████████ \n", "YELLOW") +
                Color.colourText("   ███    ███   ███    ███ ███▀▀▀██▄   ███    ███ ███   ███    ███   ███    ███   ███    ███ ███▀▀▀██▄ ███    ███   ███    ███ \n", "YELLOW") +
                Color.colourText("   ███    ███   ███    █▀  ███   ███   ███    ███ ███▌   ███    █▀    ███    █▀    ███    ███ ███   ███ ███    █▀    ███    █▀  \n", "YELLOW") +
                Color.colourText("  ▄███▄▄▄▄██▀  ▄███▄▄▄     ███   ███   ███    ███ ███▌   ███          ███          ███    ███ ███   ███ ███         ▄███▄▄▄     \n", "YELLOW") +
                Color.colourText(" ▀▀███▀▀▀▀▀   ▀▀███▀▀▀     ███   ███ ▀███████████ ███▌ ▀███████████ ▀███████████ ▀███████████ ███   ███ ███        ▀▀███▀▀▀     \n", "YELLOW") +
                Color.colourText(" ▀███████████   ███    █▄  ███   ███   ███    ███ ███           ███          ███   ███    ███ ███   ███ ███    █▄    ███    █▄  \n", "YELLOW") +
                Color.colourText("   ███    ███   ███    ███ ███   ███   ███    ███ ███     ▄█    ███    ▄█    ███   ███    ███ ███   ███ ███    ███   ███    ███ \n", "YELLOW") +
                Color.colourText("   ███    ███   ██████████  ▀█   █▀    ███    █▀  █▀    ▄████████▀   ▄████████▀    ███    █▀   ▀█   █▀  ████████▀    ██████████ \n", "YELLOW") +
                Color.colourText("   ███    ███                                                                                                                   \n", "YELLOW"));

        System.out.println("\n" + "\n" + "\n" + "Write down the IP address of the server you want to connect:");
        String ip = input.nextLine();
        System.out.println("Write down the port number of the server you want to connect:");
        String port = input.nextLine();
        System.out.println("Now, please, enter the username you want to play with:");
        String username = input.nextLine();
        new Thread(() -> client.connect(ip, port, username)).start();

        waitForInput();
    }

    private void waitForInput(){
        while (true){
            if (input.hasNext()){
                if (someoneIsWaitingForInput) {
                    inputString = input.nextLine().trim();
                    newInputStringReady = true;
                    synchronized (this) {
                        notifyAll();
                    }
                }
                else {
                    // operazioni non attese (esempio richiedere di stampare board di un player quando non è il suo turno)
                }
            }
        }
    }

    private String getInput() {
        someoneIsWaitingForInput = true;
        synchronized (this) {
            while (!newInputStringReady) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }
        }
        newInputStringReady = false;
        someoneIsWaitingForInput = false;
        return inputString;
    }

    @Override
    public void updateLobbyState(int lobbyId) {
//        System.out.println("\nLobby number "+ Color.colourInt( lobbyId, "YELLOW") + ": \n" +
//                " __________________\n" +
//                "|                  |\n" +
//                "|          LOBBY ID: " + lobbyId + "\n" +
//                "|    PLAYERS INSIDE: ");
//
//        for(int i=0; i<players.length; i++){
//            if (i > 0)
//                System.out.print(", ");
//            System.out.print(players[i]);
//        }
//
//        System.out.println("\n" +
//                "|                  |\n" +
//                " ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ ");
    }

    @Override
    public void removeLobby(int lobbyId) {
        System.out.println("Lobby number " + lobbyId + " has been removed so it is no longer available");
    }

    @Override
    public void chooseLobby(int lobbyId){
        //mostro lobby che ha scelto
    }

    @Override
    public void chooseLeaderCards() {
        Thread thread = new Thread(() -> {
            System.out.println("You have just received your leader cards!");
            for (Integer i : model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards())
                leaderCardPrinter.printFromID(i, null);
            System.out.println("Pick and discard two of them before starting. \n " +
                    "Write down their id, one by one:");
            while (model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards().size() > 2){
                int id = Integer.parseInt(getInput());
                if (model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards().contains(id))
                    client.update(MessageToServerCreator.createLeaderDiscardMessage(id));
                else
                    System.out.println("Your input is not correct, please, enter the id of card you own! Retry:");
            }

        });

        thread.start();
    }

    @Override
    public void nextTurn(int playerId) {

    }

    @Override
    public void chooseInitialResources() {
        System.out.println("Setting up is almost done, you only have to choose ");

        if (model.getLocalPlayerIndex() == 3)
            System.out.print(2 + " resources ");
        else
            System.out.print(1 + " resource ");

        System.out.print("(between ");

        boolean printedOne = false;
        for (Resource r : Resource.values()) {
            if (r != Resource.ANY && r != Resource.FAITH && r != Resource.WHITE) {
                if (printedOne)
                    System.out.print(", ");
                else
                    printedOne = true;
                System.out.print(r.toString());
            }
        }

        System.out.print(") and decide in which shelf you want to put them.");
    }

    @Override
    public void startGame() {

    }

    @Override
    public void showMarket() {
        //mostro il market al player
    }

    @Override
    public void showDevDecks() {

    }

    @Override
    public void updatePositions(int playerId, int pos) {

    }

    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {

    }

    @Override
    public void updateStrongbox(int playerId) {

    }

    @Override
    public void updateShelves(int playerId, int numShelf) {

    }

    @Override
    public void updateSupportContainer(int playerId) {

    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {
        System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW")+
                " has moved some resources, now his leader card (with id number " +
                Color.colourInt(cardId, "YELLOW") + ") contains the following:");

    }

    @Override
    public void activateLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void discardLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {

    }

    @Override
    public void showWinner(String username) {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public Game getModel(){
        return model;
    }
}
