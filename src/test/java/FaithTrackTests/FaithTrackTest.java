package FaithTrackTests;

import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.FaithTrack.VaticanRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackTest {

    private FaithTrack faithTrack;

    @BeforeEach
    private void init() throws Exception{
        this.faithTrack = new FaithTrack();
    }

    @Test
    public void victoryPointsTest(){
        assertEquals(0, faithTrack.victoryPoints(0));
        assertEquals(0, faithTrack.victoryPoints(1));
        assertEquals(0, faithTrack.victoryPoints(2));
        assertEquals(1, faithTrack.victoryPoints(3));
        assertEquals(1, faithTrack.victoryPoints(4));
        assertEquals(1, faithTrack.victoryPoints(5));
        assertEquals(2, faithTrack.victoryPoints(6));
        assertEquals(2, faithTrack.victoryPoints(7));
        assertEquals(2, faithTrack.victoryPoints(8));
        assertEquals(4, faithTrack.victoryPoints(9));
        assertEquals(4, faithTrack.victoryPoints(10));
        assertEquals(4, faithTrack.victoryPoints(11));
        assertEquals(6, faithTrack.victoryPoints(12));
        assertEquals(6, faithTrack.victoryPoints(13));
        assertEquals(6, faithTrack.victoryPoints(14));
        assertEquals(9, faithTrack.victoryPoints(15));
        assertEquals(9, faithTrack.victoryPoints(16));
        assertEquals(9, faithTrack.victoryPoints(17));
        assertEquals(12, faithTrack.victoryPoints(18));
        assertEquals(12, faithTrack.victoryPoints(19));
        assertEquals(12, faithTrack.victoryPoints(20));
        assertEquals(16, faithTrack.victoryPoints(21));
        assertEquals(16, faithTrack.victoryPoints(22));
        assertEquals(16, faithTrack.victoryPoints(23));
        assertEquals(20, faithTrack.victoryPoints(24));

        assertEquals(20, faithTrack.victoryPoints(642645276));
        assertEquals(20, faithTrack.victoryPoints(-1));
    }

    @Test
    public void finishedTest(){
        assertFalse(faithTrack.finished(-1));
        assertFalse(faithTrack.finished(0));
        assertFalse(faithTrack.finished(1));
        assertFalse(faithTrack.finished(2));
        assertFalse(faithTrack.finished(3));
        assertFalse(faithTrack.finished(4));
        assertFalse(faithTrack.finished(5));
        assertFalse(faithTrack.finished(6));
        assertFalse(faithTrack.finished(7));
        assertFalse(faithTrack.finished(8));
        assertFalse(faithTrack.finished(9));
        assertFalse(faithTrack.finished(10));
        assertFalse(faithTrack.finished(11));
        assertFalse(faithTrack.finished(12));
        assertFalse(faithTrack.finished(12));
        assertFalse(faithTrack.finished(14));
        assertFalse(faithTrack.finished(15));
        assertFalse(faithTrack.finished(16));
        assertFalse(faithTrack.finished(17));
        assertFalse(faithTrack.finished(18));
        assertFalse(faithTrack.finished(19));
        assertFalse(faithTrack.finished(20));
        assertFalse(faithTrack.finished(21));
        assertFalse(faithTrack.finished(22));
        assertFalse(faithTrack.finished(23));

        assertTrue(faithTrack.finished(24));
        assertTrue(faithTrack.finished(324782));
    }

    @Test
    public void lengthTest(){
        assertEquals(24, faithTrack.getLength());
    }

    @Test
    public void getVaticanRelationsTest(){
        VaticanRelation[] vaticanRelations = faithTrack.getVaticanRelations();

        assertEquals(0, vaticanRelations[0].getId());
        assertEquals(1, vaticanRelations[1].getId());
        assertEquals(2, vaticanRelations[2].getId());
    }

    @Test
    public void doneVaticanRelationTest(){
        faithTrack.doneVaticanRelation(0);

        assertThrows(IllegalArgumentException.class, () -> faithTrack.doneVaticanRelation(-1));
        assertThrows(IllegalArgumentException.class, () -> faithTrack.doneVaticanRelation(3));
    }

}
