package it.polimi.ingsw.PreGameModel;

import java.util.List;

public class LocalPreGameModel implements PreGameModel{

    public List<Integer> getAllLobbiesId(){
        return null;
    }
    public List<Integer> getAllUsersIds(){
        return null;
    }

    public void createLobby(int id){
    }

    public int getUserLobbyId(int userId){
        return 0;
    }

    public Lobby getAndRemoveLobby(int lobbyId){
        return null;
    }

    public boolean isLobbyFull(int lobbyId){
        return true;
    }

    public User getAndRemoveUser(int userId){
        return null;
    }

    public void addUserToLobby(User user, int lobbyId){
    }

    public void addNewUser(User user){
    }
}
