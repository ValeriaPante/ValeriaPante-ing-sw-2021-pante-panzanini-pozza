package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Deposit.Shelf;
import it.polimi.ingsw.Model.Deposit.StrongBox;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Game.Table;

import java.util.EnumMap;

/**
 * This controller implements all the business logic linked to selection of resources
 */
public class SelectionController extends CertifiedResourceUsage{
    protected final Table table;
    protected EnumMap<Resource, Integer> enumMap;
    protected FaithTrackController faithTrackController;

    public SelectionController(FaithTrackController ftc){
        this.table = ftc.getTable();
        this.enumMap = new EnumMap<>(Resource.class);
        this.faithTrackController = ftc;
    }

    /**
     *
     * @param serial
     * @return
     */
    protected LeaderCard getUsableLeaderCard(int serial){
        LeaderCard specifiedLeaderCard = leaderCardFromID(serial);
        if (specifiedLeaderCard == null)
            return null;

        if (!specifiedLeaderCard.hasBeenPlayed()){
            table.turnOf().setErrorMessage("Card not played");
            return null;
        }


        return specifiedLeaderCard;
    }

    private boolean resCheck(Resource resource){
        if(super.getLegalResource(resource))
            return true;

        table.turnOf().setErrorMessage("Illegal resource type specified");
        return false;
    }

    protected LeaderCard leaderCardFromID(int serial){
        boolean ownCard = false;
        LeaderCard specifiedLeaderCard = null;
        for (LeaderCard lc : table.turnOf().getLeaderCards()) {
            if (lc.getId() == serial){
                ownCard = true;
                specifiedLeaderCard = lc;
            }
        }
        if (!ownCard) {
            table.turnOf().setErrorMessage("Not own leader card");
            return null;
        }

        return specifiedLeaderCard;
    }

    //takes the shelf with "numberOfShelf" capacity and tries to select one Resource
    protected void selectFromShelf(Resource resType, int numberOfShelf){
        if(!resCheck(resType))
            return;

        Shelf currentShelf = shelfFromCapacity(numberOfShelf);
        if (currentShelf == null)
            return;

        if (currentShelf.getResourceType() != resType){
            table.turnOf().setErrorMessage("Wrong Resource type selected");
            return;
        }

        try{
            currentShelf.singleSelection();
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Maximum already selected");
        }
    }

    //takes the leader card with ID "serial" and tries to single select
    protected void selectFromLeaderStorage(Resource resType, int serial, int resPosition){
        if(!resCheck(resType))
            return;

        LeaderCard specifiedLeaderCard = getUsableLeaderCard(serial);
        if(specifiedLeaderCard == null)
            return;

        try{
            if (!specifiedLeaderCard.getAbility().getCapacity().containsKey(resType)){
                table.turnOf().setErrorMessage("Resource type not allowed");
                return;
            }
            if (!specifiedLeaderCard.getAbility().getContent().containsKey(resType)){
                table.turnOf().setErrorMessage("Resource not contained");
                return;
            }
            enumMap = specifiedLeaderCard.getAbility().getSelected();
            specifiedLeaderCard.getAbility().select(resType, resPosition);
            if (enumMap.equals(specifiedLeaderCard.getAbility().getSelected())){
                table.turnOf().setErrorMessage("Not selectable");
            }
        } catch (WrongLeaderCardType e){
            table.turnOf().setErrorMessage("Wrong leader card");
        }
    }

    //selects "quantity" resources of "resType" type from player's StrongBox
    protected void selectFromStrongBox(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getStrongBox(), resType, quantity);
    }

    protected void selectFromSupportContainer(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getSupportContainer(), resType, quantity);
    }

    //takes the shelf with "numberOfShelf" capacity and tries to DEselect one Resource
    protected void deselectFromShelf(Resource resType, int numberOfShelf){
        if(!resCheck(resType))
            return;

        Shelf currentShelf = shelfFromCapacity(numberOfShelf);
        if (currentShelf == null)
            return;

        if (currentShelf.getResourceType() != resType){
            table.turnOf().setErrorMessage("Wrong Resource type selected");
            return;
        }

        try{
            currentShelf.singleDeselection();
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Noting to deselect");
        }
    }

    //DEselects "quantity" resources of "resType" type from player's StrongBox
    protected void deselectFromStrongBox(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getStrongBox(), resType,quantity);
    }

    protected void deselectFromSupportContainer(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getSupportContainer(), resType,quantity);
    }

    private void selectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if(!resCheck(resType))
            return;

        if (quantity < 1){
            table.turnOf().setErrorMessage("Wrong quantity specified");
            return;
        }

        enumMap.clear();
        enumMap.put(resType, quantity);

        try{
            strongBox.mapSelection(enumMap);
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Selection exceeding limits");
        }
    }

    private void deselectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if(!resCheck(resType))
            return;

        if (quantity < 1){
            table.turnOf().setErrorMessage("Wrong quantity specified");
            return;
        }

        enumMap.clear();
        enumMap.put(resType, quantity);

        try{
            strongBox.mapDeselection(enumMap);
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Deselection exceeding limits");
        }
    }

    protected void deselectAllResources(){
        table.turnOf().getStrongBox().clearSelection();
        table.turnOf().getSupportContainer().clearSelection();
        for (Shelf s : table.turnOf().getShelves())
            s.clearSelection();
        for (LeaderCard lc : table.turnOf().getLeaderCards()){
            try{
                lc.getAbility().deselectAll();
            } catch (WrongLeaderCardType | NullPointerException ignored) {}
        }
    }

    protected Shelf shelfFromCapacity(int capacity){
        if ((capacity > 3) || (capacity < 1)){
            table.turnOf().setErrorMessage("Capacity specified is exceeding limits");
            return null;
        }

        Shelf shelfToBeReturned = null;
        for (Shelf s : table.turnOf().getShelves())
            if (s.getCapacity() == capacity)
                shelfToBeReturned = s;

        return shelfToBeReturned;
    }
}
