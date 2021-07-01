package SupportCLITest;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.PrintWithoutMessageCreationException;
import it.polimi.ingsw.Exceptions.SuppressNotificationsException;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.CreationLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.DisconnectMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.MoveToLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.StartGameMessage;
import it.polimi.ingsw.View.CLI.InputManager;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InputManagerTest {
    @Test
    @DisplayName("case sensitivity test")
    public void test() {
        Game model = new Game();
        InputManager inputManager = new InputManager(model);
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("CREATE LOBBY")));
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("                 CREATE LOBBY          ")));
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("         crEate lObby          ")));
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("CREATE LBBY"));
        try{
            inputManager.preGameInput("CREATE LBBY");
        } catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "Your input was not correct! Please, retry...");
        }

        assertDoesNotThrow(() -> assertInstanceOf(MoveToLobbyMessage.class, inputManager.preGameInput("move to lobby 10")));
        MoveToLobbyMessage messageMTL = (MoveToLobbyMessage) inputManager.preGameInput("move to lobby 10");
        assertEquals(10, messageMTL.getLobbyId());
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("move to lobby a"));
        try{
            inputManager.preGameInput("move to lobby a");
        } catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "You entered a wrong integer! Please, retry...");
        }
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("Throw exception"));
    }

    @Test
    @DisplayName("pre game test")
    public void testPreGame(){
        Game model = new Game();
        InputManager inputManager = new InputManager(model);


        assertThrows(SuppressNotificationsException.class, () -> inputManager.preGameInput("NOTIFICATIONS"));
        assertThrows(SuppressNotificationsException.class, () -> inputManager.preGameInput("         NoTIFICatiONS          "));


        assertThrows(PrintWithoutMessageCreationException.class, () -> inputManager.preGameInput("PRINT: MY LOBBY"));
        try{
            inputManager.preGameInput("PRINT: MY LOBBY");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MY LOBBY");
        }
        assertThrows(PrintWithoutMessageCreationException.class, () -> inputManager.preGameInput("                 PRINT: My LobBY          "));
        try{
            inputManager.preGameInput("                 PRINT: My LobBY          ");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MY LOBBY");
        }


        assertThrows(PrintWithoutMessageCreationException.class, () -> inputManager.preGameInput("PRINT: ALL LOBBIES"));
        try{
            inputManager.preGameInput("PRINT: ALL LOBBIES");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "ALL LOBBIES");
        }
        assertThrows(PrintWithoutMessageCreationException.class, () -> inputManager.preGameInput("                 PRINT: aLL LobbIES          "));
        try{
            inputManager.preGameInput("                 PriNT: aLL LobbIES          ");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "ALL LOBBIES");
        }


        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("MOVE TO LOBBY e"));
        try{
            inputManager.preGameInput("MOVE TO LOBBY e");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You entered a wrong integer! Please, retry...");
        }
        assertInstanceOf(MoveToLobbyMessage.class, inputManager.preGameInput("MOVE TO LOBBY 3"));
        assertEquals(3, ((MoveToLobbyMessage) inputManager.preGameInput("MOVE TO LOBBY 3")).getLobbyId());


        assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("CReate LObby   "));
        assertInstanceOf(DisconnectMessage.class, inputManager.preGameInput("  DiscoNNect"));


        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("start GAME"));
        try{
            inputManager.preGameInput("start GAME");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You are not inside a lobby yet, please pick one");
        }
        model.setLocalPlayerLobbyId(3);
        assertInstanceOf(StartGameMessage.class, inputManager.preGameInput("start GAME"));


        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("invalid request"));
        try{
            inputManager.preGameInput("invalid request");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Your input was not correct! Please, retry...");
        }
    }

    @Test
    @DisplayName("initialization test")
    public void testInitialization(){
        Game model = new Game();
        InputManager inputManager = new InputManager(model);

        assertThrows(PrintWithoutMessageCreationException.class, () -> inputManager.initializationInput("usernames"));
        try{
            inputManager.initializationInput("usernames");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "USERNAMES");
        }

        assertThrows(IllegalArgumentException.class, () -> inputManager.initializationInput("invalid request"));
        try{
            inputManager.initializationInput("invalid request");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Syntax error: what you wrote was not correct, please, retry...");
        }


        assertInstanceOf(SelectResourceMessage.class, inputManager.initializationInput("SH1: COIN"));
        assertEquals(1, ((SelectResourceMessage) inputManager.initializationInput("SH1: COIN")).getInteger());
        assertEquals(Resource.COIN, ((SelectResourceMessage) inputManager.initializationInput("SH1: COIN")).getResource());
        assertInstanceOf(SelectResourceMessage.class, inputManager.initializationInput("SH2: STONE"));
        assertEquals(2, ((SelectResourceMessage) inputManager.initializationInput("SH2: STONE")).getInteger());
        assertEquals(Resource.STONE, ((SelectResourceMessage) inputManager.initializationInput("SH2: STONE")).getResource());
        assertInstanceOf(SelectResourceMessage.class, inputManager.initializationInput("SH2: SHIELD"));
        assertEquals(Resource.SHIELD, ((SelectResourceMessage) inputManager.initializationInput("SH2: SHIELD")).getResource());
        assertInstanceOf(SelectResourceMessage.class, inputManager.initializationInput("SH2: SERVANT"));
        assertEquals(Resource.SERVANT, ((SelectResourceMessage) inputManager.initializationInput("SH2: SERVANT")).getResource());

        model.addPlayer(0, new SimplifiedPlayer());
        model.setLocalPlayerId(0);
        model.getPlayerFromId(0).addLeaderCard(50);
        assertThrows(IllegalArgumentException.class, () -> inputManager.initializationInput("20"));
        try{
            inputManager.initializationInput("20");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }
        assertThrows(IllegalArgumentException.class, () -> inputManager.initializationInput("50"));
        try{
            inputManager.initializationInput("50");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You have already discarded enough leader cards!");
        }
        model.getPlayerFromId(0).addLeaderCard(51);
        model.getPlayerFromId(0).addLeaderCard(52);
        assertInstanceOf(LeaderDiscardMessage.class, inputManager.initializationInput("50"));
        model.getPlayerFromId(0).addLeaderCard(50);
        assertEquals(50, ((LeaderDiscardMessage) inputManager.initializationInput("50")).getInteger());
    }

    @Test
    @DisplayName("not in turn test")
    public void testNotInTurn(){
        Game model = new Game();
        InputManager inputManager = new InputManager(model);


        assertThrows(SuppressNotificationsException.class, () -> inputManager.NOTinTurnInput("NOTIFICATIONS"));
        assertThrows(SuppressNotificationsException.class, () -> inputManager.NOTinTurnInput("         NoTIFICatiONS          "));


        assertThrows(IllegalArgumentException.class, () -> inputManager.NOTinTurnInput("invalid request"));
        try{
            inputManager.initializationInput("invalid request");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Syntax error: what you wrote was not correct, please, retry...");
        }


        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: usernames"));
        try{
            inputManager.NOTinTurnInput("PRINT: usernames");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "USERNAMES");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: market"));
        try{
            inputManager.NOTinTurnInput("PRINT: market");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MARKET");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: market legend"));
        try{
            inputManager.NOTinTurnInput("PRINT: market legend");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MARKET LEGEND");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev cards on table"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev cards on table");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV CARDS ON TABLE");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: faith track"));
        try{
            inputManager.NOTinTurnInput("PRINT: faith track");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: faith track points"));
        try{
            inputManager.NOTinTurnInput("PRINT: faith track points");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK POINTS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: faith track vatican relations"));
        try{
            inputManager.NOTinTurnInput("PRINT: faith track vatican relations");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK VATICAN RELATIONS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: shelves"));
        try{
            inputManager.NOTinTurnInput("PRINT: shelves");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SHELVES");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: strongbox"));
        try{
            inputManager.NOTinTurnInput("PRINT: strongbox");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "STRONGBOX");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: support container"));
        try{
            inputManager.NOTinTurnInput("PRINT: support container");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SUPPORT CONTAINER");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slots top"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slots top");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS TOP");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slots content"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slots content");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS CONTENT");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slot 1 top"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slot 1 top");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 1 TOP");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slot 2 content"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slot 2 content");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 2 CONTENT");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: leader cards"));
        try{
            inputManager.NOTinTurnInput("PRINT: leader cards");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "LEADER CARDS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: shelves @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: shelves @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SHELVES @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: strongbox @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: strongbox @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "STRONGBOX @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: support container @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: support container @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SUPPORT CONTAINER @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slots top @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slots top @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS TOP @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slots content @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slots content @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS CONTENT @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slot 1 top @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slot 1 top @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 1 TOP @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: dev slot 2 content @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: dev slot 2 content @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 2 CONTENT @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.NOTinTurnInput("PRINT: leader cards @2"));
        try{
            inputManager.NOTinTurnInput("PRINT: leader cards @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "LEADER CARDS @2");
        }
    }

    @Test
    @DisplayName("in turn test")
    public void testInTurn(){
        Game model = new Game();
        InputManager inputManager = new InputManager(model);


        assertThrows(SuppressNotificationsException.class, () -> inputManager.inTurnInput("NOTIFICATIONS"));
        assertThrows(SuppressNotificationsException.class, () -> inputManager.inTurnInput("         NoTIFICatiONS          "));


        assertThrows(IllegalArgumentException.class, () -> inputManager.inTurnInput("invalid request"));
        try{
            inputManager.initializationInput("invalid request");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Syntax error: what you wrote was not correct, please, retry...");
        }


        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: usernames"));
        try{
            inputManager.inTurnInput("PRINT: usernames");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "USERNAMES");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: market"));
        try{
            inputManager.inTurnInput("PRINT: market");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MARKET");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: market legend"));
        try{
            inputManager.inTurnInput("PRINT: market legend");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "MARKET LEGEND");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev cards on table"));
        try{
            inputManager.inTurnInput("PRINT: dev cards on table");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV CARDS ON TABLE");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: faith track"));
        try{
            inputManager.inTurnInput("PRINT: faith track");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: faith track points"));
        try{
            inputManager.inTurnInput("PRINT: faith track points");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK POINTS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: faith track vatican relations"));
        try{
            inputManager.inTurnInput("PRINT: faith track vatican relations");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "FAITH TRACK VATICAN RELATIONS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: shelves"));
        try{
            inputManager.inTurnInput("PRINT: shelves");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SHELVES");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: strongbox"));
        try{
            inputManager.inTurnInput("PRINT: strongbox");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "STRONGBOX");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: support container"));
        try{
            inputManager.inTurnInput("PRINT: support container");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SUPPORT CONTAINER");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slots top"));
        try{
            inputManager.inTurnInput("PRINT: dev slots top");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS TOP");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slots content"));
        try{
            inputManager.inTurnInput("PRINT: dev slots content");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS CONTENT");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slot 1 top"));
        try{
            inputManager.inTurnInput("PRINT: dev slot 1 top");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 1 TOP");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slot 2 content"));
        try{
            inputManager.inTurnInput("PRINT: dev slot 2 content");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 2 CONTENT");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: leader cards"));
        try{
            inputManager.inTurnInput("PRINT: leader cards");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "LEADER CARDS");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: shelves @2"));
        try{
            inputManager.inTurnInput("PRINT: shelves @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SHELVES @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: strongbox @2"));
        try{
            inputManager.inTurnInput("PRINT: strongbox @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "STRONGBOX @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: support container @2"));
        try{
            inputManager.inTurnInput("PRINT: support container @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "SUPPORT CONTAINER @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slots top @2"));
        try{
            inputManager.inTurnInput("PRINT: dev slots top @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS TOP @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slots content @2"));
        try{
            inputManager.inTurnInput("PRINT: dev slots content @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOTS CONTENT @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slot 1 top @2"));
        try{
            inputManager.inTurnInput("PRINT: dev slot 1 top @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 1 TOP @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: dev slot 2 content @2"));
        try{
            inputManager.inTurnInput("PRINT: dev slot 2 content @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "DEV SLOT 2 CONTENT @2");
        }
        assertThrows(PrintWithoutMessageCreationException.class,() -> inputManager.inTurnInput("PRINT: leader cards @2"));
        try{
            inputManager.inTurnInput("PRINT: leader cards @2");
        } catch (PrintWithoutMessageCreationException e) {
            assertEquals(e.getMessage(), "LEADER CARDS @2");
        }


        assertInstanceOf(EndTurnMessage.class, inputManager.inTurnInput("end turn"));
        assertInstanceOf(BackFromAnySelectionMessage.class, inputManager.inTurnInput(" CLear any seleCtion "));
        assertInstanceOf(BuyDevCardMessage.class, inputManager.inTurnInput("Buy CaRD"));
        assertInstanceOf(ProductionActivationMessage.class, inputManager.inTurnInput("ActiVate ProDucTION power"));
        assertInstanceOf(TakeFromMarketMessage.class, inputManager.inTurnInput("TakE FRom MaRKet"));
        assertInstanceOf(ExchangeMessage.class, inputManager.inTurnInput("ExCHanGe"));
        assertInstanceOf(QuitFromMarketMessage.class, inputManager.inTurnInput("QuiT"));
        assertInstanceOf(PaySelectedMessage.class, inputManager.inTurnInput("CHeCKouT"));


        //DISCOUNT:(LC\d\d)
        model.addPlayer(0, new SimplifiedPlayer());
        model.getPlayerFromId(0).addLeaderCard(56);
        assertInstanceOf(DiscountAbilityMessage.class, inputManager.inTurnInput("DiscouNT: Lc56"));
        assertEquals(56, ((DiscountAbilityMessage) inputManager.inTurnInput("DiscouNT: Lc56")).getInteger());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("DiscouNT: Lc45"));
        try{
            inputManager.inTurnInput("DiscouNT: Lc45");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own that leader card! Please, retry...");
        }


        //(PLAY|DISCARD):LC\d\d
        assertInstanceOf(LeaderCardActionMessage.class, inputManager.inTurnInput("PLAY: LC56"));
        assertEquals(56, ((LeaderCardActionMessage) inputManager.inTurnInput("PLAY: LC56")).getInteger());
        assertFalse(((LeaderCardActionMessage) inputManager.inTurnInput("PLAY: LC56")).isaBoolean());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("Play: Lc45"));
        try{
            inputManager.inTurnInput("Play: Lc45");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }
        assertInstanceOf(LeaderCardActionMessage.class, inputManager.inTurnInput("DiscARd: LC56"));
        assertEquals(56, ((LeaderCardActionMessage) inputManager.inTurnInput("DiscARd: LC56")).getInteger());
        assertTrue(((LeaderCardActionMessage) inputManager.inTurnInput("DiscARd: LC56")).isaBoolean());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("DIScarD: Lc45"));
        try{
            inputManager.inTurnInput("DIScarD: Lc45");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }


        //SELECT FROM MARKET:(ROW,[0-2]|COLUMN,[0-3])
        assertInstanceOf(MarketSelectionMessage.class, inputManager.inTurnInput("SelECt FrOM MarKet: Row,2"));
        assertEquals(2, ((MarketSelectionMessage) inputManager.inTurnInput("SelECt FrOM MarKet: Row,2")).getInteger());
        assertTrue(((MarketSelectionMessage) inputManager.inTurnInput("SelECt FrOM MarKet: Row,2")).isaBoolean());
        assertInstanceOf(MarketSelectionMessage.class, inputManager.inTurnInput("SelECt FrOM MarKet: CoLumN,1"));
        assertEquals(1, ((MarketSelectionMessage) inputManager.inTurnInput("SelECt FrOM MarKet: CoLumN,1")).getInteger());
        assertFalse(((MarketSelectionMessage) inputManager.inTurnInput("SelECt FrOM MarKet: CoLumN,1")).isaBoolean());


        //SELECT:(((LC\d\d|SB|SC),(COIN|STONE|SERVANT|SHIELD),(\d)+)|((SH[1-3]),(COIN|STONE|SERVANT|SHIELD)))
        assertInstanceOf(ShelfSelectionMessage.class, inputManager.inTurnInput("SELECT: SH1,STONE"));
        assertEquals(1, ((ShelfSelectionMessage) inputManager.inTurnInput("SELECT: SH1,STONE")).getInteger());
        assertEquals(Resource.STONE, ((ShelfSelectionMessage) inputManager.inTurnInput("SELECT: SH1,STONE")).getResource());

        assertInstanceOf(LeaderStorageSelectionMessage.class, inputManager.inTurnInput("SELECT: LC56,STONE,6"));
        assertEquals(56, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("SELECT: LC56,STONE,6")).getId());
        assertEquals(Resource.STONE, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("SELECT: LC56,STONE,6")).getResource());
        assertEquals(6, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("SELECT: LC56,STONE,6")).getResPosition());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("SELECT: LC40,STONE,6"));
        try{
            inputManager.inTurnInput("SELECT: LC40,STONE,6");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }

        assertInstanceOf(SupportContainerSelectionMessage.class, inputManager.inTurnInput("SELECT: SC,STONE,6"));
        assertEquals(6, ((SupportContainerSelectionMessage) inputManager.inTurnInput("SELECT: SC,STONE,6")).getInteger());
        assertEquals(Resource.STONE, ((SupportContainerSelectionMessage) inputManager.inTurnInput("SELECT: SC,STONE,6")).getResource());

        assertInstanceOf(StrongBoxSelectionMessage.class, inputManager.inTurnInput("SELECT: SB,STONE,6"));
        assertEquals(6, ((StrongBoxSelectionMessage) inputManager.inTurnInput("SELECT: SB,STONE,6")).getInteger());
        assertEquals(Resource.STONE, ((StrongBoxSelectionMessage) inputManager.inTurnInput("SELECT: SB,STONE,6")).getResource());


        //DESELECT:(((LC\d\d|SB|SC),(COIN|STONE|SERVANT|SHIELD),(\d)+)|((SH[1-3]),(COIN|STONE|SERVANT|SHIELD)))
        assertInstanceOf(ShelfDeselectionMessage.class, inputManager.inTurnInput("DESELECT: SH1,STONE"));
        assertEquals(1, ((ShelfDeselectionMessage) inputManager.inTurnInput("DESELECT: SH1,STONE")).getInteger());
        assertEquals(Resource.STONE, ((ShelfDeselectionMessage) inputManager.inTurnInput("DESELECT: SH1,STONE")).getResource());

