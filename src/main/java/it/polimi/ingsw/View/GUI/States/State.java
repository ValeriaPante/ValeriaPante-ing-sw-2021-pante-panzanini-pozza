package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.Initializable;
import it.polimi.ingsw.View.GUI.Transition;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * Class that indicates the current state of the game
 */
public class State {
    protected ArrayList<Initializable> toDo;
    protected ArrayList<Initializable> done;

    /**
     * Goes back to the previous scene
     */
    public void goBack(){ }

    /**
     * Moves to the next scene
     */
    public void next(){
        if(toDo.size() == 0){
            Platform.runLater(Transition::hideDialog);
        } else {
            toDo.get(0).initialise();
            Transition.setDialogScene(toDo.get(0).getRoot());
            done.add(0, toDo.get(0));
            toDo.remove(0);
            Transition.reshowDialog();
        }
    }

    /**
     * Updates the current scene
     */
    public void refresh(){ }

}
