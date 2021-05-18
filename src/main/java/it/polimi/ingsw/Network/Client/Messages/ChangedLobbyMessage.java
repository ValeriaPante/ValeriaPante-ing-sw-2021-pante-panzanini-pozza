package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class ChangedLobbyMessage extends WithIntMessage{

    private final String[] players;

    public ChangedLobbyMessage(int lobbyId, String[] players) {
        this.id = lobbyId;
        this.players = players;
    }

    public String[] getPlayers() {
        return players;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
