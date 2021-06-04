package it.polimi.ingsw.View.GUI;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;

public class Transition {
    private static Stage primaryStage;
    private static Stage dialogStage;
    private static Scene welcomeScene;
    private static Scene usernameScene;
    private static Scene lobbiesScene;
    private static Scene waitingToStartScene;
    private static Scene loadingScene;
    private static Scene leaderCardsScene;
    private static Scene initialResourcesScene;
    private static Scene mainScene;
    private static Scene winnerScene;

    public static void setPrimaryStage(Stage primaryStage) {
        Transition.primaryStage = primaryStage;
    }

    public static void setDialogStage(Stage dialogStage){
        Transition.dialogStage = dialogStage;
    }

    public static void setDialogScene(Scene scene){
        dialogStage.setScene(scene);
    }

    public static void setWelcomeScene(WelcomeScene welcomeScene) {
        Transition.welcomeScene = new Scene(welcomeScene.getRoot());
    }

    public static void setUsernameScene(UsernameScene usernameScene) {
        Transition.usernameScene = new Scene(usernameScene.getRoot());
    }

    public static void setLobbiesScene(LobbiesScene lobbiesScene) {
        Transition.lobbiesScene = new Scene(lobbiesScene.getRoot());
    }

    public static void setWaitingToStartScene(WaitingToStartScene waitingToStartScene) {
        Transition.waitingToStartScene = new Scene(waitingToStartScene.getRoot());
    }

    public static void setLoadingScene(LoadingScene loadingScene){
        Transition.loadingScene = new Scene(loadingScene.getRoot());
    }

    public static void setLeaderCardsScene(LeaderCardScene leaderCardsScene){
        Transition.leaderCardsScene = new Scene(leaderCardsScene.getRoot());
    }

    public static void setInitialResourcesScene(InitialResourcesScene initialResourcesScene){
        Transition.initialResourcesScene = new Scene(initialResourcesScene.getRoot());
    }

    public static void setMainScene(MainScene mainScene){
        Transition.mainScene = new Scene(mainScene.getRoot());
    }

    public static void setMainScene(SinglePlayerMainScene mainScene){
        Transition.mainScene = new Scene(mainScene.getRoot());
    }

    public static void setWinnerScene(WinnerScene winnerScene){
        Transition.winnerScene = new Scene(winnerScene.getRoot());
    }

    public static void toWelcomeScene(){
        primaryStage.setScene(welcomeScene);
    }

    public static void toUsernameScene(){
        primaryStage.setScene(usernameScene);
    }

    public static void toLobbiesScene(){
        if(!WaitingToStartScene.isReady()) primaryStage.setScene(lobbiesScene);
    }

    public static void toWaitingToStartScene(){
        if(loadingScene == null) primaryStage.setScene(waitingToStartScene);
    }

    public static void toLoadingScene(){
        primaryStage.setScene(loadingScene);
    }

    public static void toLeaderCardScene(){
        primaryStage.setScene(leaderCardsScene);
    }

    public static void toInitialResourcesScene(){
        primaryStage.setScene(initialResourcesScene);
    }

    public static void toMainScene(){
        primaryStage.setScene(mainScene);
    }

    public static void toWinnerScene(){
        primaryStage.setScene(winnerScene);
    }

    public static void showDialog(){
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.showAndWait();
    }

    public static void setUnclosableDialog(){

    }

    public static void hideDialog(){
        dialogStage.close();
    }

    public static void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    public static void updateLeaderCards(int index){
        GridPane gridPane = (GridPane) leaderCardsScene.getRoot().lookup("#gridPane");
        gridPane.getChildren().remove(index);
        gridPane.add(new Pane(),index,0);
    }

    public static void enableProductionButton(){
        dialogStage.getScene().getRoot().lookup("#continue").setDisable(false);
    }

    public static void addCardInSlot(int playerIndex, int cardId, int slot, int row){
        InputStream in = Transition.class.getResourceAsStream("/Images/"+cardId+".png");
        ImageView image = new ImageView();
        image.setImage(new Image(in));
        image.setFitWidth(50);
        image.setPreserveRatio(true);
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        AnchorPane card = (AnchorPane) grid.getChildren().get(playerIndex).lookup("#slot"+slot).lookup("#card"+row);
        card.getChildren().add(image);
    }

    public static void addCardInSlot( int cardId, int slot, int row){
        InputStream in = Transition.class.getResourceAsStream("/Images/"+cardId+".png");
        ImageView image = new ImageView();
        image.setImage(new Image(in));
        image.setFitWidth(150);
        image.setPreserveRatio(true);
        AnchorPane card = (AnchorPane) mainScene.getRoot().lookup("#slot"+slot).lookup("#card"+row);
        card.getChildren().add(image);
    }

    public static void updatePosition(int index, int position){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        double[] newPos = MainScene.getPositions()[position - 2];
        grid.getChildren().get(index).lookup("#faith").setLayoutX(newPos[0]);
        grid.getChildren().get(index).lookup("#faith").setLayoutY(newPos[1]);
    }

    public static void updatePosition(boolean isLorenzo, int position){
        if(isLorenzo){
            double[] newPos = SinglePlayerMainScene.getLorenzoPositions()[position - 2];
            mainScene.getRoot().lookup("#lorenzo").setLayoutX(newPos[0]);
            mainScene.getRoot().lookup("#lorenzo").setLayoutY(newPos[1]);
        } else {
            double[] newPos = SinglePlayerMainScene.getPlayerPositions()[position - 2];
            mainScene.getRoot().lookup("#faith").setLayoutX(newPos[0]);
            mainScene.getRoot().lookup("#faith").setLayoutY(newPos[1]);
        }
    }

    public static void activateLeaderCard(int index, int cardId, boolean isLocalPlayer){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Node playerPane = grid.getChildren().get(index);
        if(isLocalPlayer){
            ((Button) playerPane.lookup("#activate"+cardId)).setDisable(true);
        } else {
            InputStream in = Transition.class.getResourceAsStream("/Images/"+cardId+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(100);
            image.setPreserveRatio(true);
            AnchorPane card = playerPane.lookup("#lc1") != null ? (AnchorPane) playerPane.lookup("#lc1") : (AnchorPane) playerPane.lookup("#lc2");
            card.getChildren().add(image);
            card.setId(String.valueOf(cardId));
        }
    }

    public static void activateLeaderCard(int cardId){
        ((Button) mainScene.getRoot().lookup("#activate"+cardId)).setDisable(true);
    }

    public static void discardLeaderCard(int index, int cardId, boolean isLocalPlayer){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Node playerPane = grid.getChildren().get(index);
        if(isLocalPlayer){
            playerPane.lookup("#"+cardId).setVisible(false);
            playerPane.lookup("#activate"+cardId).setVisible(false);
            playerPane.lookup("#discard"+cardId).setVisible(false);
        } else {
            if(playerPane.lookup("#"+cardId) != null) playerPane.lookup("#"+cardId).setVisible(false);
            else if(playerPane.lookup("#lc1") != null) playerPane.lookup("#lc1").setVisible(false);
            else playerPane.lookup("#lc2").setVisible(false);
        }
    }

    public static void discardLeaderCard(int cardId){
        mainScene.getRoot().lookup("#activate"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#discard"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#"+cardId).setVisible(false);
    }

    public static void nextTurn(int index, int numberOfPlayers){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 0; i < numberOfPlayers; i++){
            grid.getChildren().get(i).lookup("#calamaio").setVisible(false);
        }
        grid.getChildren().get(index).lookup("#calamaio").setVisible(true);
    }
}
