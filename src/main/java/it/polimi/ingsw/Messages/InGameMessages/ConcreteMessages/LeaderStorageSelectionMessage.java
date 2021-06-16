package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;

public class LeaderStorageSelectionMessage extends InGameMessage {
    private final int id;
    private final int resPosition;
    private final Resource resource;

    public LeaderStorageSelectionMessage(int id, int resPosition, Resource resource) {
        this.id = id;
        this.resPosition = resPosition;
        this.resource = resource;
    }

    public int getId() {
        return id;
    }

    public int getResPosition() {
        return resPosition;
    }

    public Resource getResource() {
        return resource;
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return InGameMessage.convertToJson(this);
    }
}
