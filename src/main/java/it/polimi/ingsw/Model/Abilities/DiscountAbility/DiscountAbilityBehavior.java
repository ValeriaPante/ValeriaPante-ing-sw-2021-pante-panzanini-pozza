package it.polimi.ingsw.Model.Abilities.DiscountAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

/**
 * Representation of the Ability Discount of LeaderCards
 */
public interface DiscountAbilityBehavior {

    EnumMap<Resource, Integer> getDiscount();
}
