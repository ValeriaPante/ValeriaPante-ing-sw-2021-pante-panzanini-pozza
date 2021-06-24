package SupportCLITest;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.View.CLI.Printers.Printer;
import it.polimi.ingsw.View.CLI.SimplifiedFaithTrack;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterTest {
    @Test
    @DisplayName("content() test")
    public void testNotify() {
        Game model = new Game();
        model.addPlayer(0, new SimplifiedPlayer(){{
            setUsername("UserName1");
        }});
        model.setLocalPlayerId(0);
        SimplifiedFaithTrack simplifiedFaithTrack = new SimplifiedFaithTrack(model.getPLayersID());
        Printer printer = new Printer(model,simplifiedFaithTrack);

        //single player
        printer.gameStarted();
        System.out.println("\n");

        printer.printWinner(0);
        printer.printWinner(-1);
        System.out.println("\n");

        printer.printSelectionError("*error text*", 34);
        printer.printError("*error text*");
        System.out.println("\n");

        printer.notifyInitializationStarted();
        System.out.println("\n");

        printer.notifyDevCardPurchase(0,23, 2);
        printer.notifyLeaderCardDiscard(0,57);
        printer.notifyLeaderCardActivation(0,56);
        printer.notifyChangeInLCStorage(0, 56);
        System.out.println("\n");

        printer.notifySupportContainerChange(0);
        printer.notifyStrongBoxChange(0);
        printer.notifyShelvesChange(0, 1);
        System.out.println("\n");

        printer.notifyPopeFavorCardChange(0);
        printer.notifyFaithTrackPositionChange(0);
        System.out.println("\n");

        printer.notifyTurnChanged(0);
        System.out.println("\n");

        model.getPlayerFromId(0).addLeaderCard(61);
        model.getPlayerFromId(0).addLeaderCard(49);
        model.getPlayerFromId(0).addLeaderCard(51);
        model.getPlayerFromId(0).addLeaderCard(53);
        printer.notifyChooseLeaderCards();
        System.out.println("\n");

        printer.notifyLobbyRemoval(23);
        printer.notifyLobbyChange(23);
        model.setLocalPlayerLobbyId(23);
        printer.notifyLobbyChange(23);
        System.out.println("\n");

        printer.printNotificationState(true);
        printer.printNotificationState(false);
        System.out.println("\n");

        model.setLocalPlayerLobbyId(0);
        String[] onePLayer = new String[1];
        onePLayer[0] = "PLAYER1";
        String[] twoPLayers = new String[2];
        twoPLayers[0] = "PLAYER1";
        twoPLayers[1] = "PLAYER2";
        String[] threePLayers = new String[3];
        threePLayers[0] = "PLAYER1";
        threePLayers[1] = "PLAYER2";
        threePLayers[2] = "PLAYER3";
        printer.preGamePrintRequest("MY LOBBY");
        printer.preGamePrintRequest("ALL LOBBIES");
        printer.preGamePrintRequest("invalid request");
        model.addLobby(23,onePLayer);
        model.addLobby(14, twoPLayers);
        model.addLobby(3, threePLayers);
        printer.preGamePrintRequest("MY LOBBY");
        printer.preGamePrintRequest("ALL LOBBIES");
        printer.preGamePrintRequest("invalid request");
        model.setLocalPlayerLobbyId(3);
        printer.preGamePrintRequest("MY LOBBY");
        printer.preGamePrintRequest("ALL LOBBIES");
        printer.preGamePrintRequest("invalid request");
        System.out.println("\n");


        //in game that are always the same
        printer.inGamePrintRequest("MARKET LEGEND");
        System.out.println("\n-----------------------------------------------------");

        Market market = new Market();
        model.updateMarketState(market.getState(), market.getSlide());
        printer.inGamePrintRequest("MARKET");
        System.out.println("\n-----------------------------------------------------");

        int[][] randomDevDecks = new int[3][4];
        for(int i=0; i<3; i++)
            for (int j=0; j<4; j++)
                randomDevDecks[i][j] = (i*3) + j + 1;
        model.initialiseDevDecks(randomDevDecks);
        printer.inGamePrintRequest("DEV CARDS ON TABLE");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("FAITH TRACK POINTS");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("@");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("USERNAMES");
        System.out.println("\n-----------------------------------------------------");


        //things owned by local player
        printer.inGamePrintRequest("SHELVES");
        model.getPlayerFromId(0).setShelf(Resource.SERVANT, 1, 0);
        model.getPlayerFromId(0).setShelf(Resource.STONE, 2, 1);
        model.getPlayerFromId(0).setShelf(Resource.COIN, 3, 2);
        printer.inGamePrintRequest("SHELVES");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("STRONGBOX");
        model.getPlayerFromId(0).setStrongbox(new HashMap<>(){{
            put(Resource.COIN, 4);
            put(Resource.SERVANT, 4);
        }});
        printer.inGamePrintRequest("STRONGBOX");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("SUPPORT CONTAINER");
        model.getPlayerFromId(0).setSupportContainer(new HashMap<>(){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 4);
        }});
        printer.inGamePrintRequest("SUPPORT CONTAINER");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("DEV SLOTS TOP");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOTS CONTENT");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 2 CONTENT");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 3 TOP");
        System.out.println("\n-----------------------------------------------------");
        model.getPlayerFromId(0).addDevCardInSlot(2, 2);
        model.getPlayerFromId(0).addDevCardInSlot(4, 2);
        model.getPlayerFromId(0).addDevCardInSlot(5, 2);
        model.getPlayerFromId(0).addDevCardInSlot(3, 3);
        model.getPlayerFromId(0).addDevCardInSlot(6, 1);
        printer.inGamePrintRequest("DEV SLOTS TOP");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOTS CONTENT");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 2 CONTENT");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 3 TOP");
        System.out.println("\n-----------------------------------------------------");

        model.getPlayerFromId(0).removeLeaderCard(61);
        model.getPlayerFromId(0).removeLeaderCard(49);
        printer.inGamePrintRequest("LEADER CARDS");
        model.getPlayerFromId(0).removeLeaderCard(51);
        printer.inGamePrintRequest("LEADER CARDS");
        Resource[] leaderCardContent = new Resource[2];
        leaderCardContent[0] = Resource.STONE;
        leaderCardContent[1] = Resource.STONE;
        model.getPlayerFromId(0).setLeaderStorage(53,leaderCardContent);
        printer.inGamePrintRequest("LEADER CARDS");
        model.getPlayerFromId(0).removeLeaderCard(53);
        printer.inGamePrintRequest("LEADER CARDS");
        System.out.println("\n-----------------------------------------------------");
    }

    @Test
    @DisplayName("content() test")
    public void testMultiPlayer() {
        Game model = new Game();
        model.addPlayer(0, new SimplifiedPlayer(){{
            setUsername("UserName1");
        }});
        model.addPlayer(1, new SimplifiedPlayer(){{
            setUsername("UserName2");
        }});
        model.addPlayer(2, new SimplifiedPlayer(){{
            setUsername("UserName3");
        }});
        model.addPlayer(3, new SimplifiedPlayer(){{
            setUsername("UserName4");
        }});
        model.setLocalPlayerId(0);
        SimplifiedFaithTrack simplifiedFaithTrack = new SimplifiedFaithTrack(model.getPLayersID());
        Printer printer = new Printer(model,simplifiedFaithTrack);

        printer.waitingInitialisation(2);
        System.out.println("\n");

        model.setLocalPlayerId(3);
        printer.notifyChooseInitialRes();
        model.setLocalPlayerId(2);
        printer.notifyChooseInitialRes();
        System.out.println("\n");


        printer.notifyTurnChanged(1);
        System.out.println("\n");


        printer.inGamePrintRequest("FAITH TRACK");
        simplifiedFaithTrack.changePosition(1, 10);
        simplifiedFaithTrack.changePosition(0, 5);
        simplifiedFaithTrack.changePosition(3, 20);
        printer.inGamePrintRequest("FAITH TRACK");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("FAITH TRACK VATICAN RELATIONS");
        PopeFavorCardState[] newStates = new PopeFavorCardState[3];
        newStates[0] = PopeFavorCardState.FACEUP;
        newStates[1] = PopeFavorCardState.FACEUP;
        newStates[2] = PopeFavorCardState.FACEUP;
        simplifiedFaithTrack.changeState(3, newStates);
        newStates[0] = PopeFavorCardState.FACEUP;
        newStates[1] = PopeFavorCardState.FACEDOWN;
        newStates[2] = PopeFavorCardState.DISABLED;
        simplifiedFaithTrack.changeState(0, newStates);
        printer.inGamePrintRequest("FAITH TRACK VATICAN RELATIONS");
        System.out.println("\n-----------------------------------------------------");


        //things owned by a player different from the local
        printer.inGamePrintRequest("SHELVES @2");
        printer.inGamePrintRequest("SHELVES @8");
        printer.inGamePrintRequest("SHELVES @0");
        model.getPlayerFromId(0).setShelf(Resource.SERVANT, 1, 0);
        model.getPlayerFromId(0).setShelf(Resource.STONE, 2, 1);
        model.getPlayerFromId(0).setShelf(Resource.COIN, 3, 2);
        printer.inGamePrintRequest("SHELVES@0");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("STRONGBOX @0");
        model.getPlayerFromId(0).setStrongbox(new HashMap<>(){{
            put(Resource.COIN, 4);
            put(Resource.SERVANT, 4);
        }});
        printer.inGamePrintRequest("STRONGBOX @0");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("SUPPORT CONTAINER @0");
        model.getPlayerFromId(0).setSupportContainer(new HashMap<>(){{
            put(Resource.STONE, 4);
            put(Resource.SHIELD, 4);
        }});
        printer.inGamePrintRequest("SUPPORT CONTAINER @0");
        System.out.println("\n-----------------------------------------------------");

        printer.inGamePrintRequest("DEV SLOTS TOP @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOTS CONTENT @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 2 CONTENT @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 3 TOP @0");
        System.out.println("\n-----------------------------------------------------");
        model.getPlayerFromId(0).addDevCardInSlot(2, 2);
        model.getPlayerFromId(0).addDevCardInSlot(4, 2);
        model.getPlayerFromId(0).addDevCardInSlot(5, 2);
        model.getPlayerFromId(0).addDevCardInSlot(3, 3);
        model.getPlayerFromId(0).addDevCardInSlot(6, 1);
        printer.inGamePrintRequest("DEV SLOTS TOP @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOTS CONTENT @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 2 CONTENT @0");
        System.out.println("\n-----------------------------------------------------");
        printer.inGamePrintRequest("DEV SLOT 3 TOP @0");
        System.out.println("\n-----------------------------------------------------");


        model.getPlayerFromId(0).addLeaderCard(51);
        model.getPlayerFromId(0).addLeaderCard(53);
        printer.inGamePrintRequest("LEADER CARDS @0");
        model.getPlayerFromId(0).removeLeaderCard(51);
        printer.inGamePrintRequest("LEADER CARDS @0");
        Resource[] leaderCardContent = new Resource[2];
        leaderCardContent[0] = Resource.STONE;
        leaderCardContent[1] = Resource.STONE;
        model.getPlayerFromId(0).setLeaderStorage(53,leaderCardContent);
        printer.inGamePrintRequest("LEADER CARDS @0");
        model.getPlayerFromId(0).removeLeaderCard(53);
        printer.inGamePrintRequest("LEADER CARDS @0");
        System.out.println("\n-----------------------------------------------------");
    }
}
