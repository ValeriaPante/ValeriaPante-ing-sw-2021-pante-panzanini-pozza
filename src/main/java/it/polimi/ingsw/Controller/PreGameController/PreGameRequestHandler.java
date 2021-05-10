package it.polimi.ingsw.Controller.PreGameController;

import it.polimi.ingsw.PreGameModel.User;
import it.polimi.ingsw.Network.Messages.CreationLobbyMessage;
import it.polimi.ingsw.Network.Messages.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.MoveToLobbyMessage;
import it.polimi.ingsw.Network.Server.ConnectionHandler;

import java.util.concurrent.ExecutorService;

public class PreGameRequestHandler implements RequestHandler{

    private PreGameController preGameController;

    public PreGameRequestHandler(ExecutorService executorService){
        this.preGameController = new PreGameController(executorService);
    }

    @Override
    public void requestEvaluator(int id, String request) {


        //crea il pacchetto giusto e lo passa
        //a PreGameController

        if (request.equals("yo")) {
            CreationLobbyMessage message = new CreationLobbyMessage(id);
        }
        else{
            MoveToLobbyMessage message = new MoveToLobbyMessage(id, 32);
            this.preGameController.request(message);
        }
    }

    @Override
    public void connectionClosed(int id) {
        this.preGameController.request(new DisconnectMessage(id));
    }

    //questo metodo lo chiamo sempre e solo tramite un thread
    public void addNewSocket(String username, ConnectionHandler connectionHandler){
        this.preGameController.addNewUser(new User(username, connectionHandler));
    }

}
