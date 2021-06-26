package it.polimi.ingsw.View.ClientModel;

import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simplified model of the game
 */
public class Game {
    private Resource[][] grid;
    private Resource slide;
    private int[][] devDecks;
    private int localPlayerId;
    private LinkedHashMap<Integer, SimplifiedPlayer> players = new LinkedHashMap<>();
    private  HashMap<Integer, String[]> lobbies = new HashMap<>();
    private int localPlayerLobbyId;

    public synchronized SimplifiedPlayer getPlayerFromId(int id){
        return players.get(id);
    }

    public synchronized void updateMarketState(Resource[][] grid, Resource slide){
        this.grid = grid;
        this.slide = slide;
    }

    public synchronized void updateDevDeck(int numDeck, int cardId){
        devDecks[numDeck/4][numDeck%4] = cardId;
    }

    public synchronized void initialiseDevDecks(int[][] decks){
        this.devDecks = decks;
    }

    public synchronized Resource[][] getGrid() {
        return grid;
    }

    public synchronized Resource getSlide() {
        return slide;
    }

    public synchronized int[][] getDevDecks() {
        return devDecks;
    }

    public synchronized void addPlayer(int playerId, SimplifiedPlayer player){
        players.put(playerId, player);
    }

    public synchronized ArrayList<String> getUsernames(){
        ArrayList<String> usernames = new ArrayList<>();
        for(Map.Entry<Integer, SimplifiedPlayer> player: players.entrySet()){
            usernames.add(player.getValue().getUsername());
        }
        return usernames;
    }

    public synchronized int getLocalPlayerId() {
        return localPlayerId;
    }

    public synchronized void setLocalPlayerId(int localPlayerId) {
        this.localPlayerId = localPlayerId;
    }

    public synchronized int getLocalPlayerIndex(){
        return new ArrayList<>(players.keySet()).indexOf(localPlayerId);
    }

    public synchronized int getPlayerIndex(int playerId){
        return new ArrayList<>(players.keySet()).indexOf(playerId);
    }

    public synchronized int getNumberOfPlayers(){
        return players.keySet().size();
    }

    public synchronized int getLocalPlayerLobbyId() {
        return localPlayerLobbyId;
    }

    public synchronized void setLocalPlayerLobbyId(int localPlayerLobbyId) {
        this.localPlayerLobbyId = localPlayerLobbyId;
    }

    public synchronized HashMap<Integer, String[]> getLobbies() {
        return lobbies;
    }

    public synchronized void addLobby(int lobbyId, String[] players) {
        lobbies.put(lobbyId, players);
    }

    public synchronized void removeLobby(int lobbyId) {
        lobbies.remove(lobbyId);
    }

    public synchronized ArrayList<Integer> getPLayersID(){
        ArrayList<Integer> playersID = new ArrayList<>();
        for(Map.Entry<Integer, SimplifiedPlayer> player: players.entrySet())
            playersID.add(player.getKey());
        return playersID;
    }
}
