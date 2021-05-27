package it.polimi.ingsw.Model.Abilities.ProductionPower;

import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

public class WithoutProductionPowerBehavior implements ProductionPowerBehavior {
    @Override
     public ProductionPower getProductionPower() throws WeDontDoSuchThingsHere {
        throw new WeDontDoSuchThingsHere();
    }
}
