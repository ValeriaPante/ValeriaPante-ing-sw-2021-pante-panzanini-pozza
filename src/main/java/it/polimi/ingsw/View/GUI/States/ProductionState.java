package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import it.polimi.ingsw.View.GUI.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * State indicating that the player chose to activate his production powers
 */
public class ProductionState extends State{
    private SimplifiedPlayer player;

    public ProductionState(GUI gui){
        toDo = new ArrayList<>();
        done = new ArrayList<>();

        player = gui.getModel().getPlayerFromId(gui.getModel().getLocalPlayerId());

        ChooseOutputScene chooseOutputScene = new ChooseOutputScene();
        chooseOutputScene.addObserver(gui);
        toDo.add(chooseOutputScene);
    }

    /**
     * Goes back to the previous scene
     */
    @Override
    public void goBack(){
        Platform.runLater(Transition::reshowDialog);
    }

    /**
     * Moves to the next scene
     */
    @Override
    public void next(){
        if(toDo.size() == 0 || player.getSupportContainer().isEmpty()){
            Platform.runLater(Transition::hideDialog);
        } else {
            toDo.get(0).initialise();
            Initializable toMove = toDo.get(0);
            done.add(0, toDo.get(0));
            toDo.remove(0);
            Platform.runLater(() -> Transition.setDialogScene(toMove.getRoot()));
            Platform.runLater(Transition::reshowDialog);
        }
    }

    /**
     * Updates the current scene
     */
    @Override
    public void refresh() {
        done.get(0).initialise();
        Transition.setDialogScene(toDo.get(0).getRoot());
    }
}
