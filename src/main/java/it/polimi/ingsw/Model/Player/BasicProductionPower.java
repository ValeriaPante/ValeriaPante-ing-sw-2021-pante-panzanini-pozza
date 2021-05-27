package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public class BasicProductionPower {
    private boolean selected;
    private ProductionPower productionPower;

    public BasicProductionPower(){
        this.selected = false;
        this.productionPower = new ProductionPower(new EnumMap<>(Resource.class){{put(Resource.ANY, 2);}}, new EnumMap<>(Resource.class){{put(Resource.ANY, 1);}});
    }

    public void select(){
        this.selected = !this.selected;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public ProductionPower getProductionPower(){
        return new ProductionPower(this.productionPower.getInput().clone(), this.productionPower.getOutput().clone());
    }
}
