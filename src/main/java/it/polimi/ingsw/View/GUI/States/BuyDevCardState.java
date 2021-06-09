package it.polimi.ingsw.View.GUI.States;

import it.polimi.ingsw.View.GUI.*;

import java.util.ArrayList;

public class BuyDevCardState extends State {

    public BuyDevCardState(GUI gui){
        toDo = new ArrayList<>();
        done = new ArrayList<>();

        ArrayList<Integer> lc = gui.getModel().getPlayerFromId(gui.getModel().getLocalPlayerId()).getLeaderCards();
        int count = 0;
        for (Integer integer : lc) {
            if (integer > 48 && integer < 53) count++;
        }

        if(count > 0){
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

    @Override
    public void goBack(){
        if(done.size() == 0){
            Transition.reshowDialog();
            return;
        }
        toDo.add(0, done.get(0));
        done.remove(0);
        Transition.setDialogScene(toDo.get(0).getRoot());
    }

    @Override
    public void refresh() {
        done.get(0).initialise();
        Transition.setDialogScene(toDo.get(0).getRoot());
    }
}
