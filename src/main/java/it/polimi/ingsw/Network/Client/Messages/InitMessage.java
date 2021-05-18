package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

public class InitMessage extends WithIntMessage{

    private final Resource[][] market;
    private final Resource slide;
    private final int[][] devDecks;
    private final int[] playersId;
    private final String[] playersUsernames;
    private final int[] localPlayerLeaderCards;

    public InitMessage(int clientId, Resource[] market, Resource slide, int[] devDecks, int[] playersId, String[] playersUsernames, int[] playersLeaderCards) {
        this.id = clientId;

        this.market = new Resource[3][4];
        for(int i = 0; i < market.length; i++)
            this.market[i/4][i%4] = market[i];

        this.slide = slide;

        this.devDecks = new int[3][4];
        for(int i = 0; i < devDecks.length; i++)
            this.devDecks[i/4][i%4] = devDecks[i];

        this.playersId = playersId;
        this.playersUsernames = playersUsernames;
        this.localPlayerLeaderCards = playersLeaderCards;
    }

    public Resource[][] getMarket() {
        return market;
    }

    public Resource getSlide() {
        return slide;
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

    public int[] getLocalPlayerLeaderCards() {
        return localPlayerLeaderCards;
    }

    @Override
    public void visit(Visitor v){
        v.updateModel(this);
    }
}
