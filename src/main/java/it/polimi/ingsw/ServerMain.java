package it.polimi.ingsw;

import it.polimi.ingsw.View.ServerGUI.ServerGUI;
import javafx.application.Application;

public class ServerMain {

    public static void main(String[] args){
        if (args.length>0 && args[0].equals("--custom")){
            Application.launch(ServerGUI.class);
        }
        else{
            ServerGUI.startServer();
        }
    }

}
