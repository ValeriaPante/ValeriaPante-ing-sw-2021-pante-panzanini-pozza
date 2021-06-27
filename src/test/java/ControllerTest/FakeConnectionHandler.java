package ControllerTest;

import it.polimi.ingsw.Network.Client.Messages.*;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;

public class FakeConnectionHandler implements MessageSenderInterface {

    private int id;

    @Override
    public void send(FromServerMessage message) {
        if (message instanceof InitMessage)
            System.out.println("init message sent");
        else if (message instanceof StartMessage)
            System.out.println("start message sent");
        else if (message instanceof TurnOfMessage)
            System.out.println("turn of message sent");
        else if (message instanceof ChangedShelfMessage)
            System.out.println("Changed shelf message sent");
        else
            System.out.println("Sending message...");
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {}

    @Override
    public void closeConnection() {}
}
