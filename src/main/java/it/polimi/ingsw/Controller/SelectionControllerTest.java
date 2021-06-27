package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Decks.LeaderDeck;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionControllerTest {
    SelectionController selectionController;

    public class FakeConnectionHandler implements MessageSenderInterface {

        @Override
        public void send(FromServerMessage message) {
            System.out.println("Sending message...");
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public void setId(int id) {

        }

        @Override
        public void setRequestHandler(RequestHandler requestHandler) {

        }

        @Override
        public void closeConnection() {

        }


    }

    @Test
    @DisplayName("leaderCardFromIdTest")
    public void testLCFID(){
        //singlePLayer
        RealPlayer player = new RealPlayer(new User("nickPlayer1", new FakeConnectionHandler()));
        Table table = new Table(1);
        table.addPlayer(player);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<Resource, Integer>(Resource.class),
                new EnumMap<>(Resource.class),
                9999
                );
        player.addLeaderCard(lc);
        selectionController = new SelectionController(new FaithTrackController(table));

        assertEquals(table.turnOf(), player);
        assertEquals(lc, selectionController.leaderCardFromID(9999));
        assertNull(player.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(3));
        assertEquals("Not own leader card", player.getErrorMessage());

        //multiplayer
        RealPlayer player2 = new RealPlayer(new User("nickPlayer2", new FakeConnectionHandler()));
        table = new Table(2);
        table.addPlayer(player);
        table.addPlayer(player2);
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                9998
        );
        player2.addLeaderCard(lc2);
        selectionController = new SelectionController(new FaithTrackController(table));

        assertEquals(table.turnOf(), player);
        assertNotEquals(table.turnOf(), player2);
        assertEquals(lc, selectionController.leaderCardFromID(9999));
        assertNull(selectionController.leaderCardFromID(9998));
        assertEquals("Not own leader card", player.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(5));
        assertEquals("Not own leader card", player.getErrorMessage());

        table.nextTurn();

        assertEquals(table.turnOf(), player2);
        assertNotEquals(table.turnOf(), player);
        assertEquals(lc2, selectionController.leaderCardFromID(9998));
        assertNull(player2.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(9999));
        assertEquals("Not own leader card", player2.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(5));
        assertEquals("Not own leader card", player2.getErrorMessage());
    }

    @Test
    @DisplayName("getUsableLeaderCardTest")
    public void testGULC(){
        //singlePLayer
        RealPlayer player = new RealPlayer(new User("nickPlayer1", new FakeConnectionHandler()));
        Table table = new Table(1);
        table.addPlayer(player);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                9999
        );
        player.addLeaderCard(lc);
        LeaderCard lc1 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                9997
        );
        player.addLeaderCard(lc1);
        lc1.play();
        selectionController = new SelectionController(new FaithTrackController(table));

        assertEquals(table.turnOf(), player);
        assertNull(player.getErrorMessage());
        assertNull(selectionController.getUsableLeaderCard(9999));
        assertEquals("Card not played", player.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(3));
        assertEquals("Not own leader card", player.getErrorMessage());
        assertEquals(lc1, selectionController.getUsableLeaderCard(9997));

        lc.play();
        assertEquals(lc, selectionController.getUsableLeaderCard(9999));

        //multiplayer
        RealPlayer player2 = new RealPlayer(new User("nickPlayer2", new FakeConnectionHandler()));
        table = new Table(2);
        table.addPlayer(player);
        table.addPlayer(player2);
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                9998
        );
        player2.addLeaderCard(lc2);
        LeaderCard lc3 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<>(Resource.class),
                new EnumMap<>(Resource.class),
                9996
        );
        player2.addLeaderCard(lc3);
        selectionController = new SelectionController(new FaithTrackController(table));

        assertEquals(table.turnOf(), player);
        assertNotEquals(table.turnOf(), player2);
        assertEquals(lc, selectionController.getUsableLeaderCard(9999));
        assertNull(selectionController.getUsableLeaderCard(9998));
        assertEquals("Not own leader card", player.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(5));
        assertEquals("Not own leader card", player.getErrorMessage());

        table.nextTurn();

        assertEquals(table.turnOf(), player2);
        assertNotEquals(table.turnOf(), player);
        assertNull(selectionController.getUsableLeaderCard(9998));
        assertEquals("Card not played", player2.getErrorMessage());
        assertNull(selectionController.leaderCardFromID(9999));
        assertEquals("Not own leader card", player2.getErrorMessage());
        assertNull(selectionController.getUsableLeaderCard(9996));
        assertEquals("Card not played", player2.getErrorMessage());

        lc2.play();

        assertEquals(lc2, selectionController.getUsableLeaderCard(9998));

        player2.discardLeaderCard(lc3);

        assertNull(selectionController.getUsableLeaderCard(9996));
        assertEquals("Not own leader card", player2.getErrorMessage());
    }

    @Test
    @DisplayName("shelf interaction test")
    public void testShelf(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler()));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        SelectionController selectionController = new SelectionController(faithTrackController);

        selectionController.selectFromShelf(Resource.ANY, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Illegal resource type specified");
        selectionController.selectFromShelf(Resource.COIN, 0);
        assertEquals(table.turnOf().getErrorMessage(), "Capacity specified is exceeding limits");
        selectionController.selectFromShelf(Resource.COIN, 4);
        assertEquals(table.turnOf().getErrorMessage(), "Capacity specified is exceeding limits");
        realPlayer.getShelves()[1].addAllIfPossible(Resource.STONE, 2);
        selectionController.selectFromShelf(Resource.COIN, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Wrong Resource type selected");
        assertEquals(0, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.selectFromShelf(Resource.STONE, 2);
        assertEquals(1, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.selectFromShelf(Resource.STONE, 2);
        assertEquals(2, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.selectFromShelf(Resource.STONE, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Maximum already selected");

        selectionController.deselectFromShelf(Resource.ANY, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Illegal resource type specified");
        selectionController.deselectFromShelf(Resource.COIN, 0);
        assertEquals(table.turnOf().getErrorMessage(), "Capacity specified is exceeding limits");
        selectionController.deselectFromShelf(Resource.COIN, 4);
        assertEquals(table.turnOf().getErrorMessage(), "Capacity specified is exceeding limits");
        selectionController.deselectFromShelf(Resource.COIN, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Wrong Resource type selected");
        assertEquals(2, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.deselectFromShelf(Resource.STONE, 2);
        assertEquals(1, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.deselectFromShelf(Resource.STONE, 2);
        assertEquals(0, table.turnOf().getShelves()[1].getQuantitySelected());
        selectionController.deselectFromShelf(Resource.STONE, 2);
        assertEquals(table.turnOf().getErrorMessage(), "Noting to deselect");

        assertEquals(selectionController.shelfFromCapacity(2), realPlayer.getShelves()[1]);
        selectionController.shelfFromCapacity(4);
        assertEquals(realPlayer.getErrorMessage(), "Capacity specified is exceeding limits");
        selectionController.shelfFromCapacity(0);
        assertEquals(realPlayer.getErrorMessage(), "Capacity specified is exceeding limits");
    }

    @Test
    @DisplayName("box interaction test")
    public void testBox(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler()));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        SelectionController selectionController = new SelectionController(faithTrackController);


        realPlayer.getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 4);
            put(Resource.COIN, 4);
        }});
        assertEquals(realPlayer, table.turnOf());
        selectionController.selectFromStrongBox(Resource.FAITH, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.WHITE, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.COIN, 0);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.COIN, -2);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.SERVANT, 3);
        assertEquals("Selection exceeding limits", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.COIN, 5);
        assertEquals("Selection exceeding limits", realPlayer.getErrorMessage());
        selectionController.selectFromStrongBox(Resource.COIN, 3);
        assertEquals(realPlayer.getStrongBox().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 3);
        }});

        selectionController.deselectFromStrongBox(Resource.FAITH, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.WHITE, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.COIN, 0);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.COIN, -2);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.SERVANT, 3);
        assertEquals("Deselection exceeding limits", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.COIN, 5);
        assertEquals("Deselection exceeding limits", realPlayer.getErrorMessage());
        selectionController.deselectFromStrongBox(Resource.COIN, 2);
        assertEquals(realPlayer.getStrongBox().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        selectionController.deselectFromStrongBox(Resource.COIN, 1);
        assertNull(realPlayer.getStrongBox().getSelection());

        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 4);
            put(Resource.COIN, 4);
        }});
        selectionController.selectFromSupportContainer(Resource.WHITE, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.selectFromSupportContainer(Resource.COIN, 0);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.selectFromSupportContainer(Resource.COIN, -2);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.selectFromSupportContainer(Resource.SERVANT, 3);
        assertEquals("Selection exceeding limits", realPlayer.getErrorMessage());
        selectionController.selectFromSupportContainer(Resource.COIN, 5);
        assertEquals("Selection exceeding limits", realPlayer.getErrorMessage());
        selectionController.selectFromSupportContainer(Resource.COIN, 3);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 3);
        }});

        selectionController.deselectFromSupportContainer(Resource.WHITE, 2);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.deselectFromSupportContainer(Resource.COIN, 0);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.deselectFromSupportContainer(Resource.COIN, -2);
        assertEquals("Wrong quantity specified", realPlayer.getErrorMessage());
        selectionController.deselectFromSupportContainer(Resource.SERVANT, 3);
        assertEquals("Deselection exceeding limits", realPlayer.getErrorMessage());
        selectionController.deselectFromSupportContainer(Resource.COIN, 5);
        assertEquals("Deselection exceeding limits", realPlayer.getErrorMessage());
        selectionController.deselectFromSupportContainer(Resource.COIN, 2);
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 1);
        }});
        selectionController.deselectFromSupportContainer(Resource.COIN, 1);
        assertNull(realPlayer.getSupportContainer().getSelection());
    }

    @Test
    @DisplayName("leader card interaction test")
    public void testLeaderCard(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler()));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        SelectionController selectionController = new SelectionController(faithTrackController);
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
        realPlayer.getLeaderCards()[0].play();
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.STORAGE,
                new EnumMap<>(Resource.class){{
                    put(Resource.COIN, 3);
                    put(Resource.SHIELD, 2);
                    put(Resource.SERVANT, 2);
                }},
                new EnumMap<>(Resource.class),
                101
        );
        realPlayer.addLeaderCard(lc2);
        realPlayer.getLeaderCards()[1].play();

        assertEquals(realPlayer.getLeaderCards()[0], lc1);
        assertEquals(realPlayer.getLeaderCards()[1], lc2);

        selectionController.selectFromLeaderStorage(Resource.FAITH, 0, 0);
        assertEquals("Illegal resource type specified", realPlayer.getErrorMessage());
        selectionController.selectFromLeaderStorage(Resource.STONE, 100, 2);
        assertEquals("Wrong leader card", realPlayer.getErrorMessage());

        realPlayer.getLeaderCards()[1].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[1].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[1].getAbility().add(Resource.SHIELD);
        realPlayer.getLeaderCards()[1].getAbility().add(Resource.SHIELD);
        selectionController.selectFromLeaderStorage(Resource.STONE, 101, 2);
        assertEquals("Resource type not allowed", realPlayer.getErrorMessage());
        selectionController.selectFromLeaderStorage(Resource.COIN, 101, 3);
        assertEquals("Not selectable", realPlayer.getErrorMessage());
        selectionController.selectFromLeaderStorage(Resource.COIN, 101, 2);
        selectionController.selectFromLeaderStorage(Resource.COIN, 101, 1);
        assertEquals(realPlayer.getLeaderCards()[1].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 2);
        }});
        selectionController.selectFromLeaderStorage(Resource.COIN, 101, 1);
        assertEquals(realPlayer.getLeaderCards()[1].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class){{
            put(Resource.COIN, 1);
        }});
    }

    @Test
    @DisplayName("block deselection test")
    public void testBlockDeselection(){
        Table table = new Table(1);
        RealPlayer realPlayer = new RealPlayer(new User("user", new FakeConnectionHandler()));
        table.addPlayer(realPlayer);
        assertEquals(table.turnOf(), realPlayer);
        FaithTrackController faithTrackController = new FaithTrackController(table);
        SelectionController selectionController = new SelectionController(faithTrackController);
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

        assertDoesNotThrow(selectionController::deselectAllResources);
        realPlayer.getShelves()[0].addWhatPossible(Resource.SHIELD, 1);
        realPlayer.getShelves()[1].addWhatPossible(Resource.STONE, 1);
        realPlayer.getShelves()[2].addWhatPossible(Resource.COIN, 3);
        realPlayer.getShelves()[0].singleSelection();
        realPlayer.getShelves()[1].singleSelection();
        realPlayer.getShelves()[2].singleSelection();
        assertEquals(1, realPlayer.getShelves()[0].getQuantitySelected());
        assertEquals(1, realPlayer.getShelves()[1].getQuantitySelected());
        assertEquals(1, realPlayer.getShelves()[2].getQuantitySelected());

        realPlayer.getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 4);
            put(Resource.COIN, 3);
        }});
        realPlayer.getStrongBox().mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 2);
            put(Resource.COIN, 2);
        }});
        assertEquals(realPlayer.getStrongBox().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 2);
            put(Resource.COIN, 2);
        }});

        realPlayer.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
            put(Resource.SHIELD, 1);
            put(Resource.COIN, 2);
        }});
        realPlayer.getSupportContainer().mapSelection(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 1);
            put(Resource.COIN, 2);
        }});
        assertEquals(realPlayer.getSupportContainer().getSelection(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 1);
            put(Resource.COIN, 2);
        }});

        realPlayer.getLeaderCards()[0].play();
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.COIN);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.SHIELD);
        realPlayer.getLeaderCards()[0].getAbility().add(Resource.SHIELD);
        realPlayer.getLeaderCards()[0].getAbility().select(Resource.COIN, 2);
        realPlayer.getLeaderCards()[0].getAbility().select(Resource.SHIELD, 1);
        realPlayer.getLeaderCards()[0].getAbility().select(Resource.SHIELD, 2);
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.COIN, 1);
            put(Resource.SHIELD, 2);
        }});

        selectionController.deselectAllResources();

        assertNull(realPlayer.getSupportContainer().getSelection());
        assertNull(realPlayer.getStrongBox().getSelection());
        assertEquals(0, realPlayer.getShelves()[0].getQuantitySelected());
        assertEquals(0, realPlayer.getShelves()[1].getQuantitySelected());
        assertEquals(0, realPlayer.getShelves()[2].getQuantitySelected());
        assertEquals(realPlayer.getLeaderCards()[0].getAbility().getSelected(), new EnumMap<Resource, Integer>(Resource.class));
    }
}
