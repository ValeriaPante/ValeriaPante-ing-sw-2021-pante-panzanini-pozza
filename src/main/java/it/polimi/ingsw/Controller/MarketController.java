package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Abilities.Ability;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Deposit.StrongBox;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.ArrayList;
import java.util.EnumMap;

public class MarketController extends SelectionController{
    public MarketController(Table table){
        super(table);
    }

    public void selectInShelf(Resource resType, int numberOfShelf){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        selectFromShelf(resType, numberOfShelf);
    }

    public void selectIntLeaderStorage(Resource resType, int serial, int resPosition){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        selectFromLeaderStorage(resType, serial, resPosition);
    }

    public void selectInSupportContainer(Resource resType, int quantity){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        selectFromSupportContainer(resType, quantity);
    }

    public void deselectInShelf(Resource resType, int numberOfShelf){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        deselectFromShelf(resType, numberOfShelf);
    }

    public void deselectInSupportContainer(Resource resType, int quantity){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        deselectFromSupportContainer(resType, quantity);
    }

    //moves all the resources selected from leader deposit and shelves to supportContainer (in player of turn)
    public void moveSelectedToSupportContainer(){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        StrongBox supportContainer = table.turnOf().getSupportContainer();

        for (Shelf s : table.turnOf().getShelves()){
            try{
                supportContainer.addEnumMap(s.takeSelected());
            } catch (IndexOutOfBoundsException ignored){}
        }

        for(LeaderCard lc: table.turnOf().getLeaderCards()){
            try{
                supportContainer.addEnumMap(lc.getAbility().getSelected());
                lc.getAbility().pay();
            }
            catch (WeDontDoSuchThingsHere | NullPointerException ignored){}
        }
    }

    //moves the resources selected in supportContainer, in player of turn, into the Shelf with the capacity equals to numberOfShelf
    public void moveToShelf(int numberOfShelf){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        enumMap = table.turnOf().getSupportContainer().getSelection();

        if ((enumMap == null) || (enumMap.size() != 1))
            //Error message: "Wrong selection"
            return;

        Resource resToBeAdded = null;
        for(Resource r : Resource.values())
            if(enumMap.containsKey(r))
                resToBeAdded = r;

        Shelf currentShelf = shelfFromCapacity(numberOfShelf);
        if (currentShelf == null)
            return;

        if (currentShelf.isEmpty())
            for (Shelf s: table.turnOf().getShelves())
                if ((currentShelf != s) && (resToBeAdded == s.getResourceType()))
                    //Error message: "Resource already contained in another Shelf"
                    return;

        try{
            currentShelf.addAllIfPossible(resToBeAdded, enumMap.get(resToBeAdded));
            table.turnOf().getSupportContainer().pay();
        } catch (IllegalArgumentException e){
            //Error message: "Wrong type of resource"
            return;
        }catch (IndexOutOfBoundsException e){
            //Error message: "Insertion exceeding limits"
            return;
        }
    }

    //moves the resources selected in supportContainer, in player of turn, into the leader card with the id equals to the serial number specified
    public void moveToLeaderStorage(int serial){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        LeaderCard specifiedLeaderCard = getUsableLeaderCard(serial);
        if(specifiedLeaderCard == null)
            return;

        enumMap = table.turnOf().getSupportContainer().getSelection();
        if (enumMap == null)
            //Error message: "No resources selected to be moved"
            return;

        if (canLeaderContain(specifiedLeaderCard.getAbility(), enumMap, false) ) {
            addEnumMapToLC(specifiedLeaderCard.getAbility(), table.turnOf().getSupportContainer().getSelection());
            table.turnOf().getSupportContainer().pay();
        }
    }

    //if there are only two containes with and if it is possible to swap the selection, this does it
    public void exchange(){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.PLACERESOURCES)
            return;

        //PART 1: counts the container with resources selected
        //==================================================================================================
        int numOfContainersSelected = 0;
        ArrayList<Shelf> shelvesSelected = new ArrayList<>();
        ArrayList<LeaderCard> leaderCardsSelected = new ArrayList<>();
        RealPlayer player = table.turnOf();

        for (Shelf s: player.getShelves()) {
            if (s.getQuantitySelected() > 0) {
                numOfContainersSelected++;
                shelvesSelected.add(s);
            }
        }
//        if (numOfContainersSelected > 2)
//            //Error message: "More than two selections"
//            return;

