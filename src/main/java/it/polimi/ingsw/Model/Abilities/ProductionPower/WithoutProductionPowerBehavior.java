package it.polimi.ingsw.Model.Abilities.ProductionPower;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

public class WithoutProductionPowerBehavior implements ProductionPowerBehavior {
    @Override
     public ProductionPower getProductionPower() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
