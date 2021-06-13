package it.polimi.ingsw.PreGameModel;

import it.polimi.ingsw.Network.Client.Messages.ChangedLobbyMessage;
import it.polimi.ingsw.Network.JsonToClient.JsonToClientPreGame;

import java.util.LinkedList;
import java.util.List;

public class RemotePreGameModel implements PreGameModel{
    //in sostanza tutti i players sono observer delle lobbies

    private final LinkedList<Lobby> lobbies;
    private final LinkedList<User> notDecidedYet;
    private final JsonToClientPreGame jsonToClientPreGame;

    public RemotePreGameModel(){
        this.lobbies = new LinkedList<>();
        this.notDecidedYet = new LinkedList<>();
        this.jsonToClientPreGame = new JsonToClientPreGame();
    }

    private void notifyAllUsers(){

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

    public void playerDisconnect(int userIdDisconnected){
        this.getAndRemoveUser(userIdDisconnected);
        this.notifyAllUsers();
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
                this.notifyAllUsers();
                return;
            }
        }
    }

    public void addNewUser(User user){
        this.notDecidedYet.add(user);
        ChangedLobbyMessage message = null;
        for (Lobby lobby : this.lobbies){
            String lobbyJson = this.jsonToClientPreGame.changedLobbyMessage(lobby);
            user.send(lobbyJson);
        }
        //update the user on the state of the lobbies
    }
}
