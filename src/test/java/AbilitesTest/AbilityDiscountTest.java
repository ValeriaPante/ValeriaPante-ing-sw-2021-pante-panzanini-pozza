package AbilitesTest;

import it.polimi.ingsw.Abilities.Ability;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;

public class AbilityDiscountTest {

    private Ability ability;
    private final EnumMap<Resource, Integer> discount = new EnumMap<>(Resource.class){{
        put(Resource.COIN, 3);
        put(Resource.STONE, 1);
    }};

    @BeforeEach
    public void init(){
        this.ability = new Ability(this.discount, LeaderCardType.DISCOUNT);
    }

    @Test
    public void discountsMethods(){
        //verifico che non crei eccezione
        assertAll(()->this.ability.getDiscount());

        //verifico che il discount nell'abilità sia uguale al parametro quello inserito
        for (Resource resource : Resource.values()){
            if (this.discount.containsKey(resource)){
                assertTrue(this.discount.containsKey(resource) && this.ability.getDiscount().containsKey(resource));
                assertEquals(this.ability.getDiscount().get(resource), this.discount.get(resource));
            }
            else{
                assertTrue(!this.discount.containsKey(resource) && !this.ability.getDiscount().containsKey(resource));
            }
        }

        //verifico che modificando l'input non cambia l'oggetto
        this.discount.remove(Resource.STONE);
        assertTrue(this.ability.getDiscount().containsKey(Resource.STONE));

        //verifico che modificando il getDiscount non modifico l'oggetto stesso
        EnumMap<Resource, Integer> clone = this.ability.getDiscount();
        clone.remove(Resource.STONE);
        assertTrue(this.ability.getDiscount().containsKey(Resource.STONE));

        //MANCA LA TO STRING
    }

    @Test
    public void productionPowerMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getProductionPower());
    }

    @Test
    public void storageAbilityMethod(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.add(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.remove(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.isEmpty());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getContent());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getCapacity());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.contains(new EnumMap<>(Resource.class)));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.pay(new EnumMap<>(Resource.class)));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.select(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.deselect(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getSelected());
    }

    @Test
    public void transmutationMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getWhiteInto());
    }


}
