package it.polimi.ingsw.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class StrongBox implements Payable{
    private final Depot inside;
    private final Depot selection;

    public StrongBox(){
        inside = new Depot();
        selection = new Depot();
    }

    public synchronized void singleSelection(Resource resourceSelected) throws IndexOutOfBoundsException{
        selection.singleAdd(resourceSelected);

        if ( !inside.contains(selection.content())){
            selection.singleRemove(resourceSelected);
            throw new IndexOutOfBoundsException();
        }
    }

    public synchronized void mapSelection(EnumMap<Resource, Integer> mapSelected) throws IndexOutOfBoundsException{
        selection.addEnumMap(mapSelected);

        if ( !inside.contains(selection.content())){
            selection.removeEnumMapWhatPossible(mapSelected);
            throw new IndexOutOfBoundsException();
        }
    }

    public synchronized void singleDeselection(Resource resourceSelected) throws IndexOutOfBoundsException{
        if( (selection.content() == null) || (selection.content().get(resourceSelected) == null) || (selection.content().get(resourceSelected) < 1))
            throw new IndexOutOfBoundsException();

        selection.singleRemove(resourceSelected);
    }

    public synchronized void mapDeselection(EnumMap<Resource, Integer> mapSelected) throws IndexOutOfBoundsException{
        if (!selection.contains(mapSelected))
            throw new IndexOutOfBoundsException();

        selection.removeEnumMapWhatPossible(mapSelected);
    }

    public synchronized void addEnumMap(EnumMap<Resource, Integer> toBeAdded){
        inside.addEnumMap(toBeAdded);
    }

    public synchronized void clearSelection(){
        selection.clearDepot();
    }

    public synchronized void clearContent(){
        inside.clearDepot();
    }

    public synchronized EnumMap<Resource, Integer> content(){
        return inside.content();
    }

    public String toString(){
        return "StrongBox";
    }

    public synchronized boolean isEmpty(){
        return inside.isEmpty();
    }

    public synchronized boolean areThereSelections(){
        return !selection.isEmpty();
    }

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
    public synchronized void pay() throws IndexOutOfBoundsException{
        if (selection.isEmpty())
            throw new IndexOutOfBoundsException();

        inside.removeEnumMapIfPossible(selection.content());
        selection.clearDepot();
    }
}
