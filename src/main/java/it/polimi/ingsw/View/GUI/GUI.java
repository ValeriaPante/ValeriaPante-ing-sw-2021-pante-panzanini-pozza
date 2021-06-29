package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.ActionTokenType;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.DeselectAllResources;
import it.polimi.ingsw.Network.Client.MessageManager;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.GUI.States.*;
import it.polimi.ingsw.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Graphic User Interface
 */
public class GUI extends Application implements View {
    private Game model;
    private MessageManager messageManager;
    private State currentState;
    private int gamePhase;
    private boolean actionDone;

    @Override
    public void start(Stage stage) throws Exception{
        model = new Game();

        stage.setTitle("Masters of Renaissance ");
        Platform.runLater(() -> Transition.setPrimaryStage(stage));
        WelcomeScene welcomeScene = new WelcomeScene();
        welcomeScene.addObserver(this);
        Platform.runLater(()-> Transition.setWelcomeScene(welcomeScene));
        Platform.runLater(Transition::toWelcomeScene);
        stage.setResizable(false);
        stage.show();

        Platform.runLater(() -> Transition.setDialogStage(new Stage(){{setResizable(false);}}));

    }

    /**
     * Message Manager setter
     * @param messageManager new message manager
     */
    public void setMessageManager(MessageManager messageManager){
        this.messageManager = messageManager;
    }

    /**
     * Message Manager getter
     * @return GUI Message Manager
     */
    public MessageManager getMessageManager(){
        return this.messageManager;
    }

    /**
     * Updates the view of a lobby
     * @param lobbyId number of lobby
     */
    @Override
    public void updateLobbyState(int lobbyId) {
        Platform.runLater(() -> LobbiesScene.addLobby(lobbyId));
        LobbiesScene lobbiesScene = new LobbiesScene(this);
        Platform.runLater(() -> Transition.setLobbiesScene(lobbiesScene));
        Platform.runLater(Transition::toLobbiesScene);

        if(lobbyId == model.getLocalPlayerLobbyId()){
            WaitingToStartScene waitingToStartScene = new WaitingToStartScene(lobbyId, model.getLobbies().get(lobbyId));
            waitingToStartScene.addObserver(this);
            Platform.runLater(() -> Transition.setWaitingToStartScene(waitingToStartScene));
            Platform.runLater(Transition::toWaitingToStartScene);
        }
    }

    /**
     * Removes a lobby from the list of lobbies
     * @param lobbyId number of the lobby to remove
     */
    @Override
    public void removeLobby(int lobbyId) {
        LobbiesScene.removeLobby(lobbyId);
        LobbiesScene lobbiesScene = new LobbiesScene(this);
        Platform.runLater(() -> Transition.setLobbiesScene(lobbiesScene));
        Platform.runLater(Transition::toLobbiesScene);
    }

    /**
     * Shows the scene displaying the leader cards to choose from at the start of the game
     */
    @Override
    public void chooseLeaderCards() {
        LeaderCardScene leaderCardScene = new LeaderCardScene(this);
        Platform.runLater(() -> Transition.setLeaderCardsScene(leaderCardScene));
        Platform.runLater(Transition::toLeaderCardScene);
    }


    /**
     * Shows the scene displaying the choice of the initial resources
     */
    @Override
    public void chooseInitialResources() {
        if(model.getLocalPlayerIndex() == 0){
            Platform.runLater(Transition::toLoadingScene);
        } else {
            currentState = new InitialResourcesState();
            InitialResourcesScene initialResourcesScene = new InitialResourcesScene(this);
            Platform.runLater(() -> Transition.setInitialResourcesScene(initialResourcesScene));
            Platform.runLater(Transition::toInitialResourcesScene);
        }
    }

    /**
     * Shows the main scene of the game
     */
    @Override
    public void startGame() {
        gamePhase = 2;
        Platform.runLater(Transition::toMainScene);

    }

