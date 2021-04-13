package FaithTrackTests;

import it.polimi.ingsw.FaithTrack.VaticanRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VaticanRelationTests {
    private VaticanRelation vaticanRelation;

    @BeforeEach
    private void init() throws Exception{
        this.vaticanRelation = new VaticanRelation(5,10,1);
    }

    @Test
    public void isInOrOverTest(){
        assertFalse(vaticanRelation.isInOrOver(3));
        assertFalse(vaticanRelation.isInOrOver(4));
        assertTrue(vaticanRelation.isInOrOver(5));
        assertTrue(vaticanRelation.isInOrOver(6));
        assertTrue(vaticanRelation.isInOrOver(7));
        assertTrue(vaticanRelation.isInOrOver(8));
        assertTrue(vaticanRelation.isInOrOver(9));
        assertTrue(vaticanRelation.isInOrOver(10));
        assertTrue(vaticanRelation.isInOrOver(11));
    }

    @Test
    public void isOnPopePositionOrOverTest(){
        assertFalse(vaticanRelation.isOnPopePositionOrOver(9));
        assertTrue(vaticanRelation.isOnPopePositionOrOver(10));
        assertTrue(vaticanRelation.isOnPopePositionOrOver(11));
    }

    @Test
    public void isAlreadyDoneTest(){
        assertFalse(vaticanRelation.isAlreadyDone());
        vaticanRelation.done();
        assertTrue(vaticanRelation.isAlreadyDone());
    }

    @Test
    public void getIdTest(){
        assertEquals(1, vaticanRelation.getId());
    }

    @Test
    public void cloneTest(){
        VaticanRelation vaticanRelation1 = vaticanRelation.clone();
        vaticanRelation1.done();
        boolean result = vaticanRelation1.isAlreadyDone() == vaticanRelation.isAlreadyDone();
        assertFalse(result);
    }


}
