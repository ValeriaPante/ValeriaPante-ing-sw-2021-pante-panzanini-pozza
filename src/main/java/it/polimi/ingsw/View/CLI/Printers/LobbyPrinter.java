package it.polimi.ingsw.View.CLI.Printers;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to print the lobby in the server
 */
public class LobbyPrinter {
    /**
     * Prints one single lobby, the one with the parameters passed
     * @param id id of the lobby
     * @param players array of usernames in the lobby with the "id" specified
     *                with the parameter "id"
     */
    public void printLobby(int id, String[] players){
        System.out.print(
                " __________________\n" +
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

    /**
     * Prints a number of lobby equals to the entry in the map passed as a parameter (eventually 0 if the map is empty)
     * @param lobbies map mapping the id if the lobby to the array of usernames in th lobby with that id
     */
    public void printAllLobbies(Map<Integer, String[]> lobbies){
        for (Map.Entry<Integer, String[]> entry : lobbies.entrySet())
            printLobby(entry.getKey(), entry.getValue());
    }
}
