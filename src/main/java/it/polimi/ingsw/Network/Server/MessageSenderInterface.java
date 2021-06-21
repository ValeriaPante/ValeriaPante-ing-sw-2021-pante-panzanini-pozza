package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;

public interface MessageSenderInterface {
    void send(FromServerMessage message);
    int getId();
    void setId(int id);
    void setRequestHandler(RequestHandler requestHandler);
}
