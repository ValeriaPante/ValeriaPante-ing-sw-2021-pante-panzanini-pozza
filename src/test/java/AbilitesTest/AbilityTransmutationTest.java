package AbilitesTest;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Abilities.Ability;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbilityTransmutationTest {
    private Ability ability;
    private final EnumMap<Resource, Integer> whiteInto = new EnumMap<>(Resource.class){{
        put(Resource.COIN, 3);
        put(Resource.STONE, 1);
    }};

    @BeforeEach
    public void init(){
        this.ability = new Ability(this.whiteInto, new EnumMap<>(Resource.class), LeaderCardType.TRANSMUTATION);
    }

    @Test
    public void discountsMethods(){
        assertThrows(WrongLeaderCardType.class, () -> this.ability.getDiscount());
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
        //verifico che non crei eccezione
        assertAll(()->this.ability.getWhiteInto());

        //verifico che l'input nell'abilità sia uguale al parametro inserito
        assertEquals(this.whiteInto, this.ability.getWhiteInto());

        //verifico che modificando l'input non cambia l'oggetto
        this.whiteInto.remove(Resource.STONE);
        assertTrue(this.ability.getWhiteInto().containsKey(Resource.STONE));

        //MANCA LA TO STRING
    }
}
