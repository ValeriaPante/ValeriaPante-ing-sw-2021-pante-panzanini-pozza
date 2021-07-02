package it.polimi.ingsw.Model.Abilities.ProductionPower;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

/**
 * Implementation of the Ability Production Behaviour of LeaderCards
 */
public class WithProductionPowerBehavior implements ProductionPowerBehavior {
    private final EnumMap<Resource, Integer> input;
    private final EnumMap<Resource, Integer> output;

    /**
     * Getter
     * @return the Production Power
     */
    @Override
    public ProductionPower getProductionPower() {
        return new ProductionPower(this.input, this.output);
    }

    /**
     * Constructor
     * @param input resources required for this production power
     * @param output output of production
     */
    public WithProductionPowerBehavior(EnumMap<Resource, Integer> input, EnumMap<Resource, Integer> output){
        this.input = input.clone();

        if(output.isEmpty()){
            this.output = new EnumMap<>(Resource.class);
            this.output.put(Resource.ANY, 1);
            this.output.put(Resource.FAITH,1);
        } else {
            this.output = output.clone();
        }

    }
}
