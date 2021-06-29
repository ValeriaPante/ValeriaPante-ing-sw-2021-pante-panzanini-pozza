package it.polimi.ingsw.Enums;

import it.polimi.ingsw.View.CLI.Color;

/**
 * This enumeration is used to represent the resources of the game. The resource "ANY" correspond to a
 * resource without a specific type, that should be converted into another: an example of usage is
 * the basic production power of the dashboard
 */
public enum Resource {
    COIN, STONE, SERVANT, SHIELD, FAITH, WHITE, ANY;

    /**
     * Converts a string into a resource if possible, otherwise returns null
     * @param alias a string corresponding to the name of a resource (written in full length without abbreviations)
     * @return the resource corresponding to the string passed as a parameter or null if
     * that string does not correspond to a resource of this game
     */
    public static Resource fromAlias(String alias){
        switch (alias.toUpperCase()){
            case ("COIN"):
                return Resource.COIN;
            case ("STONE"):
                return Resource.STONE;
            case ("SERVANT"):
                return Resource.SERVANT;
            case ("SHIELD"):
                return Resource.SHIELD;
            case ("FAITH"):
                return Resource.FAITH;
            case ("WHITE"):
                return Resource.WHITE;
            case ("ANY"):
                return Resource.ANY;
            default:
                return null;
        }
    }

    /**
     * Converts a resource into a string corresponding to the full name of that resource
     * @param resource target resource
     * @return the full name of the resource passed as a parameter
     */
    public static String toAliasFull(Resource resource){
        switch (resource){
            case COIN:
                return "COIN";
            case STONE:
                return "STONE";
            case SERVANT:
                return "SERVANT";
            case SHIELD:
                return "SHIELD";
            case FAITH:
                return "FAITH";
            case WHITE:
                return "WHITE";
            case ANY:
                return "ANY";
            default:
                return null;
        }
    }

    /**
     * Converts a resource into a string corresponding to an abbreviation of that resource
     * @param resource target resource
     * @return the abbreviation of the resource passed as a parameter
     */
    public static String toAliasCompact(Resource resource){
        switch (resource){
            case COIN:
                return Color.colourText("COI", "YELLOW");
            case STONE:
                return Color.colourText("STO", "CYAN");
            case SERVANT:
                return Color.colourText("SER", "PURPLE");
            case SHIELD:
                return Color.colourText("SHI", "BLUE");
            case ANY:
                return Color.colourText("ANY", "RED");
            case WHITE:
                return Color.colourText("WHI", "WHITE");
            default:
                return " X ";
        }
    }
}
