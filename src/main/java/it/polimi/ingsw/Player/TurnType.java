package it.polimi.ingsw.Player;

import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;

public class TurnType implements Cloneable{
    private MacroTurnType macroTurnType;
    private MicroTurnType microTurnType;

    public TurnType(){
        this.macroTurnType = MacroTurnType.NONE;
        this.microTurnType = MicroTurnType.NONE;
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
