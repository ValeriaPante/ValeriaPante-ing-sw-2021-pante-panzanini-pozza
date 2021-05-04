package ControllerTest;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.LeaderController;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeaderControllerTest {

    @Test
    public void discarding(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        input.put(Resource.SERVANT, 1);

        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 21));
        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 45));

        LeaderController controller = new LeaderController(new FaithTrackController(table));
        controller.actionOnLeaderCard(21,true);
        assertEquals(1, table.turnOf().getLeaderCards().length);
        controller.actionOnLeaderCard(45,true);
        assertEquals(0, table.turnOf().getLeaderCards().length);
    }

    @Test
    public void activateCardWithoutDevTypeRequirements(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        input.put(Resource.SERVANT, 1);

        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 21));
        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 45));

        LeaderController controller = new LeaderController(new FaithTrackController(table));
        controller.actionOnLeaderCard(21,false);
        assertFalse(table.turnOf().getLeaderCards()[0].hasBeenPlayed());
    }

    @Test
    public void activateCardWithoutResourceRequirements(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        resourceReq.put(Resource.SHIELD, 1);
        resourceReq.put(Resource.COIN, 1);
        input.put(Resource.SERVANT, 1);

        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 21));
        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 45));

        LeaderController controller = new LeaderController(new FaithTrackController(table));
        controller.actionOnLeaderCard(21,false);
        assertFalse(table.turnOf().getLeaderCards()[0].hasBeenPlayed());
    }

    @Test
    public void activateCard(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        input.put(Resource.SERVANT, 1);

        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 21));
        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, 45));

        LeaderController controller = new LeaderController(new FaithTrackController(table));
        controller.actionOnLeaderCard(21, false);
        assertEquals(2, table.turnOf().getLeaderCards().length);
        assertTrue(table.turnOf().getLeaderCards()[0].hasBeenPlayed());
        assertFalse(table.turnOf().getLeaderCards()[1].hasBeenPlayed());

        //riattiva una carta già attivata
        controller.actionOnLeaderCard(21,false);
        assertTrue(table.turnOf().getLeaderCards()[0].hasBeenPlayed());
        assertFalse(table.turnOf().getLeaderCards()[1].hasBeenPlayed());
    }
}