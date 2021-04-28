package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Deposit.StrongBox;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;

import java.util.EnumMap;

public class SelectionController {
    protected final Table table;
    protected EnumMap<Resource, Integer> enumMap;
    protected FaithTrackController faithTrackController;

    public SelectionController(Table newTable){
        this.table = newTable;
        this.enumMap = new EnumMap<>(Resource.class);
        this.faithTrackController = new FaithTrackController(newTable);
    }

    public LeaderCard getUsableLeaderCard(int serial){
        LeaderCard specifiedLeaderCard = leaderCardFromID(serial);
        if (specifiedLeaderCard == null)
            return null;

        if (!specifiedLeaderCard.hasBeenPlayed())
            //Error message: "Card not played"
            return null;

        return specifiedLeaderCard;
    }

    public LeaderCard leaderCardFromID(int serial){
        boolean ownCard = false;
        LeaderCard specifiedLeaderCard = null;
        for (LeaderCard lc : table.turnOf().getLeaderCards()) {
            if (lc.getId() == serial){
                ownCard = true;
                specifiedLeaderCard = lc;
            }
        }
        if (!ownCard)
            //Error message: "Not own leader card"
            return null;

        return specifiedLeaderCard;
    }

    //takes the shelf with "numberOfShelf" capacity and tries to select one Resource
    public void selectFromShelf(Resource resType, int numberOfShelf){
        Shelf currentShelf = shelfFromCapacity(numberOfShelf);
        if (currentShelf == null)
            return;

        if (currentShelf.getResourceType() != resType)
            //Error message: "Wrong Resource type selected"
            return;

        try{
            currentShelf.singleSelection();
        } catch (IndexOutOfBoundsException e){
            //Error message: "Maximum already selected"
        }
    }

    //takes the leader card with ID "serial" and tries to single select
    public void selectFromLeaderStorage(Resource resType, int serial, int resPosition){
        LeaderCard specifiedLeaderCard = getUsableLeaderCard(serial);
        if(specifiedLeaderCard == null)
            return;

        try{
            if (specifiedLeaderCard.getAbility().getCapacity().containsKey(resType)){
                //Error message: "Resource type note allowed"
                return;
            }
            if (specifiedLeaderCard.getAbility().getContent().containsKey(resType)){
                //Error message: "Resource not contained"
                return;
            }
            enumMap = specifiedLeaderCard.getAbility().getSelected();
            specifiedLeaderCard.getAbility().select(resType, resPosition);
            if (enumMap.equals(specifiedLeaderCard.getAbility().getSelected())){
                //Error message: "Not selectable"
                return;
            }
        } catch (WeDontDoSuchThingsHere e){
            //Error message: "Wrong leader card"
        }
    }

    //selects "quantity" resources of "resType" type from player's StrongBox
    public void selectFromStrongBox(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getStrongBox(), resType, quantity);
    }

    public void selectFromSupportContainer(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getSupportContainer(), resType, quantity);
    }

    //takes the shelf with "numberOfShelf" capacity and tries to DEselect one Resource
    public void deselectFromShelf(Resource resType, int numberOfShelf){
        Shelf currentShelf = shelfFromCapacity(numberOfShelf);
        if (currentShelf == null)
            return;

        if (currentShelf.getResourceType() != resType)
            //Error message: "Wrong Resource type selected"
            return;

        try{
            currentShelf.singleDeselection();
        } catch (IndexOutOfBoundsException e){
            //Error message: "Noting to deselect"
        }
    }

    //DEselects "quantity" resources of "resType" type from player's StrongBox
    public void deselectFromStrongBox(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getStrongBox(), resType,quantity);
    }

    public void deselectFromSupportContainer(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getSupportContainer(), resType,quantity);
    }

    private void selectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if (quantity == 0)
            //Error message: "No quantity specified"
            return;

        enumMap.clear();
        enumMap.put(resType, quantity);

        try{
            strongBox.mapSelection(enumMap);
        } catch (IndexOutOfBoundsException e){
            //Error message: "Selection exceeding limits"
        }
    }

    private void deselectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if (quantity == 0)
            //Error message: "No quantity specified"
            return;

        enumMap.clear();
        enumMap.put(resType, quantity);

        try{
            strongBox.mapDeselection(enumMap);
        } catch (IndexOutOfBoundsException e){
            //Error message: "Deselection exceeding limits"
        }
    }

    public void deselectAllResources(){
        table.turnOf().getStrongBox().clearSelection();
        table.turnOf().getSupportContainer().clearSelection();
        for (Shelf s : table.turnOf().getShelves())
            s.clearSelection();
        for (LeaderCard lc : table.turnOf().getLeaderCards()){
            try{
                lc.getAbility().deselectAll();
            } catch (WeDontDoSuchThingsHere | NullPointerException ignored) {}
        }
    }

    protected Shelf shelfFromCapacity(int capacity){
        if ((capacity > 3) || (capacity < 1))
            //Error message: "Capacity specified is exceeding limits"
            return null;

        Shelf shelfToBeReturned = null;
        for (Shelf s : table.turnOf().getShelves())
            if (s.getCapacity() == capacity)
                shelfToBeReturned = s;

        return shelfToBeReturned;
    }
}
