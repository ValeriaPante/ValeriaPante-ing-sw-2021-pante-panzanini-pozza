package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.CardProductionSelectionMessage;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.ProductionActivationMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProductionScene extends PaymentScene{
    private static final int[] devCardOnTop = new int[5];

    public ProductionScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/productionScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button pay = (Button) root.lookup("#continue");
        pay.setOnAction(event -> {
            sendMessage(new ProductionActivationMessage());
            observer.toProductionState();
            Transition.hideDialog();
        });
    }

    @Override
    public void initialise(){
        root.lookup("#quit").setOnMouseClicked(mouseEvent -> Transition.hideDialog());

        initialiseButtons();
        AnchorPane card1 = (AnchorPane) root.lookup("#card1");
        root.lookup("#tick1").setVisible(false);
        if(devCardOnTop[0] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devCardOnTop[0]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[0]))).start();
                Node tick = root.lookup("#tick1");
                tick.setVisible(!tick.isVisible());
            });
            card1.getChildren().add(image);
        }

        AnchorPane card2 = (AnchorPane) root.lookup("#card2");
        root.lookup("#tick2").setVisible(false);
        if(devCardOnTop[1] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devCardOnTop[1]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[1]))).start();
                Node tick = root.lookup("#tick2");
                tick.setVisible(!tick.isVisible());
            });
            card2.getChildren().add(image);
        }

        AnchorPane card3 = (AnchorPane) root.lookup("#card3");
        root.lookup("#tick3").setVisible(false);
        if(devCardOnTop[2] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devCardOnTop[2]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[2]))).start();
                Node tick = root.lookup("#tick3");
                tick.setVisible(!tick.isVisible());
            });
            card3.getChildren().add(image);
        }

        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
        int availableSpace = 3;
        for (Integer integer : lc) {
            if (integer > 60 && integer < 65) {
                devCardOnTop[availableSpace] = integer;
                availableSpace++;
            }
        }

        AnchorPane card4 = (AnchorPane) root.lookup("#card4");
        root.lookup("#tick4").setVisible(false);
        if(devCardOnTop[3] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devCardOnTop[3]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[3]))).start();
                Node tick = root.lookup("#tick4");
                tick.setVisible(!tick.isVisible());
            });
            card4.getChildren().add(image);
        }

        AnchorPane card5 = (AnchorPane) root.lookup("#card5");
        root.lookup("#tick5").setVisible(false);
        if(devCardOnTop[4] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devCardOnTop[4]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[4]))).start();
                Node tick = root.lookup("#tick5");
                tick.setVisible(!tick.isVisible());
            });
            card5.getChildren().add(image);
        }

        root.lookup("#tick6").setVisible(false);
        root.lookup("#basic").setOnMouseClicked(mouseEvent -> {
            new Thread(() -> sendMessage(new CardProductionSelectionMessage(0))).start();
            Node tick = root.lookup("#tick6");
            tick.setVisible(!tick.isVisible());
        });
    }

    public static void setDevCardOnTop(int devSlot, int cardId){
        devCardOnTop[devSlot] = cardId;
    }
}
