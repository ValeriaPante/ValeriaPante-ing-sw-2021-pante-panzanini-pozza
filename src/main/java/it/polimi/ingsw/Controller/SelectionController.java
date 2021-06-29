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
     *Converts an identifier into the correspondent leader card if the card
     * is owned by the player of turn and is played
     * @param serial unique identifier of the leader card
     * @return the correspondent leader card if the card with identifier equals to "serial"
     * is owned by the player of turn and was played, null otherwise
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

    /**
     * Checks id the resource passed as a parameter is different from white, any or faith
     * @param resource a resource to be checked
     * @return ture if the resource specified is "legal", false otherwise
     */
    private boolean resCheck(Resource resource){
        if(super.getLegalResource(resource))
            return true;

        table.turnOf().setErrorMessage("Illegal resource type specified");
        return false;
    }

    /**
     * Converts an identifier into the correspondent leader card if the card
     * is owned by the player of turn
     * @param serial unique identifier of the leader card
     * @return the correspondent leader card if the card with identifier equals to "serial"
     * is owned by the player of turn, null otherwise
     */
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

    /**
     *Takes the shelf with "numberOfShelf" capacity and tries to select one Resource of the specified type
     * @param resType type of the resource to be selected
     * @param numberOfShelf capacity of the target shelf
     */
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

    /**
     *Takes the leader card with ID "serial" and tries to single select a resource
     * @param resType type of the resource to be selected
     * @param serial unique identifier of the leader card (that should have storage type ability)
     * @param resPosition position of the resource to be selected in the storage ability of the leader card specified
     */
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

    /**
     *Selects "quantity" resources of "resType" type from player's StrongBox
     * @param resType resource type to be selected
     * @param quantity quantity of resources of the specified type to be selected
     */
    protected void selectFromStrongBox(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getStrongBox(), resType, quantity);
    }

    /**
     *Selects "quantity" resources of "resType" type from player's support container
     * @param resType resource type to be selected
     * @param quantity quantity of resources of the specified type to be selected
     */
    protected void selectFromSupportContainer(Resource resType, int quantity){
        selectFromAStrongBox(table.turnOf().getSupportContainer(), resType, quantity);
    }

    /**
     *Takes the shelf with "numberOfShelf" capacity and tries to Deselect one Resource of the specified type
     * @param resType type of the resource to be deselected
     * @param numberOfShelf capacity of the target shelf on which perform the deselection
     */
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

    /**
     *Deselects "quantity" resources of "resType" type from player's StrongBox
     * @param resType resource type to be deselected
     * @param quantity quantity of resources of the specified type to be deselected
     */
    protected void deselectFromStrongBox(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getStrongBox(), resType,quantity);
    }

    /**
     *Deselects "quantity" resources of "resType" type from player's support container
     * @param resType resource type to be deselected
     * @param quantity quantity of resources of the specified type to be deselected
     */
    protected void deselectFromSupportContainer(Resource resType, int quantity){
        deselectFromAStrongBox(table.turnOf().getSupportContainer(), resType,quantity);
    }

    /**
     *Selects "quantity" resources of "resType" type from an object whose type is "StrongBox"
     * (so the strongbox and the support container of the player)
     * @param resType resource type to be selected
     * @param quantity quantity of resources of the specified type to be selected
     */
    private void selectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if(!resCheck(resType))
            return;

        if (quantity < 1){
            table.turnOf().setErrorMessage("Wrong quantity specified");
            return;
        }

        if(enumMap != null){
            enumMap.clear();
        } else {
            enumMap = new EnumMap<>(Resource.class);
        }
        enumMap.put(resType, quantity);


        try{
            strongBox.mapSelection(enumMap);
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Selection exceeding limits");
        }
    }

    /**
     *Deselects "quantity" resources of "resType" type from an object whose type is "StrongBox"
     * (so the strongbox and the support container of the player)
     * @param resType resource type to be deselected
     * @param quantity quantity of resources of the specified type to be deselected
     */
    private void deselectFromAStrongBox(StrongBox strongBox, Resource resType, int quantity){
        if(!resCheck(resType))
            return;

        if (quantity < 1){
            table.turnOf().setErrorMessage("Wrong quantity specified");
            return;
        }

        if(enumMap != null){
            enumMap.clear();
        } else {
            enumMap = new EnumMap<>(Resource.class);
        }
        enumMap.put(resType, quantity);

        try{
            strongBox.mapDeselection(enumMap);
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Deselection exceeding limits");
        }
    }

    /**
     * Clears all the selections of resources in player of turn's containers
     */
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

    /**
     *Returns a shelf by passing its capacity as a parameter
     * @param capacity capacity of the target shelf
     * @return the target shelf or null if the capacity request does not correspond to a shelf
     */
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
