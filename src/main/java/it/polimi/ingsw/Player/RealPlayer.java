package it.polimi.ingsw.Player;

import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Cards.PopeFavorCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

public class RealPlayer extends Player{

    private final static int maxProductionPowerInputs = 2;
    private final static int maxProductionPowerOutputs = 3;

    private boolean actionDone;
    private DevSlot[] devSlots;
    private Shelf[] shelves;
    private Depot depot;
    private ArrayList<LeaderCard> leaderCards;
    private ProductionPower basicProductionPower;

    private void initialiseShelves(){
        this.shelves = new Shelf[]{
                new Shelf(1),
                new Shelf(2),
                new Shelf(3),
        };
    }

    private void initialiseDevSlots(){
        this.devSlots = new DevSlot[]{
                new DevSlot(),
                new DevSlot(),
                new DevSlot(),
        };
    }

    private void initialiseBasicProductionPower(){
        EnumMap<Resource, Integer> tempInput = new EnumMap<>(Resource.class);
        EnumMap<Resource, Integer> tempOutput = new EnumMap<>(Resource.class);
        tempInput.put(Resource.ANY, 2);
        tempOutput.put(Resource.ANY,1);
        this.basicProductionPower = new ProductionPower(tempInput, tempOutput);
    }

    //---Constructor---
    public RealPlayer(String nickname){
       super(nickname);
       this.actionDone = false;
       this.initialiseDevSlots();
       this.initialiseShelves();
       this.depot = new Depot();
       this.leaderCards = new ArrayList<>();
       this.initialiseBasicProductionPower();
    }
    //--------

    //---Leader Cards Section ---
    public void addLeaderCard(LeaderCard leaderCard){
        this.leaderCards.add(leaderCard);
    }
    public void discardLeaderCard(LeaderCard leaderCard){
        this.leaderCards.remove(leaderCard);
    }
    //-----

    public void setActionDone(boolean actionDone){
        this.actionDone = actionDone;
    }

    public ProductionPower getBasicProductionPower(){
        return this.basicProductionPower;
    }

    //--- Selection Section ---
    //public void paySelected(){
    //    for (Payable toPay : this.selected.keySet()){
    //        toPay.pay(this.selected.get(toPay));
    //    }
    //}
    //public void swipeSelected(){
    //    this.selected.clear();
    //}
    //public void addSelection(Payable container, EnumMap<Resource, Integer> amount){
    //    this.selected.put(container, amount);
    //}
    //------

    //---Getters---
    public boolean isActionDone(){
        return this.actionDone;
    }
    public DevSlot[] getDevSlots(){
        return Arrays.copyOf(this.devSlots, this.devSlots.length);
    }
    public Shelf[] getShelves(){
        return Arrays.copyOf(this.shelves, this.shelves.length);
    }
    public Depot getDepot(){
        return this.depot;
    }
    public LeaderCard[] getLeaderCards(){
        return this.leaderCards.toArray(new LeaderCard[0]);
    }
    //-----

    // returns null if the player owns no Resources, otherwise it will return an EnumMap with the copy of all resources
    public EnumMap<Resource, Integer> resourcesOwned(){
        Depot allResources = new Depot();

        if ( !this.depot.isEmpty())
            allResources.addEnumMap(this.depot.content());

        for (Shelf shelf: shelves)
            if ( !shelf.isEmpty())
                allResources.addEnumMap(shelf.content());

        for (LeaderCard lc : leaderCards)
            if (lc.getType() == LeaderCardType.STORAGE){
                try{
                    if ( !lc.getAbility().isEmpty())
                        allResources.addEnumMap(lc.getAbility().getContent());
                }
                catch (WeDontDoSuchThingsHere e) {
                    e.printStackTrace();
                }
            }
        return (allResources.isEmpty()) ? null : allResources.content();
    }

    //it can be used to know if the player owns any Resource
    public boolean isBroken(){
        return (this.resourcesOwned() == null);
    }
}
