package it.polimi.ingsw.Model.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

/**
 * Implementation of the non Ability Discount behaviour of LeaderCards
 */
public class WithoutDiscountAbilityBehavior implements DiscountAbilityBehavior{

    @Override
    public EnumMap<Resource, Integer> getDiscount() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
