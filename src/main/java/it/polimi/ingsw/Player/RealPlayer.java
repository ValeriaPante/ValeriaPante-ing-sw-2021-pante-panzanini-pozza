package it.polimi.ingsw.Player;

import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Cards.PopeFavorCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;

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
    private PopeFavorCard[] popeFavorCards;
    private ArrayList<LeaderCard> leaderCards;
    private HashMap<Payable, EnumMap<Resource, Integer>> selected;
    private EnumMap<Resource, Integer> discounts;

    private void initialisePopeFavorCards(){
        this.popeFavorCards = new PopeFavorCard[]{
                new PopeFavorCard(2),
                new PopeFavorCard(3),
                new PopeFavorCard(4),
        };
    }

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

    //---Constructor---
    public RealPlayer(String nickname){
       super(nickname);
       this.actionDone = false;
       initialiseDevSlots();
       initialiseShelves();
       this.depot = new Depot();
       initialisePopeFavorCards();
       this.leaderCards = new ArrayList<>();
       this.discounts = new EnumMap<>(Resource.class);
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

    public ProductionPower basicProductionPower(EnumMap<Resource, Integer> input, EnumMap<Resource, Integer> output){
        return new ProductionPower(input, output);
    }

    public void addDiscount(EnumMap<Resource, Integer> discountToAdd){
        for (EnumMap.Entry<Resource, Integer> entry : discountToAdd.entrySet())
            discounts.put(entry.getKey(), ((discounts.get(entry.getKey()) == null)? entry.getValue() : discounts.get(entry.getKey()) + entry.getValue()));
    }

    public void addDevCard(DevCard card, int devSlot){
        devSlots[devSlot].addCard(card);
    }

    //--- Selection Section ---
    public void paySelected(){
        for (Payable toPay : this.selected.keySet()){
            toPay.pay(this.selected.get(toPay));
        }
    }
    public void swipeSelected(){
        this.selected.clear();
    }
    public void addSelection(Payable container, EnumMap<Resource, Integer> amount){
        this.selected.put(container, amount);
    }
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
    public PopeFavorCard[] getPopeFavorCards(){
        return Arrays.copyOf(this.popeFavorCards, this.popeFavorCards.length);
    }
    public LeaderCard[] getLeaderCards(){
        return this.leaderCards.toArray(new LeaderCard[0]);
    }

    public EnumMap<Resource, Integer> getDiscounts(){ return this.discounts.clone(); }
    //-----

}
