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
    private ArrayList<Integer> discarded = new ArrayList<>();

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
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +leaderCards.get(i)+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(200);
            image.setPreserveRatio(true);
            image.setId(String.valueOf(i));
            image.setOnMouseClicked(mouseEvent -> {
                int index = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId());
                int cardId = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards().get(index);
                new Thread(() -> sendMessage(new LeaderDiscardMessage(cardId))).start();
                discarded.add(cardId);
                Transition.updateLeaderCards(Math.max(index - discarded.size(), 0));
                if (discarded.size() == 2){
                    for(int card: discarded)
                        observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).removeLeaderCard(card);
                    observer.setGamePhase(1);
                    Platform.runLater(() -> Transition.setLoadingScene(new LoadingScene()));
                    Platform.runLater(Transition::toLoadingScene);
                }
            });
            gridPane.addColumn(i, image);
        }

    }

    public Pane getRoot() {
        return root;
    }
}
