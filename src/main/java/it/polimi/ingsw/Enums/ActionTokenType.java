package it.polimi.ingsw.Enums;

import it.polimi.ingsw.View.CLI.Color;

public enum ActionTokenType {
    DISCARDGREEN, DISCARDBLUE, DISCARDYELLOW, DISCARDPURPLE, RESETDECKONEFP, TWOFP;

    public static String getEffectString(ActionTokenType token){
        switch(token){
            case DISCARDBLUE:
                return "Discard 2 "+ Color.colourText("BLUE", "BLUE") +" development cards\n" +
                        "from the bottom of the grid, from the lowest level to the highest";
            case DISCARDGREEN:
                return "Discard 2 "+ Color.colourText("GREEN", "GREEN") +" development cards\n" +
                        "from the bottom of the grid, from the lowest level to the highest";
            case DISCARDPURPLE:
                return "Discard 2 "+ Color.colourText("PURPLE", "PURPLE") +" development cards\n" +
                        "from the bottom of the grid, from the lowest level to the highest";
            case DISCARDYELLOW:
                return "Discard 2 "+ Color.colourText("YELLOW", "YELLOW") +" development cards\n" +
                        "from the bottom of the grid, from the lowest level to the highest";
            case TWOFP:
                return "Move LORENZO IL MAGNIFICO forward by "+ Color.colourInt(2, "YELLOW") +" spaces on the faith track";
            case RESETDECKONEFP:
                return "Move LORENZO IL MAGNIFICO forward by "+ Color.colourInt(1, "YELLOW") +" spaces on the faith track.\n" +
                        "Then, shuffle all the Solo Action tokens and create a new stack.";
            default:
                return "";
        }
    }
}
