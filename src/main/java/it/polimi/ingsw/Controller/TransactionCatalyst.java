package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.EmptyContainerException;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TransactionCatalyst {
    private final Map<Payable, Depot> howToPay;
    private final EnumMap<Resource, Integer> goal;


    public void put(Payable container, EnumMap<Resource, Integer> quantity)throws NullPointerException, IllegalArgumentException{
        if ((container == null) || (quantity == null))
            throw new NullPointerException();

        if ( !(container.contains(quantity)))
            throw new IllegalArgumentException();

        Depot insertingDepot = new Depot();
        insertingDepot.addEnumMap(quantity);
        howToPay.put(container, insertingDepot);
    }

    public void add(Payable container, EnumMap<Resource, Integer> quantity)throws NullPointerException, IllegalArgumentException{
        if ((container == null) || (quantity == null))
            throw new NullPointerException();

        //the following instructions can raise an IllegalArgumentException because of "put"
        if ( !howToPay.containsKey(container)){
            this.put(container, quantity);
            return;
        }

        //the following instructions can raise an IllegalArgumentException because of "put"
        Depot mergedDepot = new Depot();
        mergedDepot.addEnumMap(quantity);
        mergedDepot.addEnumMap(howToPay.get(container).content());
        this.put(container, mergedDepot.content());
    }

    public void subtract(Payable container, EnumMap<Resource, Integer> quantity) throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException{
        if ((container == null) || (quantity == null))
            throw new NullPointerException();

        if ( !howToPay.containsKey(container))
            throw new IllegalArgumentException();

        //the following instruction can raise an IndexOutOfBoundsException because of "removeEnumMapIfPossible"
        howToPay.get(container).removeEnumMapIfPossible(quantity);
    }

    public void remove(Payable container) throws NullPointerException{
        if (container == null)
            throw new NullPointerException();

        howToPay.remove(container);
    }

    private EnumMap<Resource, Integer> countAll(){
        Depot amount = new Depot();

        for (Map.Entry<Payable, Depot> elementOfMap : howToPay.entrySet())
            amount.addEnumMap(elementOfMap.getValue().content());

        return amount.content();
    }

    private boolean isCommittable(){
        return goal.equals(this.countAll());
    }

    public void commit() throws IndexOutOfBoundsException{
        if ( !this.isCommittable())
            throw new IndexOutOfBoundsException();

        for (Map.Entry<Payable, Depot> elementOfMap : howToPay.entrySet())
            elementOfMap.getKey().pay(elementOfMap.getValue().content());
    }

    //this method will not return a copy of the Payable, so be careful
    public Map<Payable, EnumMap<Resource, Integer>> getContent() throws EmptyContainerException {
        if (howToPay.isEmpty())
            throw new EmptyContainerException();

        Map<Payable, EnumMap<Resource, Integer>> mapToBeReturned = new HashMap<>();
        for (Map.Entry<Payable, Depot> elementOfMap : howToPay.entrySet())
            mapToBeReturned.put(elementOfMap.getKey(), elementOfMap.getValue().content());

        return mapToBeReturned;
    }

    public TransactionCatalyst(EnumMap<Resource, Integer> cost){
        this.goal = cost.clone();
        this.howToPay = new HashMap<>();
    }
}
