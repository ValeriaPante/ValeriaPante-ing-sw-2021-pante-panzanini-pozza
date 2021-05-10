package it.polimi.ingsw.Messages;

public class BooleanIntMessage extends IntMessage{
    private boolean aBoolean;

    public BooleanIntMessage(int integer, boolean aBoolean) {
        super(integer);
        this.aBoolean = aBoolean;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }
}