/*        assertInstanceOf(LeaderStorageSelectionMessage.class, inputManager.inTurnInput("DESELECT: LC56,STONE,6"));
        assertEquals(56, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("DESELECT: LC56,STONE,6")).getId());
        assertEquals(Resource.STONE, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("DESELECT: LC56,STONE,6")).getResource());
        assertEquals(6, ((LeaderStorageSelectionMessage) inputManager.inTurnInput("DESELECT: LC56,STONE,6")).getResPosition());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("DESELECT: LC40,STONE,6"));
        try{
            inputManager.inTurnInput("DESELECT: LC40,STONE,6");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }*/

        assertInstanceOf(SupportContainerDeselectionMessage.class, inputManager.inTurnInput("DESELECT: SC,STONE,6"));
        assertEquals(6, ((SupportContainerDeselectionMessage) inputManager.inTurnInput("DESELECT: SC,STONE,6")).getInteger());
        assertEquals(Resource.STONE, ((SupportContainerDeselectionMessage) inputManager.inTurnInput("DESELECT: SC,STONE,6")).getResource());

        assertInstanceOf(StrongBoxDeselectionMessage.class, inputManager.inTurnInput("deSELECT: SB,STONE,6"));
        assertEquals(6, ((StrongBoxDeselectionMessage) inputManager.inTurnInput("deSELECT: SB,STONE,6")).getInteger());
        assertEquals(Resource.STONE, ((StrongBoxDeselectionMessage) inputManager.inTurnInput("deSELECT: SB,STONE,6")).getResource());


        //MOVE TO:(SC|(LC\d\d)|(SH[1-3]))
        assertInstanceOf(MoveToSupportContainerMessage.class, inputManager.inTurnInput("MOVE TO: SC"));
        assertInstanceOf(MoveToLeaderStorageMessage.class, inputManager.inTurnInput("MOVE TO: LC56"));
        assertEquals(56, ((MoveToLeaderStorageMessage) inputManager.inTurnInput("MOVE TO: LC56")).getInteger());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("MOVE TO: LC40"));
        try{
            inputManager.inTurnInput("MOVE TO: LC40");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }
        assertInstanceOf(MoveToShelfMessage.class, inputManager.inTurnInput("MOVE TO: SH2"));
        assertEquals(2, ((MoveToShelfMessage) inputManager.inTurnInput("MOVE TO: SH2")).getInteger());


        //ANY SELECTION:(COIN|STONE|SERVANT|SHIELD)
        assertInstanceOf(AnySelectionMessage.class, inputManager.inTurnInput("ANY SELECTION: COIN"));
        assertEquals(Resource.COIN, ((AnySelectionMessage) inputManager.inTurnInput("ANY SELECTION: COIN")).getResource());
        assertInstanceOf(AnySelectionMessage.class, inputManager.inTurnInput("ANY SELECTION: SERVANT"));
        assertEquals(Resource.SERVANT, ((AnySelectionMessage) inputManager.inTurnInput("ANY SELECTION: SERVANT")).getResource());
        assertInstanceOf(AnySelectionMessage.class, inputManager.inTurnInput("ANY SELECTION: SHIELD"));
        assertEquals(Resource.SHIELD, ((AnySelectionMessage) inputManager.inTurnInput("ANY SELECTION: SHIELD")).getResource());
        assertInstanceOf(AnySelectionMessage.class, inputManager.inTurnInput("ANY SELECTION: STONE"));
        assertEquals(Resource.STONE, ((AnySelectionMessage) inputManager.inTurnInput("ANY SELECTION: STONE")).getResource());


        //TRANSMUTE:(LC\d\d)X((\d)+),(LC\d\d)X((\d)+)
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("TRANSMUTE: LC56x3,LC40x2"));
        try{
            inputManager.inTurnInput("TRANSMUTE: LC56x3,LC40x2");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with the second id you wrote! Please, retry...");
        }
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("TRANSMUTE: LC40x2,LC56x3"));
        try{
            inputManager.inTurnInput("TRANSMUTE: LC40x2,LC56x3");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with the first id you wrote! Please, retry...");
        }
        model.getPlayerFromId(0).addLeaderCard(51);
        assertInstanceOf(TransmutationMessage.class, inputManager.inTurnInput("TRANSMUTE: LC56x3,LC51x2"));
        assertEquals(56, ((TransmutationMessage) inputManager.inTurnInput("TRANSMUTE: LC56x3,LC51x2")).getSerial1());
        assertEquals(51, ((TransmutationMessage) inputManager.inTurnInput("TRANSMUTE: LC56x3,LC51x2")).getSerial2());
        assertEquals(3, ((TransmutationMessage) inputManager.inTurnInput("TRANSMUTE: LC56x3,LC51x2")).getQuantity1());
        assertEquals(2, ((TransmutationMessage) inputManager.inTurnInput("TRANSMUTE: LC56x3,LC51x2")).getQuantity2());


        //SELECT: (LC\d\d|DC\d\d|BASIC PRODUCTION POWER|ALL PRODUCTION POWERS|DS[0-2]|DD,LEVEL[1-3],COLOR:(GREEN|YELLOW|BLUE|PURPLE))
        assertInstanceOf(AllProductionPowerSelectionMessage.class, inputManager.inTurnInput("SELECT: ALL PRODUCTION POWERS"));

        assertInstanceOf(CardProductionSelectionMessage.class, inputManager.inTurnInput("SELECT: BASIC PRODUCTION POWER"));
        assertEquals(0, ((CardProductionSelectionMessage) inputManager.inTurnInput("SELECT: BASIC PRODUCTION POWER")).getInteger());

        assertInstanceOf(CardProductionSelectionMessage.class, inputManager.inTurnInput("SELECT: LC56"));
        assertEquals(56, ((CardProductionSelectionMessage) inputManager.inTurnInput("SELECT: LC56")).getInteger());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("SELECT: LC40"));
        try{
            inputManager.inTurnInput("SELECT: LC40");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You do not own a leader card with that id! Please, retry...");
        }

        model.getPlayerFromId(0).addDevCardInSlot(8,2);
        assertInstanceOf(CardProductionSelectionMessage.class, inputManager.inTurnInput("SELECT: DC08"));
        assertEquals(8, ((CardProductionSelectionMessage) inputManager.inTurnInput("SELECT: DC08")).getInteger());
        assertThrows(IllegalArgumentException.class,() -> inputManager.inTurnInput("SELECT: DC40"));
        try{
            inputManager.inTurnInput("SELECT: DC40");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "You cannot select the development card with that id! Please, retry...");
        }

        assertInstanceOf(ChooseDevSlotMessage.class, inputManager.inTurnInput("SELECT: DS1"));
        assertEquals(0, ((ChooseDevSlotMessage) inputManager.inTurnInput("SELECT: DS1")).getInteger());

        assertInstanceOf(ChooseDevCardMessage.class, inputManager.inTurnInput("SELECT: DD,LEVEL1,COLOR:GREEN"));
        assertEquals(1, ((ChooseDevCardMessage) inputManager.inTurnInput("SELECT: DD,LEVEL1,COLOR:GREEN")).getInteger());
    }
}