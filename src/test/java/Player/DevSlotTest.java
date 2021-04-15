package Player;

import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.CantPutThisHere;
import it.polimi.ingsw.Player.DevSlot;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotTest {

    @Test
    public void checksPlacing(){
        DevSlot slot = new DevSlot();

        assertTrue(slot.isEmpty());

        EnumMap<Resource, Integer> cost = new EnumMap<>(Resource.class);
        cost.put(Resource.SHIELD, 2);
        DevCard card1 = new DevCard(1, cost, new DevCardType(1, Colour.GREEN), new ProductionPower(
                new Resource[]  {Resource.COIN},
                new int[]       {1},
                new Resource[]  {Resource.FAITH},
                new int[]       {1}));
        DevCard card2 = new DevCard(1, cost, new DevCardType(2, Colour.BLUE), new ProductionPower(
                new Resource[]  {Resource.STONE},
                new int[]       {1},
                new Resource[]  {Resource.SHIELD},
                new int[]       {1}));
        DevCard card3 = new DevCard(1, cost, new DevCardType(1, Colour.BLUE), new ProductionPower(
                new Resource[]  {Resource.COIN},
                new int[]       {1},
                new Resource[]  {Resource.SHIELD},
                new int[]       {1}));

        slot.addCard(card1);
        slot.addCard(card2);
        assertThrows(CantPutThisHere.class, () -> slot.addCard(card3));
        assertEquals(2, slot.numberOfCards());
        assertTrue(slot.topCard().equals(card2));
    }

}