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
    private ArrayList<LeaderCard> leaderCards;
    private HashMap<Payable, EnumMap<Resource, Integer>> selected;

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
       this.leaderCards = new ArrayList<>();
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
    public LeaderCard[] getLeaderCards(){
        return this.leaderCards.toArray(new LeaderCard[0]);
    }
    //-----

}
