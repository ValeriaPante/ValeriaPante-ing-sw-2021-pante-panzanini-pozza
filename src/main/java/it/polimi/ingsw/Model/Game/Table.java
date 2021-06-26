package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Decks.LeaderDeck;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Model.Deposit.StrongBox;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.Player.LorenzoIlMagnifico;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.Network.Client.Messages.*;

import java.util.*;

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

    private void notifyAllPlayer(FromServerMessage message){
        players.forEach(player -> player.sendMessage(message));
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
        this.winner = new ArrayList<>();
    }

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

    public void actionOnLeaderCard(LeaderCard card, boolean discard){
        if (discard) {
            this.turnOf().discardLeaderCard(card);
        } else{
            card.play();
        }
        FromServerMessage message = new ActionOnLeaderCardMessage(this.turnOf + 1, discard, card.getId());
        this.notifyAllPlayer(message);
    }

    public void updatePlayerOfTurnSupportContainer(EnumMap<Resource, Integer> cost){
        StrongBox supportContainer = this.turnOf().getSupportContainer();
        supportContainer.clear();
        supportContainer.addEnumMap(cost);
        ChangedSupportContainerMessage message = new ChangedSupportContainerMessage(this.turnOf+1, new HashMap<>(cost));
        this.notifyAllPlayer(message);
    }

    public void updatePlayerOfTurnDevSlot(int devSlotNum, DevCard chosenCard){
        this.turnOf().getDevSlots()[devSlotNum - 1].addCard(chosenCard);
        NewDevCardMessage message = new NewDevCardMessage(this.turnOf+1, devSlotNum, chosenCard.getId());
        this.notifyAllPlayer(message);
    }

    //pensando al riuso di questo
    public DevCard drawDevDeck(int numberOfDeck){
        DevCard cardDrawn = this.devDecks[numberOfDeck].draw();
        NewTopCardMessage message = new NewTopCardMessage(cardDrawn.getId(), numberOfDeck);
        this.notifyAllPlayer(message);
        return cardDrawn;
    }

    //idee sul set winner??? questo non mi piace molto
    public void addWinner(Player winner){
        this.winner.add(winner);
    }

    public void clearWinners(){ this.winner.clear(); }

    public DevDeck[] getDevDecks() {
        return Arrays.copyOf(this.devDecks, this.devDecks.length);
    }

    public void setBroadcastMessage(String newErrorMessage) {
        this.broadcastMessage = newErrorMessage;
        //notify to client
    }

    public void clearBroadcastMessage(){
        this.broadcastMessage = null;
    }
}