        for (LeaderCard lc: player.getLeaderCards()){
            try{
                if ((lc.hasBeenPlayed()) && (lc.getAbility().getSelected() != null)) {
                    numOfContainersSelected++;
                    leaderCardsSelected.add(lc);
                }
            } catch (WeDontDoSuchThingsHere ignored) {}
        }
//        if (numOfContainersSelected == 0)
//            //Error message: "Less than two selections"
//            return;

        if (player.getSupportContainer().areThereSelections())
            numOfContainersSelected++;

        if (numOfContainersSelected > 2)
            //Error message: "More than two selections"
            return;
        if (numOfContainersSelected < 2)
            //Error message: "Less than two selections"
            return;

        //PART 2: checks if it is possible to swap the selected resources and if so proceed
        //==================================================================================================
        if (player.getSupportContainer().areThereSelections()){
            enumMap = player.getSupportContainer().getSelection();
            if(shelvesSelected.size() == 1){ //Selections in stillToBeSetBox and one Shelf
                Shelf selectedShelf = shelvesSelected.get(0);

                Resource resSelectedInSupportContainer = movableResource(enumMap, selectedShelf);
                if (resSelectedInSupportContainer == null)
                    return;

                player.getSupportContainer().addEnumMap(selectedShelf.takeSelected());
                enumMap = player.getSupportContainer().getSelection();
                player.getSupportContainer().pay();
                selectedShelf.addAllIfPossible(resSelectedInSupportContainer, enumMap.get(resSelectedInSupportContainer));
            } else { //Selections in stillToBeSetBox and one leaderCard
                Ability selectedLeaderCardAbility = leaderCardsSelected.get(0).getAbility();

                if (!canLeaderContain(selectedLeaderCardAbility, enumMap, true))
                    return;

                player.getSupportContainer().addEnumMap(selectedLeaderCardAbility.getSelected());
                selectedLeaderCardAbility.pay();
                enumMap = player.getSupportContainer().getSelection();
                player.getSupportContainer().pay();
                addEnumMapToLC(selectedLeaderCardAbility, enumMap);
            }
        } else { //selections between LeaderCards and Shelves
            if (shelvesSelected.size() == 2){ //Selections only in Shelves
                Shelf selectedShelf1 = shelvesSelected.get(0);
                Shelf selectedShelf2 = shelvesSelected.get(1);
                if ((selectedShelf2.getUsage() != selectedShelf2.getQuantitySelected()) || (selectedShelf1.getUsage() != selectedShelf1.getQuantitySelected()))
                    //Error message: "Cannot swap shelves content"
                    return;

                if ((selectedShelf1.getCapacity() < selectedShelf2.getQuantitySelected()) || (selectedShelf2.getCapacity() < selectedShelf1.getQuantitySelected()))
                    //Error message: "Swap is exceeding one shelf limit"
                    return;

                Resource shelf1Type = selectedShelf1.getResourceType();
                int shelf1Usage = selectedShelf1.getUsage();
                selectedShelf1.pay();

                Resource shelf2Type = selectedShelf2.getResourceType();
                int shelf2Usage = selectedShelf2.getUsage();
                selectedShelf2.pay();

                selectedShelf1.addAllIfPossible(shelf2Type, shelf2Usage);
                selectedShelf2.addAllIfPossible(shelf1Type, shelf1Usage);
            } else {
                if (shelvesSelected.size() == 1) { //Selections in one Shelf and one LeaderCard
                    Ability selectedLeaderCardAbility = leaderCardsSelected.get(0).getAbility();
                    Shelf selectedShelf = shelvesSelected.get(0);

                    enumMap = selectedLeaderCardAbility.getSelected();
                    Resource resourceInLeaderCard = movableResource(enumMap, selectedShelf);
                    if (resourceInLeaderCard == null)
                        return;

                    enumMap = selectedShelf.content();
                    if (!canLeaderContain(selectedLeaderCardAbility, enumMap, true))
                        return;

                    EnumMap<Resource, Integer> shelfSelection = selectedShelf.takeSelected();
                    enumMap = selectedLeaderCardAbility.getSelected();
                    selectedShelf.addAllIfPossible(resourceInLeaderCard, enumMap.get(resourceInLeaderCard));
                    selectedLeaderCardAbility.pay();
                    addEnumMapToLC(selectedLeaderCardAbility, shelfSelection);
                } else {//Selections only in LeaderCards
                    Ability leaderAbility1 = leaderCardsSelected.get(0).getAbility();
                    Ability leaderAbility2 = leaderCardsSelected.get(1).getAbility();

                    if (!canLeaderContain(leaderAbility1, leaderAbility2.getSelected(),true))
                        return;

                    if (!canLeaderContain(leaderAbility2, leaderAbility1.getSelected(), true))
                        return;

                    EnumMap<Resource, Integer> enumMap1 = new EnumMap<>(leaderAbility1.getSelected());
                    enumMap = leaderAbility2.getSelected();
                    leaderAbility1.pay();
                    leaderAbility2.pay();
                    addEnumMapToLC(leaderAbility1, enumMap);
                    addEnumMapToLC(leaderAbility2, enumMap1);
                }
            }
        }
        //==================================================================================================
    }

    //checks if it is possible to move the enumMap inside the shelf, if so returns the resource in the map otherwise null
    private Resource movableResource(EnumMap<Resource, Integer> enumMap, Shelf shelf){
        if(enumMap.size() != 1)
            //Error message: "Too many type of resources selected"
            return null;

        Resource resourceContained = null;
        for (Resource r: Resource.values())
            if(enumMap.containsKey(r))
                resourceContained = r;

        if (shelf.getUsage() == shelf.getQuantitySelected()){ //completely empty the shelf
            if (enumMap.get(resourceContained) > shelf.getCapacity())
                //Error message: "Shelf cannot contain that quantity"
                return null;

            for (Shelf s: table.turnOf().getShelves())
                if ((shelf != s) && (resourceContained == s.getResourceType()))
                    //Error message: "Resource already contained in another Shelf"
                    return null;
        } else {
            if (shelf.getResourceType() != resourceContained)
                //Error message: "Wrong type of Resource"
                return null;

            if (enumMap.get(resourceContained) > (shelf.getCapacity() -(shelf.getUsage() - shelf.getQuantitySelected())))
                //Error message: "Shelf cannot contain that quantity"
                return null;
        }
        return resourceContained;
    }

    //checks if it is possible to move the enumMap inside the LeaderCard, if so returns true otherwise false
    private boolean canLeaderContain(Ability ability, EnumMap<Resource, Integer> enumMap, boolean isForExchange){
        try{
            Depot depot = new Depot();
            depot.addEnumMap(ability.getCapacity());
            try {
                depot.removeEnumMapIfPossible(ability.getContent());
            } catch (NullPointerException ignored) {}
            if (isForExchange)
                depot.addEnumMap(ability.getSelected());
            try{
                depot.removeEnumMapIfPossible(enumMap);
                return true;
            } catch (IndexOutOfBoundsException e){
                //Error message: "LeaderCard cannot contain that quantity"
                return false;
            }
        } catch (WeDontDoSuchThingsHere e){
            //Error message: "Wrong leader card"
            return false;
        }
    }

    //adds an enumMap to a leaderCard with storage ability
    private void addEnumMapToLC (Ability ability, EnumMap<Resource, Integer> enumMap){
        for (Resource r: Resource.values())
            if (enumMap.containsKey(r))
                for (int i=0; i < enumMap.get(r); i++)
                    ability.add(r);
    }

    //selects rows or columns in market
    public void selectFromMarket(int number, boolean isRowChosen){
        if (table.turnOf().getMicroTurnType() == MicroTurnType.NONE)
            table.turnOf().setMicroTurnType(MicroTurnType.SELECTIONINMARKET);

        if(number < 1)
            //Error message: "Wrong position specified"
            return;

        if (isRowChosen){
            try{
                table.getMarket().selectRow(number - 1);
            } catch (IndexOutOfBoundsException e) {
                //Error message: "Position specified exceeded limits"
                return;
            }
        } else {
            try{
                table.getMarket().selectColumn(number - 1);
            } catch (IndexOutOfBoundsException e) {
                //Error message: "Position specified exceeded limits"
                return;
            }
        }
    }

    //move the resources selected in the market into player's strongbox
    public void takeFromMarket(){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.SELECTIONINMARKET)
            return;

