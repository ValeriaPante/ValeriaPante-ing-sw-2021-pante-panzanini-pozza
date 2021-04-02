package it.polimi.ingsw.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class WithTransmutationAbilityBehavior implements TransmutationAbilityBehavior{

    private final EnumMap<Resource, Integer> whiteInto;

    public WithTransmutationAbilityBehavior(EnumMap<Resource, Integer> whiteInto){
        this.whiteInto = whiteInto.clone();
    }

    public EnumMap<Resource,Integer> getWhiteInto(){
        return this.whiteInto.clone();
    }
}
