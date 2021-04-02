package it.polimi.ingsw.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.EnumMap;

public class WithoutTransmutationAbilityBehavior implements TransmutationAbilityBehavior{

    public EnumMap<Resource, Integer> getWhiteInto() throws WeDontDoSuchThingsHere {
        throw new WeDontDoSuchThingsHere();
    }
}
