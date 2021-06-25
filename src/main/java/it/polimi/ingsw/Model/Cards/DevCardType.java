package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.Colour;

/**
 * Representation of the combination color - level of a development card
 */
public class DevCardType {
    private final int level;
    private final Colour color;

    /**
     * Constructor
     * @param level level of the card
     * @param color color of the card
     */
    public DevCardType(int level, Colour color){
        this.level = level;
        this.color = color;
    }

    /**
     * Level getter
     * @return level of this type
     */
    public int getLevel(){
        return this.level;
    }

    /**
     * Color getter
     * @return color of this type
     */
    public Colour getColor() {
        return this.color;
    }

    /**
     * Compares this with another object
     * @param object the object to compare with
     * @return true if the object is a DevCardType and has the same features of this, false otherwise
     */
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
