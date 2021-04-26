package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Game.Table;

import java.util.EnumMap;

public class CardActionController extends SelectionController{

    public CardActionController(Table table){
        super(table);
    }

    protected boolean isAffordableSomehow(EnumMap<Resource, Integer> checkMap) {
        Depot allResources = new Depot() {{
            addEnumMap(table.turnOf().getResourcesOwned());
        }};
        EnumMap<Resource, Integer> copy = checkMap.clone();

        if (!checkMap.containsKey(Resource.ANY)) {
            return allResources.contains(checkMap);
        }

        //qui in poi ho le any
        int anyAmount = copy.get(Resource.ANY);
        copy.remove(Resource.ANY);
        int otherResourcesAmount = 0;
        for (int value : copy.values()) {
            otherResourcesAmount += value;
        }
        return (allResources.contains(copy) && allResources.countAll() >= otherResourcesAmount + anyAmount);
    }




}
