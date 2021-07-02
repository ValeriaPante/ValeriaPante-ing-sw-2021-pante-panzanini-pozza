package it.polimi.ingsw.Model.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Deposit.Depot;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class WithStorageAbilityBehavior implements StorageAbilityBehavior{

    private enum State{
        SELECTED, NOTSELECTED, UNPRESENT
    }

    private final EnumMap<Resource, State[]> contentState;

    /**
     * Evaluate the storage emptiness
     * @return true if this storage is empty
     */
    @Override
    public boolean isEmpty(){
        for (Resource resource : contentState.keySet()){
            for (State state : this.contentState.get(resource)){
                if (state != State.UNPRESENT){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Removes the resource selected
     */
    @Override
    public void removeSelected() {
        State[] elements;
        for (Resource resource : this.contentState.keySet()){
            elements = this.contentState.get(resource);
            for (int i=0; i<elements.length; i++){
                if (elements[i] == State.SELECTED){
                   elements[i] = State.UNPRESENT;
                }
            }
        }
    }

    /**
     * Map evaluator
     * @param checkMap map to evaluate
     * @return true if the resources inside the storage contains the map
     */
    @Override
    public boolean contains(EnumMap<Resource, Integer> checkMap){
        Depot content = new Depot();
        content.addEnumMap(this.content());
        return content.contains(checkMap);
    }

    /**
     * Single resource selection
     * @param toSelect resource type
     * @param position position of the resource
     * @throws WrongLeaderCardType if called on an ability that is not Storage type
     */
    @Override
    public void select(Resource toSelect, int position){
        State[] elements = this.contentState.get(toSelect);
        position -= 1;
        if (elements != null){
            if (position<=elements.length && elements[position]!=State.UNPRESENT){
                if (elements[position] == State.NOTSELECTED){
                    elements[position] = State.SELECTED;
                }
                else if (elements[position] == State.SELECTED){
                    elements[position] = State.NOTSELECTED;
                }
            }
        }
    }

    /**
     * Getter
     * @return the content of this storage LeaderCard
     */
    @Override
    public EnumMap<Resource, Integer> content(){
        EnumMap<Resource, Integer> result = new EnumMap<>(Resource.class);
        int amount;
        for (Resource resource : this.contentState.keySet()){
            amount = 0;
            for (State state : this.contentState.get(resource)){
                if (state != State.UNPRESENT){
                    amount += 1;
                }
            }
            if (amount != 0) {
                result.put(resource, amount);
            }
        }
        return result;
    }

    /**
     * Getter
     * @return the content of this storage LeaderCard with null in empty spaces
     */
    @Override
    public Resource[] fullContent() {
        Resource[] fullContent = new Resource[this.contentState.values().stream().mapToInt(value -> value.length).reduce(0, Integer::sum)];
        int i=0;
        for (Map.Entry<Resource, State[]> entry : this.contentState.entrySet()){
            for (State state : entry.getValue()){
                fullContent[i] = (state == State.UNPRESENT) ? null : entry.getKey();
                i++;
            }
        }

        return fullContent;
    }

    /**
     * Getter
     * @return the capacity of this storage LeaderCard
     */
    @Override
    public EnumMap<Resource, Integer> getCapacity(){
        EnumMap<Resource, Integer> result = new EnumMap<>(Resource.class);
        for (Resource resource : this.contentState.keySet()){
            result.put(resource, this.contentState.get(resource).length);
        }
        return result;
    }

    /**
     * Add a resource to this storage LeaderCard
     * @param toAdd resource to add
     */
    @Override
    public void singleAdd(Resource toAdd){
        State[] elements = this.contentState.get(toAdd);
        if (elements!=null){
            for (int i=0; i<elements.length; i++){
                if (elements[i]==State.UNPRESENT){
                    elements[i]=State.NOTSELECTED;
                    return;
                }
            }
        }
    }

    /**
     * Remove a resource to this storage LeaderCard
     * @param toRemove resource to remove
     */
    @Override
    public void singleRemove(Resource toRemove){
        State[] elements = this.contentState.get(toRemove);
        if (elements != null){
            for (int i=0; i<elements.length-1; i++){
                if (elements[i]!=State.UNPRESENT && elements[i+1]==State.UNPRESENT){
                    elements[i] = State.UNPRESENT;
                    return;
                }
            }
            elements[elements.length-1] = State.UNPRESENT;
        }
    }

    /**
     * Getter selected
     * @return Map that contains all the resources selected
     */
    public EnumMap<Resource, Integer> getSelected(){
        EnumMap<Resource, Integer> result = new EnumMap<>(Resource.class);
        int amount;
        for (Resource resource : this.contentState.keySet()){
            amount = 0;
            for (State state : this.contentState.get(resource)){
                if (state == State.SELECTED){
                    amount += 1;
                }
            }
            if (amount!=0){
                result.put(resource, amount);
            }
        }
        return result;
    }

    /**
     * Deselects all the selected resources
     */
    @Override
    public void deselectAll(){
        for (State[] states : this.contentState.values()){
            for (int i=0; i<states.length; i++){
                if (states[i] == State.SELECTED){
                    states[i] = State.NOTSELECTED;
                }
            }
        }
    }

    /**
     * Getter number of resources
     * @return the number af oll the resources inside
     */
    public int countAll(){
        int amount = 0;
        for (State[] state : this.contentState.values()){
            for (State value : state) {
                if (value != State.UNPRESENT) {
                    amount += 1;
                }
            }
        }
        return amount;
    }

    public String toString(){
        return "Storage Ability";
    }

    public String toString(int x){
        return this.toString()+x;
    }

    /**
     * Constructor
     * @param capacity map represent max capacity for each resource
     */
    public WithStorageAbilityBehavior(EnumMap<Resource, Integer> capacity){
        this.contentState = new EnumMap<>(Resource.class);
        ArrayList<State> state = new ArrayList<>();
        for (Resource resource : capacity.keySet()){
            state.clear();
            for (int i=0; i<capacity.get(resource); i++) {
                state.add(State.UNPRESENT);
            }
            this.contentState.put(resource, state.toArray(new State[0]));
        }
    }

}
