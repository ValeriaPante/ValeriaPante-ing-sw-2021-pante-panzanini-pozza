package it.polimi.ingsw.Abilities;

import it.polimi.ingsw.Abilities.DiscountAbility.*;
import it.polimi.ingsw.Abilities.ProductionPower.*;
import it.polimi.ingsw.Abilities.StorageAbility.*;
import it.polimi.ingsw.Abilities.TransmutationAbility.*;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.util.EnumMap;

public class Ability {

    private final ProductionPowerBehavior productionPowerBehavior;
    private final StorageAbilityBehavior storageAbilityBehavior;
    private final DiscountAbilityBehavior discountAbilityBehavior;
    private final TransmutationAbilityBehavior transmutationAbilityBehavior;

    public ProductionPower getProductionPower(EnumMap<Resource, Integer> output){
        return this.productionPowerBehavior.getProductionPower(output);
    }

    public void add(Resource resource){
        this.storageAbilityBehavior.singleAdd(resource);
    }
    public void remove(Resource resource){
        this.storageAbilityBehavior.singleRemove(resource);
    }
    public EnumMap<Resource, Integer> getContent(){
        return this.storageAbilityBehavior.getContent();
    }
    public EnumMap<Resource,Integer> getCapacity(){
        return this.storageAbilityBehavior.getCapacity();
    }
    public boolean isEmpty(){
        return this.storageAbilityBehavior.isEmpty();
    }

    public EnumMap<Resource, Integer> getWhiteInto(){
        return this.transmutationAbilityBehavior.getWhiteInto();
    }

    public EnumMap<Resource, Integer> getDiscount(){
        return this.discountAbilityBehavior.getDiscount();
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
