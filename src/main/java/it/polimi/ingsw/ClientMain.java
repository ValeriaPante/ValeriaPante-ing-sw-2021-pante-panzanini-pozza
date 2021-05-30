
package it.polimi.ingsw;

import javafx.application.Application;
import it.polimi.ingsw.View.GUI.GUI;

public class ClientMain
{
    public static void main( String[] args )
    {
        Application.launch(GUI.class);
        //gui.addObserver(client);

        /*for(int i = 0; i < 100; i++){
            gui.updateLobbyState(i, new String[]{"valeria"+i, "alberto"+i, "daniel"+i});
        }
        int[][] devDecks = {{1,4,2,3},{17,20,18,19},{33,36,34,35}};
        gui.getModel().initialiseDevDecks(devDecks);

        new Thread(() -> {
            try{
                *//*Thread.sleep(5000);
                gui.updateLobbyState(1000, new String[]{"e", "f"});
                gui.updateLobbyState(1, new String[]{"e", "f"});
                gui.updateLobbyState(1001, new String[]{"e", "f"});
                Thread.sleep(5000);
                gui.removeLobby(1001);*//*
                Thread.sleep(5000);
                gui.chooseLeaderCards();
                Thread.sleep(5000);
                gui.startGame();
            } catch( Exception e ){

            }
        }).start();*/
    }
}
