package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.Initializable;
import it.polimi.ingsw.View.GUI.Transition;

import java.util.ArrayList;

public class State {
    protected ArrayList<Initializable> toDo;
    protected ArrayList<Initializable> done;

    public void goBack(){ }

    public void next(){
        if(toDo.size() == 0){
            Transition.hideDialog();
        } else {
            toDo.get(0).initialise();
            Transition.setDialogScene(toDo.get(0).getRoot());
            done.add(0, toDo.get(0));
            toDo.remove(0);
            Transition.reshowDialog();
        }
    }

    public void refresh(){ }

}
