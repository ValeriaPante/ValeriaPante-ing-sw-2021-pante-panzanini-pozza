package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import java.util.*;

/**
 * Representation of development card
 */
public class DevCard extends CardVP implements Cloneable{

    private final EnumMap<Resource, Integer> cost;
    private final DevCardType type;
    private final ProductionPower prodPower;
    private final int id;
    private boolean selected;

    /**
     * Development card constructor
     * @param victoryPoints victory point of the card
     * @param cost cost of the card
     * @param type type (color-level) of the card
     * @param prodPower production power owned by the card
     * @param id id of the card
     * @throws IllegalArgumentException if one of the parameter is null
     */
    public DevCard(int victoryPoints, EnumMap<Resource, Integer> cost, DevCardType type, ProductionPower prodPower, int id) throws IllegalArgumentException{
        super(victoryPoints);

        if (cost == null || type == null || prodPower == null){
            throw new IllegalArgumentException();
        }

        this.cost = cost.clone();
        this.type = new DevCardType(type.getLevel(), type.getColor());
        this.prodPower = new ProductionPower(prodPower.getInput(), prodPower.getOutput());
        this.id = id;
        this.selected = false;
    }

    /**
     * Development card constructor
     * @param victoryPoints victory point of the card
     * @param cost cost of the card
     * @param type type (color-level) of the card
     * @param prodPower production power owned by the card
     * @param selected if true the constructed card will be already selected
     * @param id id of the card
     * @throws IllegalArgumentException if one of the parameter is null
     */
    private DevCard(int victoryPoints, EnumMap<Resource, Integer> cost, DevCardType type, ProductionPower prodPower, boolean selected, int id) throws IllegalArgumentException{
        super(victoryPoints);

        if (cost == null || type == null || prodPower == null){
            throw new IllegalArgumentException();
        }

        this.cost = cost.clone();
        this.type = new DevCardType(type.getLevel(), type.getColor());
        this.prodPower = new ProductionPower(prodPower.getInput(), prodPower.getOutput());
        this.id = id;
        this.selected = selected;
    }

    /**
     * Card cost getter
     * @return the cost of the card
     */
    public EnumMap<Resource, Integer> getCost(){
        return cost.clone();
    }

    /**
     * Card type getter
     * @return the type of the card
     */
    public DevCardType getType(){
        return new DevCardType(this.type.getLevel(), this.type.getColor());
    }

    /**
     * Card production power getter
     * @return the production power owned by the card
     */
    public ProductionPower getProdPower() {
        return new ProductionPower(prodPower.getInput().clone(), prodPower.getOutput().clone());
    }

    /**
     * Clones the card
     * @return cloned development card
     */
    public DevCard clone(){
        return new DevCard(this.getVictoryPoints(), cost.clone(), new DevCardType(type.getLevel(), type.getColor()), new ProductionPower(prodPower.getInput().clone(), prodPower.getOutput().clone()), selected, id);
    }

    /**
     * Compares the card with another development card
     * @param card development card to compare
     * @return true if the two card have the same features
     */
    public boolean equals(DevCard card){
        return this.cost.equals(card.getCost()) && this.type.equals(card.getType()) && this.prodPower.equals(card.getProdPower());
    }

    /**
     * Selection getter
     * @return true if the card is selected, false otherwise
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * Selects the card
     */
    public void select(){
        this.selected = !this.selected;
    }

    /**
     * Development card id getter
     * @return development card id
     */
    public int getId(){
        return this.id;
    }
}
