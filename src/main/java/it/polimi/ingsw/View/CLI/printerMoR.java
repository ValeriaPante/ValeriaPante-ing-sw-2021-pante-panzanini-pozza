package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class printerMoR {
    public static void main(String[] args) {
//        SomeWritings sw= new SomeWritings();
//        sw.printTitle2();
//        sw.printMarket();
//        Resource[] content = {Resource.COIN, Resource.SERVANT, Resource.STONE, Resource.SERVANT, Resource.COIN};
//        LeaderCardPrinter lcP = new LeaderCardPrinter();
//        for (int i= 49; i <= 64; i++)
//            lcP.printFromID(i,content );
//
//        DevCardPrinter dcP = new DevCardPrinter();
//        for (int i= 1; i <= 48; i++)
//            dcP.printFromID(i);
//
//        FaithTrackPrinter ftP = new FaithTrackPrinter();
//        ftP.printTrack();
//        ftP.printPointsForPosition();
//        ftP.printVaticanRelations();

//        LobbyPrinter lP = new LobbyPrinter();
//        String[] players = {"Alberto", "Matteo", "Filippo"};
//        lP.printLobby(10, players);


//        int i,j,k;
//        i = 14;
//        j = 16;
//        k = 4;
//        String s = "aaaa";
//        s += i;
//        System.out.println((i-1)/16);
//        System.out.println(i % 4);
//        System.out.println(i);
//        System.out.println(s);
//        Game model = new Game();
//        System.out.print(model.getLocalPlayerLobbyId());
//        InputManager inputManager= new InputManager(model);
//        String toBeChecked = "3";
//        int capacity = Integer.parseInt(""+toBeChecked.charAt(0));
//        System.out.println(capacity);
//        MarketPrinter marketPrinter = new MarketPrinter();
//        marketPrinter.printLegend();
//        Market market = new Market();
//        marketPrinter.printMarket(market.getState(), market.getSlide());
//        DevCardPrinter devCardPrinter = new DevCardPrinter();
//        int[][] intMatrix = new int[3][4];
//        intMatrix[0][0] = 21;
//        intMatrix[0][1] = 22;
//        intMatrix[0][2] = 23;
//        intMatrix[0][3] = 0;
//        intMatrix[1][0] = 25;
//        intMatrix[1][1] = 26;
//        intMatrix[1][2] = 27;
//        intMatrix[1][3] = 28;
//        intMatrix[2][0] = 29;
//        intMatrix[2][1] = 0;
//        intMatrix[2][2] = 31;
//        intMatrix[2][3] = 32;
//        devCardPrinter.printDevSlots(intMatrix, true);
//        devCardPrinter.printDevSlots(intMatrix, false);
//        devCardPrinter.printSingleDevSlot(intMatrix[1], true);
//        System.out.println("\n\n\n\n\n\n\n");
//        devCardPrinter.printSingleDevSlot(intMatrix[1], false);
//        devCardPrinter.printDevDecks(intMatrix);
//        StrongBoxPrinter strongBoxPrinter = new StrongBoxPrinter();
//        EnumMap<Resource,Integer> map = new EnumMap<>(Resource.class){{
//            put(Resource.COIN, 100);
//            put(Resource.STONE, 100);
//            put(Resource.SERVANT, 100);
//            put(Resource.SHIELD, 100);
//        }};
//        strongBoxPrinter.printStrongBox(map);
//        strongBoxPrinter.printSupportContainer(map);


        ShelvesPrinter shelvesPrinter = new ShelvesPrinter();
        SimplifiedPlayer player = new SimplifiedPlayer();
        player.setShelf(Resource.COIN, 1, 0);
        shelvesPrinter.printShelves(player.getShelf(0), player.getShelf(1), player.getShelf(2));
        System.out.println("\n\n\n\n_____________________________________________________________________\n\n\n");
        player.setShelf(Resource.COIN, 1, 0);
        player.setShelf(Resource.STONE, 2, 1);
        player.setShelf(Resource.SERVANT, 3, 2);
        shelvesPrinter.printShelves(player.getShelf(0), player.getShelf(1), player.getShelf(2));


    }


}
