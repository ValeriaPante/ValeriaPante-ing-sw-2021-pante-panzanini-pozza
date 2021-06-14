package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.IntMessage;

public class ChooseDevSlotMessage extends IntMessage {
    public ChooseDevSlotMessage(int integer) {
        super(integer);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return "{ \"type\": \"ChooseDevSlot\", " +
                "\"integer\":"+ this.getInteger() +"}";
    }
}
