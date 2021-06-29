package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;

/**
 * This class, and all the others extending this, are used for visitor pattern through an "InGameControllerSwitch"
 */
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