//        if (!table.getMarket().areThereSelections())
//            //Error message: "No selections specified"
//            return;

        enumMap = table.getMarket().takeSelection();
        table.turnOf().setMicroTurnType(MicroTurnType.PLACERESOURCES);

        if (enumMap.containsKey(Resource.WHITE)){
            int numOfTransmutationAbilities = 0;
            Ability transmutationAbility = null;

            for (LeaderCard lc : table.turnOf().getLeaderCards()){
                try{
                    if (lc.hasBeenPlayed()){
                        lc.getAbility().getWhiteInto();
                        numOfTransmutationAbilities++;
                        transmutationAbility = lc.getAbility();
                    }
                } catch (WeDontDoSuchThingsHere ignored) {}
            }

            if (numOfTransmutationAbilities == 0){
                enumMap.remove(Resource.WHITE);
            } else if (numOfTransmutationAbilities == 1) {
                int numOfWhite = enumMap.remove(Resource.WHITE);
                for (EnumMap.Entry<Resource, Integer> entry : transmutationAbility.getWhiteInto().entrySet())
                    enumMap.put(entry.getKey(), (entry.getValue() * numOfWhite) + (enumMap.getOrDefault(entry.getKey(), 0)));
            } else {
                table.turnOf().setMicroTurnType(MicroTurnType.SELECTLEADERCARD);
            }
        }

        if (enumMap.containsKey(Resource.FAITH))
            faithTrackController.movePlayerOfTurn(enumMap.remove(Resource.FAITH));


        table.turnOf().getSupportContainer().clear();
        table.turnOf().getSupportContainer().addEnumMap(enumMap);
        table.turnOf().setMacroTurnType(MacroTurnType.GETFROMMARKET);
    }

    //si potrebbe usare con if dentro e in base al momento o annulla la selezione oppure trasforma le risorse non piazzate in punti fede
    public void quit(){
        if (table.turnOf().getMicroTurnType() == MicroTurnType.SELECTIONINMARKET){
            table.getMarket().deleteSelection();
            table.turnOf().setMicroTurnType(MicroTurnType.NONE);
        }

        if (table.turnOf().getMicroTurnType() == MicroTurnType.PLACERESOURCES){
            int faithPoints = table.turnOf().getSupportContainer().countAll();
            table.turnOf().getSupportContainer().clear();
            faithTrackController.moveAllTheOthers(faithPoints);
            table.turnOf().setMicroTurnType(MicroTurnType.NONE);
            table.turnOf().setMacroTurnType(MacroTurnType.NONE);
            deselectAllResources();
        }
    }

    //it is compulsory to use this when the player owns two leader cards with transmutation ability played
    public void selectTransmutation (int serial1, int quantity1, int serial2, int quantity2){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.SELECTLEADERCARD)
            return;

        int numOfWhite = table.turnOf().getSupportContainer().content().get(Resource.WHITE);
        if((quantity1 + quantity2) != numOfWhite)
            //Error message: "Wrong amount specified"
            return;

        EnumMap<Resource, Integer> transmutation1 = getTransmutation(serial1);
        if (transmutation1 == null)
            return;

        EnumMap<Resource, Integer> transmutation2 = getTransmutation(serial2);
        if (transmutation2 == null)
            return;

        Depot depot = new Depot();
        for (int i=0; i<quantity1; i++)
            depot.addEnumMap(transmutation1);
        for (int i=0; i<quantity2; i++)
            depot.addEnumMap(transmutation2);
        enumMap = depot.content();

        if (enumMap.containsKey(Resource.FAITH))
            faithTrackController.movePlayerOfTurn(enumMap.remove(Resource.FAITH));

        EnumMap<Resource, Integer> whiteSelection = new EnumMap<>(Resource.class);
        whiteSelection.put(Resource.WHITE, numOfWhite);
        StrongBox sb = table.turnOf().getSupportContainer();
        sb.clearSelection();
        sb.mapSelection(whiteSelection);
        sb.pay();
        sb.addEnumMap(enumMap);
        table.turnOf().setMicroTurnType(MicroTurnType.PLACERESOURCES);
    }

    private EnumMap<Resource, Integer> getTransmutation(int serial){
        Ability ability;
        try{
            ability = getUsableLeaderCard(serial).getAbility();
            return ability.getWhiteInto();
        } catch (NullPointerException e) {
            return null;
        } catch (WeDontDoSuchThingsHere e) {
            //Error message: "The selected leader card cannot transmute"
            return null;
        }
    }
}