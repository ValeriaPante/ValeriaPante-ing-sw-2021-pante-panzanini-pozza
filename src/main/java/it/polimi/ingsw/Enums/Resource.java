package it.polimi.ingsw.Enums;

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

    public static String toAlias(Resource resource){
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
}
