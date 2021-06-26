package ControllerTest;

import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.MarketController;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    @Test
    @DisplayName("creation test")
    public void testCreation(){
        assertThrows(NullPointerException.class,() -> new GameController(null));
        Lobby problematicLobby = new Lobby(23);
        assertThrows(IndexOutOfBoundsException.class,() -> new GameController(problematicLobby));
        problematicLobby.addUser(new User("Username1", new FakeConnectionHandler()));
        problematicLobby.addUser(new User("Username2", new FakeConnectionHandler()));
        problematicLobby.addUser(new User("Username3", new FakeConnectionHandler()));
        problematicLobby.addUser(new User("Username4", new FakeConnectionHandler()));
        problematicLobby.addUser(new User("Username5", new FakeConnectionHandler()));
        assertThrows(IndexOutOfBoundsException.class,() -> new GameController(problematicLobby));

        Lobby multiPlayerLobby = new Lobby(3);
        User user1 = new User("user1", new FakeConnectionHandler());
        User user2 = new User("user2", new FakeConnectionHandler());
        User user3 = new User("user3", new FakeConnectionHandler());
        multiPlayerLobby.addUser(user1);
        multiPlayerLobby.addUser(user2);
        multiPlayerLobby.addUser(user3);
        GameController gameControllerMulti = new GameController(multiPlayerLobby);
        assertFalse(gameControllerMulti.getTable().isSinglePlayer());
        assertEquals(gameControllerMulti.getTable().getPlayers().length, 3);
        for (RealPlayer player : gameControllerMulti.getTable().getPlayers()){
            boolean isInside = false;
            for (User user : multiPlayerLobby.getUsers()){
                if (player.getNickname().equals(user.getUsername())) {
                    isInside = true;
                    break;
                }
            }
            assertTrue(isInside);
        }

        Lobby singlePlayerLobby = new Lobby(1);
        User user4 = new User("user4", new FakeConnectionHandler());
        singlePlayerLobby.addUser(user4);
        GameController gameControllerSingle = new GameController(singlePlayerLobby);
        assertTrue(gameControllerSingle.getTable().isSinglePlayer());
        assertEquals(gameControllerSingle.getTable().getPlayers().length, 1);
        assertEquals(user4.getUsername(), gameControllerSingle.getTable().getPlayers()[0].getNickname());
    }

    @Test
    @DisplayName("discard leader cards test")
    public void testDiscardLC(){
        Lobby lobby = new Lobby(10);
        User user1 = new User("user1", new FakeConnectionHandler());
        lobby.addUser(user1);
        User user2 = new User("user2", new FakeConnectionHandler());
        lobby.addUser(user2);
        GameController gameController = new GameController(lobby);

        LeaderCard[] before = gameController.getTable().turnOf().getLeaderCards();
        gameController.getTable().turnOf().setMicroTurnType(MicroTurnType.NONE);
        gameController.discardLeaderCard(gameController.getTable().turnOf().getLeaderCards()[3].getId());
        LeaderCard[] after = new LeaderCard[4];
        for (int i=0; i<4; i++)
            after[i] = gameController.getTable().turnOf().getLeaderCards()[i];
        for (int i=0; i<4; i++)
            assertEquals(before[i], after[i]);

        gameController.getTable().turnOf().setMicroTurnType(MicroTurnType.DISCARD_LEADER_CARD);
        RealPlayer playerOfTurn = gameController.getTable().turnOf();
        int cardNotOwnByTheOtherPlayer = playerOfTurn.getLeaderCards()[3].getId();
        gameController.discardLeaderCard(playerOfTurn.getLeaderCards()[3].getId());
        gameController.discardLeaderCard(playerOfTurn.getLeaderCards()[2].getId());
        assertEquals(2, playerOfTurn.getLeaderCards().length);
        for (int i=0; i<2; i++)
            after[i] = playerOfTurn.getLeaderCards()[i];
        for (int i=0; i<2; i++)
            assertEquals(before[i], after[i]);
        RealPlayer theOtherPLayer = gameController.getTable().getPlayers()[1];
        assertEquals(gameController.getTable().turnOf(), theOtherPLayer);
        assertEquals(gameController.getTable().turnOf().getLeaderCards().length, 4);
        gameController.discardLeaderCard(cardNotOwnByTheOtherPlayer);
        assertEquals("Wrong selection, you do not own a leader card with such id!", theOtherPLayer.getErrorMessage());
        gameController.discardLeaderCard(5);
        assertEquals("Wrong selection, you do not own a leader card with such id!", theOtherPLayer.getErrorMessage());
        assertEquals(gameController.getTable().turnOf().getLeaderCards().length, 4);

        assertEquals(MicroTurnType.CHOOSE_RESOURCES, playerOfTurn.getMicroTurnType());
        assertEquals(MicroTurnType.DISCARD_LEADER_CARD, theOtherPLayer.getMicroTurnType());
        gameController.discardLeaderCard(gameController.getTable().turnOf().getLeaderCards()[3].getId());
        gameController.discardLeaderCard(gameController.getTable().turnOf().getLeaderCards()[2].getId());
        assertEquals(MicroTurnType.NONE, playerOfTurn.getMicroTurnType());
        assertEquals(MacroTurnType.NONE, playerOfTurn.getMacroTurnType());
        assertEquals(MicroTurnType.CHOOSE_RESOURCES, theOtherPLayer.getMicroTurnType());

        Lobby singlePLayerLobby = new Lobby(2);
        singlePLayerLobby.addUser(user1);
        GameController singlePlayerGameController = new GameController(singlePLayerLobby);
        RealPlayer onlyPlayer = singlePlayerGameController.getTable().turnOf();
        singlePlayerGameController.discardLeaderCard(singlePlayerGameController.getTable().turnOf().getLeaderCards()[2].getId());
        singlePlayerGameController.discardLeaderCard(singlePlayerGameController.getTable().turnOf().getLeaderCards()[2].getId());
        assertEquals(MicroTurnType.NONE, onlyPlayer.getMicroTurnType());
        assertEquals(MacroTurnType.NONE, onlyPlayer.getMacroTurnType());
    }

    @Test
    @DisplayName("choose resources test")
    public void testChooseResources(){
        Lobby multiPlayerLobby = new Lobby(5);
        Lobby singlePlayerLobby = new Lobby(1);
        User user1 = new User("user1", new FakeConnectionHandler());
        User user2 = new User("user2", new FakeConnectionHandler());
        User user3 = new User("user3", new FakeConnectionHandler());
        User user4 = new User("user4", new FakeConnectionHandler());
        multiPlayerLobby.addUser(user1);
        multiPlayerLobby.addUser(user2);
        multiPlayerLobby.addUser(user3);
        multiPlayerLobby.addUser(user4);
        singlePlayerLobby.addUser(user1);
        GameController singleGameController = new GameController(singlePlayerLobby);
        GameController multiGameController = new GameController(multiPlayerLobby);

        RealPlayer singlePlayer = singleGameController.getTable().turnOf();
        singleGameController.discardLeaderCard(singleGameController.getTable().turnOf().getLeaderCards()[2].getId());
        singleGameController.discardLeaderCard(singleGameController.getTable().turnOf().getLeaderCards()[2].getId());
        singleGameController.selectResource(2, Resource.SERVANT);
        assertEquals("Invalid command", singlePlayer.getErrorMessage());
        assertEquals(singlePlayer, singleGameController.getTable().turnOf());
        assertEquals(MicroTurnType.NONE, singleGameController.getTable().turnOf().getMicroTurnType());
        assertEquals(MacroTurnType.NONE, singleGameController.getTable().turnOf().getMacroTurnType());

        RealPlayer player1 = multiGameController.getTable().getPlayers()[0];
        RealPlayer player2 = multiGameController.getTable().getPlayers()[1];
        RealPlayer player3 = multiGameController.getTable().getPlayers()[2];
        RealPlayer player4 = multiGameController.getTable().getPlayers()[3];
        assertEquals(player3.getPosition(), 0);
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player3.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player3.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player4.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player4.getLeaderCards()[2].getId());
        assertEquals(player2, multiGameController.getTable().turnOf());
        assertEquals(MicroTurnType.CHOOSE_RESOURCES, multiGameController.getTable().turnOf().getMicroTurnType());
        multiGameController.selectResource(1, Resource.ANY);
        assertEquals("Illegal resource type selected", player2.getErrorMessage());
        multiGameController.selectResource(1, Resource.FAITH);
        assertEquals("Illegal resource type selected", player2.getErrorMessage());
        multiGameController.selectResource(1, Resource.WHITE);
        assertEquals("Illegal resource type selected", player2.getErrorMessage());
        multiGameController.selectResource(0, Resource.COIN);
        assertEquals("Bad shelf selection", player2.getErrorMessage());
        multiGameController.selectResource(12, Resource.COIN);
        assertEquals("Bad shelf selection", player2.getErrorMessage());

        multiGameController.selectResource(2, Resource.COIN);
        assertEquals(Resource.COIN, player2.getShelves()[1].getResourceType());
        assertEquals(1, player2.getShelves()[1].getUsage());
        assertEquals(player3, multiGameController.getTable().turnOf());
        assertEquals(player3.getPosition(), 0);
        multiGameController.selectResource(1, Resource.COIN);
        assertEquals(Resource.COIN, player3.getShelves()[0].getResourceType());
        assertEquals(1, player3.getShelves()[0].getUsage());
        assertEquals(player3.getPosition(), 1);
        assertEquals(player4, multiGameController.getTable().turnOf());
        multiGameController.selectResource(1, Resource.COIN);
        multiGameController.selectResource(2, Resource.COIN);
        assertEquals("Resource already contained in another shelf", player4.getErrorMessage());
        multiGameController.selectResource(1, Resource.COIN);
        assertEquals("Selected shelf cannot contain that resource", player4.getErrorMessage());
        multiGameController.selectResource(2, Resource.SHIELD);
        assertEquals(1, player4.getShelves()[0].getUsage());
        assertEquals(1, player4.getShelves()[1].getUsage());
        assertEquals(player4.getPosition(), 1);
        assertEquals(Resource.COIN, player4.getShelves()[0].getResourceType());
        assertEquals(Resource.SHIELD, player4.getShelves()[1].getResourceType());
        assertEquals(player1, multiGameController.getTable().turnOf());
        assertEquals(MacroTurnType.NONE, player1.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player2.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player3.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player4.getMacroTurnType());
        assertEquals(MicroTurnType.NONE, player1.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player2.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player3.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player4.getMicroTurnType());
        assertEquals(player2.getPosition(), 0);
        assertEquals(player1.getPosition(), 0);

        multiGameController = new GameController(multiPlayerLobby);
        player1 = multiGameController.getTable().getPlayers()[0];
        player2 = multiGameController.getTable().getPlayers()[1];
        player3 = multiGameController.getTable().getPlayers()[2];
        player4 = multiGameController.getTable().getPlayers()[3];
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player3.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player3.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player4.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player4.getLeaderCards()[2].getId());
        multiGameController.selectResource(1, Resource.COIN);
        multiGameController.selectResource(1, Resource.COIN);
        multiGameController.selectResource(2, Resource.COIN);
        multiGameController.selectResource(2, Resource.SHIELD);
        assertEquals("Cannot place the resource here", player4.getErrorMessage());
        multiGameController.selectResource(2, Resource.COIN);
        assertEquals(2, player4.getShelves()[1].getUsage());
        assertEquals(Resource.COIN, player4.getShelves()[1].getResourceType());
        assertEquals(player1, multiGameController.getTable().turnOf());
        assertEquals(MacroTurnType.NONE, player1.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player2.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player3.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player4.getMacroTurnType());
        assertEquals(MicroTurnType.NONE, player1.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player2.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player3.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player4.getMicroTurnType());

        multiPlayerLobby = new Lobby(6);
        multiPlayerLobby.addUser(user1);
        multiPlayerLobby.addUser(user2);
        multiGameController = new GameController(multiPlayerLobby);
        player1 = multiGameController.getTable().getPlayers()[0];
        player2 = multiGameController.getTable().getPlayers()[1];
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player1.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.discardLeaderCard(player2.getLeaderCards()[2].getId());
        multiGameController.selectResource(1, Resource.COIN);
        assertEquals(1, player2.getShelves()[0].getUsage());
        assertEquals(Resource.COIN, player2.getShelves()[0].getResourceType());
        assertEquals(player1, multiGameController.getTable().turnOf());
        assertEquals(MacroTurnType.NONE, player1.getMacroTurnType());
        assertEquals(MacroTurnType.NONE, player2.getMacroTurnType());
        assertEquals(MicroTurnType.NONE, player1.getMicroTurnType());
        assertEquals(MicroTurnType.NONE, player2.getMicroTurnType());
    }

    @Test
    public void testsEndTurn(){
        Lobby multiPlayerLobby = new Lobby(5);
        Lobby singlePlayerLobby = new Lobby(1);
        User user1 = new User("user1", new FakeConnectionHandler());
        User user2 = new User("user2", new FakeConnectionHandler());
        User user3 = new User("user3", new FakeConnectionHandler());
        User user4 = new User("user4", new FakeConnectionHandler());
        multiPlayerLobby.addUser(user1);
        multiPlayerLobby.addUser(user2);
        multiPlayerLobby.addUser(user3);
        multiPlayerLobby.addUser(user4);
        singlePlayerLobby.addUser(user1);
        GameController singleGameController = new GameController(singlePlayerLobby);
        GameController multiGameController = new GameController(multiPlayerLobby);

        String userOfTurn = multiGameController.getTable().turnOf().getNickname();
        multiGameController.endTurn();
        assertNotNull(multiGameController.getTable().turnOf().getErrorMessage());
        assertEquals(userOfTurn, multiGameController.getTable().turnOf().getNickname());

        multiGameController.getTable().getPlayers()[0].setMacroTurnType(MacroTurnType.DONE);
        multiGameController.endTurn();
        assertNotEquals(userOfTurn, multiGameController.getTable().turnOf().getNickname());

        singleGameController.getTable().getPlayers()[0].setMacroTurnType(MacroTurnType.DONE);
        singleGameController.endTurn();
        assertNull(singleGameController.getTable().turnOf().getErrorMessage());
        singleGameController.getTable().setLastLap();
        singleGameController.getTable().getPlayers()[0].setMacroTurnType(MacroTurnType.DONE);
        singleGameController.endTurn();

        multiGameController.getTable().setLastLap();
        multiGameController.getTable().turnOf().setMacroTurnType(MacroTurnType.DONE);
        multiGameController.endTurn();
        multiGameController.getTable().turnOf().setMacroTurnType(MacroTurnType.DONE);
        multiGameController.endTurn();
        multiGameController.getTable().turnOf().setMacroTurnType(MacroTurnType.DONE);
        multiGameController.endTurn();
        assertTrue(multiGameController.getTable().isLastLap());
    }
}
