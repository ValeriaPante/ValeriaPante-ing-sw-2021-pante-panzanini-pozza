package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

        AnchorPane card1 = (AnchorPane) root.getChildren().get(1);
        AnchorPane card2 = (AnchorPane) root.getChildren().get(2);
        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();

        InputStream in1 = getClass().getResourceAsStream("/Images/"+lc.get(0)+".png");
        ImageView image1 = new ImageView();
        image1.setImage(new Image(in1));
        image1.setFitWidth(255);
        image1.setPreserveRatio(true);
        card1.getChildren().add(image1);

        InputStream in2 = getClass().getResourceAsStream("/Images/"+lc.get(1)+".png");
        ImageView image2 = new ImageView();
        image2.setImage(new Image(in2));
        image2.setFitWidth(255);
        image2.setPreserveRatio(true);
        card2.getChildren().add(image2);

        Spinner<Integer> spinner1 = (Spinner<Integer>) root.getChildren().get(3);
        Spinner<Integer> spinner2 = (Spinner<Integer>) root.getChildren().get(4);
        SpinnerValueFactory<Integer> spinnerValueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,12);
        spinner1.setValueFactory(spinnerValueFactory1);
        SpinnerValueFactory<Integer> spinnerValueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,12);
        spinner2.setValueFactory(spinnerValueFactory2);

        Button sendButton = (Button) root.lookup("#buyButton");
        sendButton.setOnAction(event -> {
            sendMessageToServer(MessageToServerCreator.createTransmutationMessage(lc.get(0), lc.get(1), spinner1.getValue(), spinner2.getValue()));
            Transition.hideDialog();
        });


    }
}
