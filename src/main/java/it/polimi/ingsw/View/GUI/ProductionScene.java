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

/**
 * Scene displaying all the production powers that the player can activate clicking on them
 * and the resources he needs to select as input of the powers
 */
public class ProductionScene extends PaymentScene{
    private static final int[] devCardOnTop = new int[5];
    private static Node[] ticks = new Node[6];
    private static Node quit;

    public ProductionScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/productionScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button pay = (Button) root.lookup("#continue");
        pay.setOnAction(event -> {
            observer.toProductionState();
            Transition.hideDialog();
            sendMessage(new ProductionActivationMessage());
        });
    }

    @Override
    public void initialise(){

        quit = root.lookup("#quit");
        root.lookup("#quit").setOnMouseClicked(mouseEvent -> {
            Transition.hideDialog();
        });

        initialiseButtons();
        AnchorPane card1 = (AnchorPane) root.lookup("#card1");
        ticks[0] = root.lookup("#tick1");
        root.lookup("#tick1").setVisible(false);
        if(devCardOnTop[0] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +devCardOnTop[0]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[0]))).start();
                Node tick = root.lookup("#tick1");
                tick.setVisible(!tick.isVisible());
                quit.setVisible(false);
            });
            card1.getChildren().add(image);
        }

        AnchorPane card2 = (AnchorPane) root.lookup("#card2");
        ticks[1] = root.lookup("#tick2");
        root.lookup("#tick2").setVisible(false);
        if(devCardOnTop[1] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +devCardOnTop[1]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[1]))).start();
                Node tick = root.lookup("#tick2");
                tick.setVisible(!tick.isVisible());
                quit.setVisible(false);
            });
            card2.getChildren().add(image);
        }

        AnchorPane card3 = (AnchorPane) root.lookup("#card3");
        ticks[2] = root.lookup("#tick3");
        root.lookup("#tick3").setVisible(false);
        if(devCardOnTop[2] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +devCardOnTop[2]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[2]))).start();
                Node tick = root.lookup("#tick3");
                tick.setVisible(!tick.isVisible());
                quit.setVisible(false);
            });
            card3.getChildren().add(image);
        }

        AnchorPane card4 = (AnchorPane) root.lookup("#card4");
        ticks[3] = root.lookup("#tick4");
        root.lookup("#tick4").setVisible(false);
        if(devCardOnTop[3] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +devCardOnTop[3]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[3]))).start();
                Node tick = root.lookup("#tick4");
                tick.setVisible(!tick.isVisible());
                quit.setVisible(false);
            });
            card4.getChildren().add(image);
        }

        AnchorPane card5 = (AnchorPane) root.lookup("#card5");
        ticks[4] = root.lookup("#tick5");
        root.lookup("#tick5").setVisible(false);
        if(devCardOnTop[4] != 0){
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator +"assets"+ File.separator +"imgs"+ File.separator +devCardOnTop[4]+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(150);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                new Thread(() -> sendMessage(new CardProductionSelectionMessage(devCardOnTop[4]))).start();
                Node tick = root.lookup("#tick5");
                tick.setVisible(!tick.isVisible());
                quit.setVisible(false);
            });
            card5.getChildren().add(image);
        }

        ticks[5] = root.lookup("#tick6");
        root.lookup("#tick6").setVisible(false);
        root.lookup("#basic").setOnMouseClicked(mouseEvent -> {
            new Thread(() -> sendMessage(new CardProductionSelectionMessage(0))).start();
            Node tick = root.lookup("#tick6");
            tick.setVisible(!tick.isVisible());
            quit.setVisible(false);
        });
    }

    public static void setDevCardOnTop(int devSlot, int cardId){
        devCardOnTop[devSlot] = cardId;
    }

    public static void setActiveLeaderCard(int cardId){
        if (cardId > 60 && cardId < 65) {
            if (devCardOnTop[3] == 0) devCardOnTop[3] = cardId;
            else devCardOnTop[4] = cardId;
        }
    }

    public static void removeDiscardedLeaderCard(int cardId){
        if(devCardOnTop[3] == cardId) devCardOnTop[3] = 0;
        else if (devCardOnTop[4] == cardId) devCardOnTop[4] = 0;
    }

    public static void deselectIfSelected(int cardId){
        if(ticks != null){
            if(cardId == 0){
                ticks[5].setVisible(false);
            } else {
                for(int i = 0; i < devCardOnTop.length - 1; i++){
                    if(devCardOnTop[i] == cardId){
                        ticks[i].setVisible(false);
                    }
                }
            }

            for(int i = 0; i < ticks.length; i++){
                if(ticks[i] != null && ticks[i].isVisible()) return;
            }

            if(quit != null) quit.setVisible(true);
        }
    }

}
