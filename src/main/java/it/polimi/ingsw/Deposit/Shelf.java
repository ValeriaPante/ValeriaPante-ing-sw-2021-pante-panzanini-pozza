package it.polimi.ingsw.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class Shelf implements Payable{
    private final int capacity;
    private int usage;
    private Resource resType;
    //when the Shelf is empty both these statements are true at the same time: usage==0 and resType==null

    public synchronized Resource singleRemove(Resource toBeRemoved) throws IllegalArgumentException, IndexOutOfBoundsException{
        if (usage == 0)
            throw new IndexOutOfBoundsException();
        if (resType != toBeRemoved)     //no need to check if resType == null because it is only if usage == 0
            throw new IllegalArgumentException();

        usage--;
        if (usage == 0)
            resType = null;
        return toBeRemoved;
    }

    public synchronized Resource multiRemove(Resource toBeRemoved, int amount) throws IllegalArgumentException, IndexOutOfBoundsException{
        if (amount > usage)
            throw new IndexOutOfBoundsException();
        if (resType != toBeRemoved)
            throw new IllegalArgumentException();

        usage -= amount;
        if (usage == 0)
            resType = null;
        return toBeRemoved;
    }

    public synchronized void singleAdd(Resource toBeAdded) throws IllegalArgumentException, IndexOutOfBoundsException{
        if ((usage + 1) > capacity)
            throw new IndexOutOfBoundsException();
        if ((resType != null) && (resType != toBeAdded))
            throw new IllegalArgumentException();

        if (resType == null)
            resType = toBeAdded;
        usage++;
    }

    public synchronized void addAllIfPossible(Resource toBeAdded, int amount) throws IllegalArgumentException, IndexOutOfBoundsException{
        if ((usage + amount) > capacity)
            throw new IndexOutOfBoundsException();
        if ((resType != null) && (resType != toBeAdded))
            throw new IllegalArgumentException();

        if (resType == null)
            resType = toBeAdded;
        usage += amount;
    }

    public synchronized int addWhatPossible(Resource toBeAdded, int amount) throws IllegalArgumentException{
        if ((resType != null) && (resType != toBeAdded))
            throw new IllegalArgumentException();

        if (resType == null)
            resType = toBeAdded;
        if ((usage + amount) > capacity){
            int oldUsage = usage;
            usage = capacity;
            return amount - (capacity - oldUsage);
        }
        usage += amount;
        return 0;
    }

    public synchronized EnumMap<Resource, Integer> content() {
        if (usage == 0)
            return null;
        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        tempMap.put(resType, usage);
        return tempMap;
    }

    public synchronized boolean isEmpty() {
        return usage == 0;
    }

    public synchronized int countAll() {
        return usage;
    }

    public synchronized Resource resTypeContained(){
        return resType;
    }

    public synchronized EnumMap<Resource, Integer> rifle (){
        if (usage == 0)
            return null;
        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        tempMap.put(resType, usage);
        usage = 0;
        resType = null;
        return tempMap;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized boolean isAffordable(Resource checkType, int amount){
        return (amount <= usage) && (resType == checkType);
    }

    public synchronized int isMissing(Resource checkType, int amount){
        if ((checkType != resType) || (resType == null))
            return amount;
        return Math.max((amount - usage), 0);
    }

    public Shelf (int capacity){
        this.capacity = capacity;
        this.usage = 0;
        this.resType = null;
    }

    @Override
    public boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if ((checkMap.get(r) != null) && ((r != resType) || (checkMap.get(r) > usage)))
                return false;

        return true;
    }

    @Override
    public void pay(EnumMap<Resource, Integer> removeMap) throws NullPointerException, IllegalArgumentException{
        if (removeMap == null)
            throw new NullPointerException();

        //the following instruction are written so the parameter won't be changed by this method if an Exception is thrown
        EnumMap<Resource, Integer> checkOnlyOneRes;
        checkOnlyOneRes = removeMap.clone();
        checkOnlyOneRes.remove(resType);
        if (checkOnlyOneRes.isEmpty())
            throw new IllegalArgumentException();

        usage =- removeMap.get(resType);
        if (0 == usage)
            resType = null;
    }
}