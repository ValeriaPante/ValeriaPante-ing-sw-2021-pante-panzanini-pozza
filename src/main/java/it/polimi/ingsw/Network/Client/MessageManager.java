package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

public interface MessageManager {

    void update(InGameMessage message);
    void update(PreGameMessage message);
    void connect(String ip, String port, String username);
}
