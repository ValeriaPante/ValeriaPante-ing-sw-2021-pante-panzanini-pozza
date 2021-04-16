package it.polimi.ingsw.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class Depot implements Payable{
    private final EnumMap <Resource, Integer> inside;

    //removes only one resource, returns the resource removed
    public synchronized Resource singleRemove(Resource toBeRemoved) throws IndexOutOfBoundsException{
        if ( !inside.containsKey(toBeRemoved))
            throw new IndexOutOfBoundsException();

        inside.put(toBeRemoved, inside.get(toBeRemoved) - 1);
        removeResourceIfZeroOrLess();

        return toBeRemoved;
    }

    //remove an entire EnumMap (only if all the resources in it are less or equals to the contained), returns the removed map
    public synchronized EnumMap<Resource, Integer> removeEnumMapIfPossible(EnumMap<Resource, Integer> mapToBeRemoved) throws IndexOutOfBoundsException, NullPointerException{
        if (!this.contains(mapToBeRemoved))
            throw new IndexOutOfBoundsException();

        //in this case it is possible to remove the whole enumMap
        this.removeEnumMapWhatPossible(mapToBeRemoved);

        return new EnumMap<>(mapToBeRemoved);
    }

    //removes what can be removed (less or equals to contained), returns an EnumMap with the resources not removed (more than contained)
    public synchronized EnumMap<Resource, Integer> removeEnumMapWhatPossible (EnumMap<Resource, Integer> mapToBeRemoved) throws NullPointerException{
        //this can raise a NullPointerException
        EnumMap<Resource, Integer> notRemoved = isMissing(mapToBeRemoved);

        for (Resource r: Resource.values())
            if ( (mapToBeRemoved.containsKey(r)) && (inside.containsKey(r)) )
                inside.put(r, inside.get(r) - mapToBeRemoved.get(r));

        removeResourceIfZeroOrLess();

        return notRemoved;
    }

    //no check if the Resource passed is null
    public synchronized void singleAdd(Resource toBeAdded) {
        inside.put(toBeAdded, ((inside.get(toBeAdded) == null)? 0 : inside.get(toBeAdded)) + 1);
    }

    public synchronized void addEnumMap(EnumMap<Resource, Integer> mapToBeAdded) throws NullPointerException{
        if (mapToBeAdded == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (mapToBeAdded.containsKey(r))
                inside.put(r, ((inside.get(r) == null)? 0 : inside.get(r)) + mapToBeAdded.get(r));
    }

    //returns an EnumMap with the resources contained
    public synchronized EnumMap<Resource, Integer> content() {
        if ( inside.isEmpty() )
            return null;

        removeResourceIfZeroOrLess();
        return new EnumMap<>(inside);
    }

    public synchronized boolean isEmpty() {
        return inside.isEmpty();
    }

    //returns the number of resources contained (useful for counting victory points)
    public synchronized int countAll() {
        int accumulator = 0;

        for (Resource r : Resource.values())
            if (inside.containsKey(r))
                accumulator += inside.get(r);

        return accumulator;
    }

    //return the resources in the enumMap that are more than contained (null otherwise)
    public synchronized EnumMap<Resource, Integer> isMissing(EnumMap<Resource, Integer> checkEnum) throws NullPointerException{
        if (checkEnum == null)
            throw new NullPointerException();

        EnumMap<Resource, Integer> missingResources = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values())
            if (checkEnum.containsKey(r)){
                if (inside.containsKey(r)) {
                    if (checkEnum.get(r) > inside.get(r))
                        missingResources.put(r, checkEnum.get(r) - inside.get(r));
                }
                else
                    missingResources.put(r, checkEnum.get(r));
            }

        return (missingResources.isEmpty()) ? null : missingResources;
    }

    public Depot(){
        inside = new EnumMap<>(Resource.class);
    }

    @Override
    public synchronized boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (checkMap.containsKey(r) && ( !inside.containsKey(r) || (checkMap.get(r) > inside.get(r))))
                return false;

        return true;
    }

    //this method has to be called only if the EnumMap can be removed!
    @Override
    public void pay(EnumMap<Resource, Integer> removeMap) throws NullPointerException{
        if (removeMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (removeMap.containsKey(r))
                inside.put(r, inside.get(r) - removeMap.get(r));

        removeResourceIfZeroOrLess();
    }

    public String toString(){
        return "Deposit";
    }

    //removes the key if is mapped to zero or less
    private synchronized void removeResourceIfZeroOrLess(){
        for (Resource r : Resource.values())
            if ((inside.get(r) != null) && (inside.get(r) <= 0))
                inside.remove(r);
    }
}