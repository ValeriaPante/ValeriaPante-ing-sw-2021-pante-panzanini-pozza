package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainScene extends ObservableByGUI{
    private Pane root;
    private static final double[][] positions = {
            {34,52}, {57,52}, {59, 33}, {58, 14}, {81,14}, {103,14}, {125,14},
            {145,14}, {170,14}, {170, 33}, {170,54}, {191,54}, {217,54}, {240,54},
            {263, 54}, {285,54}, {285,31}, {285,14}, {308,14}, {332,14}, {354,14},
            {376,14}, {398,14}, {421,14}
    };

    public MainScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/mainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane gridPane = (GridPane) root.lookup("#grid");
        Pane player = null;

        for(int i = 0; i < observer.getModel().getNumberOfPlayers(); i++){
            if(observer.getModel().getLocalPlayerIndex() == i){
                try {
                    player= FXMLLoader.load(getClass().getResource("/Scenes/localPlayerPane.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
                Pane grid = (Pane) player.lookup("#pane");
                AnchorPane card1 = (AnchorPane) grid.lookup("#card1");
                AnchorPane card2 = (AnchorPane) grid.lookup("#card2");
                InputStream in1 = getClass().getResourceAsStream("/Images/"+lc.get(0)+".png");
                InputStream in2 = getClass().getResourceAsStream("/Images/"+lc.get(1)+".png");
                ImageView image1 = new ImageView();
                ImageView image2 = new ImageView();
                image1.setImage(new Image(in1));
                image2.setImage(new Image(in2));
                image1.setFitWidth(85);
                image2.setFitWidth(85);
                image1.setPreserveRatio(true);
                image2.setPreserveRatio(true);
                card1.getChildren().add(image1);
                card2.getChildren().add(image2);
                gridPane.add(player, i/2, i%2);

                Button activate1 = (Button) player.lookup("#activate1");
                activate1.setId(lc.get(0).toString());
                activate1.setOnAction(actionEvent -> {
                    //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),false));
                });
                Button discard1 = (Button) player.lookup("#discard1");
                discard1.setId(lc.get(0).toString());
                discard1.setOnAction(actionEvent -> {
                    //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),true));
                    ((Button) actionEvent.getSource()).setDisable(true);
                });
                Button activate2 = (Button) player.lookup("#activate2");
                activate2.setId(lc.get(1).toString());
                activate2.setOnAction(actionEvent -> {
                    //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),false));
                });
                Button discard2 = (Button) player.lookup("#discard2");
                discard2.setId(lc.get(1).toString());
                discard2.setOnAction(actionEvent -> {
                    //sendMessageToServer(MessageToServerCreator.createLeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId()),true));
                    ((Button) actionEvent.getSource()).setDisable(true);
                });
            } else {
                try {
                    player= FXMLLoader.load(getClass().getResource("/Scenes/playerPane.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GridPane grid2 = (GridPane) player.lookup("#grid");
                InputStream in3 = getClass().getResourceAsStream("/Images/retro.jpg");
                InputStream in4 = getClass().getResourceAsStream("/Images/retro.jpg");
                ImageView image3 = new ImageView();
                ImageView image4 = new ImageView();
                image3.setImage(new Image(in3));
                image4.setImage(new Image(in4));
                image3.setFitWidth(100);
                image4.setFitWidth(100);
                image3.setPreserveRatio(true);
                image4.setPreserveRatio(true);
                grid2.add(image3, 0, 0);
                grid2.add(image4, 0,1);
                gridPane.add(player,i/2, i%2);
            }

        }

        MenuBar menuBar = (MenuBar) root.getChildren().get(0);
        Menu menu = menuBar.getMenus().get(0);
        menu.getItems().get(0).setOnAction(actionEvent -> observer.showMarket());
        menu.getItems().get(1).setOnAction(actionEvent -> observer.showDevDecks());
        menu.getItems().get(2).setOnAction(actionEvent -> observer.activateProduction());
    }

    public Pane getRoot() {
        return root;
    }

    public static double[][] getPositions() {
        return positions;
    }
}
