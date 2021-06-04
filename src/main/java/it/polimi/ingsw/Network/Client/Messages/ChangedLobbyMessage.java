package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class ChangedLobbyMessage extends WithIntMessage{

    private final String[] players;
    private final boolean itsYou;

    public ChangedLobbyMessage(int lobbyId, String[] players, boolean itsYou) {
        this.id = lobbyId;
        this.players = players;
        this.itsYou = itsYou;
    }

    public String[] getPlayers() {
        return players;
    }

    public boolean isMine(){
        return itsYou;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
