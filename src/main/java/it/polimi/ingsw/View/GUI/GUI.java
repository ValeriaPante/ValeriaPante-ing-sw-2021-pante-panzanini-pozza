package it.polimi.ingsw.View.GUI;


import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import it.polimi.ingsw.View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GUI extends Application implements View {
    private Game model;
    private Client client;

    @Override
    public void start(Stage stage) throws Exception {
        model = new Game();
        client = new Client(this);

        //to remove
        //--------------------------------------------------
        model.setLocalPlayerId(3);
        model.addPlayer(1, new SimplifiedPlayer());
        model.addPlayer(2, new SimplifiedPlayer());
        model.addPlayer(3, new SimplifiedPlayer());
        model.addPlayer(4, new SimplifiedPlayer());
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(57);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(58);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(59);
        model.getPlayerFromId(model.getLocalPlayerId()).addLeaderCard(60);
        //--------------------------------------------------

        Parent root = FXMLLoader.load(getClass().getResource("/Scenes/welcomeScene.fxml"));
        stage.setTitle("Masters of Renaissance ");
        Button loginButton = (Button) root.lookup("#startButton");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

    }

    @Override
    public void updateLobbyState(int lobbyId, String[] players) {

    }

    @Override
    public void removeLobby(int lobbyId) {

    }

    public void createNewLobby(){
    }

    @Override
    public void chooseLobby(int lobbyId){

    }

    @Override
    public void chooseLeaderCards() {
    }


    @Override
    public void chooseInitialResources() {

    }

    @Override
    public void startGame() {
    }

    @Override
    public void showMarket() {

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
