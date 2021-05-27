package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;

public class TurnType implements Cloneable{
    private MacroTurnType macroTurnType;
    private MicroTurnType microTurnType;

    public TurnType(){
        this.macroTurnType = MacroTurnType.STARTING;
        this.microTurnType = MicroTurnType.DISCARD_LEADER_CARD;
    }

    public void setMacroTurnType(MacroTurnType macroTurnType) {
        this.macroTurnType = macroTurnType;
    }

    public void setMicroTurnType(MicroTurnType microTurnType) {
        this.microTurnType = microTurnType;
    }

    public MacroTurnType getMacroTurnType(){
        return this.macroTurnType;
    }

    public MicroTurnType getMicroTurnType(){
        return this.microTurnType;
    }
}
