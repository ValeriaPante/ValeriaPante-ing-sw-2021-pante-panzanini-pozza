package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class WelcomeScene extends ObservableByGUI{
    private Pane root;

    public WelcomeScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/welcomeScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button playButton = (Button) root.lookup("#startButton");
        playButton.setOnAction(event -> {
            UsernameScene usernameScene = new UsernameScene();
            usernameScene.addObserver(this.observer);
            Transition.setUsernameScene(usernameScene);
            Transition.toUsernameScene();
        });
    }

    public Pane getRoot() {
        return root;
    }
}
