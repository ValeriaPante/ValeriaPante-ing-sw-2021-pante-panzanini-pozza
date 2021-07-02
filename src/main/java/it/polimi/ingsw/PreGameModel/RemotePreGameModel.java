package it.polimi.ingsw.PreGameModel;

import it.polimi.ingsw.Network.Client.Messages.ChangedLobbyMessage;
import it.polimi.ingsw.Network.Client.Messages.ErrorMessage;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * Model of pre game grouped
 * Every new user is not placed in a lobby
 * Every player in a lobby can switch lobby
 */
public class RemotePreGameModel{
    //basically evey user is observer of the lobbies
    private final LinkedList<Lobby> lobbies;
    private final LinkedList<User> notDecidedYet;

    /**
     * Constructor
     */
    public RemotePreGameModel(){
        this.lobbies = new LinkedList<>();
        this.notDecidedYet = new LinkedList<>();
    }

    private ChangedLobbyMessage messageBuilder(Lobby lobby, boolean bool){
        String[] usernames = lobby.getUsers().stream().map(User::getUsername).toArray(String[]::new);
        return new ChangedLobbyMessage(lobby.getId(), usernames, bool);
    }

    private void notifyAllUsers(FromServerMessage message){
        this.notDecidedYet.forEach(user -> user.send(message));
        this.lobbies.forEach(lobby -> lobby.getUsers().forEach(user -> user.send(message)));
    }

    private void notifyUserAllLobbies(User user){
        this.lobbies.forEach(lobby -> user.send(this.messageBuilder(lobby, false)));
    }

    private void notifyAllUsers(FromServerMessage message, int userId, FromServerMessage messageToSpecificUser){
        this.notDecidedYet.forEach(user -> user.send(message));
        this.lobbies.forEach(lobby -> lobby.getUsers().forEach(user -> {
            if (user.getId() == userId){
                user.send(messageToSpecificUser);
            }
            else{
                user.send(message);
            }
        }));
    }

    /**
     * Getter
     * @return all the users ids
     */
    public List<Integer> getAllUsersIds(){
        LinkedList<Integer> allIds = new LinkedList<>();
        for(Lobby lobby : this.lobbies){
            for (User user : lobby.getUsers()){
                allIds.add(user.getId());
            }
        }
        for (User user : this.notDecidedYet){
            allIds.add(user.getId());
        }
        System.out.println("Returned all ids");
        return allIds;
    }

    /**
     * Getter
     * @return all the lobbies id
     */
    public List<Integer> getAllLobbiesId(){
        LinkedList<Integer> allIds = new LinkedList<>();
        for (Lobby lobby : this.lobbies){
            allIds.add(lobby.getId());
        }
        return allIds;
    }

    /**
     * Creation of a lobby
     * @param id the id of the new lobby
     */
    public void createLobby(int id){
        this.lobbies.add(new Lobby(id));
    }

    /**
     * Asking if a user is the first user in a lobby
     * @param userId the user id that we are searching
     * @return the lobby id the user is the first in or 0 if no match are found
     */
    public int getUserLobbyId(int userId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getFirstUserId() == userId){
                return lobby.getId();
            }
        }
        return 0;
    }

    /**
     * Removes a lobby
     * @param lobbyId the id of the lobby to remove
     * @return the lobby removed or null if there was no lobby with that id
     */
    public Lobby getAndRemoveLobby(int lobbyId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                this.lobbies.remove(lobby);
                this.notifyAllUsers(this.messageBuilder(lobby, false));
                return lobby;
            }
        }
        return null;
    }

    /**
     * Removes a user
     * @param userId the id of the user to remove
     * @return the user removed or null if there was no user with that id
     */
    public User getAndRemoveUser(int userId){
        for (User user : this.notDecidedYet){
            if (user.getId() == userId){
                this.notDecidedYet.remove(user);
                return user;
            }
        }

        for (Lobby lobby : this.lobbies){
            for (User user : lobby.getUsers()){
                if (user.getId() == userId){
                    lobby.removeUser(user);
                    this.notDecidedYet.add(user);
                    this.notifyAllUsers(this.messageBuilder(lobby, false));

                    if (lobby.isEmpty()){
                        this.lobbies.remove(lobby);
                    }

                    return this.getAndRemoveUser(userId);
                }
            }
        }

        return null;
    }

    /**
     * Asking if a lobby is full
     * @param lobbyId the lobby id that we are asking
     * @return true if the lobby is full or if the lobby does not exist
     */
    public boolean isLobbyFull(int lobbyId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                return lobby.isFull();
            }
        }
        return true;
    }

    /**
     * Add a user to a lobby
     * @param user user to add
     * @param lobbyId the destination lobby
     */
    public void addUserToLobby(User user, int lobbyId){
        for(Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                lobby.addUser(user);
                this.notifyAllUsers(this.messageBuilder(lobby, false), user.getId(), this.messageBuilder(lobby, true));
                return;
            }
        }
    }

    /**
     * Send and error message to a specified user
     * @param userToNotifyError the user id
     * @param errorString the error
     */
    public void notifyError(int userToNotifyError, String errorString){
        ErrorMessage errorMessage = new ErrorMessage(errorString);
        for (User user : this.notDecidedYet){
            if (user.getId() == userToNotifyError){
                user.send(errorMessage);
                return;
            }
        }
        for (Lobby lobby : this.lobbies) {
            for (User user : lobby.getUsers()) {
                if (user.getId() == userToNotifyError) {
                    user.send(errorMessage);
                    return;
                }
            }
        }
    }

    /**
     * Adding a new user to the model: basically adding a new observer
     * @param user user to add
     */
    public void addNewUser(User user){
        this.notDecidedYet.add(user);
        this.notifyUserAllLobbies(user);
    }
}
