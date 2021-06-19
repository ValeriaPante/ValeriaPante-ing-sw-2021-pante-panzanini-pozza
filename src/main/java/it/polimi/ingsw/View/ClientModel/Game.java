package it.polimi.ingsw.View.ClientModel;

import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Game {
    private Resource[][] grid;
    private Resource slide;
    private int[][] devDecks;
    private int localPlayerId;
    private LinkedHashMap<Integer, SimplifiedPlayer> players = new LinkedHashMap<>();
    private  HashMap<Integer, String[]> lobbies = new HashMap<>();
    private int localPlayerLobbyId;

    public SimplifiedPlayer getPlayerFromId(int id){
        return players.get(id);
    }

    public void updateMarketState(Resource[][] grid, Resource slide){
        this.grid = grid;
        this.slide = slide;
    }

    public void updateDevDeck(int numDeck, int cardId){
        devDecks[numDeck/4][numDeck%4] = cardId;
    }

    public void initialiseDevDecks(int[][] decks){
        this.devDecks = decks;
    }

    public Resource[][] getGrid() {
        return grid;
    }

    public Resource getSlide() {
        return slide;
    }

    public int[][] getDevDecks() {
        return devDecks;
    }

    public void addPlayer(int playerId, SimplifiedPlayer player){
        players.put(playerId, player);
    }

    public ArrayList<String> getUsernames(){
        ArrayList<String> usernames = new ArrayList<>();
        for(Map.Entry<Integer, SimplifiedPlayer> player: players.entrySet()){
            usernames.add(player.getValue().getUsername());
        }
        return usernames;
    }

    public int getLocalPlayerId() {
        return localPlayerId;
    }

    public void setLocalPlayerId(int localPlayerId) {
        this.localPlayerId = localPlayerId;
    }

    public int getLocalPlayerIndex(){
        return new ArrayList<>(players.keySet()).indexOf(localPlayerId);
    }

    public int getPlayerIndex(int playerId){
        return new ArrayList<>(players.keySet()).indexOf(playerId);
    }

    public int getNumberOfPlayers(){
        return players.keySet().size();
    }

    public int getLocalPlayerLobbyId() {
        return localPlayerLobbyId;
    }

    public void setLocalPlayerLobbyId(int localPlayerLobbyId) {
        this.localPlayerLobbyId = localPlayerLobbyId;
    }

    public HashMap<Integer, String[]> getLobbies() {
        return lobbies;
    }

    public void addLobby(int lobbyId, String[] players) {
        lobbies.put(lobbyId, players);
    }

    public void removeLobby(int lobbyId) {
        lobbies.remove(lobbyId);
    }
}
