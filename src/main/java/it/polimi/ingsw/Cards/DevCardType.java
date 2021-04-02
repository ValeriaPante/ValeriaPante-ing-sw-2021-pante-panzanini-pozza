package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Enums.Colour;

public class DevCardType {
    private final int level;
    private final Colour color;

    public DevCardType(int level, Colour color){
        this.level = level;
        this.color = color;
    }

    public int getLevel(){
        return this.level;
    }

    public Colour getColor() {
        return this.color;
    }

    public boolean equals(Object object){
        boolean equals;
        if (object instanceof DevCardType){
            DevCardType devCardType = (DevCardType) object;
            equals = (this.color == devCardType.color && this.level == devCardType.level);
        }
        else{
            return false;
        }
        return equals;
    }
}
