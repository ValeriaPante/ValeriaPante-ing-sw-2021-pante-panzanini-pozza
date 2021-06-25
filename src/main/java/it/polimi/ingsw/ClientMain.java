
package it.polimi.ingsw;

import it.polimi.ingsw.View.CLI.CLI;
import javafx.application.Application;
import it.polimi.ingsw.View.GUI.GUI;

public class ClientMain
{
    /**
    * @param args by writing "-cli" the CLI application will start, otherwise the GUI application will start
    * */
    public static void main( String[] args )
    {
        if(args.length == 0) Application.launch(GUI.class);
        else if (args[0].equals("-cli")){
            CLI cli = new CLI();
            new Thread(cli).start();
        }
    }
}
