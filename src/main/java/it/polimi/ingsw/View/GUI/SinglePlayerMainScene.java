package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.EndTurnMessage;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.LeaderCardActionMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Scene displaying the main panel with the board of the player (singleplayer case)
 */
public class SinglePlayerMainScene extends ObservableByGUI{

    private Pane root;
    private static final double[][] local = {
            {83,143}, {129,143}, {132, 105}, {130, 58}, {179,58}, {227,58}, {275,58},
            {325,58}, {374,58}, {374, 103}, {374,143}, {425,143}, {474,143}, {522,143},
            {572, 143}, {622,143}, {622,98}, {622,59}, {669,59}, {718,59}, {769,59},
            {817,59}, {865,59}, {916,59}
    };
    private static final double[][] lorenzo = {
            {100,143}, {153,143}, {156, 105}, {154, 58}, {203,58}, {251,58}, {299,58},
            {349,58}, {398,58}, {398, 103}, {398,143}, {449,143}, {498,143}, {546,143},
            {596, 143}, {646,143}, {646,98}, {646,59}, {693,59}, {742,59}, {793,59},
            {841,59}, {889,59}, {940,59}
    };

    public SinglePlayerMainScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/singlePlayerMainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        AnchorPane background = (AnchorPane) root.lookup("#background");
        ImageView backgroundImage = new ImageView();
        try {
            File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\colored.jpg");
            backgroundImage.setImage(new Image(fileInputStream));

        } catch(Exception e) {
        }
        backgroundImage.setFitWidth(1006.0);
        backgroundImage.setFitHeight(654.0);
        backgroundImage.setVisible(true);
        background.getChildren().add(backgroundImage);

        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
        AnchorPane card1 = (AnchorPane) root.lookup("#card1");
        AnchorPane card2 = (AnchorPane) root.lookup("#card2");
        card1.setId(lc.get(0).toString());
        card2.setId(lc.get(1).toString());
        ImageView image1 = new ImageView();
        ImageView image2 = new ImageView();
        try {
            File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            FileInputStream fileInputStream1 = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\"+lc.get(0)+".png");
            image1.setImage(new Image(fileInputStream1));
            FileInputStream fileInputStream2 = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\"+lc.get(1)+".png");
            image2.setImage(new Image(fileInputStream2));

        } catch(Exception e) {
        }
        image1.setFitWidth(190);
        image2.setFitWidth(190);
        image1.setPreserveRatio(true);
        image2.setPreserveRatio(true);
        card1.getChildren().add(image1);
        card2.getChildren().add(image2);

        for (int k = 1; k < 4; k++){
            InputStream in = getClass().getResourceAsStream("/constantAssets/pope" +k+".png");
            ImageView image = new ImageView();
            image.setImage(new Image(in));
            image.setFitWidth(68);
            image.setPreserveRatio(true);
            ((AnchorPane) root.lookup("#pope"+k)).getChildren().add(image);
        }

        root.lookup("#lc11").setId("lc"+lc.get(0)+"1");
        root.lookup("#lc12").setId("lc"+lc.get(0)+"2");
        root.lookup("#lc21").setId("lc"+lc.get(1)+"1");
        root.lookup("#lc22").setId("lc"+lc.get(1)+"2");

        Button activate1 = (Button) root.lookup("#activate1");
        activate1.setId(lc.get(0).toString());
        activate1.setOnAction(actionEvent -> {
            new Thread(() -> sendMessage(new LeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId().substring(8)),false))).start();
            actionEvent.consume();
        });
        activate1.setId("activate"+lc.get(0));
        Button discard1 = (Button) root.lookup("#discard1");
        discard1.setId(lc.get(0).toString());
        discard1.setOnAction(actionEvent -> {
            new Thread(() -> sendMessage(new LeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId().substring(7)),true))).start();
            actionEvent.consume();
        });
        discard1.setId("discard"+lc.get(0));
        Button activate2 = (Button) root.lookup("#activate2");
        activate2.setId(lc.get(1).toString());
        activate2.setOnAction(actionEvent -> {
            new Thread(() -> sendMessage(new LeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId().substring(8)),false))).start();
            actionEvent.consume();
        });
        activate2.setId("activate"+lc.get(1));
        Button discard2 = (Button) root.lookup("#discard2");
        discard2.setId(lc.get(1).toString());
        discard2.setOnAction(actionEvent -> {
            new Thread(() -> sendMessage(new LeaderCardActionMessage(Integer.parseInt(((Button) actionEvent.getSource()).getId().substring(7)),true))).start();
            actionEvent.consume();
        });
        discard2.setId("discard"+lc.get(1));

        Region strongbox = (Region) root.lookup("#strongbox");
        Tooltip inside = new Tooltip();
        Pane tooltip = null;
        try {
            tooltip = FXMLLoader.load(getClass().getResource("/Scenes/strongboxPane.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Label)tooltip.lookup("#coin")).setText("0");
        ((Label)tooltip.lookup("#shield")).setText("0");
        ((Label)tooltip.lookup("#stone")).setText("0");
        ((Label)tooltip.lookup("#servant")).setText("0");
        inside.setGraphic(tooltip);
        Tooltip.install(strongbox, inside);

        MenuBar menuBar = (MenuBar) root.getChildren().get(0);
        Menu menu = menuBar.getMenus().get(0);
        menu.getItems().get(0).setOnAction(actionEvent -> observer.showMarket());
        menu.getItems().get(1).setOnAction(actionEvent -> observer.showDevDecks());
        menu.getItems().get(2).setOnAction(actionEvent -> observer.activateProduction());
        menu.getItems().get(3).setOnAction(actionEvent -> {
            Transition.setOnContainersScene(false);
            new Thread(() -> sendMessage(new EndTurnMessage())).start();
        });
    }

    public Pane getRoot() {
        return root;
    }

    public static double[][] getPlayerPositions() {
        return local;
    }

    public static double[][] getLorenzoPositions() {
        return lorenzo;
    }
}
