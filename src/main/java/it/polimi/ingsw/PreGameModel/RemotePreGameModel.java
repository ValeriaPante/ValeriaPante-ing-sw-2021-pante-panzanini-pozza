package it.polimi.ingsw.PreGameModel;

import it.polimi.ingsw.Network.JsonToClient.JsonToClientPreGame;

import java.util.LinkedList;
import java.util.List;

public class RemotePreGameModel implements PreGameModel{
    //in sostanza tutti i players sono observer delle lobbies
    private final LinkedList<Lobby> lobbies;
    private final LinkedList<User> notDecidedYet;

    public RemotePreGameModel(){
        this.lobbies = new LinkedList<>();
        this.notDecidedYet = new LinkedList<>();
    }

    private void notifyAllUsers(String message){
        this.notDecidedYet.forEach(user -> user.send(message));
        this.lobbies.forEach(lobby -> lobby.getUsers().forEach(user -> user.send(message)));
    }

    private void notifyUserAllLobbies(User user){
        this.lobbies.forEach(lobby -> user.send(JsonToClientPreGame.changedLobbyMessage(lobby, false)));

        //user.send("{\"players\":[\"Daniel\",\"Vale\",\"Alberto\"],\"itsYou\":false,\"type\":\"changedLobby\",\"id\":1}"); //debug
    }

    private void notifyAllUsers(String message, int userId, String messageToSpecificUser){
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

    public List<Integer> getAllLobbiesId(){
        LinkedList<Integer> allIds = new LinkedList<>();
        for (Lobby lobby : this.lobbies){
            allIds.add(lobby.getId());
        }
        return allIds;
    }

    //non notifico nessuno perchè ci devo ancora mettere un giocatore (è vuota)
    public void createLobby(int id){
        this.lobbies.add(new Lobby(id));
    }

    public int getUserLobbyId(int userId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getFirstUserId() == userId){
                return lobby.getId();
            }
        }
        //non ci può essere una lobby con id 0
        return 0;
    }

    public Lobby getAndRemoveLobby(int lobbyId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                this.lobbies.remove(lobby);
                //notifico a tutti che questa lobby è vuota
                this.notifyAllUsers(JsonToClientPreGame.changedLobbyMessage(new Lobby(lobbyId), false));
                return lobby;
            }
        }
        //non credo proprio che possa arrivare a questo punto
        return null;
    }

    public User getAndRemoveUser(int userId){
        for (User user : this.notDecidedYet){
            if (user.getId() == userId){
                this.notDecidedYet.remove(user);
                return user;
            }
        }

        //qua non notifico nulla perchè so sicuramente che se chiamo questo metodo
        //mi serve mettere l'user da qualche altra parte
        for (Lobby lobby : this.lobbies){
            for (User user : lobby.getUsers()){
                if (user.getId() == userId){
                    lobby.removeUser(user);
                    this.notDecidedYet.add(user);
                    //notifico il cambiamento di lobby a tutti
                    this.notifyAllUsers(JsonToClientPreGame.changedLobbyMessage(lobby, false));
                    //so che è in not decided yet
                    this.getAndRemoveUser(userId);

                    //cancello la lobby se una volta rimosso un giocatore questa è vuota
                    //è una cosa che dovrebbe fare il controller ma mi sembra stupido passarlo sopra
                    if (lobby.isEmpty()){
                        this.lobbies.remove(lobby);
                    }
                    return user;
                }
            }
        }
        //l'user non c'è, molto strano
        return null;
    }

    public boolean isLobbyFull(int lobbyId){
        for (Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                return lobby.isFull();
            }
        }
        //se la lobby non esiste la tratto come se fosse piena
        return true;
    }

    public void addUserToLobby(User user, int lobbyId){
        for(Lobby lobby : this.lobbies){
            if (lobby.getId() == lobbyId){
                lobby.addUser(user);
                this.notifyAllUsers(JsonToClientPreGame.changedLobbyMessage(lobby, false), user.getId(), JsonToClientPreGame.changedLobbyMessage(lobby, true));
                return;
            }
        }
    }

    public void addNewUser(User user){
        this.notDecidedYet.add(user);
        this.notifyUserAllLobbies(user);
    }
}
