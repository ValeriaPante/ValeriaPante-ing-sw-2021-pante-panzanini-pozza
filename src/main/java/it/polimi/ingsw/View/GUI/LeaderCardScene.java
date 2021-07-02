package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.LeaderDiscardMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;

/**
 * Scene displaying initial leader cards from which the player needs to choose
 */
public class LeaderCardScene extends ObservableByGUI{

    private Pane root;
    private int discarded;

    public LeaderCardScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/leaderCardScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane gridPane = (GridPane) root.lookup("#gridPane");
        ArrayList<Integer> leaderCards = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
        for(int i = 0; i  < leaderCards.size(); i++){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +leaderCards.get(i)+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception ignored) {
            }
            image.setFitWidth(200);
            image.setPreserveRatio(true);
            image.setId(String.valueOf(leaderCards.get(i)));
            image.setOnMouseClicked(mouseEvent -> {
                int cardId = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId());
                ((ImageView) mouseEvent.getSource()).setVisible(false);
                observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).removeLeaderCard(cardId);
                discarded++;
                //Transition.updateLeaderCards(Math.max(index - discarded.size(), 0));
                if (discarded == 2){
                    observer.setGamePhase(1);
                    if(observer.getModel().getNumberOfPlayers() != 1 ){
                        Platform.runLater(() -> Transition.setLoadingScene(new LoadingScene()));
                        Platform.runLater(Transition::toLoadingScene);
                    }
                }
                new Thread(() -> sendMessage(new LeaderDiscardMessage(cardId))).start();
                if(observer.getModel().getNumberOfPlayers() == 1 ) Platform.runLater(() -> Transition.setMainScene(new SinglePlayerMainScene(observer)));
                else Platform.runLater(() -> Transition.setMainScene(new MainScene(observer)));
            });
            gridPane.addColumn(i, image);
        }

    }

    public Pane getRoot() {
        return root;
    }
}
