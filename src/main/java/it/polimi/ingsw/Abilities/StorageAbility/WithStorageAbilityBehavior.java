package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

//per rispettare il concetto di fat controller farei tutto super stupido
//se invece vogliamo fare le cose un po più fat e vogliamo lasciar perdere la parametrizzazione su questo punto
// possiamo usare una shelf il problema è che la shelf per come è fatta non permette una capacità massima ma per tutti i tipi
public class WithStorageAbilityBehavior implements StorageAbilityBehavior{
    private final EnumMap<Resource, Integer> content;
    private final EnumMap<Resource, Integer> capacity;
    private final EnumMap<Resource, Integer> selected;

    public void singleAdd(Resource toBeAdded){
        //per come lo creo non è possibile che la get ritorni null
        content.put(toBeAdded, content.get(toBeAdded)+1);
    }

    public void singleRemove(Resource toBeRemoved){
        //per come lo creo non è possibile che la get ritorni null
        content.put(toBeRemoved, content.get(toBeRemoved)-1);
    }

    public boolean isEmpty(){
        for (Resource resource : content.keySet()){
            if (content.get(resource)>0){
                return false;
            }
        }
        return true;
    }

    public boolean contains(EnumMap<Resource, Integer> checkMap) {
        for (Resource resource : checkMap.keySet()){
            if (!(this.content.get(resource)!=null && this.content.get(resource)>=checkMap.get(resource))){
                return false;
            }
        }
        return true;
    }

    public void pay(EnumMap<Resource, Integer> removeMap) {
        for (Resource resource : removeMap.keySet()){
            this.content.put(resource,this.content.get(resource) - removeMap.get(resource));
        }
    }

    public EnumMap<Resource, Integer> getCapacity(){
        return slimMap(this.capacity);
    }
    public EnumMap<Resource, Integer> content(){
        return slimMap(this.content);
    }
    public EnumMap<Resource, Integer> getSelected(){
        return this.slimMap(this.selected);
    }

    private EnumMap<Resource, Integer> slimMap(EnumMap<Resource, Integer> map){
        EnumMap<Resource, Integer> temp = new EnumMap<>(Resource.class);
        for (Resource resource : map.keySet()){
            if (map.get(resource) != 0){
                temp.put(resource, map.get(resource));
            }
        }
        return temp;
    }

    //complete the map initialising the missing keys as 0
    private void completeTheMAp(EnumMap<Resource, Integer> map){
        for (Resource resource: Resource.values()){
            if (!map.containsKey(resource)){
                map.put(resource, 0);
            }
        }
    }

    public WithStorageAbilityBehavior(EnumMap<Resource, Integer> capacity){
        //il contenuto lo setto a zero
        this.content = new EnumMap<>(Resource.class);
        completeTheMAp(this.content);
        this.capacity = capacity.clone();
        completeTheMAp(this.capacity);
        this.selected = new EnumMap<>(Resource.class);
        completeTheMAp(this.selected);
    }

    @Override
    public void select(Resource resource) throws IllegalArgumentException{
        /*
        if (this.content.get(resource)>0 && this.content.get(resource) - this.selected.get(resource) >= 1){
            this.selected.put(resource, this.selected.get(resource));
        }
        else{
            throw new IllegalArgumentException("Cant select this resource");
        }
         */
        this.selected.put(resource, this.selected.get(resource)+1);
    }

    public void deselect(Resource resource) throws IllegalArgumentException{
        /*
        int amount = this.selected.get(resource);
        if (amount>0){
            this.selected.put(resource, amount-1);
        }
        else{
            throw new IllegalArgumentException("No such resource to deselect");
        }
         */
        this.selected.put(resource, this.selected.get(resource)-1);
    }

    @Override
    public String toString(){
        return "LeaderDeposit";
    }

    @Override
    public String toString(int number){
        return "LeaderDeposit" + number;
    }
}
