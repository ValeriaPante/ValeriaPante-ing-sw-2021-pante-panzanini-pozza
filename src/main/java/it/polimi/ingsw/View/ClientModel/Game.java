package it.polimi.ingsw.View.ClientModel;

import it.polimi.ingsw.Enums.Resource;

import java.util.HashMap;

public class Game {
    private Resource[][] grid;
    private Resource slide;
    private int[][] devDecks;
    private int localPlayerId;
    private HashMap<Integer, SimplifiedPlayer> players;


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

    public int getLocalPlayerId() {
        return localPlayerId;
    }

    public void setLocalPlayerId(int localPlayerId) {
        this.localPlayerId = localPlayerId;
    }
}
