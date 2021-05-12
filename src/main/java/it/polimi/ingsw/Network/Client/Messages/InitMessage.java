package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class InitMessage extends FromServerMessage{

    private final Resource[][] market;
    private final int[][] devDecks;
    private final int[] playersId;
    private final String[] playersUsernames;
    private final ArrayList<int[]> playersLeaderCards;
    private final ArrayList<HashMap<Resource, Integer>> playersInitialResources;

    public InitMessage(Resource[] market, int[] devDecks, int[] playersId, String[] playersUsernames, ArrayList<int[]> playersLeaderCards, ArrayList<HashMap<Resource, Integer>> playersInitialResources) {
        this.market = new Resource[3][4];
        for(int i = 0; i < market.length; i++)
            this.market[i/4][i%4] = market[i];

        this.devDecks = new int[3][4];
        for(int i = 0; i < devDecks.length; i++)
            this.devDecks[i/4][i%4] = devDecks[i];

        this.playersId = playersId;
        this.playersUsernames = playersUsernames;
        this.playersLeaderCards = playersLeaderCards;
        this.playersInitialResources = playersInitialResources;
    }

    public Resource[][] getMarket() {
        return market;
    }

    public int[][] getDevDecks() {
        return devDecks;
    }

    public int[] getPlayersId() {
        return playersId;
    }

    public String[] getPlayersUsernames() {
        return playersUsernames;
    }

    public ArrayList<int[]> getPlayersLeaderCards() {
        return playersLeaderCards;
    }

    public ArrayList<HashMap<Resource, Integer>> getPlayersInitialResources() {
        return playersInitialResources;
    }

    @Override
    public void visit(Visitor v){
        v.updateModel(this);
    }
}
