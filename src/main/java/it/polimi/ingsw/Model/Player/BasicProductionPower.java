package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

/**
 * Representation of the basic production power
 */
public class BasicProductionPower {
    private boolean selected;
    private final ProductionPower productionPower;

    /**
     * Constructor
     */
    public BasicProductionPower(){
        this.selected = false;
        this.productionPower = new ProductionPower(new EnumMap<>(Resource.class){{put(Resource.ANY, 2);}}, new EnumMap<>(Resource.class){{put(Resource.ANY, 1);}});
    }

    /**
     * Selects this Production Power
     */
    public void select(){
        this.selected = !this.selected;
    }

    /**
     * Getter
     * @return true if this Production Power is selected
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * Getter
     * @return the Production Power associated with this Basic Production Power
     */
    public ProductionPower getProductionPower(){
        return new ProductionPower(this.productionPower.getInput().clone(), this.productionPower.getOutput().clone());
    }
}
