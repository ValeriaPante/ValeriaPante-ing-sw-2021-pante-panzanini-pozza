package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.Transition;
import javafx.application.Platform;

/**
 * State indicating a type of turn as already been made
 */
public class DoneState extends State{

    @Override
    public void next(){
        Platform.runLater(Transition::hideDialog);
    }
}
