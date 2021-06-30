package it.polimi.ingsw.View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Scene displaying the winner of the game
 */
public class WinnerScene {

    private Pane root;

    public WinnerScene(String winner) {
        Transition.setExitOnClose();
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/winnerScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((Label) root.lookup("#winner")).setText(winner);
    }

    public Pane getRoot() {
        return root;
    }
}
