package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class Shelf implements Payable{
    private final int capacity;
    private int usage;
    private Resource resType;
    private int quantitySelected;
    //when the Shelf is empty both these statements are true at the same time: usage==0 and resType==null

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
        //else
        usage += amount;
        return 0;
    }

    public synchronized void singleSelection() throws IndexOutOfBoundsException{
        if ((quantitySelected + 1) > usage)
            throw new IndexOutOfBoundsException();

        quantitySelected++;
    }

    public synchronized void multiSelection(int selectedQuantity) throws IndexOutOfBoundsException{
        if ((quantitySelected + selectedQuantity) > usage)
            throw new IndexOutOfBoundsException();

        quantitySelected += selectedQuantity;
    }

    public synchronized void singleDeselection() throws IndexOutOfBoundsException{
        if (quantitySelected < 1)
            throw new IndexOutOfBoundsException();

        quantitySelected--;
    }

    public synchronized void multiDeselection(int deselectedQuantity) throws IndexOutOfBoundsException{
        if ((quantitySelected - deselectedQuantity) < 0)
            throw new IndexOutOfBoundsException();

        quantitySelected -= deselectedQuantity;
    }

    public synchronized EnumMap<Resource, Integer> takeSelected() throws IndexOutOfBoundsException{
        if (quantitySelected == 0)
            throw new IndexOutOfBoundsException();

        EnumMap<Resource, Integer> selectedMap = new EnumMap<>(Resource.class);
        selectedMap.put(resType, quantitySelected);
        usage-=quantitySelected;
        if (usage == 0)
            resType = null;
        quantitySelected = 0;
        return selectedMap;
    }

    public synchronized void clearSelection(){
        quantitySelected = 0;
    }

    public synchronized EnumMap<Resource, Integer> rifle (){
        EnumMap<Resource, Integer> tempMap = this.content();
        usage = 0;
        quantitySelected = 0;
        resType = null;
        return tempMap;
    }

    public synchronized EnumMap<Resource, Integer> content() {
        if (usage == 0)
            return null;

        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        tempMap.put(resType, usage);
        return tempMap;
    }

    @Override
    public synchronized boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if ((checkMap.get(r) != null) && ((r != resType) || (checkMap.get(r) > usage)))
                return false;

        return true;
    }

    @Override
    public synchronized void pay(){
        usage-=quantitySelected;
        if (usage == 0)
            resType = null;
        quantitySelected = 0;
    }

    public synchronized int getCapacity() {
        return capacity;
    }

    public synchronized int getQuantitySelected() {
        return quantitySelected;
    }

    public synchronized int getUsage() {
        return usage;
    }

    public synchronized Resource getResourceType(){
        return resType;
    }

    public synchronized boolean isEmpty() {
        return usage == 0;
    }

    public Shelf (int capacity) throws IndexOutOfBoundsException{
        if ((capacity > 3) || (capacity < 1))
            throw new IndexOutOfBoundsException();

        this.capacity = capacity;
        this.usage = 0;
        this.quantitySelected = 0;
        this.resType = null;
    }

    public String toString(){
        return "Shelf" + capacity;
    }
}