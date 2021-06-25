package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.InitialResourcesScene;
import it.polimi.ingsw.View.GUI.Transition;
import javafx.application.Platform;

/**
 * State indicating the phase of the game where the player needs to choose his initial resources
 */
public class InitialResourcesState extends State{

    /**
     * Goes back to the previous scene
     */
    @Override
    public void goBack(){
        InitialResourcesScene.wrongSelection();
        Platform.runLater(() -> Transition.toInitialResourcesScene());
    }

    @Override
    public void next(){

    }
}
