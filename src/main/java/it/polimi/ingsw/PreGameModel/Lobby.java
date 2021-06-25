package it.polimi.ingsw.PreGameModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Representation ob a group of users before the start of the game
 */
public class Lobby {

    private final int id;
    private final LinkedList<User> lobby;

    /**
     * Constructor
     * @param id this lobby id
     */
    public Lobby(int id){
        this.id = id;
        this.lobby = new LinkedList<>();
    }

    /**
     * Get the id of the first user in this lobby
     * @return the firs user id
     */
    public int getFirstUserId(){
        return this.lobby.getFirst().getId();
    }

    /**
     * Getter
     * @return this lobby id
     */
    public int getId(){
        return this.id;
    }

    /**
     * Adds a user to this lobby
     * @param user user to add
     */
    public void addUser(User user){
        this.lobby.add(user);
    }

    /**
     * Removes a user from this lobby
     * @param user user to remove
     */
    public void removeUser(User user){
        for (int i=0; i<this.lobby.size(); i++){
            if (this.lobby.get(i).getId() == user.getId()){
                this.lobby.remove(i);
                return;
            }
        }
    }

    /**
     * Getter
     * @return All users in this lobby
     */
    public List<User> getUsers(){
        return this.lobby;
    }

    /**
     * evaluate fullness
     * @return true if the lobby is full
     */
    public boolean isFull(){
        return  this.lobby.size() == 4;
    }

    /**
     * Evaluate emptiness
     * @return true if the lobby is empty
     */
    public boolean isEmpty(){
        return this.lobby.size() == 0;
    }

}
