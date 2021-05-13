package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;

public abstract class InGameMessage {
    private int senderId;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){};
}
