package it.polimi.ingsw.Model.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

public class WithoutTransmutationAbilityBehavior implements TransmutationAbilityBehavior{

    public EnumMap<Resource, Integer> getWhiteInto() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }
}
