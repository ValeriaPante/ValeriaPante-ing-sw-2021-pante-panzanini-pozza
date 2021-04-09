package org.example;

import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public class DepotCalssTest {
    Depot tryDepot = new Depot(){{
        singleAdd(Resource.COIN);
        singleAdd(Resource.STONE);
        addEnumMap(new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 3);
        }});
    }};


}
