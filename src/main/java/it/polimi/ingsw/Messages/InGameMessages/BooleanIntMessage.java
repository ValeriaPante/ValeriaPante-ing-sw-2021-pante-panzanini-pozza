package it.polimi.ingsw.Messages.InGameMessages;

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
