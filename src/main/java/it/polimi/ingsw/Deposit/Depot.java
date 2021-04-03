package it.polimi.ingsw.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class Depot implements Payable{
    private final EnumMap <Resource, Integer> inside;

    public synchronized Resource singleRemove(Resource toBeRemoved) throws IndexOutOfBoundsException{
        if (inside.get(toBeRemoved) == null)
            throw new IndexOutOfBoundsException();

        inside.put(toBeRemoved, inside.get(toBeRemoved) - 1);
        if (inside.get(toBeRemoved) == 0)
            inside.remove(toBeRemoved);
        return toBeRemoved;
    }

    public synchronized EnumMap<Resource, Integer> removeEnumMapIfPossible(EnumMap<Resource, Integer> mapToBeRemoved) throws IndexOutOfBoundsException, NullPointerException{
        if (mapToBeRemoved == null)
            throw new NullPointerException();
        for (Resource r : Resource.values())
            if (mapToBeRemoved.containsKey(r) && ( !(inside.containsKey(r)) || (mapToBeRemoved.get(r) > inside.get(r))))
                throw new IndexOutOfBoundsException();

        for (Resource r : Resource.values())
            if (mapToBeRemoved.containsKey(r)){
                inside.put(r, inside.get(r) - mapToBeRemoved.get(r));
                if (inside.get(r) == 0)
                    inside.remove(r);
            }

        return new EnumMap<>(mapToBeRemoved);
    }

    public synchronized EnumMap<Resource, Integer> removeEnumMapWhatPossible (EnumMap<Resource, Integer> mapToBeRemoved) throws NullPointerException{
        if (mapToBeRemoved == null)
            throw new NullPointerException();

        EnumMap<Resource, Integer> notRemoved = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values())
            if (mapToBeRemoved.containsKey(r)){
                if (inside.containsKey(r)){
                    if (mapToBeRemoved.get(r) > inside.get(r)){
                        notRemoved.put(r, mapToBeRemoved.get(r) - inside.get(r));
                        inside.remove(r);
                    }
                    else if (mapToBeRemoved.get(r) == inside.get(r))
                        inside.remove(r);
                    else
                        inside.put(r, inside.get(r) - mapToBeRemoved.get(r));
                }
                else
                    notRemoved.put(r, mapToBeRemoved.get(r));
            }
        return notRemoved;
    }

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

    public synchronized EnumMap<Resource, Integer> content() {
        if ( inside.isEmpty() )
            return null;

        return new EnumMap<>(inside);
    }

    public synchronized boolean isEmpty() {
        return inside.isEmpty();
    }

    public synchronized int countAll() {
        int accumulator = 0;
        for (Resource r : Resource.values())
            if (inside.get(r) != null)
                accumulator += inside.get(r);
        return accumulator;
    }

    public synchronized boolean isAffordable(EnumMap<Resource, Integer> checkEnum) throws NullPointerException{
        if (checkEnum == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (checkEnum.containsKey(r) && ( !(inside.containsKey(r)) || (checkEnum.get(r) > inside.get(r))))
                return false;

        return true;
    }

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
    public boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if ((checkMap.get(r) != null) && (checkMap.get(r) > inside.get(r)))
                return false;

        return true;
    }

    @Override
    public void pay(EnumMap<Resource, Integer> removeMap) throws NullPointerException{
        if (removeMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (removeMap.get(r) != null)
                inside.put(r, inside.get(r) - removeMap.get(r));
    }
}