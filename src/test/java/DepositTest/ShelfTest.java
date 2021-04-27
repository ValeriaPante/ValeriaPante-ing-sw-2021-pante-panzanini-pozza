package DepositTest;

import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class ShelfTest {
    private Shelf shelf1, shelf2, shelf3;

//    @BeforeEach
//    public void initialize(){
//        this.shelf1 = new Shelf(1);
//        this.shelf2 = new Shelf(2);
//        this.shelf3 = new Shelf(3);
//    }

    @Test
    @DisplayName("Creation and getter")
    public void testCG(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        assertThrows(IndexOutOfBoundsException.class, () -> new Shelf(4));
        assertThrows(IndexOutOfBoundsException.class, () -> new Shelf(0));
        assertThrows(IndexOutOfBoundsException.class, () -> new Shelf(-1));
        assertTrue(this.shelf1.isEmpty());
        assertTrue(this.shelf2.isEmpty());
        assertTrue(this.shelf3.isEmpty());
        assertEquals(this.shelf1.getQuantitySelected(), 0);
        assertEquals(this.shelf2.getQuantitySelected(), 0);
        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertEquals(this.shelf1.getUsage(), 0);
        assertEquals(this.shelf2.getUsage(), 0);
        assertEquals(this.shelf3.getUsage(), 0);
        assertEquals(this.shelf1.getCapacity(), 1);
        assertEquals(this.shelf2.getCapacity(), 2);
        assertEquals(this.shelf3.getCapacity(), 3);
        assertNull(this.shelf1.getResourceType());
        assertNull(this.shelf2.getResourceType());
        assertNull(this.shelf3.getResourceType());

        shelf1.singleAdd(Resource.COIN);
        shelf2.singleAdd(Resource.SHIELD);
        shelf3.addAllIfPossible(Resource.STONE, 2);

        assertFalse(this.shelf1.isEmpty());
        assertFalse(this.shelf2.isEmpty());
        assertFalse(this.shelf3.isEmpty());
        assertEquals(this.shelf1.getQuantitySelected(), 0);
        assertEquals(this.shelf2.getQuantitySelected(), 0);
        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertEquals(this.shelf1.getUsage(), 1);
        assertEquals(this.shelf2.getUsage(), 1);
        assertEquals(this.shelf3.getUsage(), 2);
        assertEquals(this.shelf1.getCapacity(), 1);
        assertEquals(this.shelf2.getCapacity(), 2);
        assertEquals(this.shelf3.getCapacity(), 3);
        assertEquals(this.shelf1.getResourceType(), Resource.COIN);
        assertEquals(this.shelf2.getResourceType(), Resource.SHIELD);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        shelf1.singleSelection();
        shelf2.singleSelection();
        shelf3.multiSelection(2);
        shelf1.pay();
        shelf2.pay();
        shelf3.pay();

        assertTrue(this.shelf1.isEmpty());
        assertTrue(this.shelf2.isEmpty());
        assertTrue(this.shelf3.isEmpty());
        assertEquals(this.shelf1.getQuantitySelected(), 0);
        assertEquals(this.shelf2.getQuantitySelected(), 0);
        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertEquals(this.shelf1.getUsage(), 0);
        assertEquals(this.shelf2.getUsage(), 0);
        assertEquals(this.shelf3.getUsage(), 0);
        assertEquals(this.shelf1.getCapacity(), 1);
        assertEquals(this.shelf2.getCapacity(), 2);
        assertEquals(this.shelf3.getCapacity(), 3);
        assertNull(this.shelf1.getResourceType());
        assertNull(this.shelf2.getResourceType());
        assertNull(this.shelf3.getResourceType());
    }

    @Test
    @DisplayName("singleAdd testing (Exceptions and happy flow)")
    public void testSA(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        assertNull(this.shelf1.getResourceType());

        shelf1.singleAdd(Resource.STONE);

        assertEquals(this.shelf1.getResourceType(), Resource.STONE);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf1.singleAdd(Resource.COIN));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf1.singleAdd(Resource.STONE));

        assertNull(this.shelf2.getResourceType());

        shelf2.singleAdd(Resource.STONE);

        assertEquals(this.shelf2.getResourceType(), Resource.STONE);
        assertThrows(IllegalArgumentException.class, () -> this.shelf2.singleAdd(Resource.COIN));

        shelf1.singleSelection();
        shelf1.pay();

        assertNull(this.shelf1.getResourceType());

        shelf1.singleAdd(Resource.COIN);
        shelf1.singleSelection();
        shelf1.takeSelected();

        assertNull(this.shelf1.getResourceType());

        shelf1.singleAdd(Resource.SHIELD);

        assertEquals(this.shelf1.getResourceType(), Resource.SHIELD);
    }

    @Test
    @DisplayName("addAllIfPossible testing (Exceptions and happy flow)")
    public void testAAIP(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        shelf3.addAllIfPossible(Resource.COIN, 2);

        assertEquals(this.shelf3.getResourceType(), Resource.COIN);
        assertEquals(this.shelf3.getUsage(), 2);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.addAllIfPossible(Resource.COIN, 2));
        assertThrows(IllegalArgumentException.class, () -> this.shelf3.addAllIfPossible(Resource.STONE, 1));

        shelf3.multiSelection(2);
        shelf3.pay();

        assertNull(this.shelf3.getResourceType());

        shelf3.addAllIfPossible(Resource.SERVANT, 1);

        assertEquals(this.shelf3.getResourceType(), Resource.SERVANT);
        assertEquals(this.shelf3.getUsage(), 1);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.addAllIfPossible(Resource.COIN, 3));
        assertThrows(IllegalArgumentException.class, () -> this.shelf3.addAllIfPossible(Resource.STONE, 1));
    }

    @Test
    @DisplayName("addWhatPossible testing (Exceptions and happy flow)")
    public void testAWP(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        shelf3.addWhatPossible(Resource.COIN, 2);

        assertEquals(this.shelf3.getResourceType(), Resource.COIN);
        assertEquals(this.shelf3.getUsage(), 2);
        assertThrows(IllegalArgumentException.class, () -> this.shelf3.addWhatPossible(Resource.STONE, 1));
        assertEquals(this.shelf3.addWhatPossible(Resource.COIN, 10), 9);
        assertEquals(this.shelf3.addWhatPossible(Resource.COIN, 10), 10);

        shelf3.multiSelection(3);
        shelf3.pay();

        assertNull(this.shelf3.getResourceType());

        shelf3.addWhatPossible(Resource.SERVANT, 1);

        assertEquals(this.shelf3.getResourceType(), Resource.SERVANT);
        assertEquals(this.shelf3.getUsage(), 1);
        assertThrows(IllegalArgumentException.class, () -> this.shelf3.addWhatPossible(Resource.STONE, 1));

        shelf3.addWhatPossible(Resource.SERVANT, 1);

        assertEquals(this.shelf3.getResourceType(), Resource.SERVANT);
        assertEquals(this.shelf3.getUsage(), 2);
        assertThrows(IllegalArgumentException.class, () -> this.shelf3.addWhatPossible(Resource.STONE, 1));

        Integer externalChange = 1;
        this.shelf3.addWhatPossible(Resource.SERVANT, externalChange);
        externalChange = 10;

        assertEquals(this.shelf3.getUsage(), 3);

        shelf3.rifle();

        //this is a CRITICAL BEHAVIOUR!!!! all int required in shelves' methods must be positive!
        shelf3.addWhatPossible(Resource.SERVANT, -1);
        assertEquals(this.shelf3.getUsage(), -1);
        shelf3.addWhatPossible(Resource.SERVANT, -5);
        assertEquals(this.shelf3.getUsage(), -6);
        shelf3.addWhatPossible(Resource.SERVANT, 9);
        assertEquals(this.shelf3.getUsage(), 3);
    }

    @Test
    @DisplayName("creating and editing selections testing (Exceptions and happy flow)")
    public void testS(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        assertEquals(this.shelf3.getQuantitySelected(), 0);

        this.shelf3.addAllIfPossible(Resource.STONE, 3);
        this.shelf3.singleSelection();

        assertEquals(this.shelf3.getQuantitySelected(), 1);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(3));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(3));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        this.shelf3.multiSelection(2);

        assertEquals(this.shelf3.getQuantitySelected(), 3);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(1));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(4));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        this.shelf3.singleDeselection();

        assertEquals(this.shelf3.getQuantitySelected(), 2);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(2));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(3));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        this.shelf3.multiDeselection(2);

        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(4));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(1));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        this.shelf3.multiSelection(3);
        this.shelf3.clearSelection();

        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(4));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(1));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);

        this.shelf3.clearSelection();
        this.shelf3.clearSelection();
        this.shelf3.clearSelection();

        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiSelection(4));
        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.multiDeselection(1));
        assertEquals(this.shelf3.getUsage(), 3);
        assertEquals(this.shelf3.getResourceType(), Resource.STONE);
    }

    @Test
    @DisplayName("takeSelected() testing (Exceptions and happy flow)")
    public void testTS(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        shelf3.addAllIfPossible(Resource.STONE, 3);

        assertThrows(IndexOutOfBoundsException.class, () -> this.shelf3.takeSelected());

        shelf3.singleSelection();

        assertEquals(this.shelf3.takeSelected(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.STONE, 1);
        }});
        assertEquals(this.shelf3.getUsage(), 2);
        assertEquals(this.shelf3.getQuantitySelected(), 0);

        shelf3.singleSelection();
        shelf3.singleDeselection();
        shelf3.multiSelection(2);
        shelf3.multiDeselection(1);
        shelf3.singleSelection();
        shelf3.singleAdd(Resource.STONE);
        shelf3.clearSelection();
        shelf3.multiSelection(2);

        assertEquals(this.shelf3.takeSelected(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.STONE, 2);
        }});
        assertEquals(this.shelf3.getUsage(), 1);
        assertEquals(this.shelf3.getQuantitySelected(), 0);
    }

    @Test
    @DisplayName("rifle() testing")
    public void testR(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        shelf2.addAllIfPossible(Resource.SHIELD, 2);
        shelf2.multiSelection(2);

        assertEquals(this.shelf2.getQuantitySelected(), 2);
        assertFalse(this.shelf2.isEmpty());
        assertEquals(this.shelf2.rifle(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.SHIELD, 2);
        }});
        assertEquals(this.shelf2.getQuantitySelected(), 0);
        assertEquals(this.shelf2.getUsage(), 0);
        assertNull(this.shelf2.getResourceType());
        assertTrue(this.shelf2.isEmpty());

        shelf2.singleAdd(Resource.COIN);

        assertFalse(this.shelf2.isEmpty());
        assertEquals(this.shelf2.getResourceType(), Resource.COIN);
        assertEquals(this.shelf2.getUsage(), 1);
        assertEquals(this.shelf2.getQuantitySelected(), 0);
    }

    @Test
    @DisplayName("content() testing")
    public void testCT(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        assertNull(this.shelf3.content());

        shelf3.addAllIfPossible(Resource.COIN, 3);

        assertEquals(this.shelf3.content(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.COIN, 3);
        }});

        shelf3.singleSelection();
        shelf3.singleSelection();
        shelf3.pay();

        assertEquals(this.shelf3.content(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.COIN, 1);
        }});

        shelf3.rifle();

        assertNull(this.shelf3.content());
    }

    @Test
    @DisplayName("contains testing (Exceptions and happy flow)")
    public void testCS(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        EnumMap<Resource, Integer> enumMap = new EnumMap<>(Resource.class);
        enumMap.put(Resource.STONE, 2);
        shelf2.addWhatPossible(Resource.STONE, 2);

        assertTrue(this.shelf2.contains(enumMap));

        enumMap.put(Resource.STONE, 1);

        assertTrue(this.shelf2.contains(enumMap));

        enumMap.put(Resource.STONE, 3);

        assertFalse(this.shelf2.contains(enumMap));

        enumMap.put(Resource.STONE, 2);
        enumMap.put(Resource.SHIELD, 1);

        assertFalse(this.shelf2.contains(enumMap));

        enumMap.clear();
        enumMap.put(Resource.SHIELD, 1);

        assertFalse(this.shelf2.contains(enumMap));

        assertThrows(NullPointerException.class, () -> this.shelf3.contains(null));
    }

    @Test
    @DisplayName("pay() testing")
    public void testP(){
        this.shelf1 = new Shelf(1);
        this.shelf2 = new Shelf(2);
        this.shelf3 = new Shelf(3);

        shelf2.pay();
        shelf3.addWhatPossible(Resource.STONE, 3);
        shelf3.pay();

        assertEquals(this.shelf3.content(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.STONE, 3);
        }});

        shelf3.multiSelection(3);
        shelf3.pay();

        assertNull(this.shelf3.content());
        assertEquals(this.shelf3.getQuantitySelected(), 0);
        assertEquals(this.shelf3.getUsage(), 0);
        assertNull(this.shelf3.getResourceType());

        shelf3.addWhatPossible(Resource.SERVANT, 2);
        shelf3.pay();

        assertEquals(this.shelf3.content(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.SERVANT, 2);
        }});
    }
}
