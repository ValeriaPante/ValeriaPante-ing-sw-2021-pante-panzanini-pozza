package it.polimi.ingsw.Abilities.ProductionPower;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public interface ProductionPowerBehavior {

    ProductionPower getProductionPower(EnumMap<Resource, Integer> output);
}
