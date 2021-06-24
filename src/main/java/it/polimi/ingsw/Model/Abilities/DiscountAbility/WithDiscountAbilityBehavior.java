package it.polimi.ingsw.Model.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

/**
 * Implementation of the Ability Discount Behaviour of LeaderCards
 */
public class WithDiscountAbilityBehavior implements DiscountAbilityBehavior{
    private final EnumMap<Resource, Integer> discount;

    /**
     * Getter discount
     * @return the map containing the discount
     */
    @Override
    public EnumMap<Resource, Integer> getDiscount(){
        return this.discount.clone();
    }

    /**
     * Constructor
     * @param discount map representing the discount
     */
    public WithDiscountAbilityBehavior(EnumMap<Resource, Integer> discount){
        this.discount = discount.clone();
    }
}
