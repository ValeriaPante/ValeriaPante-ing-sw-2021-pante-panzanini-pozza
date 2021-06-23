package it.polimi.ingsw.Model.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

public class WithoutDiscountAbilityBehavior implements DiscountAbilityBehavior{

    public EnumMap<Resource, Integer> getDiscount() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
