package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.InputStream;

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
            //sendMessageToServer(MessageToServerCreator.createChooseDevSlotMessage(1));
            observer.getCurrentState().next();
        });
        root.lookup("#slot2").setOnMouseClicked(mouseEvent -> {
            //sendMessageToServer(MessageToServerCreator.createChooseDevSlotMessage(2));
            observer.getCurrentState().next();
        });
        root.lookup("#slot3").setOnMouseClicked(mouseEvent -> {
            //sendMessageToServer(MessageToServerCreator.createChooseDevSlotMessage(3));
            observer.getCurrentState().next();
        });

        int[][] devCards = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getDevSlots();
        for(int i = 0; i < devCards.length; i++)
            for (int j = 0; j < devCards[i].length; j++){
                if(devCards[i][j] != 0){
                    AnchorPane card = (AnchorPane) root.lookup("#card"+(j+3*i));
                    InputStream in = getClass().getResourceAsStream("/Images/"+devCards[i][j]+".png");
                    ImageView image = new ImageView();
                    image.setImage(new Image(in));
                    image.setFitWidth(150);
                    image.setPreserveRatio(true);
                    card.getChildren().add(image);
                } else break;
            }
    }

}
