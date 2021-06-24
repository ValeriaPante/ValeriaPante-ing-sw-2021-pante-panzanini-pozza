package it.polimi.ingsw.Model.Abilities.ProductionPower;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

/**
 * Implementation of the non Ability Production Behaviour of LeaderCards
 */
public class WithoutProductionPowerBehavior implements ProductionPowerBehavior {

    @Override
     public ProductionPower getProductionPower() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
