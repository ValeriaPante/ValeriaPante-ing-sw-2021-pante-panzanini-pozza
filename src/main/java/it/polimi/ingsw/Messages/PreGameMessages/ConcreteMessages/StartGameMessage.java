package it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

public class StartGameMessage extends PreGameMessage{
    @Override
    public void readThrough(PreGameControllerSwitch preGameControllerSwitch) {
        preGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return "{ \"type\": \"StartGame\"}";
    }
}
