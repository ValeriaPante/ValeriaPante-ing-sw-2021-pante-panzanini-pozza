package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.*;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * State indicating that the player chose to buy a development card
 */
public class BuyDevCardState extends State {

    public BuyDevCardState(GUI gui){
        toDo = new ArrayList<>();
        done = new ArrayList<>();

        if(DiscountsScene.getNumberOfDiscounts() > 0){
            DiscountsScene discountsScene = new DiscountsScene();
            discountsScene.addObserver(gui);
            toDo.add(discountsScene);
        }

        PaymentScene paymentScene = new PaymentScene();
        paymentScene.addObserver(gui);
        toDo.add(paymentScene);

        DevSlotChoiceScene devSlotChoiceScene = new DevSlotChoiceScene();
        devSlotChoiceScene.addObserver(gui);
        toDo.add(devSlotChoiceScene);
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
        Platform.runLater(() -> Transition.setDialogScene(done.get(0).getRoot()));
        Platform.runLater(Transition::reshowDialog);
    }

    /**
     * Updates the current scene
     */
    @Override
    public void refresh() {
        done.get(0).initialise();
        Platform.runLater(() -> Transition.setDialogScene(toDo.get(0).getRoot()));
    }
}
