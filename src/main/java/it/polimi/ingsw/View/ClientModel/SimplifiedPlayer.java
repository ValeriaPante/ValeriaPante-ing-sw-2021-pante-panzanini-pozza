package it.polimi.ingsw.View.ClientModel;

import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simplified model of the player
 */
public class SimplifiedPlayer {
    private String username;
    private final HashMap<Integer, Resource[]> leaderStorages;
    private HashMap<Resource, Integer> strongbox;
    private HashMap<Resource, Integer> supportContainer;
    private final HashMap<Resource, Integer>[] shelves;
    private final ArrayList<Integer> leaderCards;
    private final int[][] devSlots;

    public SimplifiedPlayer(){
        leaderStorages = new HashMap<>();
        strongbox = new HashMap<>();
        supportContainer = new HashMap<>();
        shelves  = new HashMap[] {
                new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()
        };
        leaderCards = new ArrayList<>();
        devSlots = new int[3][3];
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized Resource[] getLeaderStorage(int cardId) {
        return leaderStorages.get(cardId);
    }

    public synchronized HashMap<Integer, Resource[]> getAllLeaderStorages(){
        return leaderStorages;
    }

    public synchronized void setLeaderStorage(int cardId, Resource[] leaderStorage) {
        if(this.leaderStorages.get(cardId) == null) this.leaderStorages.put(cardId, leaderStorage);
        else this.leaderStorages.replace(cardId, leaderStorage);
    }

    public synchronized HashMap<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    public synchronized void setStrongbox(HashMap<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    public synchronized HashMap<Resource, Integer> getSupportContainer() {
        return supportContainer;
    }

    public synchronized void setSupportContainer(HashMap<Resource, Integer> supportContainer) {
        this.supportContainer = supportContainer;
    }

    public synchronized HashMap<Resource, Integer> getShelf(int numShelf) {
        return this.shelves[numShelf];
    }

    public synchronized void setShelf(Resource type, int quantity, int numShelf) {
        this.shelves[numShelf].clear();
        if(type != null) this.shelves[numShelf].put(type, quantity);
    }

    public synchronized ArrayList<Integer> getLeaderCards() {
        return leaderCards;
    }

    public synchronized void addLeaderCard(int cardId) {
        this.leaderCards.add(cardId);
    }

    public synchronized void removeLeaderCard(int cardId) {
        this.leaderCards.remove(Integer.valueOf(cardId));
    }

    public synchronized int[][] getDevSlots() {
        return devSlots;
    }

    public synchronized void addDevCardInSlot(int cardId, int numberOfSlot){
        for (int j = 0; j < devSlots[numberOfSlot - 1].length; j++){
            if(devSlots[numberOfSlot - 1][j] == 0){
                devSlots[numberOfSlot - 1][j] = cardId;
                break;
            }
        }
    }
}
