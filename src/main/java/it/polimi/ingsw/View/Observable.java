package it.polimi.ingsw.View;

import it.polimi.ingsw.Network.Client.Client;

public abstract class Observable {
    private Client observer;

    public void addObserver(Client observer){
        this.observer = observer;
    }

    public void sendMessageToServer(String messageToSend){
        observer.update(messageToSend);
    }
}
