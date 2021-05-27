package AbilitesTest;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumMap;

public class ProductionPowerTest {

    private EnumMap<Resource, Integer> input;
    private EnumMap<Resource, Integer> output;
    private ProductionPower productionPower;

    @BeforeEach
    private void init(){
        this.input = new EnumMap<>(Resource.class){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 5);
        }};
        this.output = new EnumMap<>(Resource.class){{
           put(Resource.COIN, 1);
           put(Resource.SERVANT, 2);
        }};
        this.productionPower = new ProductionPower(this.input, this.output);
    }

    @Test
    public void getInputOutputTest(){
        //verifico che inserisca correttamente l'input
        EnumMap<Resource, Integer> input = this.productionPower.getInput();
        for (Resource resource : Resource.values()){
            if (this.input.containsKey(resource)){
                assertTrue(this.input.containsKey(resource) && input.containsKey(resource));
                assertEquals(this.input.get(resource), input.get(resource));
            }
            else{
                assertTrue(!this.input.containsKey(resource) && !input.containsKey(resource));
            }
        }

        //verifico che inserisca correttamente l'output
        EnumMap<Resource, Integer> output = this.productionPower.getOutput();
        for (Resource resource : Resource.values()){
            if (this.output.containsKey(resource)){
                assertTrue(this.output.containsKey(resource) && output.containsKey(resource));
                assertEquals(this.output.get(resource), output.get(resource));
            }
            else{
                assertTrue(!this.output.containsKey(resource) && !output.containsKey(resource));
            }
        }
    }

    @Test
    public void toStringTest(){
        assertEquals("( STONE:4  SHIELD:5 ) -> ( COIN:1  SERVANT:2 )", this.productionPower.toString());
    }

    @Test
    public void equalsTest(){
        //creo un nuovo productionPower uguale e verifico che l'equals funzioni
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 5);
        }};
        EnumMap<Resource, Integer> output = new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT, 2);
        }};

        assertTrue(this.productionPower.equals(new ProductionPower(input, output)));
    }


    @Test
    public void stabilityTest(){
        //modificando i parametri di input e output il ProductionPower non cambia
        this.input.remove(Resource.STONE);
        assertTrue(this.productionPower.getInput().containsKey(Resource.STONE));
        this.output.remove(Resource.COIN);
        assertTrue(this.productionPower.getOutput().containsKey(Resource.COIN));
    }

}
