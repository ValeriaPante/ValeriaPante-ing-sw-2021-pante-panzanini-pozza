package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SinglePlayerMainScene extends ObservableByGUI{

    private Pane root;

    public SinglePlayerMainScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/singlePlayerMainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
        AnchorPane card1 = (AnchorPane) root.lookup("#card1");
        AnchorPane card2 = (AnchorPane) root.lookup("#card2");
        InputStream in1 = getClass().getResourceAsStream("/Images/"+lc.get(0)+".png");
        InputStream in2 = getClass().getResourceAsStream("/Images/"+lc.get(1)+".png");
        ImageView image1 = new ImageView();
        ImageView image2 = new ImageView();
        image1.setImage(new Image(in1));
        image2.setImage(new Image(in2));
        image1.setFitWidth(190);
        image2.setFitWidth(190);
        image1.setPreserveRatio(true);
        image2.setPreserveRatio(true);
        card1.getChildren().add(image1);
        card2.getChildren().add(image2);

        Button activate1 = (Button) root.lookup("#activate1");
        activate1.setId(lc.get(0).toString());
        activate1.setOnAction(actionEvent -> {
            //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),false));
            actionEvent.consume();
        });
        Button discard1 = (Button) root.lookup("#discard1");
        discard1.setId(lc.get(0).toString());
        discard1.setOnAction(actionEvent -> {
            //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),true));
            actionEvent.consume();
        });
        Button activate2 = (Button) root.lookup("#activate2");
        activate2.setId(lc.get(1).toString());
        activate2.setOnAction(actionEvent -> {
            //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),false));
            actionEvent.consume();
        });
        Button discard2 = (Button) root.lookup("#discard2");
        discard2.setId(lc.get(1).toString());
        discard2.setOnAction(actionEvent -> {
            //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),true));
            actionEvent.consume();
        });




        MenuBar menuBar = (MenuBar) root.getChildren().get(0);
        Menu menu = menuBar.getMenus().get(0);
        menu.getItems().get(0).setOnAction(actionEvent -> {
            observer.showMarket();
        });
        menu.getItems().get(1).setOnAction(actionEvent -> {
            observer.showDevDecks();
        });
    }

    public Pane getRoot() {
        return root;
    }
}
