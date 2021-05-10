package it.polimi.ingsw.Network.Messages;

public abstract class Message {

    private final int senderId;

    public Message(int senderId){
        this.senderId = senderId;
    }

    public int getSenderId(){
        return this.senderId;
    }
}
