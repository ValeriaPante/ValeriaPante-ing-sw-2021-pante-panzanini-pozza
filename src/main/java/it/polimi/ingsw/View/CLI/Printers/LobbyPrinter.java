package it.polimi.ingsw.View.CLI.Printers;

import java.util.HashMap;
import java.util.Map;

public class LobbyPrinter {
    public void printLobby(int id, String[] players){
        System.out.print("\n" +
                "__________________\n" +
                "|                  |\n" +
                "|          LOBBY ID: " + id + "\n" +
                "|    PLAYERS INSIDE: ");

        for(int i=0; i<players.length; i++){
            if (i > 0)
                System.out.print(", ");
            System.out.print(players[i]);
        }

        System.out.println("\n" +
                "|                  |\n" +
                " ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ \n");
    }

    public void printAllLobbies(HashMap<Integer, String[]> lobbies){
        for (Map.Entry<Integer, String[]> entry : lobbies.entrySet())
            printLobby(entry.getKey(), entry.getValue());
    }
}
