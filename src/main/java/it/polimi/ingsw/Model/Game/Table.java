package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Decks.LeaderDeck;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.Player.LorenzoIlMagnifico;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.Network.Client.Messages.InitMessage;
import it.polimi.ingsw.Network.Client.Messages.TurnOfMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Table {
    public static final int maxPlayers = 4;
    private boolean isLastLap;
    private boolean singlePlayer;
    private final Market market;
    private final FaithTrack faithTrack;
    private final LinkedList<RealPlayer> players;
    private LorenzoIlMagnifico lorenzoIlMagnifico;
    private int turnOf;
    private ArrayList<Player> winner;
    private boolean isLorenzoTurn;

    private String broadcastMessage;

    private DevDeck[] devDecks;

    private void notifyAllPlayer(){

    }

    private void initialiseDevDecks(){
        this.devDecks = new DevDeck[]{
                new DevDeck(new DevCardType(1, Colour.GREEN)), new DevDeck(new DevCardType(1, Colour.YELLOW)), new DevDeck(new DevCardType(1,Colour.BLUE)), new DevDeck(new DevCardType(1, Colour.PURPLE)),
                new DevDeck(new DevCardType(2, Colour.GREEN)), new DevDeck(new DevCardType(2, Colour.YELLOW)), new DevDeck(new DevCardType(2,Colour.BLUE)), new DevDeck(new DevCardType(2, Colour.PURPLE)),
                new DevDeck(new DevCardType(3, Colour.GREEN)), new DevDeck(new DevCardType(3, Colour.YELLOW)), new DevDeck(new DevCardType(3,Colour.BLUE)), new DevDeck(new DevCardType(3, Colour.PURPLE)),
        };
    }

    public Table(int numberOfPlayers){
        if (1 == numberOfPlayers){
            this.lorenzoIlMagnifico = new LorenzoIlMagnifico();
            this.setSinglePlayer();
        }
        else this.setMultiPlayer();

        this.market = new Market();
        this.isLastLap = false;
        this.turnOf = 0;
        this.players = new LinkedList<>();
        this.faithTrack = new FaithTrack();
        this.initialiseDevDecks();
        this.broadcastMessage = null;
    }

    /*
    Qualcuno ha comprato una dev card->

    for (player){
        metodoNewOgetto(DevCard[]) -> void
    }

    metodoNewOgetto void(Oggetti del model){
        cosrstruisce il messaggio
        send
    }
    * */



    public void setLastLap(){
        this.isLastLap = true;
    }

    public boolean isLastLap(){
        return this.isLastLap;
    }

    private void setMultiPlayer(){
        this.singlePlayer = false;
    }

    private void setSinglePlayer() {
        this.singlePlayer = true;
        this.lorenzoIlMagnifico = new LorenzoIlMagnifico();
    }

    public boolean isSinglePlayer(){
        return this.singlePlayer;
    }

    public Market getMarket() {
        return this.market;
    }

    public FaithTrack getFaithTrack(){
        return this.faithTrack;
    }

    public void addPlayer(RealPlayer player) throws WrongLeaderCardType {
        if (player == null){
            throw new WrongLeaderCardType();
        }
        this.players.add(player);
    }

    public RealPlayer[] getPlayers(){
        return this.players.toArray(new RealPlayer[0]);
    }

    public LorenzoIlMagnifico getLorenzo(){
        return this.lorenzoIlMagnifico;
    }

    public boolean isLorenzoTurn() {
        return isLorenzoTurn;
    }

    //ritorna solo un realplayer
    //SE volete lorenzo chiamate getLorenzo()
    public RealPlayer turnOf(){
        return this.players.get(this.turnOf);
    }

    public void nextTurn(){
        if (!this.singlePlayer){
            this.turnOf = (this.turnOf + 1) % this.players.size();
            for (RealPlayer player : this.players){
                player.sendMessage(new TurnOfMessage(this.turnOf+1));
            }
        }
        else{

            this.isLorenzoTurn = !this.isLorenzoTurn;
        }
    }

    public void initLeaderCards(){
        LeaderDeck leaderDeck = new LeaderDeck();
        leaderDeck.shuffle();

        for (int i = 0; i < 4; i++)
            for (RealPlayer player : this.players)
                player.addLeaderCard(leaderDeck.draw());

        int[] playersId = new int[this.players.size()];
        for (int i=0; i<this.players.size(); i++){
            playersId[i] = i+1;
        }
        int[] topDevCardsId = Arrays.stream(this.devDecks).mapToInt(devDeck -> devDeck.getTopCard().getId()).toArray();
        for (int i=0; i<this.players.size(); i++){
            this.players.get(i).sendMessage(new InitMessage(
                    i+1,
                    this.market.getState(),
                    this.market.getSlide(),
                    topDevCardsId,
                    playersId,
                    this.players.stream().map(Player::getNickname).toArray(String[]::new),
                    Arrays.stream(this.players.get(i).getLeaderCards()).mapToInt(LeaderCard::getId).toArray()
            ));
        }
    }

    //idee sul set winner??? questo non mi piace molto
    public void addWinner(Player winner){
        this.winner.add(winner);
    }

    public void clearWinners(){ this.winner.clear(); }

    public DevDeck[] getDevDecks() {
        return Arrays.copyOf(this.devDecks, this.devDecks.length);
    }

    public DevCard drawDevDeck(int numberOfDeck){
        return this.devDecks[numberOfDeck].draw();
    }

    public void setBroadcastMessage(String newErrorMessage) {
        this.broadcastMessage = newErrorMessage;
        //notify to client
    }

    public void clearBroadcastMessage(){
        this.broadcastMessage = null;
    }
}
