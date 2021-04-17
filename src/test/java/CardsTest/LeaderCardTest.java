package CardsTest;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {

    @Test
    public void checksPlayLeaderCard(){
        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        input.put(Resource.SERVANT, 1);
        LeaderCard card = new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 21);

        assertNull(card.getAbility());
        card.play();
        assertTrue(card.hasBeenPlayed());
        assertNotNull(card.getAbility());
    }

}