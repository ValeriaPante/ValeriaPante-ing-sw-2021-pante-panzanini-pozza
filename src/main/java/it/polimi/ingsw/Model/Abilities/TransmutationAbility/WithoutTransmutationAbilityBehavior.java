package it.polimi.ingsw.Model.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

/**
 * Implementation of the non Ability Transmutation behaviour of LeaderCards
 */
public class WithoutTransmutationAbilityBehavior implements TransmutationAbilityBehavior{

    @Override
    public EnumMap<Resource, Integer> getWhiteInto() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
