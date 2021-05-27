package DepositTest;

import it.polimi.ingsw.Model.Deposit.Depot;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class DepotTest {
    private Depot testingDepot;

    //MAYBE THIS IS NOT NEEDED, I prefer writing the line at the beginning of the test so I don't forget
    //@BeforeEach
    //public void initialise() throws Exception{
    //    this.testingDepot = new Depot();
    //}

    @Test
    @DisplayName("content() test")
    public void testCT() {
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 8);
            put(Resource.STONE, 8);
            put(Resource.SHIELD, 8);
        }};
        testingDepot = new Depot(){{
            addEnumMap(enumMap);
            singleRemove(Resource.COIN);
            singleRemove(Resource.COIN);
            singleAdd(Resource.COIN);
            singleAdd(Resource.COIN);
            singleAdd(Resource.FAITH);
            singleRemove(Resource.FAITH);
        }};
        EnumMap<Resource, Integer> enumMapsContent = this.testingDepot.content();

        assertEquals(enumMapsContent, enumMap);

        enumMapsContent.clear();

        assertEquals(this.testingDepot.content(), enumMap);

        Depot nullDepot = new Depot();

        assertNull(nullDepot.content());
    }

    @Test
    @DisplayName("singleAdd test")
    public void testSA(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }};
        testingDepot = new Depot(){{
            singleAdd(Resource.COIN);
            singleAdd(Resource.SERVANT);
            singleAdd(Resource.STONE);
            singleAdd(Resource.SHIELD);
        }};

        assertEquals(this.testingDepot.content(), enumMap);
    }

    @Test
    @DisplayName("singleRemove test (Exceptions and happy flow)")
    public void testSR(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT, 1);
            put(Resource.STONE, 1);
        }};
        testingDepot = new Depot(){{
            singleAdd(Resource.COIN);
            singleAdd(Resource.COIN);
            singleAdd(Resource.COIN);
            singleAdd(Resource.SERVANT);
            singleAdd(Resource.STONE);
            singleAdd(Resource.SHIELD);
            singleRemove(Resource.SHIELD);
            singleRemove(Resource.COIN);
            singleRemove(Resource.COIN);
        }};

        assertEquals(this.testingDepot.content(), enumMap);
        assertEquals(this.testingDepot.singleRemove(Resource.COIN), Resource.COIN);
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingDepot.singleRemove(Resource.FAITH));
    }

    @Test
    @DisplayName("addEnumMap test (Exceptions and happy flow)")
    public void testAE(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 5);
        }};
        testingDepot = new Depot(){{
            addEnumMap(enumMap);
        }};

        assertEquals(this.testingDepot.content(), enumMap);

        //the two containers are modified separately
        enumMap.put(Resource.SHIELD, 4);
        testingDepot.singleRemove(Resource.SHIELD);

        assertEquals(this.testingDepot.content(), enumMap);
        assertThrows(NullPointerException.class, () -> this.testingDepot.addEnumMap(null));

        enumMap.clear();
        testingDepot.clearDepot();
        Integer outsideChange = 4;
        enumMap.put(Resource.SERVANT, outsideChange);
        testingDepot.addEnumMap(enumMap);

        assertEquals(this.testingDepot.content(), enumMap);

        outsideChange = 10;

        assertEquals(this.testingDepot.content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 4);
        }});
    }

    @Test
    @DisplayName("removeEnumMapIfPossible test (Exceptions and happy flow)")
    public void testREIP(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 5);
        }};
        testingDepot = new Depot(){{
            addEnumMap(enumMap);
        }};

        assertEquals(this.testingDepot.removeEnumMapIfPossible(enumMap), enumMap);
        assertNull(this.testingDepot.content());

        testingDepot.addEnumMap(enumMap);
        enumMap.put(Resource.COIN, 1);

        assertEquals(this.testingDepot.removeEnumMapIfPossible(enumMap), enumMap);
        assertEquals(this.testingDepot.content(), new EnumMap<Resource, Integer>(Resource.class) {{ put(Resource.COIN, 2); }});

        testingDepot.addEnumMap(enumMap);
        enumMap.put(Resource.COIN, 10);

        assertThrows(IndexOutOfBoundsException.class, () -> this.testingDepot.removeEnumMapIfPossible(enumMap));
        assertThrows(NullPointerException.class, () -> this.testingDepot.removeEnumMapIfPossible(null));
    }

    @Test
    @DisplayName("removeEnumMapWhatPossible test (Exceptions and happy flow)")
    public void testREWP(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 5);
        }};
        testingDepot = new Depot(){{
            addEnumMap(enumMap);
        }};

        assertNull(this.testingDepot.removeEnumMapWhatPossible(enumMap));
        assertNull(this.testingDepot.content());

        testingDepot.addEnumMap(enumMap);
        enumMap.remove(Resource.STONE);

        assertNull(this.testingDepot.removeEnumMapWhatPossible(enumMap));
        assertEquals(this.testingDepot.content(), new EnumMap<Resource, Integer>(Resource.class){{ put(Resource.STONE, 2); }});

        testingDepot.addEnumMap(enumMap);
        enumMap.put(Resource.STONE, 5);

        assertEquals(this.testingDepot.removeEnumMapWhatPossible(enumMap), new EnumMap<Resource, Integer>(Resource.class){{ put(Resource.STONE, 3); }});
        assertNull(this.testingDepot.content());
        assertThrows(NullPointerException.class, () -> this.testingDepot.removeEnumMapWhatPossible(null));
    }

    @Test
    @DisplayName("isEmpty() test")
    public void testIE(){
        testingDepot = new Depot();

        assertTrue(this.testingDepot.isEmpty());

        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
        }};
        testingDepot.addEnumMap(enumMap);

        assertFalse(this.testingDepot.isEmpty());
    }

    @Test
    @DisplayName("countAll() test")
    public void testCA(){
        testingDepot = new Depot();

        assertEquals(this.testingDepot.countAll(), 0);

        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 3);
            put(Resource.SERVANT, 1);
        }};
        testingDepot.addEnumMap(enumMap);

        assertEquals(this.testingDepot.countAll(), 9);
    }

    @Test
    @DisplayName("isMissing() test (Exceptions and happy flow)")
    public void testIM(){
        testingDepot = new Depot();
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 3);
            put(Resource.SERVANT, 1);
        }};

        assertEquals(this.testingDepot.isMissing(enumMap), enumMap);

        testingDepot.addEnumMap(enumMap);

        assertNull(this.testingDepot.isMissing(enumMap));
        assertEquals(this.testingDepot.content(), enumMap);

        EnumMap<Resource, Integer> enumMapClone = enumMap.clone();
        enumMap.remove(Resource.COIN);

        assertNull(this.testingDepot.isMissing(enumMap));
        assertEquals(this.testingDepot.content(), enumMapClone);

        enumMap.put(Resource.COIN, 10);

        assertEquals(this.testingDepot.isMissing(enumMap), new EnumMap<Resource, Integer>(Resource.class) {{ put(Resource.COIN, 5); }});
        assertEquals(this.testingDepot.content(), enumMapClone);
        assertThrows(NullPointerException.class, () -> this.testingDepot.isMissing(null));
    }

    @Test
    @DisplayName("contains test (Exceptions and happy flow)")
    public void testCS(){
        testingDepot = new Depot();
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 3);
            put(Resource.SERVANT, 1);
        }};

        assertFalse(this.testingDepot.contains(enumMap));
        assertNull(this.testingDepot.content());

        testingDepot.addEnumMap(enumMap);

        assertTrue(this.testingDepot.contains(enumMap));
        assertEquals(this.testingDepot.content(), enumMap);

        EnumMap<Resource, Integer> enumMapCloned = enumMap.clone();
        enumMap.remove(Resource.SERVANT);

        assertTrue(this.testingDepot.contains(enumMap));
        assertEquals(this.testingDepot.content(), enumMapCloned);

        testingDepot.singleRemove(Resource.COIN);
        enumMapCloned.put(Resource.COIN, 4);

        assertFalse(this.testingDepot.contains(enumMap));
        assertEquals(this.testingDepot.content(), enumMapCloned);
        assertThrows(NullPointerException.class, () -> this.testingDepot.contains(null));
    }

    @Test
    @DisplayName("clearDepot() test")
    public void testCD(){
        testingDepot = new Depot(){{
            singleAdd(Resource.COIN);
            singleAdd(Resource.SHIELD);
            singleAdd(Resource.SERVANT);
        }};

        EnumMap<Resource,Integer> containedEnumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};

        assertEquals(this.testingDepot.content(), containedEnumMap);
        assertFalse(this.testingDepot.isEmpty());
        assertEquals(this.testingDepot.countAll(), 3);

        testingDepot.clearDepot();

        assertTrue(testingDepot.isEmpty());
        assertEquals(this.testingDepot.countAll(), 0);
        assertNotEquals(this.testingDepot.content(), containedEnumMap);
        assertNull(this.testingDepot.content());
    }

    /*@Test
    @DisplayName("pay test (Exceptions and happy flow)")
    public void testP(){
        EnumMap<Resource, Integer> enumMap= new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 5);
            put(Resource.SERVANT, 1);
        }};
        testingDepot = new Depot(){{
            addEnumMap(enumMap);
        }};
        testingDepot.pay(enumMap);

        assertEquals(this.testingDepot.content(), null);

        testingDepot.addEnumMap(enumMap);
        enumMap.remove(Resource.SERVANT);
        testingDepot.pay(enumMap);

        assertEquals(this.testingDepot.content(), new EnumMap<Resource, Integer>(Resource.class) {{ put(Resource.SERVANT, 1); }});
        assertThrows(NullPointerException.class, () -> this.testingDepot.pay(null));
    }*/
}