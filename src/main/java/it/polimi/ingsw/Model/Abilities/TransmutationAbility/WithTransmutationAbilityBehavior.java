package it.polimi.ingsw.Model.Abilities.TransmutationAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

/**
 * Implementation of the Ability Transmutation behaviour of LeaderCards
 */
public class WithTransmutationAbilityBehavior implements TransmutationAbilityBehavior{

    private final EnumMap<Resource, Integer> whiteInto;

    /**
     * Constructor
     * @param whiteInto amount of resources that the white must be transmuted into
     */
    public WithTransmutationAbilityBehavior(EnumMap<Resource, Integer> whiteInto){
        this.whiteInto = whiteInto.clone();
    }

    /**
     * Getter
     * @return the map containing the transmutation
     */
    public EnumMap<Resource,Integer> getWhiteInto(){
        return this.whiteInto.clone();
    }
}
