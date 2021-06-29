package it.polimi.ingsw.Enums;

import it.polimi.ingsw.View.CLI.Color;

/**
 * This enumeration contains the action token used in single player for Lorenzo il Magnifico
 */
public enum ActionTokenType {
    DISCARDGREEN, DISCARDBLUE, DISCARDYELLOW, DISCARDPURPLE, RESETDECKONEFP, TWOFP;

    /**
     *Interprets an action token and returns a string explaining its effect
     * @param token target token
     * @return a string corresponding to the effect of the token "token" passed as a parameter
     */
    public static String getEffectString(ActionTokenType token){
        switch(token){
            case DISCARDBLUE:
                return "Discard 2 "+ Color.colourText("BLUE", "BLUE") +" development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDGREEN:
                return "Discard 2 "+ Color.colourText("GREEN", "GREEN") +" development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDPURPLE:
                return "Discard 2 "+ Color.colourText("PURPLE", "PURPLE") +" development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDYELLOW:
                return "Discard 2 "+ Color.colourText("YELLOW", "YELLOW") +" development cards from the bottom of the grid, from the lowest level to the highest";
            case TWOFP:
                return "Move LORENZO IL MAGNIFICO forward by "+ Color.colourInt(2, "YELLOW") +" spaces on the faith track";
            case RESETDECKONEFP:
                return "Move LORENZO IL MAGNIFICO forward by "+ Color.colourInt(1, "YELLOW") +" spaces on the faith track. Then, shuffle all the Solo Action tokens and create a new stack.";
            default:
                return "";
        }
    }

    /**
     *Interprets an action token and returns a string explaining its effect (this method is used in GUI)
     * @param token target token
     * @return a string corresponding to the effect of the token "token" passed as a parameter
     */
    public static String getEffectStringForGUI(ActionTokenType token){
        switch(token){
            case DISCARDBLUE:
                return "Discard 2 BLUE development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDGREEN:
                return "Discard 2 GREEN development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDPURPLE:
                return "Discard 2 PURPLE development cards from the bottom of the grid, from the lowest level to the highest";
            case DISCARDYELLOW:
                return "Discard 2 YELLOW development cards from the bottom of the grid, from the lowest level to the highest";
            case TWOFP:
                return "Move LORENZO IL MAGNIFICO forward by 2 spaces on the faith track";
            case RESETDECKONEFP:
                return "Move LORENZO IL MAGNIFICO forward by 1 spaces on the faith track. Then, shuffle all the Solo Action tokens and create a new stack.";
            default:
                return "";
        }
    }
}
