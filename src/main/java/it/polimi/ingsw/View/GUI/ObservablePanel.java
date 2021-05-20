package it.polimi.ingsw.View.GUI;

import javax.swing.*;

public class ObservablePanel extends JPanel {
    protected GUI observer;

    public void addObserver(GUI observer){
        this.observer = observer;
    }
}
