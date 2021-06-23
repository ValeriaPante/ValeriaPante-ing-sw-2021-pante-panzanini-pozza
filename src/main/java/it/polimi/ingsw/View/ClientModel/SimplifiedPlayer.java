package it.polimi.ingsw.View.ClientModel;

import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class SimplifiedPlayer {
    private String username;
    private HashMap<Integer, Resource[]> leaderStorages = new HashMap<>();
    private HashMap<Resource, Integer> strongbox = new HashMap<>();
    private HashMap<Resource, Integer> supportContainer = new HashMap<>();
    private HashMap<Resource, Integer>[] shelves = new HashMap[] {
            new HashMap(), new HashMap(), new HashMap()
    };
    private ArrayList<Integer> leaderCards = new ArrayList<>();
    private int[][] devSlots = new int[3][3];

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Resource[] getLeaderStorage(int cardId) {
        return leaderStorages.get(cardId);
    }

    public HashMap<Integer, Resource[]> getAllLeaderStorages(){
        return leaderStorages;
    }

    public void setLeaderStorage(int cardId, Resource[] leaderStorage) {
        if(this.leaderStorages.get(cardId) == null) this.leaderStorages.put(cardId, leaderStorage);
        else this.leaderStorages.replace(cardId, leaderStorage);
    }

    public HashMap<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(HashMap<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    public HashMap<Resource, Integer> getSupportContainer() {
        return supportContainer;
    }

    public void setSupportContainer(HashMap<Resource, Integer> supportContainer) {
        this.supportContainer = supportContainer;
    }

    public HashMap<Resource, Integer> getShelf(int numShelf) {
        return this.shelves[numShelf];
    }

    public void setShelf(Resource type, int quantity, int numShelf) {
        this.shelves[numShelf].clear();
        if(type != null) this.shelves[numShelf].put(type, quantity);
    }

    public ArrayList<Integer> getLeaderCards() {
        return leaderCards;
    }

    public void addLeaderCard(int cardId) {
        this.leaderCards.add(cardId);
    }

    public void removeLeaderCard(int cardId) {
        this.leaderCards.remove(Integer.valueOf(cardId));
    }

    public int[][] getDevSlots() {
        return devSlots;
    }

    public void addDevCardInSlot(int cardId, int numberOfSlot){
        for (int j = 0; j < devSlots[numberOfSlot - 1].length; j++){
            if(devSlots[numberOfSlot - 1][j] == 0){
                devSlots[numberOfSlot - 1][j] = cardId;
                break;
            }
        }
    }
}
