package it.polimi.ingsw.Messages.PreGameMessages;

import it.polimi.ingsw.Controller.PreGameControllerSwitch;

/**
 * This class, and all the others extending this, are used for visitor pattern through an "PreGameControllerSwitch"
 */
public abstract class PreGameMessage {
    private int senderId;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public abstract void readThrough(PreGameControllerSwitch preGameControllerSwitch);

    public String toJson(){
        return null;
    }
}
