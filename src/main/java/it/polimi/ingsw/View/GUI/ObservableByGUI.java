package it.polimi.ingsw.View.GUI;

public abstract class ObservableByGUI {
    protected GUI observer;

    public void addObserver(GUI observer){
        this.observer = observer;
    }

    public void sendMessageToServer(String messageToSend){
        observer.getClient().update(messageToSend);
    }

    public void connect(String ip, String port, String username){
        observer.getClient().connect(ip, port, username);
    }
}
