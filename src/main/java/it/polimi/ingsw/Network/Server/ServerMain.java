package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.RequestHandlers.PreGameRequestHandler;

public class ServerMain {

    public static void main(String[] args){
        PreGameRequestHandler controller = new PreGameRequestHandler();

        Server server = new Server(42000, controller);
        server.start();

    }

}
