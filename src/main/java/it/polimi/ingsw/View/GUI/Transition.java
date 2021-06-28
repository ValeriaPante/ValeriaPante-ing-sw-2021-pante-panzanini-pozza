package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.DisconnectMessage;
import it.polimi.ingsw.Network.Client.MessageManager;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to change current scene or to transition from a scene to another
 */
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
        dialogStage.setScene(new Scene(scene));
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
        if(dialogStage == null) {
            dialogStage = new Stage();
            dialogStage.setResizable(false);
        }

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

    public static void showLorenzoMove(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    public static void setOnContainersScene(boolean onContainersScene){
        Transition.onContainersScene = onContainersScene;
    }

    public static boolean isOnContainersScene(){
        return Transition.onContainersScene;
    }

    /**
     * Removes from view a leader card player chose from his inital ones
     * @param index index of the card
     */
    public static void updateLeaderCards(int index){
        GridPane gridPane = (GridPane) leaderCardsScene.getRoot().lookup("#gridPane");
        gridPane.getChildren().remove(index);
        gridPane.add(new Pane(),index,0);
    }

    /**
     * Adds a card in a slot (multiplayer case)
     * @param playerIndex index of the player to which apply the update
     * @param cardId id of the card to insert
     * @param slot number of slot where to put the card
     * @param row row on the slot
     */
    public static void addCardInSlot(int playerIndex, int cardId, int slot, int row){
        ImageView image = new ImageView();
        try {
            File fullPath = new File(Transition.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +cardId+".png");
            image.setImage(new Image(fileInputStream));

        } catch(Exception e) {
        }
        image.setFitWidth(50);
        image.setPreserveRatio(true);
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        AnchorPane card = (AnchorPane) grid.getChildren().get(playerIndex).lookup("#slot"+slot).lookup("#card"+row);
        card.getChildren().add(image);
    }

    /**
     * Adds a card in a slot (single player case)
     * @param cardId id of the card to insert
     * @param slot number of slot where to put the card
     * @param row row on the slot
     */
    public static void addCardInSlot( int cardId, int slot, int row){
        ImageView image = new ImageView();
        try {
            File fullPath = new File(Transition.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +cardId+".png");
            image.setImage(new Image(fileInputStream));

        } catch(Exception e) {
        }
        image.setFitWidth(150);
        image.setPreserveRatio(true);
        AnchorPane card = (AnchorPane) mainScene.getRoot().lookup("#slot"+slot).lookup("#card"+row);
        card.getChildren().add(image);
    }

    /**
     * Updates faith marker (multiplayer case)
     * @param index index of the player to which apply the update
     * @param position new position
     */
    public static void updatePosition(int index, int position){
        if(position != 0){
            GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
            double[] newPos = MainScene.getPositions()[position - 1];
            grid.getChildren().get(index).lookup("#faith").setLayoutX(newPos[0]);
            grid.getChildren().get(index).lookup("#faith").setLayoutY(newPos[1]);
        }
    }

    /**
     * Updates faith marker (singleplayer case)
     * @param isLorenzo indicates if the updates concerns Lorenzo Il Magnifico or not
     * @param position new position
     */
    public static void updatePosition(boolean isLorenzo, int position){
        if(position != 0){
            if(isLorenzo){
                double[] newPos = SinglePlayerMainScene.getLorenzoPositions()[position - 1];
                mainScene.getRoot().lookup("#lorenzo").setLayoutX(newPos[0]);
                mainScene.getRoot().lookup("#lorenzo").setLayoutY(newPos[1]);
            } else {
                double[] newPos = SinglePlayerMainScene.getPlayerPositions()[position - 1];
                mainScene.getRoot().lookup("#faith").setLayoutX(newPos[0]);
                mainScene.getRoot().lookup("#faith").setLayoutY(newPos[1]);
            }
        }
    }

    /**
     * Disable activate button if player is local, otherwise turns the card up (multiplayer case)
     * @param index index of the player to which apply the update
     * @param cardId id of the activated leader card
     * @param isLocalPlayer indicates if the player to which apply the update is the local one
     */
    public static void activateLeaderCard(int index, int cardId, boolean isLocalPlayer){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Node playerPane = grid.getChildren().get(index);
        if(isLocalPlayer){
            playerPane.lookup("#activate"+cardId).setDisable(true);
        } else {
            ImageView image = new ImageView();
            try {
                File fullPath = new File(Transition.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +cardId+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
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

    /**
     * Disable activate button if player (singleplayer case)
     * @param cardId id of the activated leader card
     */
    public static void activateLeaderCard(int cardId){
        mainScene.getRoot().lookup("#activate"+cardId).setDisable(true);
    }

    /**
     * Removes discarded card (multiplayer case)
     * @param index index of the player to which apply the update
     * @param cardId id of the discarded leader card
     * @param isLocalPlayer indicates if the player to which apply the update is the local one
     */
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

    /**
     * Removes discarded card (single player case)
     * @param cardId id of the discarded leader card
     */
    public static void discardLeaderCard(int cardId){
        mainScene.getRoot().lookup("#activate"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#discard"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#"+cardId).setVisible(false);
        mainScene.getRoot().lookup("#lc"+cardId+"1").setVisible(false);
        mainScene.getRoot().lookup("#lc"+cardId+"2").setVisible(false);
    }

    /**
     * Updates the position of the inkwell; if it's the turn of the local player,
     * it abilitate the menu, otherwise it disables it
     * @param index index of the player to which apply the update
     * @param numberOfPlayers number of players
     * @param itsMyTurn indicates if the turn is the local players's one
     */
    public static void nextTurn(int index, int numberOfPlayers, boolean itsMyTurn){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 0; i < numberOfPlayers; i++){
            grid.getChildren().get(i).lookup("#calamaio").setVisible(false);
        }
        grid.getChildren().get(index).lookup("#calamaio").setVisible(true);
        ((MenuBar) ((Pane)mainScene.getRoot()).getChildren().get(0)).getMenus().get(0).setVisible(itsMyTurn);
    }

    /**
     * Updates the images for each pope favour card (multiplayer case)
     * @param index index of the player to which apply the update
     * @param states new states of the cards
     */
    public static void updatePopeFavourCards(int index, PopeFavorCardState[] states){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 0; i < states.length; i++){
            switch (states[i]){
                case FACEDOWN:
                    break;
                case FACEUP:
                    InputStream in = Transition.class.getResourceAsStream("/constantAssets/check.png");
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

    /**
     * Updates the images for each pope favour card (single player case)
     * @param states new states of the cards
     */
    public static void updatePopeFavourCards(PopeFavorCardState[] states){
        for(int i = 0; i < states.length; i++){
            switch (states[i]){
                case FACEDOWN:
                    break;
                case FACEUP:
                    InputStream in = Transition.class.getResourceAsStream("/constantAssets/check.png");
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

    /**
     * Updates the content of a shelf (multiplayer case)
     * @param index index of the player to which apply the update
     * @param shelfNumber number of shelf
     * @param inside new content
     * @param gui view
     */
    public static void updateShelf(int index, int shelfNumber, HashMap<Resource, Integer> inside, GUI gui){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        for(int i = 1; i < shelfNumber + 1; i++){
            for(Map.Entry<Resource, Integer> shelf: inside.entrySet()){
                AnchorPane container = (AnchorPane) grid.getChildren().get(index).lookup("#shelf"+(shelfNumber)+(i));
                insertImagesOnShelf(container, i, shelf.getValue(), shelf.getKey(), 18);
            }
        }
        if(onContainersScene) gui.showDeposits();
    }

    /**
     * Updates the content of a shelf (single player case)
     * @param shelfNumber number of shelf
     * @param inside new content
     * @param gui view
     */
    public static void updateShelf(int shelfNumber, HashMap<Resource, Integer> inside, GUI gui){
        for(int i = 1; i < shelfNumber + 1; i++){
            AnchorPane container = (AnchorPane) mainScene.getRoot().lookup("#shelf"+(shelfNumber)+(i));
            for(Map.Entry<Resource, Integer> shelf: inside.entrySet()){
                insertImagesOnShelf(container, i, shelf.getValue(), shelf.getKey(),30);
            }
        }
        if(onContainersScene) gui.showDeposits();
    }

    /**
     * Insert a new image of the resource
     * @param container container of the image
     * @param resourcePosition position of the resource on the shelf
     * @param occupied number of resource contained on the shelf
     * @param resource type of resource contained
     */
    private static void insertImagesOnShelf(AnchorPane container, int resourcePosition, int occupied, Resource resource, int size){
        if(container.getChildren().size() > 0) container.getChildren().remove(0);
        if( resourcePosition <= occupied){
            InputStream in = Transition.class.getResourceAsStream("/constantAssets/" +resource.toString().toLowerCase()+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(size);
            image.setPreserveRatio(true);
            container.getChildren().add(image);
        }
    }

    /**
     * Updates the content of the strongbox (multiplayer case)
     * @param index index of the player to which apply the update
     * @param inside strongbox content
     */
    public static void updateStrongbox(int index, HashMap<Resource, Integer> inside){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Region region = (Region) grid.getChildren().get(index).lookup("#strongbox");
        updateStrongboxTooltip(region, inside);
    }

    /**
     * Updates the content of the strongbox (single player case)
     * @param inside strongbox content
     */
    public static void updateStrongbox(HashMap<Resource, Integer> inside){
        Region region = (Region) mainScene.lookup("#strongbox");
        updateStrongboxTooltip(region, inside);
    }

    /**
     * Updates the tooltip showing the inside of the strongbox
     * @param region region containing the tooltip
     * @param inside strongbox content
     */
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

    /**
     * Updates leader storage content (multiplayer case)
     * @param index index of the player to which apply the update
     * @param cardId id of the leader card with storage ability
     * @param resources storage content
     */
    public static void updateLeaderStorage(int index, int cardId, Resource[] resources){
        GridPane grid = (GridPane) ((Pane) mainScene.getRoot()).getChildren().get(1);
        Pane player = (Pane) grid.getChildren().get(index);
        for(int i = 1; i < 3; i++){
            AnchorPane container = (AnchorPane) player.lookup("#lc"+(cardId)+(i));
            if(container.getChildren().size() > 0) container.getChildren().remove(0);
            if(resources.length >= i){
                InputStream in = Transition.class.getResourceAsStream("/constantAssets/" +resources[i - 1].toString().toLowerCase()+".png");
                ImageView image = new ImageView();
                image.setImage(new Image(in));
                image.setFitWidth(34);
                image.setPreserveRatio(true);
                container.getChildren().add(image);
            }
        }
    }

    /**
     * Updates leader storage content (singleplayer case)
     * @param cardId id of the leader card with storage ability
     * @param resources storage content
     */
    public static void updateLeaderStorage(int cardId, Resource[] resources){
        for(int i = 1; i < 3; i++){
            AnchorPane container = (AnchorPane) mainScene.getRoot().lookup("#lc"+(cardId)+(i));
            if(container.getChildren().size() > 0) container.getChildren().remove(0);
            if(resources.length >= i){
                InputStream in = Transition.class.getResourceAsStream("/constantAssets/" +resources[i - 1].toString().toLowerCase()+".png");
                ImageView image = new ImageView();
                image.setImage(new Image(in));
                image.setFitWidth(86);
                image.setPreserveRatio(true);
                container.getChildren().add(image);
            }
        }
    }

    /**
     * Deselects a card in the DevDecksScen or in the ProductionScene
     * @param cardId id of the card to deselect
     */
    public static void deselectCardIfSelected(int cardId){
        DevDecksScene.deselectIfSelected(cardId);
        ProductionScene.deselectIfSelected(cardId);
    }

}
