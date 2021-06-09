package it.polimi.ingsw.View.GUI;


import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.GUI.States.*;
import it.polimi.ingsw.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application implements View {
    private Game model;
    private Client client;
    private State currentState;
    private int gamePhase;
    private boolean actionDone;

    @Override
    public void start(Stage stage) throws Exception{
        model = new Game();
        client = new Client(this);

        stage.setTitle("Masters of Renaissance ");
        Transition.setPrimaryStage(stage, client);
        WelcomeScene welcomeScene = new WelcomeScene();
        welcomeScene.addObserver(this);
        Transition.setWelcomeScene(welcomeScene);
        Transition.toWelcomeScene();
        stage.setResizable(false);
        stage.show();

    }

    public Client getClient(){
        return this.client;
    }

    @Override
    public void updateLobbyState(int lobbyId) {
        Platform.runLater(() -> {
            LobbiesScene.addLobby(lobbyId);
            LobbiesScene lobbiesScene = new LobbiesScene(this);
            Transition.setLobbiesScene(lobbiesScene);
            Transition.toLobbiesScene();
        });

        if(WaitingToStartScene.isReady() && lobbyId == model.getLocalPlayerLobbyId()){
            Platform.runLater(() -> {
                WaitingToStartScene waitingToStartScene = new WaitingToStartScene(lobbyId, model.getLobbies().get(lobbyId));
                Transition.setWaitingToStartScene(waitingToStartScene);
                Transition.toWaitingToStartScene();
            });
        }
    }

    @Override
    public void removeLobby(int lobbyId) {
        Platform.runLater(() -> {
            LobbiesScene.removeLobby(lobbyId);
            LobbiesScene lobbiesScene = new LobbiesScene(this);
            Transition.setLobbiesScene(lobbiesScene);
            Transition.toLobbiesScene();
        });
    }

    @Override
    public void chooseLobby(int lobbyId){

    }

    @Override
    public void chooseLeaderCards() {
        Platform.runLater(() -> {
            LeaderCardScene leaderCardScene = new LeaderCardScene(this);
            Transition.setLeaderCardsScene(leaderCardScene);
            Transition.toLeaderCardScene();
        });
    }


    @Override
    public void chooseInitialResources() {
        if(model.getLocalPlayerIndex() == 0){
            Platform.runLater(Transition::toLoadingScene);
        } else {
            currentState = new InitialResourcesState();
            Platform.runLater(() -> {
                InitialResourcesScene initialResourcesScene = new InitialResourcesScene(this);
                Transition.setInitialResourcesScene(initialResourcesScene);
                Transition.toInitialResourcesScene();
            });
        }
    }

    @Override
    public void startGame() {
        gamePhase = 2;
        if(model.getNumberOfPlayers() == 1){
            Platform.runLater(() -> {
                SinglePlayerMainScene mainScene = new SinglePlayerMainScene(this);
                Transition.setMainScene(mainScene);
                Transition.toMainScene();
            });
        } else {
            Platform.runLater(() -> {
                MainScene mainScene = new MainScene(this);
                Transition.setMainScene(mainScene);
                Transition.toMainScene();
            });
        }

    }

    @Override
    public void showMarket() {
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            Platform.runLater(() -> {
                MarketScene marketScene = new MarketScene(this);
                Stage dialog = new Stage();
                dialog.setScene(new Scene(marketScene.getRoot()));
                dialog.setTitle("Get from market");
                dialog.setResizable(false);
                Transition.setDialogStage(dialog);
                Transition.showDialog();
            });
        }
    }

    @Override
    public void showDevDecks() {
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            Platform.runLater(() -> {
                DevDecksScene devDecksScene = new DevDecksScene(this);
                Stage dialog = new Stage();
                dialog.setScene(new Scene(devDecksScene.getRoot()));
                dialog.setTitle("Buy development card");
                dialog.setResizable(false);
                Transition.setDialogStage(dialog);
                Transition.showDialog();
            });
        }

    }

    public void activateProduction(){
        if(actionDone){
            Platform.runLater(() -> Transition.showErrorMessage("You already made your turn."));
        } else {
            Platform.runLater(() -> {
                ProductionScene productionScene = new ProductionScene();
                productionScene.addObserver(this);
                productionScene.initialise();
                Stage dialog = new Stage();
                dialog.setScene(new Scene(productionScene.getRoot()));
                dialog.setTitle("Activate production");
                dialog.setResizable(false);
                Transition.setDialogStage(dialog);
                Transition.showDialog();
            });
        }
    }

    public void showDeposits(){
        Platform.runLater(() -> {
            ContainersScene containersScene = new ContainersScene();
            containersScene.addObserver(this);
            containersScene.initialise();
            Stage dialog = new Stage();
            dialog.setScene(new Scene(containersScene.getRoot()));
            dialog.setTitle("Your deposits");
            dialog.setResizable(false);
            Transition.setDialogStage(dialog);
            Transition.showDialog();
        });
    }

    @Override
    public void updatePositions(int playerId, int pos) {
        if(playerId == model.getLocalPlayerId()) toDoneState();
        Platform.runLater(() -> {
            if(model.getNumberOfPlayers() == 1){
                Platform.runLater(() -> Transition.updatePosition(playerId != model.getLocalPlayerId(), pos));
            } else {
                Platform.runLater(() -> Transition.updatePosition(model.getPlayerIndex(playerId), pos));
            }
        });
    }

    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updatePopeFavourCards(model.getPlayerIndex(playerId), states));
        } else {
            Platform.runLater(() -> Transition.updatePopeFavourCards(states));
        }
    }

    @Override
    public void updateStrongbox(int playerId) {
        if(playerId == model.getLocalPlayerId()) toDoneState();

        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateStrongbox(model.getPlayerIndex(playerId), model.getPlayerFromId(playerId).getStrongbox()));
        } else {
            Platform.runLater(() -> Transition.updateStrongbox(model.getPlayerFromId(playerId).getStrongbox()));
        }
    }

    @Override
    public void updateShelves(int playerId, int numShelf) {
        if(playerId == model.getLocalPlayerId()) toDoneState();

        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateShelf(model.getPlayerIndex(playerId), numShelf + 1, model.getPlayerFromId(playerId).getShelf(numShelf), this));
        } else {
            Platform.runLater(() -> Transition.updateShelf(numShelf + 1, model.getPlayerFromId(playerId).getShelf(numShelf), this));

        }
    }

    @Override
    public void updateSupportContainer(int playerId) {
        if(playerId == model.getLocalPlayerId()){
            Platform.runLater(() -> currentState.next());
            this.actionDone = true;
        }
    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {
        if(playerId == getModel().getLocalPlayerId()) toDoneState();
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.updateLeaderStorage(model.getPlayerIndex(playerId), cardId, model.getPlayerFromId(playerId).getLeaderStorage(cardId)));
        } else {
            Platform.runLater(() -> Transition.updateLeaderStorage(cardId, model.getPlayerFromId(playerId).getLeaderStorage(cardId)));
        }

    }

    @Override
    public void activateLeaderCard(int playerId, int cardId) {
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.activateLeaderCard(model.getPlayerIndex(playerId), cardId, playerId == model.getLocalPlayerId()));
        } else {
            Platform.runLater(() -> Transition.activateLeaderCard(cardId));
        }
    }

    @Override
    public void discardLeaderCard(int playerId, int cardId) {
        if(model.getNumberOfPlayers() > 1){
            Platform.runLater(() -> Transition.discardLeaderCard(model.getPlayerIndex(playerId), cardId, playerId == model.getLocalPlayerId()));
        } else {
            Platform.runLater(() -> Transition.discardLeaderCard(cardId));

        }
    }

    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {
        if(playerId == model.getLocalPlayerId()){
            ProductionScene.setDevCardOnTop(slot-1, cardId);
            toDoneState();
        }

        Platform.runLater(() -> {
            int[] singleSlot = model.getPlayerFromId(playerId).getDevSlots()[slot - 1];
            if(model.getNumberOfPlayers() == 1){
                if(singleSlot[1] == 0) Transition.addCardInSlot(cardId, slot, 1);
                else if(singleSlot[2] == 0) Transition.addCardInSlot(cardId, slot, 2);
                else Transition.addCardInSlot(cardId, slot, 3);
            } else {
                if(singleSlot[1] == 0) Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 1);
                else if(singleSlot[2] == 0) Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 2);
                else Transition.addCardInSlot(model.getPlayerIndex(playerId), cardId, slot, 3);
            }
        });
    }

    @Override
    public void nextTurn(int playerId) {
        if(gamePhase == 0 && playerId == model.getLocalPlayerId()) this.chooseLeaderCards();
        else if(gamePhase == 1 && playerId == model.getLocalPlayerId()) this.chooseInitialResources();
        else if(gamePhase > 1 && model.getNumberOfPlayers() > 1) Platform.runLater(() -> Transition.nextTurn(model.getPlayerIndex(playerId), model.getNumberOfPlayers(), playerId == model.getLocalPlayerId()));
    }

    @Override
    public void showWinner(String username) {
        Platform.runLater(() -> {
            Transition.setWinnerScene(new WinnerScene(username));
            Transition.toWinnerScene();
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            Transition.showErrorMessage(message);
            currentState.goBack();
        });
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
