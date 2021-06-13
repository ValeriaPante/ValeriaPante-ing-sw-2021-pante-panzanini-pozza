package it.polimi.ingsw.View;

import it.polimi.ingsw.Network.Client.MessageToServerManager;

public abstract class Observable {
    private MessageToServerManager observer;

    public void addObserver(MessageToServerManager observer){
        this.observer = observer;
    }

    public void sendMessageToServer(String messageToSend){
        observer.update(messageToSend);
    }
}
