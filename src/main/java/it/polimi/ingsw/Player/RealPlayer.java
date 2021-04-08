package it.polimi.ingsw.Player;

import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Cards.PopeFavorCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.BrokenPlayerException;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;

public class RealPlayer extends Player{

    private final static int maxProductionPowerInputs = 2;
    private final static int maxProductionPowerOutputs = 3;

    private boolean actionDone;
    private DevSlot[] devSlots;
    private Shelf[] shelves;
    private Depot depot;
    private ArrayList<LeaderCard> leaderCards;
    private PopeFavorCard[] popeFavorCards;
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

    private void initialisePopeFavorCards(){
        this.popeFavorCards = new PopeFavorCard[]{
                new PopeFavorCard(2),
                new PopeFavorCard(3),
                new PopeFavorCard(4),
        };
    }

    //---Constructor---
    public RealPlayer(String nickname){
       super(nickname);
       this.actionDone = false;
       this.initialiseDevSlots();
       this.initialiseShelves();
       this.depot = new Depot();
       this.leaderCards = new ArrayList<>();
       this.initialisePopeFavorCards();
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
    public ProductionPower getBasicProductionPower(){
        return this.basicProductionPower;
    }
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
    public LinkedList<ProductionPower> getAllProductionPowers(){
        return this.calculateAllProductionPowers();
    }
    public EnumMap<Resource, Integer> getResourcesOwned(){
        return this.resourcesOwned();
    }
    public PopeFavorCard[] getPopeFavorCards(){
        return Arrays.copyOf(this.popeFavorCards, this.popeFavorCards.length);
    }
    //-----

    // returns null if the player owns no Resources, otherwise it will return an EnumMap with the copy of all resources
    private EnumMap<Resource, Integer> resourcesOwned() {
        Depot allResources = new Depot();

        if (!this.depot.isEmpty())
            allResources.addEnumMap(this.depot.content());

        for (Shelf shelf : this.shelves)
            if (!shelf.isEmpty())
                allResources.addEnumMap(shelf.content());

        for (LeaderCard leaderCard : leaderCards) {
            if (leaderCard.hasBeenPlayed()) {
                try {
                    if (!leaderCard.getAbility().isEmpty())
                        allResources.addEnumMap(leaderCard.getAbility().getContent());
                } catch (WeDontDoSuchThingsHere e) {
                    e.printStackTrace();
                }
            }
            if (allResources.isEmpty())
                throw new BrokenPlayerException();
        }
        return allResources.content();
    }

    //it can be used to know if the player owns any Resource
    //public boolean isBroken(){
    //    return (this.resourcesOwned() == null);
    //}

    //Returns all the production powers that the player has
    private LinkedList<ProductionPower> calculateAllProductionPowers(){
        LinkedList<ProductionPower> allProductionPowers = new LinkedList<>();

        //default
        allProductionPowers.add(this.basicProductionPower);

        //ProdPowers in devslots
        for (DevSlot devSlot : this.devSlots){
            if (!devSlot.isEmpty()){
                //here i should have a dev card
                allProductionPowers.add(devSlot.topCard().getProdPower());
            }
        }

        //leaderCards
        for (LeaderCard leaderCard : this.leaderCards){
            if (leaderCard.hasBeenPlayed()) {
                try {
                    //no Exceptions -> the leaderCard type = ProdPower
                    allProductionPowers.add(leaderCard.getAbility().getProductionPower());
                } catch (WeDontDoSuchThingsHere e) {
                    //pass
                }
            }
        }
        return allProductionPowers;
    }
}
