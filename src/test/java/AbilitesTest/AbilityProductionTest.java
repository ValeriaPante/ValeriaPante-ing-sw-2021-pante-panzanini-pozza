package AbilitesTest;

import it.polimi.ingsw.Model.Abilities.Ability;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbilityProductionTest {
    private Ability ability;
    private final EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class){{
        put(Resource.COIN, 3);
        put(Resource.STONE, 1);
    }};

    @BeforeEach
    public void init(){
        this.ability = new Ability(this.input, LeaderCardType.PRODPOWER);
    }

    @Test
    public void discountsMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getDiscount());
    }

    @Test
    public void productionPowerMethods(){
        //verifico che non crei eccezione
        assertAll(()->this.ability.getProductionPower());

        //verifico che l'input nell'abilità sia uguale al parametro inserito
        assertEquals(this.input, this.ability.getProductionPower().getInput());

        //verifico che l'output sia quello che mi aspetto (1FP, 1ANY)
        EnumMap<Resource, Integer> outputExpected = new EnumMap<>(Resource.class){{
            put(Resource.ANY, 1);
            put(Resource.FAITH, 1);
        }};
        assertEquals(outputExpected, this.ability.getProductionPower().getOutput());


        //verifico che modificando l'input non cambia l'oggetto
        this.input.remove(Resource.STONE);
        assertTrue(this.ability.getProductionPower().getInput().containsKey(Resource.STONE));

        //MANCA LA TOSTRING
    }

    @Test
    public void storageAbilityMethod(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.add(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.remove(Resource.ANY));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.isEmpty());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getContent());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getCapacity());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.contains(new EnumMap<>(Resource.class)));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.pay());
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.select(Resource.ANY, 6));
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getSelected());
        //manca la toString
    }

    @Test
    public void transmutationMethods(){
        assertThrows(WeDontDoSuchThingsHere.class, () -> this.ability.getWhiteInto());
    }



}
