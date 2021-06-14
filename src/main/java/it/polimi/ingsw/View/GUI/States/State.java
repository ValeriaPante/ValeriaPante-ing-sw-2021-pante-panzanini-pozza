package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.Initializable;
import it.polimi.ingsw.View.GUI.Transition;
import javafx.application.Platform;

import java.util.ArrayList;

public class State {
    protected ArrayList<Initializable> toDo;
    protected ArrayList<Initializable> done;

    public void goBack(){ }

    public void next(){
        if(toDo.size() == 0){
            Platform.runLater(Transition::hideDialog);
        } else {
            toDo.get(0).initialise();
            Platform.runLater(() -> Transition.setDialogScene(toDo.get(0).getRoot()));
            done.add(0, toDo.get(0));
            toDo.remove(0);
            Platform.runLater(Transition::reshowDialog);
        }
    }

    public void refresh(){ }

}
