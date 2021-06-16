package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.ClientModel.Game;

import java.util.EnumMap;

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
        Game model = new Game();
//        System.out.print(model.getLocalPlayerLobbyId());
        InputManager inputManager= new InputManager(model);
        String toBeChecked = "3";
        int capacity = Integer.parseInt(""+toBeChecked.charAt(0));
        System.out.println(capacity);
    }


}
