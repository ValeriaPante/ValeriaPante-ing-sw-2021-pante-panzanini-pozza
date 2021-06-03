package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.InitialResourcesScene;
import it.polimi.ingsw.View.GUI.Transition;

public class InitialResourcesState extends State{

    @Override
    public void goBack(){
        InitialResourcesScene.wrongSelection();
        Transition.toInitialResourcesScene();
    }

    @Override
    public void next(){

    }
}
