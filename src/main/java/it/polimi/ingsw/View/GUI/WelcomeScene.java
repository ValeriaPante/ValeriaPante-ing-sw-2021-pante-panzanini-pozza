package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.LocalMessageManager;
import it.polimi.ingsw.Network.Client.MessageToServerManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Scene displaying game logo, where player can choose if to play online or offline
 */
public class WelcomeScene extends ObservableByGUI{
    private Pane root;

    public WelcomeScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/welcomeScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button onlineButton = (Button) root.lookup("#startOnline");
        onlineButton.setOnAction(event -> {
            observer.setMessageManager(new MessageToServerManager(observer));
            UsernameScene usernameScene = new UsernameScene();
            usernameScene.addObserver(this.observer);
            Platform.runLater(() -> Transition.setUsernameScene(usernameScene));
            Platform.runLater(Transition::toUsernameScene);
        });

        Button offlineButton = (Button) root.lookup("#startOffline");
        offlineButton.setOnAction(event -> {
            observer.setMessageManager(new LocalMessageManager(observer));
            //Platform.runLater(() -> Transition.setLoadingScene(new LoadingScene()));
            //Platform.runLater(Transition::toLoadingScene);
        });
    }

    public Pane getRoot() {
        return root;
    }
}
