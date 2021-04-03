package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TransactionCatalyst {
    private Map<Payable, EnumMap<Resource, Integer>> howToPay;
    private EnumMap<Resource, Integer> goal;

    public void put(Payable container, EnumMap<Resource, Integer> quantity)throws NullPointerException, IllegalArgumentException{
        if ((container == null) || (quantity == null))
            throw new NullPointerException();

        if ( !(container.contains(quantity)))
            throw new IllegalArgumentException();

        howToPay.put(container, quantity);
    }

    public void remove(Payable container) throws NullPointerException{
        if (container == null)
            throw new NullPointerException();

        howToPay.remove(container);
    }

    private EnumMap<Resource, Integer> countAll(){
        EnumMap<Resource, Integer> amount = new EnumMap<>(Resource.class);

        for (Map.Entry<Payable, EnumMap<Resource, Integer>> elementOfMap : howToPay.entrySet())
            for (Resource r : Resource.values())
                if (elementOfMap.getValue().get(r) != null)
                    amount.put(r, elementOfMap.getValue().get(r) + ((amount.get(r) == null) ? 0 : amount.get(r)) );

        return amount;
    }

    private boolean isCommittable(){
        return goal.equals( this.countAll() );
    }

    public void commit() throws IndexOutOfBoundsException{
        if ( !isCommittable() )
            throw new IndexOutOfBoundsException();

        for (Map.Entry<Payable, EnumMap<Resource, Integer>> elementOfMap : howToPay.entrySet())
            elementOfMap.getKey().pay(elementOfMap.getValue());
    }

    public TransactionCatalyst(EnumMap<Resource, Integer> cost){
        this.goal = cost.clone();
        this.howToPay = new HashMap<>();
    }
}
