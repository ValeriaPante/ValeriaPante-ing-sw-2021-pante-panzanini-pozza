package ControllerTest;

import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Controller.BuyDevCardController;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.LeaderController;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BuyDevCardControllerTest {

    @Test
    public void validBuying(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));
        table.turnOf().setMacroTurnType(MacroTurnType.NONE);

        EnumMap<Resource, Integer> resourceOwned = new EnumMap<>(Resource.class);
        resourceOwned.put(Resource.SERVANT, 50);
        resourceOwned.put(Resource.COIN, 50);
        resourceOwned.put(Resource.SHIELD, 50);
        resourceOwned.put(Resource.STONE, 50);

        table.turnOf().getStrongBox().addEnumMap(resourceOwned);

        BuyDevCardController controller = new BuyDevCardController(new FaithTrackController(table));
        controller.chooseDevCard(3);
        int id = table.getDevDecks()[2].getTopCard().getId();
        EnumMap<Resource, Integer> cost = table.getDevDecks()[2].getTopCard().getCost();

        controller.buyDevCard();
        table.turnOf().getStrongBox().mapSelection(cost);
        controller.paySelected();
        controller.chooseDevSlot(2);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertFalse(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        assertEquals(id, table.turnOf().getDevSlots()[1].topCard().getId());

        for(EnumMap.Entry<Resource, Integer> entry: cost.entrySet())
            assertEquals(50 - entry.getValue(), table.turnOf().getStrongBox().content().get(entry.getKey()));
    }

    @Test
    public void invalidBuying(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));
        table.turnOf().setMacroTurnType(MacroTurnType.NONE);

        EnumMap<Resource, Integer> resourceOwned = new EnumMap<>(Resource.class);
        resourceOwned.put(Resource.SERVANT, 50);
        resourceOwned.put(Resource.COIN, 50);
        resourceOwned.put(Resource.SHIELD, 50);
        resourceOwned.put(Resource.STONE, 50);

        table.turnOf().getStrongBox().addEnumMap(resourceOwned);

        BuyDevCardController controller = new BuyDevCardController(new FaithTrackController(table));

        //1st invalid buying: wrong deck index
        controller.chooseDevCard(14);
        controller.buyDevCard();
        controller.paySelected();
        controller.chooseDevSlot(2);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        for(int i = 0; i < table.getDevDecks().length; i++)
            assertEquals(4, table.getDevDecks()[i].size());

        //2nd invalid buying: no selection
        controller.chooseDevCard(4);
        controller.buyDevCard();
        controller.paySelected();
        controller.chooseDevSlot(2);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        for(int i = 0; i < table.getDevDecks().length; i++)
            assertEquals(4, table.getDevDecks()[i].size());
    }

    @Test
    public void invalidPlacing(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));
        table.turnOf().setMacroTurnType(MacroTurnType.NONE);

        EnumMap<Resource, Integer> resourceOwned = new EnumMap<>(Resource.class);
        resourceOwned.put(Resource.SERVANT, 50);
        resourceOwned.put(Resource.COIN, 50);
        resourceOwned.put(Resource.SHIELD, 50);
        resourceOwned.put(Resource.STONE, 50);

        table.turnOf().getStrongBox().addEnumMap(resourceOwned);

        BuyDevCardController controller = new BuyDevCardController(new FaithTrackController(table));

        //chooses another card with level 3, which he can't place anywhere
        controller.chooseDevCard(11);
        EnumMap<Resource, Integer> cost = table.getDevDecks()[2].getTopCard().getCost();
        controller.buyDevCard();
        table.turnOf().getStrongBox().mapSelection(cost);
        controller.paySelected();
        controller.chooseDevSlot(1);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        for(EnumMap.Entry<Resource, Integer> entry: table.turnOf().getStrongBox().content().entrySet())
            assertEquals(50, entry.getValue());

        table.turnOf().getStrongBox().clearSelection();

        controller.chooseDevCard(3);
        int id = table.getDevDecks()[2].getTopCard().getId();
        cost = table.getDevDecks()[2].getTopCard().getCost();

        controller.buyDevCard();
        table.turnOf().getStrongBox().mapSelection(cost);
        controller.paySelected();
        controller.chooseDevSlot(5);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        for(EnumMap.Entry<Resource, Integer> entry: cost.entrySet())
            assertEquals(50 - entry.getValue(), table.turnOf().getStrongBox().content().get(entry.getKey()));

        controller.chooseDevSlot(2);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertFalse(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        assertEquals(id, table.turnOf().getDevSlots()[1].topCard().getId());
    }

    @Test
    public void discountUsage(){
        Table table = new Table(2);
        table.addPlayer(new RealPlayer("user1"));
        table.addPlayer(new RealPlayer("user2"));
        table.turnOf().setMacroTurnType(MacroTurnType.NONE);

        EnumMap<Resource, Integer> resourceOwned = new EnumMap<>(Resource.class);
        resourceOwned.put(Resource.SERVANT, 50);
        resourceOwned.put(Resource.COIN, 50);
        resourceOwned.put(Resource.SHIELD, 50);
        resourceOwned.put(Resource.STONE, 50);

        table.turnOf().getStrongBox().addEnumMap(resourceOwned);

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        input.put(Resource.SERVANT, 1);
        input.put(Resource.COIN, 1);
        input.put(Resource.SHIELD, 1);
        input.put(Resource.STONE, 1);

        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, new EnumMap<>(Resource.class),21));
        table.turnOf().addLeaderCard(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input, new EnumMap<>(Resource.class),45));

        FaithTrackController ftc = new FaithTrackController(table);
        LeaderController lController = new LeaderController(ftc);
        lController.actionOnLeaderCard(45, false);

        BuyDevCardController controller = new BuyDevCardController(ftc);
        controller.chooseDevCard(3);
        int id = table.getDevDecks()[2].getTopCard().getId();
        EnumMap<Resource, Integer> cost = table.getDevDecks()[2].getTopCard().getCost();

        controller.buyDevCard();
        controller.applyDiscountAbility(21);
        assertEquals(cost, table.turnOf().getSupportContainer().content());

        controller.applyDiscountAbility(45);
        assertNotEquals(cost, table.turnOf().getSupportContainer().content());

        if(table.turnOf().getSupportContainer().content() != null)
            table.turnOf().getStrongBox().mapSelection(table.turnOf().getSupportContainer().content());
        controller.paySelected();
        controller.chooseDevSlot(2);
        assertTrue(table.turnOf().getDevSlots()[0].isEmpty());
        assertFalse(table.turnOf().getDevSlots()[1].isEmpty());
        assertTrue(table.turnOf().getDevSlots()[2].isEmpty());
        assertEquals(id, table.turnOf().getDevSlots()[1].topCard().getId());

        for(EnumMap.Entry<Resource, Integer> entry: cost.entrySet())
            assertEquals(50 - entry.getValue() + 1, table.turnOf().getStrongBox().content().get(entry.getKey()));
    }
}