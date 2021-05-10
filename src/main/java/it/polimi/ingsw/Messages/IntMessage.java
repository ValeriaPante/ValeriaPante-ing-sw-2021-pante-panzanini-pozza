package it.polimi.ingsw.Messages;

public class IntMessage extends Message{
    private final int integer;

    public IntMessage(int integer) {
        this.integer = integer;
    }

    public int getInteger() {
        return integer;
    }
}
