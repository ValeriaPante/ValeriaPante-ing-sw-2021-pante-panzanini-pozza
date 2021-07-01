package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Decks.LeaderDeck;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Model.Deposit.Shelf;
import it.polimi.ingsw.Model.Deposit.StrongBox;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.FaithTrack.VaticanRelation;
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
            this.notifyAllPlayer(new TurnOfMessage(this.turnOf + 1));
        }
        else{

            this.isLorenzoTurn = !this.isLorenzoTurn;
            if(isLorenzoTurn) this.notifyAllPlayer(new TurnOfMessage(0));
            else this.notifyAllPlayer(new TurnOfMessage(this.turnOf + 1));
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

    public void addToPlayerOfTurnStrongbox(EnumMap<Resource, Integer> toAdd){
        this.turnOf().getStrongBox().addEnumMap(toAdd);
        this.notifyStrongBoxChange();
    }

    public void updatePlayerOfTurnSupportContainer(EnumMap<Resource, Integer> cost){
        StrongBox supportContainer = this.turnOf().getSupportContainer();
        supportContainer.clear();
        addToSupportContainer(cost);
    }

    public void addToSupportContainer(EnumMap<Resource, Integer> map){
        if (map != null)
            this.turnOf().getSupportContainer().addEnumMap(map);
        this.notifySupportContainerChange();
    }

    public void updatePlayerOfTurnDevSlot(int devSlotNum, DevCard chosenCard){
        if(chosenCard.isSelected()) chosenCard.select();
        this.turnOf().getDevSlots()[devSlotNum - 1].addCard(chosenCard);
        NewDevCardMessage message = new NewDevCardMessage(this.turnOf+1, chosenCard.getId(), devSlotNum);
        this.notifyAllPlayer(message);
    }

    //pensando al riuso di questo
    public DevCard drawDevDeck(int numberOfDeck){
        DevCard cardDrawn = this.devDecks[numberOfDeck].draw();
        DevCard newTopCard = null;
        try{
             newTopCard = this.devDecks[numberOfDeck].getTopCard();
        } catch (IndexOutOfBoundsException e) {

        }
        NewTopCardMessage message = new NewTopCardMessage((newTopCard==null)? 0 : newTopCard.getId(), numberOfDeck);
        this.notifyAllPlayer(message);
        return cardDrawn;
    }

    public EnumMap<Resource, Integer> takeFromMarket(){
        EnumMap<Resource, Integer> picked = market.takeSelection();
        notifyAllPlayer(new NewMarketStateMessage(market.getState(), market.getSlide()));
        return picked;
    }

    public ActionToken drawToken(){
        ActionToken token = this.lorenzoIlMagnifico.getActionTokenDeck().draw();
        this.notifyAllPlayer(new LorenzoTurnMessage(token.getType()));
        return token;
    }

    /**
     * Discards 2 development cards of a certain color
     * @param color indicating the color of the development cards to discard
     */
    public void discardTwoDevCards(Colour color){
        int cardToDiscards = 2;
        int level = 0;
        int i = 0;
        for (i=0; i<this.devDecks.length; i++){
            if (this.devDecks[i].getType().getColor() == color){
                break;
            }
        }
        while (cardToDiscards > 0 && level<3){
            if (this.devDecks[4*level + i].size() > 1){
                this.devDecks[4*level + i].draw();
                cardToDiscards--;
                if (cardToDiscards == 0){
                    this.notifyAllPlayer(new NewTopCardMessage(this.devDecks[4*level + i].getTopCard().getId(), 4*level+i));
                }
            }
            else if(this.devDecks[4*level + i].size() == 1){
                this.devDecks[4*level + i].draw();
                cardToDiscards--;
                this.notifyAllPlayer(new NewTopCardMessage(0, 4*level + i));
                level++;
            }
            else{
                level++;
            }
        }
    }

    public void addWinners(List<Player> winners){
        this.winner.addAll(winners);
        Player winner = this.winner.get(0);
        WinnerMessage message;
        if (winner.getId() == 0){
            message = new WinnerMessage(0);
        }
        else{
            message = new WinnerMessage(turnOf+1);
        }
        this.notifyAllPlayer(message);
    }

    public void addAllIfPossibleToShelf(int capacity, Resource resourceToAdd, int quantity){
        for (Shelf s : this.turnOf().getShelves())
            if (s.getCapacity() == capacity){
                s.addAllIfPossible(resourceToAdd, quantity);
                notifyShelfChange(s);
                break;
            }
    }

    /**
     * adds an enumMap to a leaderCard with storage ability
     * @param cardId  the id of the leader card
     * @param enumMap enumMap to be put in the specified leader storage ability
     */
    public void addResourcesToPlayerOfTurnLC(int cardId, EnumMap<Resource, Integer> enumMap){
        LeaderCard specifiedLeaderCard = null;
        for (LeaderCard leaderCard : this.turnOf().getLeaderCards()){
            if (leaderCard.getId() == cardId){
                specifiedLeaderCard = leaderCard;
                break;
            }
        }
        if (specifiedLeaderCard != null){
            for (Resource r: Resource.values())
                if (enumMap.containsKey(r))
                    for (int i=0; i < enumMap.get(r); i++)
                        specifiedLeaderCard.getAbility().add(r);

            this.notifyLeaderStorageChange(specifiedLeaderCard);
        }
    }

    private void notifyStrongBoxChange(){
        ChangedStrongboxMessage message = new ChangedStrongboxMessage(turnOf+1, this.turnOf().getStrongBox().content() == null ? new HashMap<>() : new HashMap<>(this.turnOf().getStrongBox().content()));
        this.notifyAllPlayer(message);
    }

    private void notifyShelfChange(Shelf shelfTarget){
        ChangedShelfMessage message = new ChangedShelfMessage(
                turnOf+1,
                shelfTarget.getCapacity()-1,
                shelfTarget.getResourceType(),
                shelfTarget.getUsage());

        this.notifyAllPlayer(message);
    }

    private void notifySupportContainerChange(){
        HashMap<Resource, Integer> container = new HashMap<>();
        EnumMap<Resource, Integer> content = this.turnOf().getSupportContainer().content();
        if(content != null){
            for(Map.Entry<Resource, Integer> entry: content.entrySet())
                container.put(entry.getKey(), entry.getValue());
        }

        ChangedSupportContainerMessage message = new ChangedSupportContainerMessage(this.turnOf+1, container);
        this.notifyAllPlayer(message);
    }

    private void notifyLeaderStorageChange(LeaderCard leaderCard){
        ChangedLeaderStorageMessage message = new ChangedLeaderStorageMessage(this.turnOf+1, leaderCard.getId(), leaderCard.getAbility().getFullContent());
        this.notifyAllPlayer(message);
    }

    public void payPlayerOfTurn(Payable payable){
        payable.pay();

        if (payable == this.turnOf().getStrongBox()){
            this.notifyStrongBoxChange();
            return;
        }
        if (payable == this.turnOf().getSupportContainer()){
            notifySupportContainerChange();
            return;
        }
        for (Shelf shelf : this.turnOf().getShelves()){
            if (payable == shelf){
                this.notifyShelfChange(shelf);
                return;
            }
        }
        for (LeaderCard leaderCard : this.turnOf().getLeaderCards()){
            if (leaderCard.getAbility() == payable){
                this.notifyLeaderStorageChange(leaderCard);
                return;
            }
        }

    }

    public void moveForwardOnFaithTrack(int playerId, int amount){
        FromServerMessage message = null;
        if (playerId == 0){
            this.lorenzoIlMagnifico.moveForward(amount);
            message = new NewPlayerPositionMessage(0, this.getLorenzo().getPosition());
        }
        else {
            for (int i=0; i<this.players.size(); i++){
                if (this.players.get(i).getId() == playerId){
                    this.players.get(i).moveForward(amount);
                    message = new NewPlayerPositionMessage(i+1, this.players.get(i).getPosition());
                    break;
                }
            }
        }
        if (message != null) {
            this.notifyAllPlayer(message);
        }
    }

    public void updatePlayersPopeCards(int vaticanRelationId){
        VaticanRelation vaticanRelation = this.faithTrack.getVaticanRelations()[vaticanRelationId];
        for (int i=0; i<this.players.size(); i++){
            if (vaticanRelation.isInOrOver(this.players.get(i).getPosition())){
                this.players.get(i).getPopeFavorCards()[vaticanRelationId].toFaceUp();
            }
            else{
                this.players.get(i).getPopeFavorCards()[vaticanRelationId].discard();
            }

            this.notifyAllPlayer(new PopeFavourCardStateMessage(i+1, Arrays.stream(this.players.get(i).getPopeFavorCards()).map(PopeFavorCard::getState).toArray(PopeFavorCardState[]::new)));
        }
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

    public void closeAll(int id){
        FromServerMessage message = new DisconnectionMessage("Player disconnected", id);
        this.notifyAllPlayer(message);
        for (RealPlayer player : this.players){
            player.closeConnection();
        }
    }
}
