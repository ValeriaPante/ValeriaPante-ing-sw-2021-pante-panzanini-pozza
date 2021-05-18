package it.polimi.ingsw.Network.Client.Messages;

public abstract class WithIntMessage extends FromServerMessage{
    protected int id;

    public int getId() {
        return id;
    }
}
