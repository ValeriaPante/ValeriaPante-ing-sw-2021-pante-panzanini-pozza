package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

/**
 * This class is used to represent the shelves on player's dashboard
 */
public class Shelf implements Payable{
    private final int capacity;
    private int usage;
    private Resource resType;
    private int quantitySelected;
    //when the Shelf is empty both these statements are true at the same time: usage==0 and resType==null

    /**
     * Adds a single resource in the shelf
     * @param toBeAdded resource to be added in the shelf
     * @throws IllegalArgumentException if the resource is different from the others already inside this shelf
     * @throws IndexOutOfBoundsException if the shelf can contain no more resources
     */
    public synchronized void singleAdd(Resource toBeAdded) throws IllegalArgumentException, IndexOutOfBoundsException{
        if ((usage + 1) > capacity)
            throw new IndexOutOfBoundsException();
        if ((resType != null) && (resType != toBeAdded))
            throw new IllegalArgumentException();

        if (resType == null)
            resType = toBeAdded;
        usage++;
    }

    /**
     * Adds an amount of resources only if it is possible to add all of them (everything or nothing)
     * @param toBeAdded type of resource to be added
     * @param amount amount of resource to add
     * @throws IllegalArgumentException if the resource is different from the others already inside this shelf
     * @throws IndexOutOfBoundsException if it is not possible to add al the specified resources
     */
    public synchronized void addAllIfPossible(Resource toBeAdded, int amount) throws IllegalArgumentException, IndexOutOfBoundsException{
        if ((usage + amount) > capacity)
            throw new IndexOutOfBoundsException();
        if ((resType != null) && (resType != toBeAdded))
            throw new IllegalArgumentException();

        if (resType == null)
            resType = toBeAdded;
        usage += amount;
    }

    /**
     * Adds only the resources that can be added, the rest will not be added
     * @param toBeAdded type of resource to be added
     * @param amount amount of resource to add
     * @throws IllegalArgumentException if the resource is different from the others already inside this shelf
     */
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

    /**
     * Adds a single resource to the existing selection
     * @throws IndexOutOfBoundsException if it is not possible to select more than what was previously selected
     */
    public synchronized void singleSelection() throws IndexOutOfBoundsException{
        if ((quantitySelected + 1) > usage)
            throw new IndexOutOfBoundsException();

        quantitySelected++;
    }

    /**
     * Adds an amount of resources equals to "selectedQuantity" to the existing selection
     * @param selectedQuantity quantity of resource to add to the already existing selection
     * @throws IndexOutOfBoundsException if it is not possible to select the amount of resources specified
     */
    public synchronized void multiSelection(int selectedQuantity) throws IndexOutOfBoundsException{
        if ((quantitySelected + selectedQuantity) > usage)
            throw new IndexOutOfBoundsException();

        quantitySelected += selectedQuantity;
    }

    /**
     * Removes a single resource to the existing selection
     * @throws IndexOutOfBoundsException if it is not possible to deselect anything more
     */
    public synchronized void singleDeselection() throws IndexOutOfBoundsException{
        if (quantitySelected < 1)
            throw new IndexOutOfBoundsException();

        quantitySelected--;
    }

    /**
     * Removes an amount of resources equals to "deselectedQuantity" from the existing selection
     * @param deselectedQuantity quantity of resource to remove from the already existing selection
     * @throws IndexOutOfBoundsException if it is not possible to deselect the amount of resources specified
     */
    public synchronized void multiDeselection(int deselectedQuantity) throws IndexOutOfBoundsException{
        if ((quantitySelected - deselectedQuantity) < 0)
            throw new IndexOutOfBoundsException();

        quantitySelected -= deselectedQuantity;
    }

    /**
     * Clears the selection in the shelf and returns the quantity selected, represented as enumMap
     * @return an enumMap representing the previous selection, now cleared, of the shelf
     * @throws IndexOutOfBoundsException if there was no selection in the shelf
     */
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

    /**
     * Empty the shelf and returns the content of it
     * @return an enumMap representing what was inside the shelf
     */
    public synchronized EnumMap<Resource, Integer> rifle (){
        EnumMap<Resource, Integer> tempMap = this.content();
        usage = 0;
        quantitySelected = 0;
        resType = null;
        return tempMap;
    }

    /**
     * It is a getter for the content of the shelf
     * @return a copy of what is contained in the shelf
     */
    public synchronized EnumMap<Resource, Integer> content() {
        if (usage == 0)
            return null;

        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        tempMap.put(resType, usage);
        return tempMap;
    }

    /**
     * Checks if the shelf contains an enumMap
     * @param checkMap target enumMap
     * @return true if the shelf contains that enumMap, false otherwise
     * @throws NullPointerException if the parameter passed is null
     */
    @Override
    public synchronized boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if ((checkMap.get(r) != null) && ((r != resType) || (checkMap.get(r) > usage)))
                return false;

        return true;
    }

    /**
     * Removes from the shelf the selection, if there was no selection, nothing will be removed:
     * the request will be ignored
     */
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