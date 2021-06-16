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
    private boolean showNotifications;
    private boolean gameInitialized;

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
        this.inputManager = new InputManager(model);
        this.showNotifications = true;
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
                    System.out.println("Please, wait! You cannot do anything right now");
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
        if (showNotifications){
            if (lobbyId == model.getLocalPlayerLobbyId()){
                System.out.println("\nYour lobby has "+ Color.colourText("changed", "YELLOW") + " this way:");
            } else {
                System.out.println("\nThere is something new with lobby number "+ Color.colourInt(lobbyId, "YELLOW") + ": ");
            }
            System.out.println(
                    " __________________\n" +
                    "|                  |\n" +
                    "|          LOBBY ID: " + lobbyId + "\n" +
                    "|    PLAYERS INSIDE: ");

            String[] players = model.getLobbies().get(lobbyId);
            for(int i=0; i<players.length; i++){
                if (i > 0)
                    System.out.print(", ");
                System.out.print(Color.colourText(players[i], "YELLOW"));
            }

            System.out.println("\n" +
                    "|                  |\n" +
                    " ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ ");
        }
    }

    @Override
    public void removeLobby(int lobbyId) {
        if (showNotifications){
            System.out.println("Lobby number " + lobbyId + " has been removed so it is no longer available");
        }
    }

    @Override
    public void chooseLobby(int lobbyId){
        //mostro lobby che ha scelto
    }

    @Override
    public void chooseLeaderCards() {
        Thread thread = new Thread(() -> {
            System.out.println("You have just received your four leader cards! Here they are:");
            for (Integer i : model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards())
                leaderCardPrinter.printFromID(i, null);
            System.out.println("Pick and discard two of them before starting. \n " +
                    "Write down their ids, one by one:");
            int discardedCards = 0;
            while (discardedCards < 2){
                int id = Integer.parseInt(getInput());
                if (model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards().contains(id)) {
                    client.update(MessageToServerCreator.createLeaderDiscardMessage(id));
                    discardedCards++;
                }
                else
                    System.out.println("Your input is not correct, please, enter the id of a card you own! Retry:");
            }
            if (model.getLocalPlayerIndex() == 0)
                gameInitialized = true;
        });

        thread.start();
    }

    @Override
    public void nextTurn(int playerId) {
        if (playerId == model.getLocalPlayerId()){
            System.out.println("========= " + Color.colourText("Now it's your turn!", "RED")+" =========");
        } else {
            System.out.println("========= " + Color.colourText("Now it's " + model.getPlayerFromId(playerId).getUsername() + " turn!", "RED")+ " =========");
        }

        if (gameInitialized){
            //scegliere le varie azioni disponibili prima di scegliere definitivamente il tipo di turno che si vuole giocare
        } else {
            System.out.println("Please wait until " + Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") + " decides!");
        }

    }

    @Override
    public void chooseInitialResources() {
        Thread thread = new Thread(() -> {
            int resRequired;
            System.out.println("Setting up is almost done, you only have to choose ");
            if (model.getLocalPlayerIndex() == 3){
                resRequired = 2;
                System.out.print(resRequired + " resources ");
            } else {
                resRequired = 1;
                System.out.print(resRequired + " resource ");
            }
            System.out.print("(between " + stringLegalResources() + ") and decide in which shelf you want to put them. \n" +
                    "Please list them one by one with their full name:");
            int resWritten = 0;
            while (resWritten < resRequired){

            }
            gameInitialized = true;
        });

      thread.start();
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
        if (playerId == model.getLocalPlayerId()){
            System.out.println(Color.colourText(model.getPlayerFromId(playerId).getUsername(), "YELLOW") +
                    "has successfully discarded the leader card:");
            leaderCardPrinter.printFromID(cardId, null);
        } else {
            System.out.println("Leader card (with id "+ cardId +") discarded successfully! ");
        }
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
