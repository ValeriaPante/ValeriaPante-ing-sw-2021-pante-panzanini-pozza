package it.polimi.ingsw.Messages.InGameMessages;

/**
 * The massages extending this class contains an integer
 */
public abstract class IntMessage extends InGameMessage {
    private final int integer;

    public IntMessage(int integer) {
        this.integer = integer;
    }

    public int getInteger() {
        return integer;
    }
}
