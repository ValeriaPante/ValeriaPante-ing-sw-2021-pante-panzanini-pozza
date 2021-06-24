package it.polimi.ingsw.Model.Abilities;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Abilities.DiscountAbility.*;
import it.polimi.ingsw.Model.Abilities.ProductionPower.*;
import it.polimi.ingsw.Model.Abilities.StorageAbility.*;
import it.polimi.ingsw.Model.Abilities.TransmutationAbility.*;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.util.EnumMap;

/**
 * Representation of the Ability of leader Cards
 */
public class Ability implements Payable{

    private final ProductionPowerBehavior productionPowerBehavior;
    private final StorageAbilityBehavior storageAbilityBehavior;
    private final DiscountAbilityBehavior discountAbilityBehavior;
    private final TransmutationAbilityBehavior transmutationAbilityBehavior;

    /**
     * Getter production power
     * @return the Production Power
     * @throws WrongLeaderCardType if called on an ability that is not Production type
     */
    public ProductionPower getProductionPower(){
        return this.productionPowerBehavior.getProductionPower();
    }

    //---Storage ability methods-------------------------------------------------------------------------------

    /**
     * Add a resource to this storage LeaderCard
     * @param resource resource to add
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public void add(Resource resource){
        this.storageAbilityBehavior.singleAdd(resource);
    }

    /**
     * Remove a resource to this storage LeaderCard
     * @param resource resource to remove
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public void remove(Resource resource){
        this.storageAbilityBehavior.singleRemove(resource);
    }

    /**
     * Getter
     * @return the content of this storage LeaderCard
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public EnumMap<Resource, Integer> getContent(){
        return this.storageAbilityBehavior.content();
    }

    /**
     * Getter
     * @return the capacity of this storage LeaderCard
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public EnumMap<Resource,Integer> getCapacity(){
        return this.storageAbilityBehavior.getCapacity();
    }

    /**
     * Evaluate the storage emptiness
     * @return true if this storage is empty
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public boolean isEmpty(){
        return this.storageAbilityBehavior.isEmpty();
    }

    /**
     * Evaluate a specific resource emptiness
     * @param resource resource to evaluate
     * @return true if the space for that specific resource is full
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public boolean isFull(Resource resource){
        return getCapacity().get(resource).equals(getContent().get(resource));
    }

    /**
     * Map evaluator
     * @param checkMap map to evaluate
     * @return true if the resources inside the storage contains the map
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    @Override
    public boolean contains(EnumMap<Resource, Integer> checkMap){
        return this.storageAbilityBehavior.contains(checkMap);
    }

    /**
     * Removes the resource selected
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    @Override
    public void pay() {
        this.storageAbilityBehavior.removeSelected();
    }

    /**
     * Single resource selection
     * @param resource resource type
     * @param position position of the resource
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public void select(Resource resource, int position){
        this.storageAbilityBehavior.select(resource, position);
    }

    /**
     * Deselects all the selected resources
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public void deselectAll(){
        this.storageAbilityBehavior.deselectAll();
    }

    /**
     * Getter number of resources
     * @return the number af oll the resources inside
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public int countAll(){
        return this.storageAbilityBehavior.countAll();
    }

    /**
     * Getter selected
     * @return Map that contains all the resources selected
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    public EnumMap<Resource, Integer> getSelected(){
        return this.storageAbilityBehavior.getSelected();
    }
    //----------------------------------------------------------------------------------------------------------
    /**
     * Getter transmutation
     * @return the map containing the transmutation
     * @throws WrongLeaderCardType if called on an ability that is not Transmutation type
     */
    public EnumMap<Resource, Integer> getWhiteInto(){
        return this.transmutationAbilityBehavior.getWhiteInto();
    }

    /**
     * Getter discount
     * @return the map containing the discount
     * @throws WrongLeaderCardType if called on an ability that is not Discount type
     */
    public EnumMap<Resource, Integer> getDiscount(){
        return this.discountAbilityBehavior.getDiscount();
    }

    /**
     * Ability constructor
     * @param map1 discount amount, amount of resources that the white must be transmuted into, capacity of the storage, input of the production power
     * @param map2 output of the production power; only used if the type is production power
     * @param type Ability type that must be created
     */
    public Ability(EnumMap<Resource, Integer> map1, EnumMap<Resource, Integer> map2, LeaderCardType type){
        if (type == LeaderCardType.STORAGE){
            this.storageAbilityBehavior = new WithStorageAbilityBehavior(map1);
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.DISCOUNT){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithDiscountAbilityBehavior(map1);
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.TRANSMUTATION){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithTransmutationAbilityBehavior(map1);
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.PRODPOWER){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithProductionPowerBehavior(map1, map2);
        }
        //questo else lo teniamo per sicurezza
        else{
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
    }
}
