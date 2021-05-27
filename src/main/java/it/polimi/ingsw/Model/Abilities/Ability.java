package it.polimi.ingsw.Model.Abilities;

import it.polimi.ingsw.Model.Abilities.DiscountAbility.*;
import it.polimi.ingsw.Model.Abilities.ProductionPower.*;
import it.polimi.ingsw.Model.Abilities.StorageAbility.*;
import it.polimi.ingsw.Model.Abilities.TransmutationAbility.*;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.util.EnumMap;

public class Ability implements Payable{

    private final ProductionPowerBehavior productionPowerBehavior;
    private final StorageAbilityBehavior storageAbilityBehavior;
    private final DiscountAbilityBehavior discountAbilityBehavior;
    private final TransmutationAbilityBehavior transmutationAbilityBehavior;

    public ProductionPower getProductionPower(){
        return this.productionPowerBehavior.getProductionPower();
    }

    //---Storage ability methods-------------------------------------------------------------------------------
    public void add(Resource resource){
        this.storageAbilityBehavior.singleAdd(resource);
    }
    public void remove(Resource resource){
        this.storageAbilityBehavior.singleRemove(resource);
    }
    public EnumMap<Resource, Integer> getContent(){
        return this.storageAbilityBehavior.content();
    }
    public EnumMap<Resource,Integer> getCapacity(){
        return this.storageAbilityBehavior.getCapacity();
    }
    public boolean isEmpty(){
        return this.storageAbilityBehavior.isEmpty();
    }
    public boolean isFull(Resource resource){
        return getCapacity().get(resource).equals(getContent().get(resource));
    }
    public boolean contains(EnumMap<Resource, Integer> checkMap){
        return this.storageAbilityBehavior.contains(checkMap);
    }
    public void pay() {
        this.storageAbilityBehavior.removeSelected();
    }
    public void select(Resource resource, int position){
        this.storageAbilityBehavior.select(resource, position);
    }
    public void deselectAll(){
        this.storageAbilityBehavior.deselectAll();
    }
    public int countAll(){
        return this.storageAbilityBehavior.countAll();
    }
    //----------------------------------------------------------------------------------------------------------

    public EnumMap<Resource, Integer> getWhiteInto(){
        return this.transmutationAbilityBehavior.getWhiteInto();
    }

    public EnumMap<Resource, Integer> getDiscount(){
        return this.discountAbilityBehavior.getDiscount();
    }

    public EnumMap<Resource, Integer> getSelected(){
        return this.storageAbilityBehavior.getSelected();
    }

    //type == LeaderCardType.DISCOUNT, map represent the amount discount
    //type == LeaderCardType.TRANSMUTATION, map represent the amount of resources that the white must be transmuted in to
    //type == LeaderCardType.STORAGE map, represent the capacity of the storage
    public Ability(EnumMap<Resource, Integer> map, LeaderCardType type){
        if (type == LeaderCardType.STORAGE){
            this.storageAbilityBehavior = new WithStorageAbilityBehavior(map);
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.DISCOUNT){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithDiscountAbilityBehavior(map);
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.TRANSMUTATION){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithTransmutationAbilityBehavior(map);
            this.productionPowerBehavior = new WithoutProductionPowerBehavior();
        }
        else if (type == LeaderCardType.PRODPOWER){
            this.storageAbilityBehavior = new WithoutStorageAbilityBehavior();
            this.discountAbilityBehavior = new WithoutDiscountAbilityBehavior();
            this.transmutationAbilityBehavior = new WithoutTransmutationAbilityBehavior();
            this.productionPowerBehavior = new WithProductionPowerBehavior(map);
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
