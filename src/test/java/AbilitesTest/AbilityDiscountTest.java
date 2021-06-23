package AbilitesTest;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Abilities.Ability;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
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
        this.ability = new Ability(this.discount, new EnumMap<>(Resource.class), LeaderCardType.DISCOUNT);
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
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getProductionPower());
    }

    @Test
    public void storageAbilityMethod(){
        assertThrows(WrongLeaderCardType.class, () -> this.ability.add(Resource.ANY));
        assertThrows(WrongLeaderCardType.class, () -> this.ability.remove(Resource.ANY));
        assertThrows(WrongLeaderCardType.class, () -> this.ability.isEmpty());
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getContent());
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getCapacity());
        assertThrows(WrongLeaderCardType.class, () -> this.ability.contains(new EnumMap<>(Resource.class)));
        assertThrows(WrongLeaderCardType.class, () -> this.ability.pay());
        assertThrows(WrongLeaderCardType.class, () -> this.ability.select(Resource.ANY, 6));
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getSelected());
        //manca la toString
    }

    @Test
    public void transmutationMethods(){
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getWhiteInto());
    }


}
