package it.polimi.ingsw.Abilities.ProductionPower;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public class WithProductionPowerBehavior implements ProductionPowerBehavior {
    private final EnumMap<Resource, Integer> input;

    @Override
    public ProductionPower getProductionPower(EnumMap<Resource, Integer> output) {
        output.put(Resource.FAITH, 1);
        return new ProductionPower(this.input, output);
    }

    public WithProductionPowerBehavior(EnumMap<Resource, Integer> input){
        this.input = input.clone();
    }
}
