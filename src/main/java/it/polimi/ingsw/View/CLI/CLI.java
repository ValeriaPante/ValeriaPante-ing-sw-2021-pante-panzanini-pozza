package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Exceptions.PrintWithoutMessageCreationException;
import it.polimi.ingsw.Exceptions.SuppressNotificationsException;
import it.polimi.ingsw.Network.Client.LocalMessageManager;
import it.polimi.ingsw.Network.Client.MessageManager;
import it.polimi.ingsw.Network.Client.MessageToServerManager;
import it.polimi.ingsw.View.CLI.Printers.Printer;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.Observable;
import it.polimi.ingsw.View.View;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CLI extends Observable implements View, Runnable{
    private MessageManager client;
    private final Game model;
    private final SimplifiedFaithTrack simplifiedFaithTrack;

    private final Scanner input;
    private final Printer printer;
    private final InputManager inputManager;
    private ThreadPoolExecutor executor;

    private boolean showNotifications;

    //Turn state is an integer used to represent the state of the game, here are the meaning of the values:
    // 0: pre game
    // 1: initialisation of the game
    // 2: player's turn
    // 3: another player's turn
    private int turnState;

    public CLI(){
        this.model = new Game();
        this.simplifiedFaithTrack = new SimplifiedFaithTrack(model.getPLayersID());
        this.input = new Scanner(System.in);
        this.printer = new Printer(model, simplifiedFaithTrack);
        this.inputManager = new InputManager(model);
        this.showNotifications = true;
    }

    @Override
    public void run() {
        printTitle();

        System.out.println("\n\n" + "Would you like to play:\n"+
                "\t+ " + Color.colourText("online","YELLOW") + " (write down \"" + Color.colourText("ONLINE", "YELLOW") + "\") \n" +
                "\t+ " + Color.colourText("offline","YELLOW") + " (write down \"" + Color.colourText("OFFLINE", "YELLOW") + "\")");
        String onlineOfflineDecision = input.nextLine().trim().toUpperCase();

        while (!onlineOfflineDecision.equals("ONLINE") && !onlineOfflineDecision.equals("OFFLINE")){
            System.out.println("Your input was not correct! Please, retry...");
            onlineOfflineDecision = input.nextLine().trim().toUpperCase();
        }

        if (onlineOfflineDecision.equals("ONLINE")){
            this.client = new MessageToServerManager(this);
            System.out.println("\n" + "Write down the IP address of the server you want to connect:");
            String ip = input.nextLine().trim();
            System.out.println("Write down the port number of the server you want to connect:");
            String port = input.nextLine().trim();
            System.out.println("Now, please, enter the username you want to play with:");
            String username = input.nextLine();
            turnState = 0;
            new Thread(() -> client.connect(ip, port, username)).start();
        } else {
            this.client = new LocalMessageManager(this);
            //manca la parte per partita offline
            turnState = 1;
        }

        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        waitForInput();
    }

    public void printTitle(){
        System.out.println("\n" +
                Color.colourText(
                "        ▄▄▄▄███▄▄▄▄      ▄████████    ▄████████     ███        ▄████████    ▄████████    ▄████████       ▄██████▄     ▄████████ \n"+
                    "      ▄██▀▀▀███▀▀▀██▄   ███    ███   ███    ███ ▀█████████▄   ███    ███   ███    ███   ███    ███      ███    ███   ███    ███ \n"+
                    "      ███   ███   ███   ███    ███   ███    █▀     ▀███▀▀██   ███    █▀    ███    ███   ███    █▀       ███    ███   ███    █▀  \n"+
                    "      ███   ███   ███   ███    ███   ███            ███   ▀  ▄███▄▄▄      ▄███▄▄▄▄██▀   ███             ███    ███  ▄███▄▄▄     \n"+
                    "      ███   ███   ███ ▀███████████ ▀███████████     ███     ▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ▀███████████      ███    ███ ▀▀███▀▀▀     \n"+
                    "      ███   ███   ███   ███    ███          ███     ███       ███    █▄  ▀███████████          ███      ███    ███   ███        \n"+
                    "      ███   ███   ███   ███    ███    ▄█    ███     ███       ███    ███   ███    ███    ▄█    ███      ███    ███   ███        \n"+
                    "       ▀█   ███   █▀    ███    █▀   ▄████████▀     ▄████▀     ██████████   ███    ███  ▄████████▀        ▀██████▀    ███        \n"+
                    "                                                                           ███    ███                                           \n"+
                    "    ▄████████    ▄████████ ███▄▄▄▄      ▄████████  ▄█     ▄████████    ▄████████    ▄████████ ███▄▄▄▄    ▄████████    ▄████████ \n"+
                    "   ███    ███   ███    ███ ███▀▀▀██▄   ███    ███ ███    ███    ███   ███    ███   ███    ███ ███▀▀▀██▄ ███    ███   ███    ███ \n"+
                    "   ███    ███   ███    █▀  ███   ███   ███    ███ ███▌   ███    █▀    ███    █▀    ███    ███ ███   ███ ███    █▀    ███    █▀  \n"+
                    "  ▄███▄▄▄▄██▀  ▄███▄▄▄     ███   ███   ███    ███ ███▌   ███          ███          ███    ███ ███   ███ ███         ▄███▄▄▄     \n"+
                    " ▀▀███▀▀▀▀▀   ▀▀███▀▀▀     ███   ███ ▀███████████ ███▌ ▀███████████ ▀███████████ ▀███████████ ███   ███ ███        ▀▀███▀▀▀     \n"+
                    " ▀███████████   ███    █▄  ███   ███   ███    ███ ███           ███          ███   ███    ███ ███   ███ ███    █▄    ███    █▄  \n"+
                    "   ███    ███   ███    ███ ███   ███   ███    ███ ███     ▄█    ███    ▄█    ███   ███    ███ ███   ███ ███    ███   ███    ███ \n"+
                    "   ███    ███   ██████████  ▀█   █▀    ███    █▀  █▀    ▄████████▀   ▄████████▀    ███    █▀   ▀█   █▀  ████████▀    ██████████ \n"+
                    "   ███    ███                                                                                                                   \n", "YELLOW"));
    }

    private void waitForInput(){
        String inputFromPlayer;
        while (true){
            inputFromPlayer = input.nextLine();
            try{
                if (turnState == 0) {
                    client.update(inputManager.preGameInput(inputFromPlayer));
                } else if (turnState == 1) {
                    client.update(inputManager.initializationInput(inputFromPlayer));
                } else if (turnState == 2){
                    client.update(inputManager.inTurnInput(inputFromPlayer));
                } else {
                    inputManager.NOTinTurnInput(inputFromPlayer);
                }
            } catch (IllegalArgumentException error) {
                printer.printError(error.getMessage());
            } catch (PrintWithoutMessageCreationException request) {
                if (turnState == 0)
                    printer.preGamePrintRequest(request.getMessage());
                else
                    printer.inGamePrintRequest(request.getMessage());
            } catch (SuppressNotificationsException toggleNotificationState){
                showNotifications = !showNotifications;
                printer.printNotificationState(showNotifications);
            }
        }
    }

    @Override
    public void updateLobbyState(int lobbyId) {
        if (showNotifications)
            executor.execute(() -> printer.notifyLobbyChange(lobbyId));
    }

    @Override
    public void removeLobby(int lobbyId) {
        if (showNotifications)
            executor.execute(() -> printer.notifyLobbyRemoval(lobbyId));
    }

    @Override
    public void chooseLeaderCards() {
        executor.execute(() -> {
            turnState = 1;
            printer.notifyChooseLeaderCards();
        });
    }

    @Override
    public void nextTurn(int playerId) {
        executor.execute(() -> {
            if (turnState == 2)
                turnState = 3;

            if (turnState == 3 && playerId == model.getLocalPlayerId())
                turnState = 2;

            if (turnState == 1 && playerId != model.getLocalPlayerId()){
                printer.waitingInitialisation(playerId);
                return;
            }

            printer.notifyTurnChanged(playerId);
        });
    }

    @Override
    public void chooseInitialResources() {
        executor.execute(printer::notifyChooseInitialRes);
    }

    @Override
    public void startGame() {
        executor.execute(() -> {
            turnState = (model.getPLayersID().get(0) == model.getLocalPlayerId() ? 2 : 3);
            printer.gameStarted();
        });
    }

    @Override
    public void updatePositions(int playerId, int pos) {
        executor.execute(() -> {
            simplifiedFaithTrack.changePosition(playerId, pos);
            if (showNotifications)
                printer.notifyFaithTrackPositionChange(playerId);
        });
    }

    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {
        executor.execute(() -> {
            simplifiedFaithTrack.changeState(playerId, states);
            if (showNotifications)
                printer.notifyPopeFavorCardChange(playerId);
        });
    }

    @Override
    public void updateStrongbox(int playerId) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyStrongBoxChange(playerId));
    }

    @Override
    public void updateShelves(int playerId, int numShelf) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyShelvesChange(playerId, numShelf));
    }

    @Override
    public void updateSupportContainer(int playerId) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifySupportContainerChange(playerId));
    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyChangeInLCStorage(playerId, cardId));
    }

    @Override
    public void activateLeaderCard(int playerId, int cardId) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyLeaderCardActivation(playerId, cardId));
    }

    @Override
    public void discardLeaderCard(int playerId, int cardId) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyLeaderCardDiscard(playerId, cardId));
    }

    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {
        if (playerId != model.getLocalPlayerId() && showNotifications)
            executor.execute(() -> printer.notifyDevCardPurchase(playerId, cardId, slot));
    }

    @Override
    public void showWinner(int winnerId) {
        executor.execute(() -> printer.printWinner(winnerId));
    }

    @Override
    public void showErrorMessage(String message) {
        executor.execute(() -> printer.printError(message));
    }

    @Override
    public void startInitialisation() {
        executor.execute(() -> {
            turnState = 1;
            printer.notifyInitializationStarted();
    });
    }

    @Override
    public void showSelectionError(String message, int cardId) {
        executor.execute(() -> printer.printSelectionError(message, cardId));
    }

    @Override
    public Game getModel(){
        return model;
    }
}
