package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.BooleanIntMessage;

public class MarketSelectionMessage extends BooleanIntMessage {
    public MarketSelectionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return "{ \"type\": \"MarketSelection\", " +
                "\"integer\":"+ this.getInteger() +", "  +
                "\"aBoolean\":"+ this.isaBoolean() +"}";
    }
}
