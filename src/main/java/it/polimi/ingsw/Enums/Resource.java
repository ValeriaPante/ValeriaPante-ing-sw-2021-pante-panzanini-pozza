package it.polimi.ingsw.Enums;

import it.polimi.ingsw.View.CLI.Color;

public enum Resource {
    COIN, STONE, SERVANT, SHIELD, FAITH, WHITE, ANY;

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
