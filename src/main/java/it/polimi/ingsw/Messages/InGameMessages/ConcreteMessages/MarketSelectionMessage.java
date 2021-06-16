package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.BooleanIntMessage;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;

public class MarketSelectionMessage extends BooleanIntMessage {
    public MarketSelectionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return InGameMessage.convertToJson(this);
    }
}
