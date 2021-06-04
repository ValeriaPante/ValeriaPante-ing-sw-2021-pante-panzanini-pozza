package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LobbiesScene extends ObservableByGUI{
    private static final ArrayList<Integer> orderedLobbies = new ArrayList<>();
    private static Pane root;

    public LobbiesScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/lobbyScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        FlowPane box = (FlowPane) scrollPane.getContent();
        Button newLobbyButton = (Button) root.lookup("#createLobbyButton");
        newLobbyButton.setOnAction(event -> {
            //sendMessageToServer(MessageToServerCreator.createCreationLobbyMessage());
            Transition.toWaitingToStartScene();
        });
        Pane lobbyPane = null;

        HashMap<Integer, String[]> lobbies = observer.getModel().getLobbies();

        for(int j = 0; j < orderedLobbies.size(); j++){
            try {
                lobbyPane = FXMLLoader.load(getClass().getResource("/Scenes/lobbyPane.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pane pane = (Pane) lobbyPane.lookup("#scrollPane");
            Label lobbyId = (Label) pane.getChildren().get(0);
            lobbyId.setText(orderedLobbies.get(j).toString());
            for(int i = 0; i < lobbies.get(orderedLobbies.get(j)).length; i++){
                Label username = (Label) pane.getChildren().get(1+i);
                username.setText(lobbies.get(orderedLobbies.get(j))[i]);
            }
            Button goButton = (Button) pane.getChildren().get(5);
            goButton.setOnAction(event -> {
                int lobbyNum = Integer.parseInt(((Label) ((Pane)((Button) event.getSource()).getParent()).getChildren().get(0)).getText());
                WaitingToStartScene waitingToStartScene = new WaitingToStartScene(lobbyNum, observer.getModel().getLobbies().get(lobbyNum));
                waitingToStartScene.addObserver(this.observer);
                Transition.setWaitingToStartScene(waitingToStartScene);
                Transition.toWaitingToStartScene();
            });
            box.getChildren().add(pane);
        }
    }

    public static void addLobby(int lobbyId){
        if(orderedLobbies.indexOf(lobbyId) == -1) orderedLobbies.add(lobbyId);
    }

    public static void removeLobby(int lobbyId){
        orderedLobbies.remove(LobbiesScene.getIndexFromId(lobbyId));
    }

    public static int getIndexFromId(int lobbyId){
        return orderedLobbies.indexOf(lobbyId);
    }

    public Pane getRoot() {
        return root;
    }
}
