package it.polimi.ingsw.Network.JsonToClient;

import com.google.gson.Gson;
import it.polimi.ingsw.Network.Client.Messages.ChangedLobbyMessage;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;

public class JsonToClientPreGame {

    private static final Gson gson = new Gson();

    public static String changedLobbyMessage(Lobby lobby, boolean isMine){
        String[] usernames = lobby.getUsers().stream().map(User::getUsername).toArray(String[]::new);
        ChangedLobbyMessage message = new ChangedLobbyMessage(lobby.getId(), usernames, isMine);
        return gson.toJson(message);
    }

}
