
package it.polimi.ingsw;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.View.GUI.GUI;

public class ClientMain
{
    public static void main( String[] args )
    {
        GUI view = new GUI();
        Client client = new Client(view);
        view.addObserver(client);
    }
}
