package it.polimi.ingsw.Controller.PreGameController;

public interface RequestHandler{
    void requestEvaluator(int id, String request);
    void connectionClosed(int id);
}
