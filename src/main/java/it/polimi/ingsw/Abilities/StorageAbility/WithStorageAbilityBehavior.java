package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Deposit.Depot;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class WithStorageAbilityBehavior implements StorageAbilityBehavior{

    private enum State{
        SELECTED, NOTSELECTED, UNPRESENT
    }

    private final EnumMap<Resource, State[]> contentState;

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
        //rimuovi tute le risorse selezionate
    }

    public boolean contains(EnumMap<Resource, Integer> checkMap){
        Depot content = new Depot(){{addEnumMap(this.content());}};
        return content.contains(checkMap);
    }

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

    public EnumMap<Resource, Integer> getCapacity(){
        EnumMap<Resource, Integer> result = new EnumMap<>(Resource.class);
        for (Resource resource : this.contentState.keySet()){
            result.put(resource, this.contentState.get(resource).length);
        }
        return result;
    }

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

    public String toString(){
        return "Storage Ability";
    }

    public String toString(int x){
        return this.toString()+x;
    }

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

    public void deselectAll(){
        for (State[] states : this.contentState.values()){
            for (int i=0; i<states.length; i++){
                if (states[i] == State.SELECTED){
                    states[i] = State.NOTSELECTED;
                }
            }
        }
    }

}
