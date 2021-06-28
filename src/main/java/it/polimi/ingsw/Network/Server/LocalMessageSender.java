package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.Client.Visitor;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.View.View;

public class LocalMessageSender implements MessageSenderInterface{
    private final Visitor visitor;
    private int id;

    public LocalMessageSender(View view){
        visitor = new Visitor(view);
    }

    @Override
    public void send(FromServerMessage message) {
        message.visit(visitor);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {

    }

    @Override
    public void closeConnection() {

    }
}
