package DepositTest;

import it.polimi.ingsw.Deposit.StrongBox;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class StrongBoxTest {
    private StrongBox testingSB;

    @Test
    @DisplayName("singleSelection test (Exceptions and happy flow)")
    public void testSS(){
        this.testingSB = new StrongBox();
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.singleSelection(Resource.COIN));

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN, 10);
            put(Resource.SHIELD, 23);
            put(Resource.SERVANT, 56);
        }};
        testingSB.addEnumMap(enumMap);
        testingSB.singleSelection(Resource.SERVANT);

        assertEquals(this.testingSB.getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.SERVANT, 1);
        }});

        testingSB.singleSelection(Resource.SERVANT);
        testingSB.singleSelection(Resource.SERVANT);
        testingSB.singleSelection(Resource.COIN);
        testingSB.singleSelection(Resource.COIN);
        testingSB.singleSelection(Resource.COIN);
        testingSB.singleSelection(Resource.SHIELD);
        testingSB.singleSelection(Resource.SHIELD);
        testingSB.singleSelection(Resource.SHIELD);
        testingSB.singleSelection(Resource.SHIELD);

        assertEquals(this.testingSB.getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.SHIELD, 4);
            put(Resource.SERVANT, 3);
        }});
        assertThrows(NullPointerException.class, () -> this.testingSB.singleSelection(null));
    }

    @Test
    @DisplayName("singleDeselection test (Exceptions and happy flow)")
    public void testSD(){
        this.testingSB = new StrongBox();
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.singleDeselection(Resource.COIN));

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN, 10);
            put(Resource.SHIELD, 23);
            put(Resource.SERVANT, 56);
        }};
        testingSB.addEnumMap(enumMap);
        testingSB.singleSelection(Resource.COIN);

        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.singleDeselection(Resource.FAITH));

        testingSB.mapSelection(new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 4);
            put(Resource.SERVANT, 10);
        }});
        testingSB.singleDeselection(Resource.SERVANT);

        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 4);
            put(Resource.SERVANT, 10-1);
        }});

        testingSB.singleDeselection(Resource.SHIELD);
        testingSB.singleDeselection(Resource.SHIELD);
        testingSB.singleDeselection(Resource.COIN);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);

        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 4-2);
            put(Resource.SERVANT, 9-4);
        }});

        testingSB.singleDeselection(Resource.SHIELD);
        testingSB.singleDeselection(Resource.SHIELD);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);
        testingSB.singleDeselection(Resource.SERVANT);

        assertNull(this.testingSB.getSelection());
    }

    @Test
    @DisplayName("mapSelection test (Exceptions and happy flow)")
    public void testMS(){
        this.testingSB = new StrongBox();
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }}));

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 30);
            put(Resource.COIN, 30);
            put(Resource.SHIELD, 30);
            put(Resource.STONE, 30);
        }};
        testingSB.addEnumMap(enumMap);
        testingSB.mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 5);
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 5);
            put(Resource.STONE, 5);
        }});

        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 5);
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 5);
            put(Resource.STONE, 5);
        }});

        testingSB.mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 5);
            put(Resource.COIN, 5);
            put(Resource.SHIELD, 5);
            put(Resource.STONE, 5);
        }});

        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 5+5);
            put(Resource.COIN, 5+5);
            put(Resource.SHIELD, 5+5);
            put(Resource.STONE, 5+5);
        }});
        assertThrows(NullPointerException.class, () -> this.testingSB.mapSelection(null));
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 80);
        }}));
    }

    @Test
    @DisplayName("mapDeselection test (Exceptions and happy flow)")
    public void testMD(){
        this.testingSB = new StrongBox();
        assertThrows(IndexOutOfBoundsException.class, () -> this.testingSB.mapDeselection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }}));
        assertThrows(NullPointerException.class, () -> this.testingSB.mapDeselection(null));

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 30);
            put(Resource.COIN, 30);
            put(Resource.SHIELD, 30);
            put(Resource.STONE, 30);
        }};
        testingSB.addEnumMap(enumMap);
        testingSB.mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 20);
            put(Resource.COIN, 20);
            put(Resource.SHIELD, 20);
            put(Resource.STONE, 20);
        }});
        testingSB.mapDeselection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 2);
            put(Resource.STONE, 2);
        }});

        assertTrue(this.testingSB.areThereSelections());
        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 20-2);
            put(Resource.COIN, 20-2);
            put(Resource.SHIELD, 20-2);
            put(Resource.STONE, 20-2);
        }});

        testingSB.mapDeselection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 5);
            put(Resource.SHIELD, 5);
        }});

        assertTrue(this.testingSB.areThereSelections());
        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 18-5);
            put(Resource.COIN, 18);
            put(Resource.SHIELD, 18-5);
            put(Resource.STONE, 18);
        }});

        testingSB.mapDeselection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 13);
            put(Resource.COIN, 18);
            put(Resource.SHIELD, 13);
            put(Resource.STONE, 18);
        }});

        assertNull(this.testingSB.getSelection());
        assertFalse(this.testingSB.areThereSelections());
    }

    @Test
    @DisplayName("clearSelection() test")
    public void testCSLN(){
        this.testingSB = new StrongBox();
        assertFalse(this.testingSB.areThereSelections());
        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN, 30);
        }};
        testingSB.addEnumMap(enumMap);
        testingSB.singleSelection(Resource.COIN);

        assertTrue(this.testingSB.areThereSelections());

        testingSB.clearSelection();

        assertFalse(this.testingSB.areThereSelections());
    }

    @Test
    @DisplayName("addEnumMap test (Exceptions and happy flow)")
    public void testAE(){
        this.testingSB = new StrongBox();
        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 1);
        }};

        assertTrue(this.testingSB.isEmpty());
        assertEquals(this.testingSB.countAll(), 0);

        testingSB.addEnumMap(enumMap);

        assertFalse(this.testingSB.isEmpty());
        assertEquals(this.testingSB.countAll(), 6);
        assertEquals(this.testingSB.content(), enumMap);

        EnumMap<Resource, Integer> enumMapSecond = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 2);
            put(Resource.STONE, 4);
        }};

        testingSB.addEnumMap(enumMapSecond);
        enumMap.put(Resource.SHIELD, 3+2);
        enumMap.put(Resource.STONE, 4);

        assertFalse(this.testingSB.isEmpty());
        assertEquals(this.testingSB.countAll(), 12);
        assertEquals(this.testingSB.content(), enumMap);
        assertThrows(NullPointerException.class, () -> this.testingSB.addEnumMap(null));

        enumMapSecond.clear();
        testingSB.addEnumMap(enumMapSecond);

        assertFalse(this.testingSB.isEmpty());
        assertEquals(this.testingSB.countAll(), 12);
        assertEquals(this.testingSB.content(), enumMap);
    }

    @Test
    @DisplayName("content() test")
    public void testCT(){
        this.testingSB = new StrongBox();
        assertTrue(this.testingSB.isEmpty());
        assertNull(this.testingSB.content());

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN,7);
            put(Resource.SHIELD,2);
        }};
        testingSB.addEnumMap(enumMap);

        assertFalse(this.testingSB.isEmpty());
        assertEquals(this.testingSB.content(), enumMap);

        testingSB.singleSelection(Resource.SHIELD);
        testingSB.pay();
        enumMap.put(Resource.SHIELD, 1);

        assertFalse(this.testingSB.isEmpty());
        assertEquals(this.testingSB.content(), enumMap);

        testingSB.mapSelection(enumMap);
        testingSB.pay();

        assertTrue(this.testingSB.isEmpty());
        assertNull(this.testingSB.content());
    }

    @Test
    @DisplayName("isEmpty() test")
    public void testIE(){
        this.testingSB = new StrongBox();
        assertTrue(this.testingSB.isEmpty());

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN,1);
        }};
        testingSB.addEnumMap(enumMap);

        assertFalse(this.testingSB.isEmpty());

        testingSB.singleSelection(Resource.COIN);
        testingSB.pay();

        assertTrue(this.testingSB.isEmpty());
    }

    @Test
    @DisplayName("areThereSelections() test")
    public void testATS(){
        this.testingSB = new StrongBox();
        assertFalse(this.testingSB.areThereSelections());

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class){{
            put(Resource.COIN,1);
        }};
        testingSB.addEnumMap(enumMap);

        assertFalse(this.testingSB.areThereSelections());
        assertFalse(this.testingSB.isEmpty());

        testingSB.singleSelection(Resource.COIN);

        assertTrue(this.testingSB.areThereSelections());
        assertEquals(this.testingSB.content(), enumMap);

        testingSB.pay();

        assertFalse(this.testingSB.areThereSelections());
        assertNull(this.testingSB.content());

        testingSB.addEnumMap(enumMap);
        testingSB.singleSelection(Resource.COIN);
        testingSB.clearSelection();

        assertFalse(this.testingSB.areThereSelections());
        assertEquals(this.testingSB.content(), enumMap);
    }

    @Test
    @DisplayName("countAll() test")
    public void testCA(){
        this.testingSB = new StrongBox();
        assertEquals(this.testingSB.countAll(), 0);

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 1);
        }};
        testingSB.addEnumMap(enumMap);

        assertEquals(this.testingSB.countAll(), 6);

        testingSB.singleSelection(Resource.SERVANT);
        testingSB.pay();

        assertEquals(this.testingSB.countAll(), 5);

        enumMap.remove(Resource.SERVANT);
        testingSB.mapSelection(enumMap);
        testingSB.pay();

        assertEquals(this.testingSB.countAll(), 0);
    }

    @Test
    @DisplayName("getSelection() test")
    public void testGS(){
        this.testingSB = new StrongBox();
        assertNull(this.testingSB.getSelection());

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 1);
        }};
        testingSB.addEnumMap(enumMap);

        assertNull(this.testingSB.getSelection());

        testingSB.singleSelection(Resource.COIN);

        assertEquals(this.testingSB.getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});

        testingSB.clearSelection();

        assertNull(this.testingSB.getSelection());

        testingSB.mapSelection(enumMap);

        assertEquals(this.testingSB.getSelection(), enumMap);

        testingSB.mapDeselection(enumMap);

        assertNull(this.testingSB.getSelection());
    }

    @Test
    @DisplayName("contains test (Exceptions and happy flow)")
    public void testCS(){
        this.testingSB = new StrongBox();
        assertThrows(NullPointerException.class, () -> this.testingSB.contains(null));

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 1);
        }};

        assertFalse(this.testingSB.contains(enumMap));

        testingSB.addEnumMap(enumMap);

        assertTrue(this.testingSB.contains(enumMap));

        enumMap.put(Resource.COIN, 5);

        assertFalse(this.testingSB.contains(enumMap));

        enumMap.clear();

        boolean exceptionNOTThrown = true;
        try{
            testingSB.contains(enumMap);
        }
        catch (NullPointerException e){
            exceptionNOTThrown = false;
        }

        assertTrue(exceptionNOTThrown);
    }

    @Test
    @DisplayName("pay() test (Exceptions and happy flow)")
    public void testP(){
        this.testingSB = new StrongBox();

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 1);
        }};
        testingSB.pay();
        testingSB.addEnumMap(enumMap);
        testingSB.pay();
        testingSB.singleSelection(Resource.SHIELD);
        testingSB.pay();
        enumMap.put(Resource.SHIELD, 3-1);

        assertEquals(this.testingSB.content(), enumMap);

        testingSB.singleSelection(Resource.SERVANT);
        testingSB.clearSelection();
    }
}
