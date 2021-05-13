package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;

public class PreGameControllerSwitch {

    public synchronized void actionOnMessage(CreationLobbyMessage message){}

    public synchronized void actionOnMessage(DisconnectMessage message){}

    public synchronized void actionOnMessage(MoveToLobbyMessage message){}

    public synchronized void actionOnMessage(StartGameMessage message){}
}
