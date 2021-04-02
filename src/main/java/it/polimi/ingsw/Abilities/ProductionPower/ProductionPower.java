package it.polimi.ingsw.Abilities.ProductionPower;
import java.util.EnumMap;
import it.polimi.ingsw.Enums.Resource;

public class ProductionPower {
    private final EnumMap<Resource,Integer> input;
    private final EnumMap<Resource,Integer> output;

    //@ signals (IllegalArgumentException e)    (resourcesTypeInput == null || resourceAmountInput == null || resourcesTypeOutput == null || resourcesAmountOutput == null) ||
    //                                          (resourcesTypeInput.length != resourceAmountInput.length || resourcesTypeOutput.length != resourcesAmountOutput.length)
    public ProductionPower(Resource[] resourcesTypeInput, int[] resourceAmountInput, Resource[] resourcesTypeOutput, int[] resourcesAmountOutput) throws IllegalArgumentException{
        if (resourcesTypeInput == null || resourceAmountInput == null || resourcesTypeOutput == null || resourcesAmountOutput == null){
            throw new IllegalArgumentException();
        }
        else if (resourcesTypeInput.length != resourceAmountInput.length || resourcesTypeOutput.length != resourcesAmountOutput.length){
            throw new IllegalArgumentException();
        }
        int i;
        this.input = new EnumMap<>(Resource.class);
        this.output = new EnumMap<>(Resource.class);
        for (i=0; i<resourcesTypeInput.length; i++){
            this.input.put(resourcesTypeInput[i], resourceAmountInput[i]);
        }
        for (i=0; i<resourcesTypeOutput.length; i++){
            this.output.put(resourcesTypeOutput[i], resourcesAmountOutput[i]);
        }
    }

    //@ signals (IllegalArgumentException e)    (input == null || output == null)
    public ProductionPower(EnumMap<Resource,Integer> input, EnumMap<Resource,Integer> output) throws IllegalArgumentException{
        if (input == null || output == null){
            throw new IllegalArgumentException();
        }
        this.input = input.clone();
        this.output = output.clone();
    }

    public EnumMap<Resource,Integer> getInput(){
        return this.input.clone();
    }

    public EnumMap<Resource, Integer> getOutput() {
        return this.output.clone();
    }
}
