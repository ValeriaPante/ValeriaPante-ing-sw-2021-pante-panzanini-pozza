package it.polimi.ingsw.Network.RequestHandlers;

/**
 * Interface that declares the methods to handle requests from a client
 */
public interface RequestHandler{
    void requestEvaluator(int id, String request);
    void connectionClosed(int id);
}
