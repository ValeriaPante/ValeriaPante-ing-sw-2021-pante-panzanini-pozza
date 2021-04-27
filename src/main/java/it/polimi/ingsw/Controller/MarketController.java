package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Abilities.Ability;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Deposit.StrongBox;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;

import java.util.ArrayList;
import java.util.EnumMap;

public class MarketController extends SelectionController{
//    2 tipi di metodi: uno per mettere le cose dentro la shelf, uno per togliere da dentro la shelf, in entrambi i casi bisogna
//    fare l'azione opposta dalla mappa delle risorse ricevute dal mercato
//    __ funzionamento attraverso selezione
//
//    metodo che elimina la mappa delle risorse da aggiungere ma prima conta le contenute per poter dare eventuali punti vittoria agli altri player
//
//
//
//
//
//


    public MarketController(Table table){
        super(table);
    }

//    public int done(){
//        int resourcesNotPlaced = stillToBeSetBox.countAll();
//        this.clear();
//        return resourcesNotPlaced;
//    }













    //moves all the resources selected from leader deposit and shelves to supportContainer (in player of turn)
    public void moveSelectedToSupportContainer(){
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
        LeaderCard specifiedLeaderCard = getUsableLeaderCard(serial);
        if(specifiedLeaderCard == null)
            return;

        enumMap = table.turnOf().getSupportContainer().getSelection();
        if (enumMap == null)
            //Error message: "No resources selected to be moved"
            return;

        try {
            Depot depot = new Depot();
            depot.addEnumMap(enumMap);
            try{
                depot.addEnumMap(specifiedLeaderCard.getAbility().getContent());
            } catch (NullPointerException ignored){}
            enumMap = depot.content();
            depot.clearDepot();
            depot.addEnumMap(specifiedLeaderCard.getAbility().getCapacity());
            try{
                depot.removeEnumMapIfPossible(enumMap);
            } catch (IndexOutOfBoundsException e){
                //Error message: "Can't move resources there"
                return;
            }
            addEnumMapToLC(specifiedLeaderCard.getAbility(), table.turnOf().getSupportContainer().getSelection());
            table.turnOf().getSupportContainer().pay();
        } catch (WeDontDoSuchThingsHere e){
            //Error message: "Wrong leader card"
            return;
        }
    }

    //un metodo exchange per scambiare le risorse contenute in due contenitori: deve controllare che le risorse siano selezionate solo in due contenitori
    //poi deve controllare che lo spostamento sia fattibile, poi deve spostarle senza perderne traccia e poi deve fare deselezione nei due contenitori

    public void exchange(){
        //controllo che ci siano solo due contenitori con selezioni e me li segno
        int numOfContainersSelected = 0;
        ArrayList<Shelf> shelvesSelected = new ArrayList<>();
        ArrayList<LeaderCard> leaderCardsSelected = new ArrayList<>();

        for (Shelf s: table.turnOf().getShelves()) {
            if (s.getQuantitySelected() != 0) {
                numOfContainersSelected++;
                shelvesSelected.add(s);
            }
        }
//        if (numOfContainersSelected > 2)
//            //Error message: "More than two selections"
//            return;

        for (LeaderCard lc: table.turnOf().getLeaderCards()){
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

        if (table.turnOf().getSupportContainer().areThereSelections())
            numOfContainersSelected++;

        if (numOfContainersSelected > 2)
            //Error message: "More than two selections"
            return;
        if (numOfContainersSelected < 2)
            //Error message: "Less than two selections"
            return;

        //prendo le risorse selezionate da entrambi e le salvo, provo a metterle nell'altro, togliendo quelle selezionate, nel caso ci siano eccezioni allora annullo
        //altrimenti mantengo la modifica

        if (table.turnOf().getSupportContainer().areThereSelections()){
            enumMap = table.turnOf().getSupportContainer().getSelection();
            if(shelvesSelected.size() == 1){ //Selections in stillToBeSetBox and one Shelf
                Shelf selectedShelf = shelvesSelected.get(0);

                Resource resContainedInSTBSB = movableResource(enumMap, selectedShelf);
                if (resContainedInSTBSB == null)
                    return;

                table.turnOf().getSupportContainer().addEnumMap(selectedShelf.takeSelected());
                selectedShelf.addAllIfPossible(resContainedInSTBSB, enumMap.get(resContainedInSTBSB));
            } else { //Selections in stillToBeSetBox and one leaderCard
                Ability selectedLeaderCardAbility = leaderCardsSelected.get(0).getAbility();

                if (!canLeaderContain(selectedLeaderCardAbility, enumMap))
                    return;

                table.turnOf().getSupportContainer().addEnumMap(selectedLeaderCardAbility.getSelected());
                selectedLeaderCardAbility.pay();
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
                    enumMap = selectedLeaderCardAbility.getSelected();
                    Shelf selectedShelf = shelvesSelected.get(0);

                    Resource resContainedInLSC = movableResource(enumMap, selectedShelf);
                    if (resContainedInLSC == null)
                        return;

                    if (!canLeaderContain(selectedLeaderCardAbility, enumMap))
                        return;

                    table.turnOf().getSupportContainer().addEnumMap(selectedShelf.takeSelected());
                    selectedShelf.addAllIfPossible(resContainedInLSC, enumMap.get(resContainedInLSC));
                } else {//Selections only in LeaderCards
                    Ability leaderAbility1 = leaderCardsSelected.get(0).getAbility();
                    Ability leaderAbility2 = leaderCardsSelected.get(1).getAbility();

                    if (!canLeaderContain(leaderAbility1, leaderAbility2.getSelected()))
                        return;

                    if (!canLeaderContain(leaderAbility2, leaderAbility1.getSelected()))
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
    }

    private synchronized Resource movableResource(EnumMap<Resource, Integer> enumMap, Shelf shelf){
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

    private synchronized boolean canLeaderContain(Ability ability, EnumMap<Resource, Integer> enumMap){
        try{
            Depot depot = new Depot();
            depot.addEnumMap(ability.getCapacity());
            depot.removeEnumMapIfPossible(ability.getContent());
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

    private synchronized void addEnumMapToLC (Ability ability, EnumMap<Resource, Integer> enumMap){
        for (Resource r: Resource.values())
            if (enumMap.containsKey(r))
                for (int i=0; i < enumMap.get(r); i++)
                    ability.add(r);
    }



















    //Transmutation: da rifare
    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced){
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
                toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*toBePlaced.get(Resource.WHITE));
            toBePlaced.remove(Resource.WHITE);
        }
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced, int quantity){
        for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
            toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*quantity);
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction1, LeaderCard leaderCardForAction2, EnumMap<Resource, Integer> toBePlaced, int firstQuantity, int secondQuantity) throws IndexOutOfBoundsException{
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            while(firstQuantity + secondQuantity != toBePlaced.get(Resource.WHITE)){
                System.out.println("The sum doesn't match with the number of whites you gained.");
                throw new IndexOutOfBoundsException();
            }

            applyTransmutationAbility(leaderCardForAction1, toBePlaced, firstQuantity);
            applyTransmutationAbility(leaderCardForAction2, toBePlaced, secondQuantity);
            toBePlaced.remove(Resource.WHITE);

        }
    }
}
