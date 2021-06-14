package it.polimi.ingsw.PreGameModel;

import java.util.List;

public interface PreGameModel {

    List<Integer> getAllLobbiesId();
    List<Integer> getAllUsersIds();
    void createLobby(int id);
    int getUserLobbyId(int userId);
    Lobby getAndRemoveLobby(int lobbyId);
    boolean isLobbyFull(int lobbyId);
    User getAndRemoveUser(int userId);
    void addUserToLobby(User user, int lobbyId);
    void addNewUser(User user);
}
