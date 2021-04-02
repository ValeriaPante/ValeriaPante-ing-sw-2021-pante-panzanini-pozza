package it.polimi.ingsw.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class WithDiscountAbilityBehavior implements DiscountAbilityBehavior{
    private final EnumMap<Resource, Integer> discount;

    public EnumMap<Resource, Integer> getDiscount(){
        return this.discount.clone();
    }

    public WithDiscountAbilityBehavior(EnumMap<Resource, Integer> discount){
        this.discount = discount.clone();
    }
}
