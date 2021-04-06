package it.polimi.ingsw.Enums;

import java.util.HashMap;
import java.util.Map;

//is there any way to keep it staic?
public enum Resource {
    COIN("CO"), STONE("ST"), SERVANT("SE"), SHIELD("SH"), FAITH, WHITE, ANY;

    private /*static*/ final Map<String, Resource> resourcesAlias = new HashMap<>();

    private Resource(String... aliases) {
        for(final String alias : aliases)
            resourcesAlias.put(alias, this);
    }

    public /*static*/ Resource fromAlias(String alias) {
        return resourcesAlias.get(alias);
    }
}
