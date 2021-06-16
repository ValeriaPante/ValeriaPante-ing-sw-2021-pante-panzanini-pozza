package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.DisconnectMessage;
import it.polimi.ingsw.Network.Client.MessageManager;
import it.polimi.ingsw.Network.Client.MessageToServerManager;
import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    private static boolean onContainersScene;

    public static void setPrimaryStage(Stage primaryStage) {
        Transition.primaryStage = primaryStage;
    }

    public static void setDialogStage(Stage dialogStage){
        Transition.dialogStage = dialogStage;
    }

    public static void setDisconnectOnClose(MessageManager connectionHandler){
        primaryStage.setOnCloseRequest(windowEvent -> {
            connectionHandler.update(new DisconnectMessage());
            System.exit(1);
        });

    }

    public static void setDialogScene(Pane scene){
        dialogStage.getScene().setRoot(scene);
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

    public static void reshowDialog(){
        if(!dialogStage.isShowing()) dialogStage.showAndWait();
    }

    public static void hideDialog(){
        dialogStage.close();
    }

    public static void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    public static void setOnContainersScene(boolean onContainersScene){
        Transition.onContainersScene = onContainersScene;
    }

    public static void updateLeaderCards(int index){
        GridPane gridPane = (GridPane) leaderCardsScene.getRoot().lookup("#gridPane");
        gridPane.getChildren().remove(index);
        gridPane.add(new Pane(),index,0);
    }

    public static void addCardInSlot(int playerIndex, int cardId, int slot, int row){
        InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +cardId+".png");
        ImageView image = new ImageView();
        image.setImage(new Image(in));
        image.setFitWidth(50);
        image.setPreserveRatio(true);
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        AnchorPane card = (AnchorPane) grid.getChildren().get(playerIndex).lookup("#slot"+slot).lookup("#card"+row);
        card.getChildren().add(image);
    }

    public static void addCardInSlot( int cardId, int slot, int row){
        InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +cardId+".png");
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
            playerPane.lookup("#activate"+cardId).setDisable(true);
        } else {
            InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +cardId+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(100);
            image.setPreserveRatio(true);

            AnchorPane card;
            if(playerPane.lookup("#lc1") != null){
                card = (AnchorPane) playerPane.lookup("#lc1");
                playerPane.lookup("#lc11").setId("lc"+cardId+"1");
                playerPane.lookup("#lc12").setId("lc"+cardId+"2");
            } else {
                card = (AnchorPane) playerPane.lookup("#lc2");
                playerPane.lookup("#lc21").setId("lc"+cardId+"1");
                playerPane.lookup("#lc22").setId("lc"+cardId+"2");
            }
            card.getChildren().add(image);
            card.setId(String.valueOf(cardId));
        }

    }

    public static void activateLeaderCard(int cardId){
        mainScene.getRoot().lookup("#activate"+cardId).setDisable(true);
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
        if(playerPane.lookup("#"+cardId) == null){
            playerPane.lookup("#lc"+cardId+"1").setVisible(false);
            playerPane.lookup("#lc"+cardId+"2").setVisible(false);
        }
    }

    public static void discardLeaderCard(int cardId){
        mainScene.getRoot().lookup("#activate"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#discard"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#lc"+cardId+"1").setVisible(false);
        mainScene.getRoot().lookup("#lc"+cardId+"2").setVisible(false);
    }

    public static void nextTurn(int index, int numberOfPlayers, boolean itsMyTurn){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 0; i < numberOfPlayers; i++){
            grid.getChildren().get(i).lookup("#calamaio").setVisible(false);
        }
        grid.getChildren().get(index).lookup("#calamaio").setVisible(true);
        ((MenuBar) ((Pane)mainScene.getRoot()).getChildren().get(0)).getMenus().get(0).setVisible(itsMyTurn);
    }

    public static void updatePopeFavourCards(int index, PopeFavorCardState[] states){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 0; i < states.length; i++){
            switch (states[i]){
                case FACEDOWN:
                    break;
                case FACEUP:
                    InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/check.png");
                    ImageView image = new ImageView();
                    image.setImage(new Image(in));
                    image.setFitWidth(31);
                    image.setPreserveRatio(true);
                    AnchorPane space = (AnchorPane) grid.getChildren().get(index).lookup("#pope"+(i+1));
                    space.getChildren().remove(0);
                    space.getChildren().add(image);
                    break;
                case DISABLED:
                    grid.getChildren().get(index).lookup("#pope"+(i+1)).setVisible(false);
                    break;
            }
        }
    }

    public static void updatePopeFavourCards(PopeFavorCardState[] states){
        for(int i = 0; i < states.length; i++){
            switch (states[i]){
                case FACEDOWN:
                    break;
                case FACEUP:
                    InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/tick.png");
                    ImageView image = new ImageView();
                    image.setImage(new Image(in));
                    image.setFitWidth(31);
                    image.setPreserveRatio(true);
                    AnchorPane space = (AnchorPane) mainScene.getRoot().lookup("#pope"+(i+1));
                    space.getChildren().remove(0);
                    space.getChildren().add(image);
                    break;
                case DISABLED:
                    mainScene.getRoot().lookup("#pope"+(i+1)).setVisible(false);
                    break;
            }
        }
    }

    public static void updateShelf(int index, int shelfNumber, HashMap<Resource, Integer> inside, GUI gui){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 1; i < shelfNumber + 1; i++){
            for(Map.Entry<Resource, Integer> shelf: inside.entrySet()){
                AnchorPane container = (AnchorPane) grid.getChildren().get(index).lookup("#shelf"+(shelfNumber)+(i));
                insertImagesOnShelf(container, i, shelf.getValue(), shelf.getKey());
            }
        }
        if(onContainersScene) gui.showDeposits();
    }

    public static void updateShelf(int shelfNumber, HashMap<Resource, Integer> inside, GUI gui){
        for(int i = 1; i < shelfNumber + 1; i++){
            AnchorPane container = (AnchorPane) mainScene.getRoot().lookup("#shelf"+(shelfNumber)+(i));
            for(Map.Entry<Resource, Integer> shelf: inside.entrySet()){
                insertImagesOnShelf(container, i, shelf.getValue(), shelf.getKey());
            }
        }
        if(onContainersScene) gui.showDeposits();
    }

    private static void insertImagesOnShelf(AnchorPane container, int i, int occupied, Resource resource){
        if(container.getChildren().size() > 0) container.getChildren().remove(0);
        if( i <= occupied){
            InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +resource.toString().toLowerCase()+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(18);
            image.setPreserveRatio(true);
            container.getChildren().add(image);
        }
    }

    public static void updateStrongbox(int index, HashMap<Resource, Integer> inside){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Region region = (Region) grid.getChildren().get(index).lookup("#strongbox");
        updateStrongboxTooltip(region, inside);
    }

    public static void updateStrongbox(HashMap<Resource, Integer> inside){
        Region region = (Region) mainScene.lookup("#strongbox");
        updateStrongboxTooltip(region, inside);
    }

    private static void updateStrongboxTooltip(Region region, HashMap<Resource, Integer> inside){
        Tooltip strongbox = new Tooltip();
        Pane tooltip = null;
        try {
            tooltip = FXMLLoader.load(Transition.class.getResource("/Scenes/strongboxPane.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Resource, Integer> entry: inside.entrySet()){
            ((Label)tooltip.lookup("#"+entry.getKey().toString().toLowerCase())).setText(entry.getValue().toString());
        }

        strongbox.setGraphic(tooltip);
        Tooltip.install(region, strongbox);
    }

    public static void updateLeaderStorage(int index, int cardId, Resource[] resources){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Pane player = (Pane) grid.getChildren().get(index);
        for(int i = 1; i < 3; i++){
            AnchorPane container = (AnchorPane) player.lookup("#lc"+(cardId)+(i));
            if(container.getChildren().size() > 0) container.getChildren().remove(0);
            if(resources.length >= i){
                InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +resources[i - 1].toString().toLowerCase()+".png");
                ImageView image = new ImageView();
                image.setImage(new Image(in));
                image.setFitWidth(34);
                image.setPreserveRatio(true);
                container.getChildren().add(image);
            }
        }
    }

    public static void updateLeaderStorage(int cardId, Resource[] resources){
        for(int i = 1; i < 3; i++){
            AnchorPane container = (AnchorPane) mainScene.getRoot().lookup("#lc"+(cardId)+(i));
            if(container.getChildren().size() > 0) container.getChildren().remove(0);
            if(resources.length >= i){
                InputStream in = Transition.class.getResourceAsStream("/accessible/assets/imgs/" +resources[i - 1].toString().toLowerCase()+".png");
                ImageView image = new ImageView();
                image.setImage(new Image(in));
                image.setFitWidth(86);
                image.setPreserveRatio(true);
                container.getChildren().add(image);
            }
        }
    }
}