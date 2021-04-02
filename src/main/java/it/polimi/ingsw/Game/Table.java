package it.polimi.ingsw.Game;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Decks.LeaderDeck;
import it.polimi.ingsw.Deposit.Market;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Player.LorenzoIlMagnifico;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.Arrays;
import java.util.LinkedList;

public class Table {
    public static final int maxPlayers = 4;

    private boolean isLastLap;
    private boolean singlePlayer;

    private final Market market;

    private final LinkedList<RealPlayer> players;
    private LorenzoIlMagnifico lorenzoIlMagnifico;
    private int turnOf;
    private RealPlayer winner;

    private final LeaderDeck leaderDeck;
    private DevDeck[] devDecks;

    private void initialiseDevDecks(){
        this.devDecks = new DevDeck[]{
                new DevDeck(new DevCardType(1, Colour.GREEN)), new DevDeck(new DevCardType(1, Colour.YELLOW)), new DevDeck(new DevCardType(1,Colour.BLUE)), new DevDeck(new DevCardType(1, Colour.PURPLE)),
                new DevDeck(new DevCardType(2, Colour.GREEN)), new DevDeck(new DevCardType(2, Colour.YELLOW)), new DevDeck(new DevCardType(2,Colour.BLUE)), new DevDeck(new DevCardType(2, Colour.PURPLE)),
                new DevDeck(new DevCardType(3, Colour.GREEN)), new DevDeck(new DevCardType(3, Colour.YELLOW)), new DevDeck(new DevCardType(3,Colour.BLUE)), new DevDeck(new DevCardType(3, Colour.PURPLE)),
        };
    }

    public Table(){
        this.market = new Market();
        this.isLastLap = false;
        this.turnOf = 0;
        this.players = new LinkedList<>();
        this.leaderDeck = new LeaderDeck();
        initialiseDevDecks();
    }

    public void setLastLap(){
        this.isLastLap = true;
    }
    public boolean isLastLap(){
        return this.isLastLap;
    }

    public void setMultiPlayer(){
        this.singlePlayer = false;
    }
    public void setSinglePlayer() {
        this.singlePlayer = true;
        this.lorenzoIlMagnifico = new LorenzoIlMagnifico();
    }
    public boolean isSinglePlayer(){
        return this.singlePlayer;
    }

    public Market getMarket() {
        return this.market;
    }

    public void addPlayer(String nickname) throws WeDontDoSuchThingsHere {
        if (this.players.size() >= Table.maxPlayers){
            throw new WeDontDoSuchThingsHere();
        }
        this.players.add(new RealPlayer(nickname));
    }
    public RealPlayer[] getPlayers(){
        return this.players.toArray(new RealPlayer[this.players.size()]);
    }

    public LorenzoIlMagnifico getLorenzo(){
        return this.lorenzoIlMagnifico;
    }

    public RealPlayer turnOf(){
        return (this.players.size() == 0) ? null : this.players.get(this.turnOf);
    }

    public void nextTurn(){
        if (!this.singlePlayer){
            this.turnOf = this.turnOf+1 % this.players.size();
        }
        else{
            //da decidere: -1, lasciamo 0 bho
        }
    }

    //idee sul set winner??? questo non mi piace molto
    public void setWinner(RealPlayer winner){
        this.winner = winner;
    }

    public LeaderDeck getLeaderDeck() {
        return this.leaderDeck;
    }

    public DevDeck[] getDevDecks() {
        return Arrays.copyOf(this.devDecks, this.devDecks.length);
    }
}
