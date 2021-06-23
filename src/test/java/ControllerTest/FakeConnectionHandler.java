package ControllerTest;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;

public class FakeConnectionHandler implements MessageSenderInterface {

    @Override
    public void send(FromServerMessage message) {
        System.out.println("Sending message...");
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
