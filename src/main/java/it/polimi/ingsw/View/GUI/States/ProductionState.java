package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.GUI.*;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductionState extends State{
    private HashMap<Resource, Integer> supportContainer;

    public ProductionState(GUI gui){
        toDo = new ArrayList<>();
        done = new ArrayList<>();

        supportContainer = gui.getModel().getPlayerFromId(gui.getModel().getLocalPlayerId()).getSupportContainer();

        ChooseOutputScene chooseOutputScene = new ChooseOutputScene();
        chooseOutputScene.addObserver(gui);
        toDo.add(chooseOutputScene);
    }

    @Override
    public void goBack(){
        Transition.reshowDialog();
    }

    @Override
    public void next(){
        if(toDo.size() == 0 || supportContainer.entrySet().isEmpty()){
            Transition.hideDialog();
        } else {
            toDo.get(0).initialise();
            Transition.setDialogScene(toDo.get(0).getRoot());
            done.add(0, toDo.get(0));
            toDo.remove(0);
            Transition.reshowDialog();
        }
    }

    @Override
    public void refresh() {
        done.get(0).initialise();
        Transition.setDialogScene(toDo.get(0).getRoot());
    }
}
