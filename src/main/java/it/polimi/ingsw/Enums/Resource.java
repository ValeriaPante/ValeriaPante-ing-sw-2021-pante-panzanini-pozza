package it.polimi.ingsw.Enums;

import java.util.HashMap;
import java.util.Map;

//is there any way to keep it staic?
public enum Resource {
    COIN("CO"), STONE("ST"), SERVANT("SE"), SHIELD("SH"), FAITH, WHITE, ANY;

    private /*static*/ final Map<String, Resource> resourcesAlias = new HashMap<>();
    private boolean selected = false;

    private Resource(String... aliases) {
        for(final String alias : aliases)
            resourcesAlias.put(alias, this);
    }

    public /*static*/ Resource fromAlias(String alias) {
        return resourcesAlias.get(alias);
    }

    //straight method
    //public static Resource fromAlias(String aliasGiven) throws IllegalArgumentException{
    //    switch (aliasGiven){
    //        case ("CO"):
    //            return Resource.COIN;
    //        case ("ST"):
    //            return Resource.STONE;
    //        case ("SE"):
    //            return Resource.SERVANT;
    //        case ("SH"):
    //            return Resource.SHIELD;
    //        default:
    //            throw new IllegalArgumentException();
    //    }
    //}

    public void select(){
        selected = !selected;
    }

    public boolean isSelected(){
        return selected;
    }
}
