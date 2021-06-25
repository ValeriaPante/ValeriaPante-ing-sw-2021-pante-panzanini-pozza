package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

/**
 * Object that can provoke an action to be performed by GUI
 */
public abstract class ObservableByGUI {
    protected GUI observer;

    public void addObserver(GUI observer){
        this.observer = observer;
    }

    public void sendMessage(InGameMessage messageToSend){
        observer.getMessageManager().update(messageToSend);
    }

    public void sendMessage(PreGameMessage messageToSend){
        observer.getMessageManager().update(messageToSend);
    }

    public void connect(String ip, String port, String username){
        observer.getMessageManager().connect(ip, port, username);
    }
}
