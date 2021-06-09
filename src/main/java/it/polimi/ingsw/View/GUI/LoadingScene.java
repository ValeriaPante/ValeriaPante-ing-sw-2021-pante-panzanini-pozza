package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoadingScene {
    private Pane root;

    public LoadingScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/loadingScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Pane getRoot() {
        return root;
    }
}
