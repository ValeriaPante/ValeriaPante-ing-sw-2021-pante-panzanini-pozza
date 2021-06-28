package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.ContainersScene;
import it.polimi.ingsw.View.GUI.GUI;
import it.polimi.ingsw.View.GUI.Transition;
import it.polimi.ingsw.View.GUI.TransmutationScene;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * State indicating that the player chose to pick from market
 */
public class MarketState extends State{

    public MarketState(GUI gui){

        this.toDo = new ArrayList<>();
        this.done = new ArrayList<>();

        if(TransmutationScene.getNumberOfTransmutations() == 2){
            TransmutationScene transmutationScene = new TransmutationScene();
            transmutationScene.addObserver(gui);
            toDo.add(transmutationScene);
        }

        ContainersScene containersScene = new ContainersScene();
        containersScene.addObserver(gui);
        toDo.add(containersScene);
    }

    /**
     * Moves to the next scene
     */
    @Override
    public void next(){
        super.next();
        if(toDo.size() == 0) Transition.setOnContainersScene(true);
    }

    /**
     * Goes back to the previous scene
     */
    @Override
    public void goBack(){
        if(done.size() == 0){
            Transition.reshowDialog();
            return;
        }
        toDo.add(0, done.get(0));
        done.remove(0);
        Platform.runLater(() -> Transition.setDialogScene(toDo.get(0).getRoot()));
    }

    /**
     * Updates the current scene
     */
    @Override
    public void refresh() {
        done.get(0).initialise();

        Platform.runLater(() -> Transition.setDialogScene(done.get(0).getRoot()));
    }
}
