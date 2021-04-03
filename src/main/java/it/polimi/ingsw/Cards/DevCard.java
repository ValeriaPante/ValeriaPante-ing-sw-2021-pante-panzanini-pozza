package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import java.util.*;

public class DevCard extends CardVP implements Cloneable{
    private final EnumMap<Resource, Integer> cost;
    private final DevCardType type;
    private final ProductionPower prodPower;

    public DevCard(int victoryPoints, EnumMap<Resource, Integer> cost, DevCardType type, ProductionPower prodPower) throws IllegalArgumentException{
        super(victoryPoints);

        if (cost == null || type == null || prodPower == null){
            throw new IllegalArgumentException();
        }

        this.cost = cost.clone();
        this.type = new DevCardType(type.getLevel(), type.getColor());
        this.prodPower = new ProductionPower(prodPower.getInput(), prodPower.getOutput());
    }

    public EnumMap<Resource, Integer> getCost(){
        return cost.clone();
    }

    public DevCardType getType(){
        return new DevCardType(this.type.getLevel(), this.type.getColor());
    }

    public ProductionPower getProdPower() {
        return new ProductionPower(prodPower.getInput().clone(), prodPower.getOutput().clone());
    }

    public DevCard clone(){
        return new DevCard(this.getVictoryPoints(), cost.clone(), new DevCardType(type.getLevel(), type.getColor()), new ProductionPower(prodPower.getInput().clone(), prodPower.getOutput().clone()));
    }

    public boolean equals(DevCard card){
        return this.cost.equals(card.getCost()) && this.type == card.getType() && this.prodPower.equals(card.getProdPower());
    }
}
