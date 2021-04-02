package it.polimi.ingsw.Abilities.ProductionPower;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public class WithProductionPowerBehavior implements ProductionPowerBehavior {
    private final EnumMap<Resource, Integer> input;
    private final EnumMap<Resource, Integer> output;

    @Override
    public ProductionPower getProductionPower() {
        return new ProductionPower(this.input, this.output);
    }

    public WithProductionPowerBehavior(EnumMap<Resource, Integer> input, EnumMap<Resource, Integer> output){
        this.input = input.clone();
        this.output = output.clone();
    }
}
