package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;

public class LocalMessageSender implements MessageSenderInterface{
    @Override
    public void send(FromServerMessage message) {

    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {

    }
}
