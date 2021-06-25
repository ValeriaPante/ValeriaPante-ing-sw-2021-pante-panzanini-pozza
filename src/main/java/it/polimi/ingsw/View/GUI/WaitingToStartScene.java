package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.StartGameMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Scene displaying the lobby the player is in and its updates
 */
public class WaitingToStartScene extends ObservableByGUI{
    private Pane root;

    private static int lobbyId;
    private static boolean ready = false;

    public WaitingToStartScene(int lobbyId, String[] players){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/waitingToStartScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WaitingToStartScene.lobbyId = lobbyId;
        WaitingToStartScene.ready = true;

        for(int i = 0; i < players.length; i++){
            Label user = (Label) root.lookup("#user"+(i+1));
            user.setText(players[i]);
        }

        Button startButton = (Button) root.lookup("#startButton");
        startButton.setOnAction(event -> {
            new Thread(() -> this.sendMessage(new StartGameMessage())).start();
            Platform.runLater(() -> Transition.setLoadingScene(new LoadingScene()));
            Platform.runLater(Transition::toLoadingScene);
        });

        root.lookup("#back").setOnMouseClicked(mouseEvent -> {
            ready = false;
            Platform.runLater(() -> LobbiesScene.addLobby(lobbyId));
            LobbiesScene lobbiesScene = new LobbiesScene(observer);
            Platform.runLater(() -> Transition.setLobbiesScene(lobbiesScene));
            Platform.runLater(Transition::toLobbiesScene);
        });
    }

    public static int getLobbyId() {
        return lobbyId;
    }

    public static boolean isReady() {
        return ready;
    }

    public Pane getRoot() {
        return root;
    }
}
