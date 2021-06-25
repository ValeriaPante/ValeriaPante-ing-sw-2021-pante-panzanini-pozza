package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.TransmutationMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Scene where the player needs to choose how to transmute his white marbles
 */
public class TransmutationScene extends Initializable{

    public TransmutationScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/transmutationScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialise(){

        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();

        for(int i = 0; i < 2; i++){
            AnchorPane card = (AnchorPane) root.getChildren().get(i);
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +lc.get(i)+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(255);
            image.setPreserveRatio(true);
            card.getChildren().add(image);
        }

        Spinner<Integer> spinner1 = new Spinner<>();
        spinner1.setLayoutX(86.0);
        spinner1.setLayoutY(500.0);
        Spinner<Integer> spinner2 = new Spinner<>();
        spinner2.setLayoutX(364.0);
        spinner2.setLayoutY(500.0);
        SpinnerValueFactory<Integer> spinnerValueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,12);
        spinner1.setValueFactory(spinnerValueFactory1);
        SpinnerValueFactory<Integer> spinnerValueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,12);
        spinner2.setValueFactory(spinnerValueFactory2);

        root.getChildren().add(spinner1);
        root.getChildren().add(spinner2);

        Button sendButton = (Button) root.lookup("#buyButton");
        sendButton.setOnAction(event -> {
            new Thread(() -> sendMessage(new TransmutationMessage(lc.get(0), lc.get(1), spinner1.getValue(), spinner2.getValue()))).start();
            Platform.runLater(Transition::hideDialog);
        });


    }
}
