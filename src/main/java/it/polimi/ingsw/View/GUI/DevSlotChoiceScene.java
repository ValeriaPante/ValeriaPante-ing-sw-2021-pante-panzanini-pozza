package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.ChooseDevSlotMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Scene that displays the slots from which player need to choose the one where he wants to put his development card
 */
public class DevSlotChoiceScene extends Initializable{

    public DevSlotChoiceScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/devSlotChoiceScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialise(){
        root.lookup("#slot1").setOnMouseClicked(mouseEvent -> {
            new Thread(() -> sendMessage(new ChooseDevSlotMessage(1))).start();
            observer.getCurrentState().next();
        });
        root.lookup("#slot2").setOnMouseClicked(mouseEvent -> {
            new Thread(() -> sendMessage(new ChooseDevSlotMessage(2))).start();
            observer.getCurrentState().next();
        });
        root.lookup("#slot3").setOnMouseClicked(mouseEvent -> {
            new Thread(() -> sendMessage(new ChooseDevSlotMessage(3))).start();
            observer.getCurrentState().next();
        });

        int[][] devCards = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getDevSlots();
        for(int i = 0; i < devCards.length; i++)
            for (int j = 0; j < devCards[i].length; j++){
                if(devCards[i][j] != 0){
                    AnchorPane card = (AnchorPane) root.lookup("#card"+(j+3*i));
                    ImageView image = new ImageView();
                    try {
                        File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                        FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + File.separator + "assets"+ File.separator +"imgs"+ File.separator +devCards[i][j]+".png");
                        image.setImage(new Image(fileInputStream));

                    } catch(Exception e) {
                    }
                    image.setFitWidth(150);
                    image.setPreserveRatio(true);
                    card.getChildren().add(image);
                } else break;
            }
    }

}
