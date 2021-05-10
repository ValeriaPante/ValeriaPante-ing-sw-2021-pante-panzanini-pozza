package it.polimi.ingsw.PreGameModel;

import java.util.LinkedList;
import java.util.List;

public class Lobby {

    private final int id;
    private LinkedList<User> lobby;

    public Lobby(int id){
        this.id = id;
        this.lobby = new LinkedList<>();
    }

    public int getFirstUserId(){
        return this.lobby.getFirst().getId();
    }

    public int getId(){
        return this.id;
    }

    public void addUser(User user){
        this.lobby.add(user);
    }

    public void removePlayer(User user){
        //this.lobby.remove(user);

        for (int i=0; i<this.lobby.size(); i++){
            if (this.lobby.get(i).getId() == user.getId()){
                this.lobby.remove(i);
                return;
            }
        }
    }

    public List<User> getPlayers(){
        return this.lobby;
    }

    public boolean isFull(){
        return  this.lobby.size() == 4;
    }

    public boolean isEmpty(){
        return this.lobby.size() == 0;
    }

}
