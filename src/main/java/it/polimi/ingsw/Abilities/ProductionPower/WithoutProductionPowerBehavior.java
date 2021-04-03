package it.polimi.ingsw.Abilities.ProductionPower;

import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class WithoutProductionPowerBehavior implements ProductionPowerBehavior {
    @Override
     public ProductionPower getProductionPower(EnumMap<Resource, Integer> output) throws WeDontDoSuchThingsHere {
        throw new WeDontDoSuchThingsHere();
    }
}
