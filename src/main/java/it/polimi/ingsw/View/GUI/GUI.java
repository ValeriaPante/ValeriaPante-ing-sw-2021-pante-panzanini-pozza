package it.polimi.ingsw.View.GUI;


import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import it.polimi.ingsw.View.GUI.States.*;
import it.polimi.ingsw.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class GUI extends Application implements View {
    private Game model;
    private Client client;
    private State currentState;

    @Override
    public void start(Stage stage) throws Exception {
        model = new Game();
        client = new Client(this);

        stage.setTitle("Masters of Renaissance ");
        Transition.setPrimaryStage(stage);
        WelcomeScene welcomeScene = new WelcomeScene();
        welcomeScene.addObserver(this);
        Transition.setWelcomeScene(welcomeScene);
        Transition.toWelcomeScene();
        stage.setResizable(false);
        stage.show();

        //to remove
        //--------------------------------------------------
        model.setLocalPlayerId(4);
        model.addPlayer(1, new SimplifiedPlayer());
        model.addPlayer(2, new SimplifiedPlayer());
        model.addPlayer(3, new SimplifiedPlayer());
        model.addPlayer(4, new SimplifiedPlayer());
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(61);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(62);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(59);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(60);
        model.getPlayerFromId(model.getLocalPlayerId()).setSupportContainer(new HashMap<>());
        model.getPlayerFromId(model.getLocalPlayerId()).setShelf(Resource.COIN,1, 0);
        int[][] devDecks = {{1,4,2,3},{17,20,18,19},{33,36,34,35}};
        model.initialiseDevDecks(devDecks);
        Resource[][] market = {{Resource.SHIELD, Resource.STONE, Resource.FAITH,Resource.WHITE},
                {Resource.FAITH, Resource.WHITE, Resource.WHITE, Resource.SERVANT},
                {Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD}};
        model.updateMarketState(market, Resource.STONE);

        HashMap<Resource, Integer> map = new HashMap<>();
        map.put(Resource.ANY,7);
        model.getPlayerFromId(model.getLocalPlayerId()).setSupportContainer(map);

        model.addLobby(0, new String[]{"valeria"});
        model.addLobby(1, new String[]{"valeria"});
        model.addLobby(2, new String[]{"valeria"});
        model.addLobby(3, new String[]{"valeria"});
        model.addLobby(4, new String[]{"valeria"});
        model.addLobby(5, new String[]{"valeria", "daniel", "alberto"});
        model.addLobby(6, new String[]{"valeria"});
        model.addLobby(7, new String[]{"valeria"});
        model.addLobby(8, new String[]{"valeria"});
        model.addLobby(9, new String[]{"valeria"});
        model.addLobby(10, new String[]{"valeria"});
        model.addLobby(11, new String[]{"valeria"});
        LobbiesScene.addLobby(0);
        LobbiesScene.addLobby(1);
        LobbiesScene.addLobby(2);
        LobbiesScene.addLobby(3);
        LobbiesScene.addLobby(4);
        LobbiesScene.addLobby(5);
        LobbiesScene.addLobby(6);
        LobbiesScene.addLobby(7);
        LobbiesScene.addLobby(8);
        LobbiesScene.addLobby(9);
        LobbiesScene.addLobby(10);
        LobbiesScene.addLobby(11);

        new Thread(() ->{
            try{
                Thread.sleep(5000);
                model.addLobby(1000, new String[]{"valeria"});
                LobbiesScene.addLobby(1000);
                this.updateLobbyState(1000);
                Thread.sleep(5000);
                model.addLobby(0, new String[]{"valeria", "salvo"});
                LobbiesScene.addLobby(0);
                this.updateLobbyState(0);
                model.addLobby(1001, new String[]{"valeria", "salvo"});
                LobbiesScene.addLobby(1001);
                this.updateLobbyState(1001);
                this.removeLobby(2);
                model.addLobby(1002, new String[]{"seby", "salvo"});
                LobbiesScene.addLobby(1002);
                this.updateLobbyState(1002);
                Thread.sleep(5000);
                this.chooseLeaderCards();
                Thread.sleep(5000);
                this.startGame();
                /*Thread.sleep(7000);
                this.updateSupportContainer(model.getLocalPlayerId());
                model.getPlayerFromId(3).addDevCardInSlot(1,2);
                addDevCardInSlot(3, 1, 2);
                Thread.sleep(7000);
                model.getPlayerFromId(3).addDevCardInSlot(2,2);
                addDevCardInSlot(3,2,2);
                Thread.sleep(7000);
                model.getPlayerFromId(3).addDevCardInSlot(3,2);
                addDevCardInSlot(3,3,2);
                Thread.sleep(3000);
                updatePositions(3, 2);
                Thread.sleep(3000);
                updatePositions(2, 8);*/
                /*Thread.sleep(3000);
                showErrorMessage("sbagliato!!!");*/
                Thread.sleep(3000);
                activateLeaderCard(3, 64);
                Thread.sleep(3000);
                discardLeaderCard(3, 61);
            } catch (Exception e){

            }
        }).start();
        //----------------------------------------------------------------------------------

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

    @Override
    public void showDevDecks() {
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

    public void activateProduction(){
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

    @Override
    public void updatePositions(int playerId, int pos) {
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

    }

    @Override
    public void updateStrongbox(int playerId) {

    }

    @Override
    public void updateShelves(int playerId, int numShelf) {

    }

    @Override
    public void updateSupportContainer(int playerId) {
        if(playerId == model.getLocalPlayerId()) Platform.runLater(() -> currentState.next());
    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {

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

    }

    @Override
    public void showWinner(String username) {

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
}