    /**
     * Shows a new dialog with the market inside
     */
    public void showMarket() {
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            MarketScene marketScene = new MarketScene(this);
            Stage dialog = new Stage();
            Platform.runLater(() -> dialog.setScene(new Scene(marketScene.getRoot())));
            dialog.setTitle("Get from market");
            dialog.setResizable(false);
            Platform.runLater(() -> Transition.setDialogStage(dialog));
            Platform.runLater(Transition::showDialog);
        }
    }

    /**
     * Shows a new dialog with the development decks inside
     */
    public void showDevDecks() {
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            DevDecksScene devDecksScene = new DevDecksScene(this);
            Stage dialog = new Stage();
            Platform.runLater(() -> dialog.setScene(new Scene(devDecksScene.getRoot())));
            dialog.setTitle("Buy development card");
            dialog.setResizable(false);
            Platform.runLater(() -> Transition.setDialogStage(dialog));
            Platform.runLater(Transition::showDialog);
        }

    }

    /**
     * Shows a new dialog with the production power that can be used
     */
    public void activateProduction(){
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            ProductionScene productionScene = new ProductionScene();
            productionScene.addObserver(this);
            productionScene.initialise();
            Stage dialog = new Stage();
            dialog.setTitle("Activate production");
            dialog.setResizable(false);
            Platform.runLater(() -> dialog.setScene(new Scene(productionScene.getRoot())));
            Platform.runLater(() -> Transition.setDialogStage(dialog));
            Platform.runLater(Transition::showDialog);
        }
    }

    /**
     * Shows a new dialog with all the deposits where it is possible to move resources
     */
    public void showDeposits(){
        ContainersScene containersScene = new ContainersScene();
        containersScene.addObserver(this);
        containersScene.initialise();
        Platform.runLater(() -> {
            Platform.runLater(() -> Transition.setDialogScene(containersScene.getRoot()));
            Platform.runLater(() -> Transition.reshowDialog());
        });

    }

    /**
     * Updates the faith marker
     * @param playerId id of the player to which the change is associated
     * @param pos new position
     */
    @Override
    public void updatePositions(int playerId, int pos) {
        //if(playerId == model.getLocalPlayerId()) toDoneState();
        if(model.getNumberOfPlayers() == 1){
            Platform.runLater(() -> Transition.updatePosition(playerId == 0, pos));
        } else {
            Platform.runLater(() -> Transition.updatePosition(model.getPlayerIndex(playerId), pos));
        }
    }

    /**
     * Updates the state of the pope favour cards
     * @param playerId id of the player to which the change is associated
     * @param states new states
     */
    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updatePopeFavourCards(model.getPlayerIndex(playerId), states));
        } else {
            Platform.runLater(() -> Transition.updatePopeFavourCards(states));
        }
    }

    /**
     * Updates the strongbox
     * @param playerId id of the player to which the change is associated
     */
    @Override
    public void updateStrongbox(int playerId) {
        if(playerId == model.getLocalPlayerId()) toDoneState();

        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateStrongbox(model.getPlayerIndex(playerId), model.getPlayerFromId(playerId).getStrongbox()));
        } else {
            Platform.runLater(() -> Transition.updateStrongbox(model.getPlayerFromId(playerId).getStrongbox()));
        }
    }

    /**
     * Updates a shelf
     * @param playerId id of the player to which the change is associated
     * @param numShelf number of shelf
     */
    @Override
    public void updateShelves(int playerId, int numShelf) {
        if(Transition.isOnContainersScene()) showDeposits();
        if(playerId == model.getLocalPlayerId()) toDoneState();

        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateShelf(model.getPlayerIndex(playerId), numShelf + 1, model.getPlayerFromId(playerId).getShelf(numShelf), this));
        } else {
            Platform.runLater(() -> Transition.updateShelf(numShelf + 1, model.getPlayerFromId(playerId).getShelf(numShelf), this));

        }
    }

    /**
     * Updates the support container
     * @param playerId id of the player to which the change is associated
     */
    @Override
    public void updateSupportContainer(int playerId) {
        if(Transition.isOnContainersScene()) showDeposits();
        else {
            if(playerId == model.getLocalPlayerId()){
                currentState.next();
                this.actionDone = true;
            }
        }
    }

    /**
     * Updates leader storage
     * @param playerId id of the player to which the change is associated
     * @param cardId id of the leader card with storage ability
     */
    @Override
    public void updateLeaderStorage(int playerId, int cardId) {
        if(Transition.isOnContainersScene()) showDeposits();
        if(playerId == getModel().getLocalPlayerId()) toDoneState();
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateLeaderStorage(model.getPlayerIndex(playerId), cardId, model.getPlayerFromId(playerId).getLeaderStorage(cardId)));
        } else {
            Platform.runLater(() -> Transition.updateLeaderStorage(cardId, model.getPlayerFromId(playerId).getLeaderStorage(cardId)));
        }

    }

    /**
     * Activates a leader card
     * @param playerId id of the player to which the change is associated
     * @param cardId id of the leader card to activate
     */
    @Override
    public void activateLeaderCard(int playerId, int cardId) {
        if(playerId == model.getLocalPlayerId()){
            ProductionScene.setActiveLeaderCard(cardId);
            TransmutationScene.addTransmutation(cardId);
            DiscountsScene.putDiscount(cardId);
        }
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.activateLeaderCard(model.getPlayerIndex(playerId), cardId, playerId == model.getLocalPlayerId()));
        } else {
            Platform.runLater(() -> Transition.activateLeaderCard(cardId));
        }
    }

    /**
     * Discards a leader card
     * @param playerId id of the player to which the change is associated
     * @param cardId id of the leader card to discard
     */
    @Override
    public void discardLeaderCard(int playerId, int cardId) {
        if(playerId == model.getLocalPlayerId()){
            ProductionScene.removeDiscardedLeaderCard(cardId);
            TransmutationScene.removeTransmutation(cardId);
            DiscountsScene.removeDiscount(cardId);
        }
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.discardLeaderCard(model.getPlayerIndex(playerId), cardId, playerId == model.getLocalPlayerId()));
        } else {
            Platform.runLater(() -> Transition.discardLeaderCard(cardId));

        }
    }

    /**
     * Adds a new card in a slot
     * @param playerId id of the player to which the change is associated
     * @param cardId id of the new card
     * @param slot number of slot
     */
    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {
        if(playerId == model.getLocalPlayerId()){
            ProductionScene.setDevCardOnTop(slot-1, cardId);
            toDoneState();
        }

        int[] singleSlot = model.getPlayerFromId(playerId).getDevSlots()[slot - 1];
        if(model.getNumberOfPlayers() == 1){
            if(singleSlot[1] == 0) Platform.runLater(() -> Transition.addCardInSlot(cardId, slot, 1));
            else if(singleSlot[2] == 0) Platform.runLater(() -> Transition.addCardInSlot(cardId, slot, 2));
            else Platform.runLater(() -> Transition.addCardInSlot(cardId, slot, 3));
        } else {
            if(singleSlot[1] == 0) Platform.runLater(() -> Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 1));
            else if(singleSlot[2] == 0) Platform.runLater(() -> Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 2));
            else Platform.runLater(() -> Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 3));
        }
    }

    /**
     * Updates teh inkwell indicating whose turn is this
     * @param playerId id of the player in turn
     */
    @Override
    public void nextTurn(int playerId) {
        actionDone = false;
        if(gamePhase == 0 && playerId == model.getLocalPlayerId()) this.chooseLeaderCards();
        else if(gamePhase == 1 && playerId == model.getLocalPlayerId()) this.chooseInitialResources();
        else if(gamePhase > 1 && model.getNumberOfPlayers() > 1) Platform.runLater(() -> Transition.nextTurn(model.getPlayerIndex(playerId), model.getNumberOfPlayers(), playerId == model.getLocalPlayerId()));
    }


    @Override
    public void startInitialisation(){

    }

    /**
     * Shows the winner
     * @param winnerId id of the player to which the change is associated
     */
    @Override
    public void showWinner(int winnerId) {
        if(model.getNumberOfPlayers() == 1 && model.getLocalPlayerId() != winnerId){
            Platform.runLater(() -> Transition.setWinnerScene(new WinnerScene("Lorenzo Il Magnifico")));
        } else {
            Platform.runLater(() -> Transition.setWinnerScene(new WinnerScene(model.getPlayerFromId(winnerId).getUsername()+" (id: "+winnerId+")")));
        }
        Platform.runLater(Transition::toWinnerScene);

    }

    /**
     * Shows a new error
     * @param message message to show
     */
    @Override
    public void showErrorMessage(String message) {
        if(currentState != null) Platform.runLater(() -> currentState.goBack());
        Platform.runLater(() -> Transition.showErrorMessage(message));
        if(Transition.isOnContainersScene()) messageManager.update(new DeselectAllResources());
    }

    /**
     * Deselects a wrong selection
     * @param message message to show
     * @param cardId id of the card to deselect
     */
    @Override
    public void showSelectionError(String message, int cardId){

        Platform.runLater(() -> Transition.showErrorMessage(message));
        Platform.runLater(() -> DevDecksScene.deselectAll());
        Platform.runLater(() -> ProductionScene.deselectIfSelected(cardId));

    }

    @Override
    public void showLorenzoTurn(ActionTokenType actionToken){
        Platform.runLater(() -> Transition.showLorenzoMove("Lorenzo drew a token, its effect is: "+ ActionTokenType.getEffectStringForGUI(actionToken)));
    }

    @Override
    public Game getModel(){
        return model;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void toMarketState(){
        currentState = new MarketState(this);
    }

    public void toBuyDevCardState(){
        currentState = new BuyDevCardState(this);
    }

    public void toProductionState(){
        currentState = new ProductionState(this);
    }

    public void toDoneState(){
        currentState = new DoneState();
    }

    public void setGamePhase(int numberOfPhase){
        this.gamePhase = numberOfPhase;
    }
}
