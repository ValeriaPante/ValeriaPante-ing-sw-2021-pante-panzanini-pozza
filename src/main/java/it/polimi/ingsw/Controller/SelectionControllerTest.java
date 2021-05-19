package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.SelectionController;
import it.polimi.ingsw.Decks.LeaderDeck;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionControllerTest {
    SelectionController selectionController;

    @Test
    @DisplayName("leaderCardFromIdTest")
    public void testLCFID(){
        //singlePLayer
        RealPlayer player = new RealPlayer("nickPlayer1");
        Table table = new Table(1);
        table.addPlayer(player);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
                new EnumMap<Resource, Integer>(Resource.class),
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
        RealPlayer player2 = new RealPlayer("nickPlayer2");
        table = new Table(2);
        table.addPlayer(player);
        table.addPlayer(player2);
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
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
        RealPlayer player = new RealPlayer("nickPlayer1");
        Table table = new Table(1);
        table.addPlayer(player);
        LeaderCard lc = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
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
        RealPlayer player2 = new RealPlayer("nickPlayer2");
        table = new Table(2);
        table.addPlayer(player);
        table.addPlayer(player2);
        LeaderCard lc2 = new LeaderCard(
                2,
                new EnumMap<>(Resource.class),
                new HashMap<>(),
                LeaderCardType.PRODPOWER,
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
    @DisplayName("leader cards test")
    public void testLC(){
//        RealPlayer player = new RealPlayer("testingPlayer");
//        Table table = new Table(1);
//        table.addPlayer(player);
//        selectionController = new SelectionController(new FaithTrackController(table));
//
//        LeaderDeck deck = new LeaderDeck();
//        deck.shuffle();
//        LeaderCard pickedCard;
//        LeaderCard[] arrayOfLC = new LeaderCard[4];
//        int storageLC = 0;
//        int notStorageLC = 0;
//        while ((storageLC + notStorageLC) < 4){
//            pickedCard = deck.draw();
//            try{
//                pickedCard.getAbility().getCapacity();
//                if (storageLC < 2){
//                    arrayOfLC[storageLC] = pickedCard;
//                    storageLC ++;
//                }
//            } catch (WeDontDoSuchThingsHere e) {
//                if (notStorageLC < 2){
//                    arrayOfLC[notStorageLC + 2] = pickedCard;
//                    notStorageLC ++;
//                }
//            }
//        }
//
//        player.addLeaderCard(arrayOfLC[0]);
//        player.addLeaderCard(arrayOfLC[2]);
//        arrayOfLC[0].play();
//        int count = 0;
//        LeaderCard[] playersLC = table.turnOf().getLeaderCards();
//        for (LeaderCard lc : playersLC){
//            if (lc == arrayOfLC[0]) {
//                assertTrue(lc.hasBeenPlayed());
//                count++;
//            }
//            else assertFalse(lc.hasBeenPlayed());
//        }
//        assertEquals(count, 1);
    }
}
