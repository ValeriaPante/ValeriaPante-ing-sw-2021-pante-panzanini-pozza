package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class UsernameScene extends ObservableByGUI {

    private Pane root;

    public UsernameScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/usernameScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button sendButton = (Button) root.lookup("#sendButton");
        sendButton.setOnAction(event -> {
            TextField ip = (TextField) root.lookup("#ipAddress");
            TextField port = (TextField) root.lookup("#port");
            TextField username = (TextField) root.lookup("#usernameBox");
            this.connect(ip.getText(), port.getText(), username.getText());
            LobbiesScene lobbiesScene = new LobbiesScene(this.observer);
            Transition.setLobbiesScene(lobbiesScene);
            Transition.toLobbiesScene();
        });

    }

    public Pane getRoot() {
        return root;
    }
}
