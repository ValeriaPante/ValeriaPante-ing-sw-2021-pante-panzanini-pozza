package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import java.util.EnumMap;

//per rispettare il concetto di fat controller farei tutto super stupido
//se invece vogliamo fare le cose un po più fat e vogliamo lasciar perdere la parametrizzazione su questo punto
// possiamo usare una shelf il problema è che la shelf per come è fatta non permette una capacità massima ma per tutti i tipi

public class WithStorageAbilityBehavior implements StorageAbilityBehavior{
    EnumMap<Resource, Integer> content;
    EnumMap<Resource, Integer> capacity;

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

    public EnumMap<Resource, Integer> getCapacity(){
        return this.capacity.clone();
    }
    public EnumMap<Resource, Integer> getContent(){
        return this.content.clone();
    }

    private void initialise(){
        this.content = new EnumMap<Resource, Integer>(Resource.class);
        this.capacity = new EnumMap<Resource, Integer>(Resource.class);
    }

    //complete the map initialising the missing keys as 0
    private void completeTheMAp(EnumMap<Resource, Integer> map){
        EnumMap<Resource, Integer> temp = new EnumMap<Resource, Integer>(Resource.class);
        for (Resource resource: Resource.values()){
            if (!map.keySet().contains(resource)){
                map.put(resource, 0);
            }
        }
    }

    public WithStorageAbilityBehavior(EnumMap<Resource, Integer> capacity){
        initialise();
        //il contenuto lo setto a zero
        completeTheMAp(this.content);
        this.capacity = capacity.clone();
        completeTheMAp(this.capacity);
    }
}
