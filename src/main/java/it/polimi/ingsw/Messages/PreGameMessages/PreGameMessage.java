package it.polimi.ingsw.Messages.PreGameMessages;

import it.polimi.ingsw.Controller.PreGameControllerSwitch;

public abstract class PreGameMessage {
    private int senderId;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void readThrough(PreGameControllerSwitch preGameControllerSwitch){};
}
