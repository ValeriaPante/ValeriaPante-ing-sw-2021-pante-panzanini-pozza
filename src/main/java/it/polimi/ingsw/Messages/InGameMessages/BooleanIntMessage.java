package it.polimi.ingsw.Messages.InGameMessages;

/**
 * The messages extending this class contains an integer (already from IntMessage) and a boolean
 */
public abstract class BooleanIntMessage extends IntMessage{
    private final boolean aBoolean;

    public BooleanIntMessage(int integer, boolean aBoolean) {
        super(integer);
        this.aBoolean = aBoolean;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }
}
