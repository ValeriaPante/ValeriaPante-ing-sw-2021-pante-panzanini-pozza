package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;

public class LeaderCardScene extends ObservableByGUI{

    private Pane root;
    private static int count;

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
            InputStream in = getClass().getResourceAsStream("/Images/"+leaderCards.get(i)+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(200);
            image.setPreserveRatio(true);
            image.setId(String.valueOf(i));
            image.setOnMouseClicked(mouseEvent -> {
                int index = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId());
                //sendMessageToServer(MessageToServerCreator.createLeaderDiscardMessage(observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards().get(index)));
                Transition.updateLeaderCards(Math.max(index - count, 0));
                count++;
                if (count == 2){
                    observer.chooseInitialResources();
                }
            });
            gridPane.addColumn(i, image);
        }

    }

    public static int getCount() {
        return count;
    }

    public Pane getRoot() {
        return root;
    }
}
