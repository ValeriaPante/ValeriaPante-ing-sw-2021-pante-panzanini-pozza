package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;

public class TransmutationMessage extends InGameMessage {
    private final int serial1;
    private final int serial2;
    private final int quantity1;
    private final int quantity2;

    public TransmutationMessage(int serial1, int serial2, int quantity1, int quantity2) {
        this.serial1 = serial1;
        this.serial2 = serial2;
        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
    }

    public int getSerial1() {
        return serial1;
    }

    public int getSerial2() {
        return serial2;
    }

    public int getQuantity1() {
        return quantity1;
    }

    public int getQuantity2() {
        return quantity2;
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }

    @Override
    public String toJson(){
        return "{ \"type\": \"Transmutation\", " +
                "\"serial1\":"+ serial1 +", "  +
                "\"serial1\":"+ serial2 +", "  +
                "\"quantity1\":"+ quantity1 +", "  +
                "\"quantity2\":"+ quantity2 +"}";
    }
}
