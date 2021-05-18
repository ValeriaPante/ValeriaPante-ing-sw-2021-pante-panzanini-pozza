package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class NewLobbyMessage extends WithIntMessage{

    private final String firstPlayer;

    public NewLobbyMessage(int lobbyId, String firstPlayer) {
        this.id = lobbyId;
        this.firstPlayer = firstPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
