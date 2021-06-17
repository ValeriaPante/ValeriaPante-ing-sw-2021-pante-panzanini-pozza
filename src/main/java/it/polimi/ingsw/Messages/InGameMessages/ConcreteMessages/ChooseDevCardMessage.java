package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.InGameMessages.IntMessage;
import it.polimi.ingsw.Messages.InGameMessages.MessageConverterToJSON;

public class ChooseDevCardMessage extends IntMessage {
    public ChooseDevCardMessage(int integer) {
        super(integer);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return MessageConverterToJSON.convertToJson(this);
    }
}
