package it.polimi.ingsw.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import java.util.EnumMap;

public class WithoutDiscountAbilityBehavior implements DiscountAbilityBehavior{

    public EnumMap<Resource, Integer> getDiscount() throws WeDontDoSuchThingsHere {
        throw new WeDontDoSuchThingsHere();
    }
}
