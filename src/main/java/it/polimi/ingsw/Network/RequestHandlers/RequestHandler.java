package it.polimi.ingsw.Network.RequestHandlers;

public interface RequestHandler{
    void requestEvaluator(int id, String request);
    void connectionClosed(int id);
}
