package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

public class Depot{
    private final EnumMap <Resource, Integer> inside;

    /**
     * Removes only one resource, returns the resource removed
     * @param toBeRemoved resource type of the resource to be removed
     * @return the resource removed
     * @throws IndexOutOfBoundsException if the "Depot" does not contain that resource
     */
    public synchronized Resource singleRemove(Resource toBeRemoved) throws IndexOutOfBoundsException{
        if ( !inside.containsKey(toBeRemoved))
            throw new IndexOutOfBoundsException();

        inside.put(toBeRemoved, inside.get(toBeRemoved) - 1);
        removeResourceIfZeroOrLess();

        return toBeRemoved;
    }

    /**
     * Removes an entire EnumMap (only if all the resources in it are less or equals to the contained in the "Depot"), returns the removed map
     * @param mapToBeRemoved the enumMap of resources to be removed from the depot
     * @return the enumMap removed
     * @throws IndexOutOfBoundsException if the quantity specified for deletion is exceeding th quantity contained
     * @throws NullPointerException if a parameter equals to null is passed
     */
    public synchronized EnumMap<Resource, Integer> removeEnumMapIfPossible(EnumMap<Resource, Integer> mapToBeRemoved) throws IndexOutOfBoundsException, NullPointerException{
        if (!this.contains(mapToBeRemoved))
            throw new IndexOutOfBoundsException();

        //in this case it is possible to remove the whole enumMap
        this.removeEnumMapWhatPossible(mapToBeRemoved);

        return new EnumMap<>(mapToBeRemoved);
    }

    /**
     * Removes what can be removed (less or equals to contained)
     * @param mapToBeRemoved the enumMap of resources to be removed from the depot
     * @return an EnumMap with the resources not removed (more than contained)
     * @throws NullPointerException if a parameter equals to null is passed
     */
    public synchronized EnumMap<Resource, Integer> removeEnumMapWhatPossible (EnumMap<Resource, Integer> mapToBeRemoved) throws NullPointerException{
        //this can raise a NullPointerException
        EnumMap<Resource, Integer> notRemoved = isMissing(mapToBeRemoved);

        for (Resource r: Resource.values())
            if ( (mapToBeRemoved.containsKey(r)) && (inside.containsKey(r)) )
                inside.put(r, inside.get(r) - mapToBeRemoved.get(r));

        removeResourceIfZeroOrLess();

        return notRemoved;
    }

    /**
     * Add a single resource to the depot, does not check if the Resource passed is null
     * @param toBeAdded resource to be added in the depot
     */
    public synchronized void singleAdd(Resource toBeAdded) {
        inside.put(toBeAdded, (inside.getOrDefault(toBeAdded, 0)) + 1);
    }

    /**
     * Adds an entire enumMap to the depot
     * @param mapToBeAdded enumMap to be added in the depot
     * @throws NullPointerException if a null papameter is passed
     */
    public synchronized void addEnumMap(EnumMap<Resource, Integer> mapToBeAdded) throws NullPointerException{
        if (mapToBeAdded == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (mapToBeAdded.containsKey(r))
                inside.put(r, (inside.getOrDefault(r, 0)) + mapToBeAdded.get(r));
    }

    /**
     *
     * @return an EnumMap with the resources contained in the deposit, null if the depot is empty
     */
    public synchronized EnumMap<Resource, Integer> content() {
        if ( inside.isEmpty() )
            return null;

        removeResourceIfZeroOrLess();
        return new EnumMap<>(inside);
    }

    /**
     *
     * @return true if the depot is empty, false otherwise
     */
    public synchronized boolean isEmpty() {
        return inside.isEmpty();
    }

    /**
     * Counts the resources contained in the depot
     * @return the number of resources contained (useful for counting victory points)
     */
    public synchronized int countAll() {
        int accumulator = 0;

        for (Resource r : Resource.values())
            if (inside.containsKey(r))
                accumulator += inside.get(r);

        return accumulator;
    }

    /**
     * Confronts the content of an enumMap and the content of the depot
     * @param checkEnum target enumMap
     * @return the resources in the enumMap that are more than contained (null otherwise): what is missing in the depot
     * in order to have the same content of the enumMap passed as a parameter
     * @throws NullPointerException is a null parameter is passed
     */
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

    /**
     * Checks if the depot (that is a payable object) contains an enumMap of resources
     * @param checkMap target enumMap
     * @return true if the depot object contains the same or more resources of the type and quantity
     * specified in the enumMap passed as a parameter
     * @throws NullPointerException if a null parameter is passed
     */
    public synchronized boolean contains(EnumMap<Resource, Integer> checkMap) throws NullPointerException{
        if (checkMap == null)
            throw new NullPointerException();

        for (Resource r : Resource.values())
            if (checkMap.containsKey(r) && ( !inside.containsKey(r) || (checkMap.get(r) > inside.get(r))))
                return false;

        return true;
    }

    public String toString(){
        return "Deposit";
    }

    /**
     * Removes the key if is mapped to zero or less in the internal map of this class
     */
    private synchronized void removeResourceIfZeroOrLess(){
        for (Resource r : Resource.values())
            if ((inside.get(r) != null) && (inside.get(r) <= 0))
                inside.remove(r);
    }

    /**
     * Clears the content of the depot
     */
    public synchronized void clearDepot(){
        inside.clear();
    }
}