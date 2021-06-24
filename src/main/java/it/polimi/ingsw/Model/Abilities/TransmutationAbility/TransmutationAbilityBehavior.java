package it.polimi.ingsw.Model.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

/**
 * Representation of the Ability Transmutation of LeaderCards
 */
public interface TransmutationAbilityBehavior {
    EnumMap<Resource, Integer> getWhiteInto();
}
