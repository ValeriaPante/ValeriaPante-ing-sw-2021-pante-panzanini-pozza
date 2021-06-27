package ControllerTest;

import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.MarketController;
import it.polimi.ingsw.Controller.SelectionController;
import it.polimi.ingsw.Controller.SelectionControllerTest;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MarketControllerTest {

    @Test
    @DisplayName("selection and deselection testing")
    public void testSelectionDeselection(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc);
        realPlayer.getLeaderCards()[0].play();

        realPlayer.getShelves()[2].addAllIfPossible(Resource.STONE,2);
        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class) {{
            put(Resource.STONE, 3);
        }});
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);

        realPlayer.setMacroTurnType(MacroTurnType.DONE);
        assertFalse(marketController.selectionFromShelf(Resource.WHITE, 3));
        assertFalse(marketController.deselectionFromShelf(Resource.WHITE, 3));
        assertFalse(marketController.selectionFromLeaderStorage(Resource.FAITH, 3, 4));
        marketController.selectFromSupportContainer(Resource.STONE, 2);
        assertNull(realPlayer.getSupportContainer().getSelection());
        marketController.deselectFromSupportContainer(Resource.STONE, 2);
        assertNull(realPlayer.getSupportContainer().getSelection());

        realPlayer.setMicroTurnType(MicroTurnType.PLACE_RESOURCES);
        realPlayer.setMacroTurnType(MacroTurnType.GET_FROM_MARKET);

        assertTrue(marketController.selectionFromShelf(Resource.STONE,3));
        assertEquals(1, realPlayer.getShelves()[2].getQuantitySelected());
        assertTrue(marketController.deselectionFromShelf(Resource.STONE,3));
        assertEquals(0, realPlayer.getShelves()[2].getQuantitySelected());

        assertTrue(marketController.selectionFromLeaderStorage(Resource.COIN,101, 1));
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertTrue(marketController.selectionFromLeaderStorage(Resource.COIN,101, 1));
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class));

        assertNull(realPlayer.getSupportContainer().getSelection());
        marketController.selectFromSupportContainer(Resource.STONE, 2);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.STONE, 2);
        }});
        marketController.deselectFromSupportContainer(Resource.STONE, 1);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.STONE, 1);
        }});
    }

    @Test
    @DisplayName("move to support cointainer testing")
    public void testMoveSC(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc);
        realPlayer.getLeaderCards()[0].play();
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getShelves()[2].addAllIfPossible(Resource.STONE,2);
        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
        }});

        realPlayer.getLeaderCards()[0].getAbility().select(Resource.COIN, 2);
        realPlayer.getShelves()[2].singleSelection();
        realPlayer.getSupportContainer().mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        realPlayer.setMacroTurnType(MacroTurnType.GET_FROM_MARKET);

        marketController.moveSelectedToSupportContainer();
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }});
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
        }});
        assertEquals(realPlayer.getShelves()[2].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 1);
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 1);
        }});
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 2);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.PLACE_RESOURCES);

        marketController.moveSelectedToSupportContainer();
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }});
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
        }});
        assertEquals(realPlayer.getShelves()[2].getUsage(), 1);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 0);
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 1);
        }});
    }

    @Test
    @DisplayName("move to shelf testing")
    public void testMoveSH(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc);
        realPlayer.getLeaderCards()[0].play();
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getShelves()[1].addAllIfPossible(Resource.STONE,2);
        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 4);
            put(Resource.STONE, 2);
        }});

        realPlayer.getLeaderCards()[0].getAbility().select(Resource.COIN, 2);
        realPlayer.getShelves()[1].singleSelection();
        realPlayer.getSupportContainer().mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 2);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        realPlayer.setMacroTurnType(MacroTurnType.GET_FROM_MARKET);

        marketController.moveToShelf(3);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 2);
        }});
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 4);
            put(Resource.STONE, 2);
        }});
        assertEquals(realPlayer.getShelves()[1].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[1].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[1].getQuantitySelected(), 1);
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 1);
        }});
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 2);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.PLACE_RESOURCES);

        marketController.moveToShelf(3);
        assertEquals("Wrong selection", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().clearSelection();
        marketController.moveToShelf(3);
        assertEquals("Wrong selection", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleSelection(Resource.STONE);
        marketController.moveToShelf(3);
        assertEquals("Resource already contained in another Shelf", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleDeselection(Resource.STONE);
        realPlayer.getSupportContainer().singleSelection(Resource.SERVANT);
        marketController.moveToShelf(3);
        assertEquals(realPlayer.getShelves()[1].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[1].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[1].getQuantitySelected(), 1);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 0);
        assertEquals(realPlayer.getShelves()[2].getUsage(), 1);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.SERVANT, 3);
            put(Resource.STONE, 2);
        }});
        assertNull(realPlayer.getSupportContainer().getSelection());
        realPlayer.getSupportContainer().singleSelection(Resource.STONE);
        marketController.moveToShelf(3);
        assertEquals("Wrong type of resource", realPlayer.getErrorMessage());
        assertEquals(realPlayer.getShelves()[1].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[1].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[1].getQuantitySelected(), 1);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 0);
        assertEquals(realPlayer.getShelves()[2].getUsage(), 1);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.SERVANT);
        realPlayer.getSupportContainer().singleDeselection(Resource.STONE);
        realPlayer.getSupportContainer().singleSelection(Resource.SERVANT);
        realPlayer.getSupportContainer().singleSelection(Resource.SERVANT);
        realPlayer.getSupportContainer().singleSelection(Resource.SERVANT);
        marketController.moveToShelf(3);
        assertEquals("Insertion exceeding limits", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleDeselection(Resource.SERVANT);
        marketController.moveToShelf(3);
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.SERVANT, 1);
            put(Resource.STONE, 2);
        }});
        assertNull(realPlayer.getSupportContainer().getSelection());
        assertEquals(realPlayer.getShelves()[1].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[1].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[1].getQuantitySelected(), 1);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 0);
        assertEquals(realPlayer.getShelves()[2].getUsage(), 3);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.SERVANT);
    }

    @Test
    @DisplayName("move to leader card testing")
    public void testMoveLC(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc);
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc1 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                100
        );
        realPlayer.addLeaderCard(lc1);
        realPlayer.getLeaderCards()[1].play();
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getShelves()[2].addAllIfPossible(Resource.STONE,2);
        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 3);
        }});

        realPlayer.getLeaderCards()[0].getAbility().select(Resource.COIN, 2);
        realPlayer.getShelves()[2].singleSelection();
        realPlayer.getSupportContainer().mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.SHIELD, 3);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        realPlayer.setMacroTurnType(MacroTurnType.GET_FROM_MARKET);

        marketController.moveToLeaderStorage(101);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.SHIELD, 3);
        }});
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
            put(Resource.SHIELD, 3);
            put(Resource.COIN, 3);
        }});
        assertEquals(realPlayer.getShelves()[2].getUsage(), 2);
        assertEquals(realPlayer.getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(realPlayer.getShelves()[2].getQuantitySelected(), 1);
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 1);
        }});
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class) {{
            put(Resource.COIN, 2);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.PLACE_RESOURCES);

        realPlayer.getSupportContainer().clearSelection();
        marketController.moveToLeaderStorage(101);
        assertEquals("No resources selected to be moved", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleSelection(Resource.COIN);
        marketController.moveToLeaderStorage(100);
        assertEquals("Wrong leader card", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleSelection(Resource.SHIELD);
        realPlayer.getSupportContainer().singleSelection(Resource.SHIELD);
        realPlayer.getSupportContainer().singleSelection(Resource.SHIELD);
        marketController.moveToLeaderStorage(101);
        assertEquals("LeaderCard cannot contain that quantity", realPlayer.getErrorMessage());
        realPlayer.getSupportContainer().singleDeselection(Resource.SHIELD);
        marketController.moveToLeaderStorage(101);
        assertNull(realPlayer.getSupportContainer().getSelection());
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class) {{
            put(Resource.SERVANT, 3);
            put(Resource.SHIELD, 1);
            put(Resource.COIN, 2);
        }});
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class) {{
            put(Resource.SHIELD, 2);
            put(Resource.COIN, 3);
        }});
    }

    @Test
    @DisplayName("market selection testing")
    public void testMarketSelection(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);

        realPlayer.setMacroTurnType(MacroTurnType.DONE);
        marketController.selectFromMarket(4, false);
        assertEquals(MacroTurnType.DONE, realPlayer.getMacroTurnType());
        assertFalse(table.getMarket().areThereSelections());

        realPlayer.setMacroTurnType(MacroTurnType.NONE);
        realPlayer.setMicroTurnType(MicroTurnType.NONE);

        marketController.selectFromMarket(-1, false);
        assertFalse(table.getMarket().areThereSelections());
        assertEquals(MicroTurnType.NONE, realPlayer.getMicroTurnType());
        assertEquals("Wrong position specified", realPlayer.getErrorMessage());

        marketController.selectFromMarket(3, true);
        assertFalse(table.getMarket().areThereSelections());
        assertEquals(MicroTurnType.NONE, realPlayer.getMicroTurnType());
        assertEquals("Position specified exceeded limits", realPlayer.getErrorMessage());
        marketController.selectFromMarket(4, false);
        assertFalse(table.getMarket().areThereSelections());
        assertEquals(MicroTurnType.NONE, realPlayer.getMicroTurnType());
        assertEquals("Position specified exceeded limits", realPlayer.getErrorMessage());

        marketController.selectFromMarket(3, false);
        assertTrue(table.getMarket().areThereSelections());
        assertFalse(table.getMarket().isRowSelected());
        assertEquals(table.getMarket().getPosSelected(), 3);
        assertEquals(MicroTurnType.SELECTION_IN_MARKET, realPlayer.getMicroTurnType());
        marketController.selectFromMarket(2, true);
        assertTrue(table.getMarket().areThereSelections());
        assertEquals(table.getMarket().getPosSelected(), 2);
        assertTrue(table.getMarket().isRowSelected());
        assertEquals(MicroTurnType.SELECTION_IN_MARKET, realPlayer.getMicroTurnType());
    }

    @Test
    @DisplayName("take from market testing")
    public void testMarketTaking(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);

        realPlayer.setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        table.getMarket().selectColumn(2);
        marketController.takeFromMarket();
        assertTrue(table.getMarket().areThereSelections());
        assertFalse(table.getMarket().isRowSelected());
        assertEquals(2, table.getMarket().getPosSelected());

        realPlayer.setMicroTurnType(MicroTurnType.NONE);
        realPlayer.setMicroTurnType(MicroTurnType.NONE);
        marketController.selectFromMarket(2, true);
        marketController.takeFromMarket();

        assertEquals(realPlayer.getMicroTurnType(), MicroTurnType.PLACE_RESOURCES);
        assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);

        LeaderCard lc1 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc1);
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.DISCOUNT,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                102
        );
        realPlayer.addLeaderCard(lc2);

        for (int i=0; i<3; i++){
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            marketController.selectFromMarket(i, true);
            marketController.takeFromMarket();

            assertEquals(realPlayer.getMicroTurnType(), MicroTurnType.PLACE_RESOURCES);
            assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);
            assertFalse(realPlayer.getSupportContainer().contains(new EnumMap<>(Resource.class){{
                put(Resource.WHITE, 1);
            }}));
        }

        realPlayer.discardLeaderCard(lc1);
        realPlayer.discardLeaderCard(lc2);

        LeaderCard lc3 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.ANY, 1);
                }},
                new EnumMap<>(Resource.class),
                103
        );
        realPlayer.addLeaderCard(lc3);
        realPlayer.addLeaderCard(lc2);

        for (int i=0; i<3; i++){
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            marketController.selectFromMarket(i, true);
            marketController.takeFromMarket();

            assertEquals(realPlayer.getMicroTurnType(), MicroTurnType.PLACE_RESOURCES);
            assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);
            assertFalse(realPlayer.getSupportContainer().content().containsKey(Resource.WHITE));
            assertFalse(realPlayer.getSupportContainer().content().containsKey(Resource.ANY));
        }

        realPlayer.discardLeaderCard(lc2);
        LeaderCard lc4 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.FAITH, 1);
                }},
                new EnumMap<>(Resource.class),
                103
        );
        realPlayer.addLeaderCard(lc4);

        int initialPosition;
        assertEquals(lc3, realPlayer.getLeaderCards()[0]);
        realPlayer.getLeaderCards()[0].play();

        for (int i=0; i<12; i++){
            initialPosition = realPlayer.getPosition();
            realPlayer.getSupportContainer().clear();
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            marketController.selectFromMarket((i % 3), true);
            marketController.takeFromMarket();

            assertEquals(realPlayer.getMicroTurnType(), MicroTurnType.PLACE_RESOURCES);
            assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);
            assertFalse(realPlayer.getSupportContainer().content().containsKey(Resource.WHITE));
            assertEquals(4, (realPlayer.getPosition() - initialPosition) + realPlayer.getSupportContainer().countAll());
        }

        realPlayer.discardLeaderCard(lc4);
        realPlayer.addLeaderCard(lc2);
        realPlayer.getLeaderCards()[1].play();

        for (int i=0; i<12; i++){
            initialPosition = realPlayer.getPosition();
            realPlayer.getSupportContainer().clear();
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            marketController.selectFromMarket((i % 3), true);
            marketController.takeFromMarket();

            assertEquals(realPlayer.getMicroTurnType(), MicroTurnType.PLACE_RESOURCES);
            assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);
            assertFalse(realPlayer.getSupportContainer().contains(new EnumMap<>(Resource.class){{
                put(Resource.WHITE, 1);
            }}));
            assertEquals(4, (realPlayer.getPosition() - initialPosition) + realPlayer.getSupportContainer().countAll());
        }

        realPlayer.discardLeaderCard(lc2);
        realPlayer.addLeaderCard(lc4);
        realPlayer.getLeaderCards()[1].play();

        for (int i=0; i<36; i++){
            initialPosition = realPlayer.getPosition();
            realPlayer.getSupportContainer().clear();
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            realPlayer.setMicroTurnType(MicroTurnType.NONE);
            marketController.selectFromMarket((i % 3), true);
            marketController.takeFromMarket();

            assertEquals(realPlayer.getMacroTurnType(), MacroTurnType.GET_FROM_MARKET);
            if(table.turnOf().getMicroTurnType() == MicroTurnType.SELECT_LEADER_CARD)
                assertTrue(realPlayer.getSupportContainer().content().containsKey(Resource.WHITE));
            else
                assertFalse(realPlayer.getSupportContainer().content().containsKey(Resource.WHITE));

            assertEquals(4, (realPlayer.getPosition() - initialPosition) + realPlayer.getSupportContainer().countAll());
        }
    }

    @Test
    @DisplayName("quit from market selection testing")
    public void testQuit(){
        Table table = new Table(2);
        RealPlayer realPlayer1 = new RealPlayer(new User("user1", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer1);
        RealPlayer realPlayer2 = new RealPlayer(new User("user2", new FakeConnectionHandler(2)));
        table.addPlayer(realPlayer2);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);

        assertEquals(realPlayer1, table.turnOf());
        realPlayer1.setMicroTurnType(MicroTurnType.SELECTION_IN_MARKET);
        table.getMarket().selectColumn(3);
        marketController.quit();
        assertFalse(table.getMarket().areThereSelections());
        assertEquals(realPlayer1.getMicroTurnType(), MicroTurnType.NONE);

        realPlayer1.setMicroTurnType(MicroTurnType.PLACE_RESOURCES);
        realPlayer1.getShelves()[2].addAllIfPossible(Resource.COIN, 2);
        realPlayer1.getShelves()[0].addAllIfPossible(Resource.STONE, 1);
        realPlayer1.getShelves()[2].singleSelection();
        realPlayer1.getShelves()[2].singleSelection();
        realPlayer1.getShelves()[0].singleSelection();
        realPlayer1.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SERVANT, 2);
            put(Resource.COIN, 2);
        }});
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer1.addLeaderCard(lc);
        realPlayer1.getLeaderCards()[0].play();
        realPlayer1.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer1.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer1.getLeaderCards()[0].getAbility().select(Resource.COIN, 1);
        realPlayer1.getLeaderCards()[0].getAbility().select(Resource.COIN, 2);

        assertEquals(realPlayer2.getPosition(), 0);
        assertEquals(6, realPlayer1.getSupportContainer().countAll());
        assertEquals(6, table.turnOf().getSupportContainer().countAll());
        assertEquals(2, faithTrackController.getTable().getPlayers().length);
        assertEquals(realPlayer1, faithTrackController.getTable().getPlayers()[0]);
        assertEquals(realPlayer2, faithTrackController.getTable().getPlayers()[1]);
        marketController.quit();
        assertEquals(2,table.getPlayers().length);
        assertEquals(realPlayer2, table.getPlayers()[1]);
        assertEquals(realPlayer1, table.turnOf());
        assertEquals(realPlayer2.getPosition(), 6);
        assertNull(realPlayer1.getSupportContainer().getSelection());
        assertNull(realPlayer1.getSupportContainer().content());
        assertEquals(realPlayer1.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class));
        assertEquals(realPlayer1.getShelves()[2].getResourceType(), Resource.COIN);
        assertEquals(realPlayer1.getShelves()[0].getResourceType(), Resource.STONE);
        assertEquals(realPlayer1.getShelves()[2].getQuantitySelected(), 0);
        assertEquals(realPlayer1.getShelves()[0].getQuantitySelected(), 0);
        assertEquals(realPlayer1.getShelves()[2].getUsage(), 2);
        assertEquals(realPlayer1.getShelves()[0].getUsage(), 1);
        assertEquals(MacroTurnType.DONE, realPlayer1.getMacroTurnType());
        assertEquals(MicroTurnType.NONE, realPlayer1.getMicroTurnType());
    }

    @Test
    @DisplayName("transmutation testing")
    public void testTransmutation(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);

        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.WHITE, 4);
        }});
        LeaderCard lc1 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.ANY, 1);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc1);
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.FAITH, 1);
                }},
                new EnumMap<>(Resource.class),
                102
        );
        realPlayer.addLeaderCard(lc2);
        realPlayer.getLeaderCards()[1].play();

        realPlayer.setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        marketController.selectTransmutation(101, 2, 102, 2);
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.WHITE, 4);
        }});

        realPlayer.setMicroTurnType(MicroTurnType.SELECT_LEADER_CARD);
        marketController.selectTransmutation(101, 1, 102, 2);
        assertEquals("Wrong amount specified", realPlayer.getErrorMessage());
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.WHITE, 4);
        }});

        marketController.selectTransmutation(101, 3, 102, 2);
        assertEquals("Wrong amount specified", realPlayer.getErrorMessage());
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.WHITE, 4);
        }});

        int previousPosition = realPlayer.getPosition();
        marketController.selectTransmutation(101, 2, 102, 2);
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.ANY, 2);
        }});
        assertEquals(2, realPlayer.getPosition() - previousPosition);
        assertEquals(MicroTurnType.PLACE_RESOURCES, table.turnOf().getMicroTurnType());

        table.turnOf().getSupportContainer().clear();
        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.WHITE, 4);
        }});
        LeaderCard lc4 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.DISCOUNT,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SERVANT, 3);
                    put(Resource.SHIELD, 3);
                    put(Resource.STONE, 3);
                }},
                new EnumMap<>(Resource.class),
                104
        );
        table.turnOf().discardLeaderCard(lc2);
        realPlayer.addLeaderCard(lc4);
        realPlayer.getLeaderCards()[1].play();

        realPlayer.setMicroTurnType(MicroTurnType.SELECT_LEADER_CARD);

        marketController.selectTransmutation(101, 2, 102, 2);
        assertEquals("Not own leader card", table.turnOf().getErrorMessage());
        marketController.selectTransmutation(101, 2, 104, 2);
        assertEquals("The selected leader card cannot transmute", table.turnOf().getErrorMessage());
        marketController.selectTransmutation(104, 2, 101, 2);
        assertEquals("The selected leader card cannot transmute", table.turnOf().getErrorMessage());

        LeaderCard lc3 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SERVANT, 3);
                    put(Resource.SHIELD, 3);
                    put(Resource.STONE, 3);
                }},
                new EnumMap<>(Resource.class),
                103
        );
        table.turnOf().discardLeaderCard(lc4);
        realPlayer.addLeaderCard(lc3);
        realPlayer.getLeaderCards()[1].play();

        previousPosition = realPlayer.getPosition();
        marketController.selectTransmutation(101, 1, 103, 3);
        assertEquals(realPlayer.getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.ANY, 1);
            put(Resource.COIN, 9);
            put(Resource.SERVANT, 9);
            put(Resource.SHIELD, 9);
            put(Resource.STONE, 9);
        }});
        assertEquals(realPlayer.getPosition(), previousPosition);
        assertEquals(MicroTurnType.PLACE_RESOURCES, table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("exchange test")
    public void testExchange(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler(1)));
        table.addPlayer(realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        MarketController marketController = new MarketController(faithTrackController);

        table.turnOf().getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        table.turnOf().getSupportContainer().singleSelection(Resource.COIN);
        table.turnOf().getShelves()[1].addAllIfPossible(Resource.SERVANT, 1);
        table.turnOf().getShelves()[1].singleSelection();
        LeaderCard lc1 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.TRANSMUTATION,
                new EnumMap<>(Resource.class){{
                    put(Resource.ANY, 1);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc1);
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 1);
                    put(Resource.SHIELD, 1);
                    put(Resource.SERVANT, 1);
                }},
                new EnumMap<>(Resource.class),
                102
        );
        realPlayer.addLeaderCard(lc2);
        table.turnOf().getLeaderCards()[1].play();


        table.turnOf().setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);

        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);

        table.turnOf().setMicroTurnType(MicroTurnType.PLACE_RESOURCES);

        table.turnOf().getShelves()[1].singleDeselection();
        marketController.exchange();
        assertEquals("Less than two selections", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);

        table.turnOf().getShelves()[1].singleSelection();
        table.turnOf().getShelves()[2].addAllIfPossible(Resource.STONE, 2);
        table.turnOf().getShelves()[2].singleSelection();
        realPlayer.getLeaderCards()[1].play();
        realPlayer.getLeaderCards()[1].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[1].getAbility().add(Resource.SHIELD);
        realPlayer.getLeaderCards()[1].getAbility().select(Resource.COIN, 1);
        realPlayer.getLeaderCards()[1].getAbility().select(Resource.SHIELD, 1);
        marketController.exchange();
        assertEquals("More than two selections", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});


        realPlayer.getLeaderCards()[1].getAbility().select(Resource.COIN, 1);
        realPlayer.getLeaderCards()[1].getAbility().select(Resource.SHIELD, 1);
        table.turnOf().getShelves()[1].singleDeselection();
        table.turnOf().getSupportContainer().singleSelection(Resource.STONE);

        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        marketController.exchange();
        assertEquals("Too many type of resources selected", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getSupportContainer().singleDeselection(Resource.STONE);
        marketController.exchange();
        assertEquals("Wrong type of Resource", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
        }});
        table.turnOf().getSupportContainer().singleDeselection(Resource.COIN);
        table.turnOf().getSupportContainer().singleSelection(Resource.STONE);
        table.turnOf().getSupportContainer().singleSelection(Resource.STONE);
        table.turnOf().getSupportContainer().singleSelection(Resource.STONE);

        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
        }});

        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);

        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);

        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getCapacity(), 3);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);

        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        marketController.exchange();
        assertEquals("Shelf cannot contain that quantity", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getSupportContainer().singleSelection(Resource.STONE);
        table.turnOf().getShelves()[2].singleSelection();
        marketController.exchange();
        assertEquals("Shelf cannot contain that quantity", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 4);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 2);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getSupportContainer().clearSelection();
        table.turnOf().getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 4);
        }});
        table.turnOf().getSupportContainer().singleSelection(Resource.SERVANT);
        marketController.exchange();
        assertEquals("Resource already contained in another Shelf", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 4);
        }});
        assertEquals(table.turnOf().getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 1);
        }});
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 2);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getSupportContainer().clearSelection();
        table.turnOf().getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 4);
        }});
        table.turnOf().getSupportContainer().singleSelection(Resource.COIN);
        table.turnOf().getSupportContainer().singleSelection(Resource.COIN);
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().discardLeaderCard(lc1);
        table.turnOf().discardLeaderCard(lc2);
        LeaderCard lc3 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SERVANT, 3);
                    put(Resource.STONE, 3);
                }},
                new EnumMap<>(Resource.class),
                103
        );
        realPlayer.addLeaderCard(lc3);
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc4 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 2);
                    put(Resource.SHIELD, 2);
                    put(Resource.SERVANT, 2);
                }},
                new EnumMap<>(Resource.class),
                104
        );
        realPlayer.addLeaderCard(lc4);
        table.turnOf().getLeaderCards()[1].play();
        table.turnOf().getLeaderCards()[1].getAbility().add(Resource.SHIELD);
        table.turnOf().getLeaderCards()[1].getAbility().add(Resource.SHIELD);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.SHIELD, 1);
        table.turnOf().getSupportContainer().singleSelection(Resource.COIN);

        marketController.exchange();

        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getShelves()[1].singleSelection();
        table.turnOf().getShelves()[2].singleSelection();
        marketController.exchange();
        assertEquals("Cannot swap shelves content", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getShelves()[1].singleDeselection();
        table.turnOf().getShelves()[2].singleSelection();
        table.turnOf().getShelves()[0].addAllIfPossible(Resource.SHIELD, 1);
        table.turnOf().getShelves()[0].singleSelection();
        marketController.exchange();
        assertEquals("Swap is exceeding one shelf limit", table.turnOf().getErrorMessage());
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[0].getResourceType(), Resource.SHIELD);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 2);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getShelves()[0].singleDeselection();
        table.turnOf().getShelves()[1].singleSelection();
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[0].getResourceType(), Resource.SHIELD);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getShelves()[2].singleSelection();
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.SHIELD, 2);
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[0].getResourceType(), Resource.SHIELD);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.SHIELD, 1);
        }});

        table.turnOf().getShelves()[2].singleDeselection();
        table.turnOf().getShelves()[0].singleSelection();
        table.turnOf().getShelves()[0].takeSelected();
        table.turnOf().getShelves()[0].addAllIfPossible(Resource.STONE, 1);
        table.turnOf().getShelves()[0].singleSelection();
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 2);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 1);
        assertEquals(table.turnOf().getShelves()[0].getResourceType(), Resource.STONE);
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.SHIELD, 1);
        }});

        table.turnOf().getShelves()[0].takeSelected();
        table.turnOf().getShelves()[1].singleSelection();
        table.turnOf().getShelves()[1].singleSelection();
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.SHIELD, 2);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.COIN, 1);
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertNull(table.turnOf().getShelves()[0].getResourceType());
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));

        table.turnOf().getLeaderCards()[0].getAbility().add(Resource.STONE);
        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.STONE, 1);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.COIN, 1);
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertNull(table.turnOf().getShelves()[0].getResourceType());
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});

        table.turnOf().getLeaderCards()[0].getAbility().add(Resource.COIN);
        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.STONE, 1);
        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.COIN, 1);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.COIN, 1);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.SHIELD, 2);
        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertNull(table.turnOf().getShelves()[0].getResourceType());
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.SHIELD, 1);
        }});

        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.COIN, 1);
        table.turnOf().getLeaderCards()[0].getAbility().add(Resource.SERVANT);
        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.SERVANT, 1);
        table.turnOf().getLeaderCards()[0].getAbility().add(Resource.SERVANT);
        table.turnOf().getLeaderCards()[0].getAbility().select(Resource.SERVANT, 2);

        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.SHIELD, 2);
        table.turnOf().getLeaderCards()[1].getAbility().select(Resource.COIN, 1);

        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertNull(table.turnOf().getShelves()[0].getResourceType());
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 1);
            put(Resource.SERVANT, 2);
        }});
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 1);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
        }});

        marketController.exchange();
        assertEquals(table.turnOf().getSupportContainer().content(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.STONE, 6);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 4);
        }});
        assertNull(table.turnOf().getSupportContainer().getSelection());
        assertEquals(table.turnOf().getShelves()[1].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[1].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[1].getResourceType(), Resource.COIN);
        assertEquals(table.turnOf().getShelves()[0].getUsage(), 0);
        assertEquals(table.turnOf().getShelves()[0].getQuantitySelected(), 0);
        assertNull(table.turnOf().getShelves()[0].getResourceType());
        assertEquals(table.turnOf().getShelves()[2].getUsage(), 1);
        assertEquals(table.turnOf().getShelves()[2].getQuantitySelected(), 0);
        assertEquals(table.turnOf().getShelves()[2].getResourceType(), Resource.SERVANT);
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 2);
        }});
        assertEquals(table.turnOf().getLeaderCards()[0].getAbility().getSelected(), new EnumMap<>(Resource.class));
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 2);
        }});
        assertEquals(table.turnOf().getLeaderCards()[1].getAbility().getSelected(), new EnumMap<>(Resource.class));
    }
}
