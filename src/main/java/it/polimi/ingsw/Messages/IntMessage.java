package it.polimi.ingsw.Messages;

public abstract class IntMessage implements Message {
    private final int integer;

    public IntMessage(int integer) {
        this.integer = integer;
    }

    public int getInteger() {
        return integer;
    }
}
