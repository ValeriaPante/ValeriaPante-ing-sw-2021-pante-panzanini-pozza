package it.polimi.ingsw.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public interface TransmutationAbilityBehavior {
    EnumMap<Resource, Integer> getWhiteInto();
}
