package it.polimi.ingsw.Network.Messages;

public class MoveToLobbyMessage extends Message{

    private final int lobbyId;

    public MoveToLobbyMessage(int senderId, int lobbyId) {
        super(senderId);
        this.lobbyId = lobbyId;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}
