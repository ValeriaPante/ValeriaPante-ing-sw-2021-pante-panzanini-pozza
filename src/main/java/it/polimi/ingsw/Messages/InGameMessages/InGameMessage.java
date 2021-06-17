package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;

public abstract class InGameMessage {
    private int senderId;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public abstract void readThrough(InGameControllerSwitch inGameControllerSwitch);

    public abstract String toJson();
}
