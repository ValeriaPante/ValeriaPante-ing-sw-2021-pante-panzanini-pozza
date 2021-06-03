package it.polimi.ingsw.View.GUI;

import javafx.scene.layout.Pane;

public abstract class Initializable extends ObservableByGUI{
    protected Pane root;

    public void initialise(){

    }

    public Pane getRoot() {
        return root;
    }
}
