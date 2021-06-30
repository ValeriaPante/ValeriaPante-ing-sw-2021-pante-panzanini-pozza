package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

/**
 * This class is used to represent strongbox and other container with depot-like behaviour
 * in which is possible to perform a selection
 */
public class StrongBox implements Payable{
    private final Depot inside;
    private final Depot selection;

    public StrongBox(){
        inside = new Depot();
        selection = new Depot();
    }

    /**
     * Adds one resource to the selection already existing
     * @param resourceSelected the resource to be selected
     * @throws IndexOutOfBoundsException if it is not possible to select the resource specified
     */
    public synchronized void singleSelection(Resource resourceSelected) throws IndexOutOfBoundsException{
        selection.singleAdd(resourceSelected);

        if ( !inside.contains(selection.content())){
            selection.singleRemove(resourceSelected);
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Adds a map to the selection already existing
     * @param mapSelected the map to be selected
     * @throws IndexOutOfBoundsException if it is not possible to select the enumMap specified
     */
    public synchronized void mapSelection(EnumMap<Resource, Integer> mapSelected) throws IndexOutOfBoundsException{
        selection.addEnumMap(mapSelected);

        if ( !inside.contains(selection.content())){
            selection.removeEnumMapWhatPossible(mapSelected);
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Removes a single resource from the existing selection
     * @param resourceSelected the resource to be removed from the selection existing
     * @throws IndexOutOfBoundsException if it is not possible to deselect the resource specified
     */
    public synchronized void singleDeselection(Resource resourceSelected) throws IndexOutOfBoundsException{
        if( (selection.content() == null) || (selection.content().get(resourceSelected) == null) || (selection.content().get(resourceSelected) < 1))
            throw new IndexOutOfBoundsException();

        selection.singleRemove(resourceSelected);
    }

    /**
     * Removes a map from the selection already existing
     * @param mapSelected the map to be deselected
     * @throws IndexOutOfBoundsException if it is not possible to deselect the enumMap specified
     */
    public synchronized void mapDeselection(EnumMap<Resource, Integer> mapSelected) throws IndexOutOfBoundsException{
        if (!selection.contains(mapSelected))
            throw new IndexOutOfBoundsException();

        selection.removeEnumMapWhatPossible(mapSelected);
    }

    /**
     * Adds an enumMap in the strongBox
     * @param toBeAdded target enumMap
     */
    public synchronized void addEnumMap(EnumMap<Resource, Integer> toBeAdded){
        inside.addEnumMap(toBeAdded);
    }

    /**
     * Clears the existing selection, if there was no selection, nothing will change
     */
    public synchronized void clearSelection(){
        selection.clearDepot();
    }

    /**
     * Clears the content of the strongbox, if it was already empty, nothing will change
     */
    public synchronized void clear(){
        inside.clearDepot();
        selection.clearDepot();
    }

    /**
     * Getter for the content of the strongBox
     * @return an enumMap representing the content of the strongBox
     */
    public synchronized EnumMap<Resource, Integer> content(){
        return inside.content();
    }

    public String toString(){
        return "StrongBox";
    }

    public synchronized boolean isEmpty(){
        return inside.isEmpty();
    }

    /**
     * Getter the state of selections
     * @return true if there is something selected, false otherwise
     */
    public synchronized boolean areThereSelections(){
        return !selection.isEmpty();
    }

    /**
     * Counts all the resources contained
     * @return the number of resources contained
     */
    public synchronized int countAll(){
        return inside.countAll();
    }

    public synchronized EnumMap<Resource, Integer> getSelection(){
        return selection.content();
    }

    @Override
    public synchronized boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        return inside.contains(checkMap);
    }

    @Override
    public synchronized void pay(){
        if (selection.isEmpty())
            return;

        inside.removeEnumMapIfPossible(selection.content());
        selection.clearDepot();
    }
}
