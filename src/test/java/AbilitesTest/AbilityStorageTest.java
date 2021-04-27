package AbilitesTest;

import it.polimi.ingsw.Abilities.Ability;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class AbilityStorageTest {
    private Ability ability;
    private final EnumMap<Resource, Integer> capacity = new EnumMap<>(Resource.class){{
        put(Resource.COIN, 3);
        put(Resource.STONE, 1);
    }};

    @BeforeEach
    public void init(){
        this.ability = new Ability(this.capacity, LeaderCardType.STORAGE);
    }

    @Test
    public void discountsMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getDiscount());
    }

    @Test
    public void productionPowerMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getProductionPower());
    }

    @Test
    @DisplayName("Storage singleAdd (Resource toBeAdded) method")
    public void add(){
        //this resource cant stay here, nothing is modified
        this.ability.add(Resource.SERVANT);
        assertTrue(this.ability.getContent().isEmpty());

        //ora mi aspetto che dentro ci sia una stone
        this.ability.add(Resource.STONE);
        assertEquals(new EnumMap<Resource, Integer>(Resource.class){{put(Resource.STONE, 1);}}, this.ability.getContent());

        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        assertEquals(this.capacity, this.ability.getContent());

        //verifico che se faccio la add su qualcosa di pieno non cambia nulla
        this.ability.add(Resource.COIN);
        assertEquals(this.capacity, this.ability.getContent());
    }

    @Test
    @DisplayName("Storage singleRemove(Resource toBeRemoved) method")
    public void remove(){
        //la remove su qualcosa di vuoto non cambia nulla
        this.ability.remove(Resource.COIN);
        assertTrue(this.ability.getContent().isEmpty());

        //verifico di aver tolto un elemento
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.remove(Resource.COIN);
        assertEquals(new EnumMap<Resource, Integer>(Resource.class){{put(Resource.COIN, 1);}}, this.ability.getContent());

        //verifico di aver tolto l'ultima occorrenza di quella risorsa
        this.ability.select(Resource.COIN, 2);
        assertTrue(this.ability.getSelected().isEmpty());
    }

    @Test
    @DisplayName("Storage isEmpty() method")
    public void isEmpty(){
        assertTrue(this.ability.isEmpty());
        this.ability.add(Resource.COIN);
        assertFalse(this.ability.isEmpty());
    }

    @Test
    @DisplayName("Storage content() method")
    public void getContent(){
        assertTrue(this.ability.getContent().isEmpty());

        this.ability.add(Resource.STONE);
        assertInstanceOf(EnumMap.class, this.ability.getContent());
        assertEquals(new EnumMap<Resource, Integer>(Resource.class){{put(Resource.STONE, 1);}}, this.ability.getContent());
    }

    @Test
    @DisplayName("Storage getCapacity() method")
    public void getCapacity(){
        assertEquals(this.capacity, this.ability.getCapacity());
    }

    @Test
    @DisplayName("Storage contains() method")
    public void contains(){
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.STONE);

        assertTrue(this.ability.contains(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }}));
        assertTrue(this.ability.contains(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
        }}));
        assertTrue(this.ability.contains(this.ability.getContent()));
    }

    @Test
    @DisplayName("Storage removeSelected() method")
    public void removeSelected(){
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.STONE);

        this.ability.select(Resource.COIN,2);
        this.ability.select(Resource.STONE, 1);

        this.ability.pay();
        assertTrue(this.ability.getSelected().isEmpty());
        assertEquals(new EnumMap<>(Resource.class){{put(Resource.COIN,1);}}, this.ability.getContent());
    }

    @Test
    @DisplayName("Storage select(Resource toSelect, int position) method")
    public void select(){
        //una risorsa che non c'è, non può essere selezionata
        this.ability.select(Resource.COIN, 1);
        assertTrue(this.ability.getSelected().isEmpty());

        //select the first coin occurrency
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.select(Resource.COIN, 1);
        assertEquals(new EnumMap<Resource, Integer>(Resource.class){{put(Resource.COIN, 1);}}, this.ability.getSelected());

        //deselection
        this.ability.select(Resource.COIN, 1);
        assertTrue(this.ability.getSelected().isEmpty());
    }

    @Test
    @DisplayName("Storage getSelect() method")
    public void getSelected(){
        assertTrue(this.ability.getSelected().isEmpty());

        this.ability.add(Resource.STONE);
        this.ability.select(Resource.STONE, 1);
        assertInstanceOf(EnumMap.class, this.ability.getSelected());
        assertEquals(new EnumMap<Resource, Integer>(Resource.class){{put(Resource.STONE, 1);}}, this.ability.getSelected());
    }

    //manca la toString

    @Test
    @DisplayName("Storage deselectAll() method")
    public void deselectAll(){
        this.ability.add(Resource.STONE);
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);
        this.ability.add(Resource.COIN);

        this.ability.select(Resource.COIN, 2);
        this.ability.select(Resource.COIN, 3);
        this.ability.select(Resource.STONE, 1);

        EnumMap<Resource, Integer> content = this.ability.getContent();
        this.ability.deselectAll();
        assertEquals(content, this.ability.getContent());
        assertTrue(this.ability.getSelected().isEmpty());
    }

    @Test
    public void transmutationMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getWhiteInto());
    }
}
