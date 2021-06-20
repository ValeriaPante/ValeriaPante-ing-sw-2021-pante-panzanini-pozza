package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.View.View;

import java.util.ArrayList;

public class LocalMessageManager implements MessageManager{

    private final InGameControllerSwitch inGameControllerSwitch;
    private final Visitor visitor;

    public LocalMessageManager(View view) {
        this.visitor = new Visitor(view);
        this.inGameControllerSwitch = new InGameControllerSwitch(new ArrayList<>(){{add("local");}}, true);
    }

    @Override
    public void update(InGameMessage message) {
        message.readThrough(inGameControllerSwitch);
    }

    @Override
    public void update(PreGameMessage message) {
    }

    @Override
    public void connect(String ip, String port, String username) {

    }

    public void dispatch(FromServerMessage message){
        message.visit(visitor);
    }
}
