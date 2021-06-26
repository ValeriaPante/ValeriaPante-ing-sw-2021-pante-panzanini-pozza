package ControllerTest;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.Client.Messages.InitMessage;
import it.polimi.ingsw.Network.Client.Messages.StartMessage;
import it.polimi.ingsw.Network.Client.Messages.TurnOfMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;

public class FakeConnectionHandler implements MessageSenderInterface {

    @Override
    public void send(FromServerMessage message) {
        if (message instanceof InitMessage)
            System.out.println("init message sent");
        else if (message instanceof StartMessage)
            System.out.println("start message sent");
        else if (message instanceof TurnOfMessage)
            System.out.println("turn of message sent");
        else
            System.out.println("Sending message...");
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {}

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {}

    @Override
    public void closeConnection() {}
}
