package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.*;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Network.Client.Messages.StartMessage;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;

import java.util.*;

/**
 * This class provides the basic tools for playing a game such as end turn, and tools for initializing the match
 */
public class GameController extends CertifiedResourceUsage{
    private Table table;
    private FaithTrackController faithTrackController;
    private final List<User> players;

    public GameController(Lobby lobby) throws NullPointerException, IndexOutOfBoundsException{
        if (lobby == null)
            throw new NullPointerException();

        if ((lobby.getUsers().size() > 4)||(lobby.getUsers().isEmpty()))
            throw new IndexOutOfBoundsException();

        this.players = lobby.getUsers();
        startGame();
    }

    /**
     *Creates all the objects necessary for playing a game
     */
    private void startGame(){
        this.table = new Table(players.size());
        this.faithTrackController = new FaithTrackController(table);

        //Players' playing order is random
        Collections.shuffle(players);
        for (User player : players)
            table.addPlayer(new RealPlayer(player));

        table.initLeaderCards();
    }

    /**
     * Getter for the Table
     * @return the table used in this precise game
     */
    public Table getTable(){
        return this.table;
    }

    /**
     * Getter for the faith track controller which is used in all the game controller to move players on the faith track
     * @return the faith track controller used in this precise match
     */
    public FaithTrackController getFaithTrackController(){
        return this.faithTrackController;
    }

    /**
     * Discards from player of turn's hand the leader card with the identifier number specified
     * (this method is only used during initialization). When the first round of discarding of initialization is over,
     * it sets the game up for the eventual initialization of resources in shelves and faith points
     * @param serial unique identifier for the card to discard
     */
    public void discardLeaderCard (int serial){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.DISCARD_LEADER_CARD)
            return;
        if (table.turnOf().getLeaderCards().length == 2)
            return;

        boolean ownCard = false;
        LeaderCard card = null;
        RealPlayer player = table.turnOf();

        for (LeaderCard lc : player.getLeaderCards())
            if (lc.getId() == serial){
                ownCard = true;
                card = lc;
            }

        if (!ownCard){
            table.turnOf().setErrorMessage("Wrong selection, you do not own a leader card with such id!");
            return;
        }

        player.discardLeaderCard(card);

        if (player.getLeaderCards().length == 2) {
            player.setMicroTurnType(MicroTurnType.CHOOSE_RESOURCES);

            table.nextTurn();

            if (table.turnOf() == table.getPlayers()[0]){
                table.turnOf().setMacroTurnType(MacroTurnType.NONE);
                table.turnOf().setMicroTurnType(MicroTurnType.NONE);
                if (table.isSinglePlayer())
                    table.turnOf().sendMessage(new StartMessage());
                table.nextTurn();
            }
        }
    }

    /**
     * Puts the resource passed as a parameter in the shelf specified
     * @param capacityShelf1 capacity of the target shelf
     * @param resType1 resource type of the resource to be added in the shelf
     *                 with capacity equals to the target capacity specified
     */
    public void selectResource(int capacityShelf1, Resource resType1){
        if (table.turnOf().getMicroTurnType() != MicroTurnType.CHOOSE_RESOURCES){
            table.turnOf().setErrorMessage("Invalid command");
            return;
        }

        if(!super.getLegalResource(resType1)){
            table.turnOf().setErrorMessage("Illegal resource type selected");
            return;
        }

        if ((capacityShelf1 > 3) || (capacityShelf1 < 1)){
            table.turnOf().setErrorMessage("Bad shelf selection");
            return;
        }


        if ((table.getPlayers().length == 4) && (table.turnOf() == table.getPlayers()[3])){
            int placedResources = 0;
            for (Shelf s : table.turnOf().getShelves())
                placedResources += s.getUsage();

            if (placedResources == 0){
                for (Shelf s : table.turnOf().getShelves())
                    if (s.getCapacity() == capacityShelf1)
                        s.singleAdd(resType1);
            } else {
                for (Shelf s : table.turnOf().getShelves())
                    if (s.getCapacity() == capacityShelf1){
                        if (s.isEmpty()) {
                            for (Shelf s1 : table.turnOf().getShelves())
                                if ((s1 != s) && (!s1.isEmpty())) {
                                    if (s1.getResourceType() == resType1){
                                        table.turnOf().setErrorMessage("Resource already contained in another shelf");
                                        return;
                                    }
                                }
                        } else {
                            if (s.getResourceType() != resType1){
                                table.turnOf().setErrorMessage("Cannot place the resource here");
                                return;
                            }

                            if (s.getCapacity() == 1){
                                table.turnOf().setErrorMessage("Selected shelf cannot contain that resource");
                                return;
                            }
                        }
                        table.addAllIfPossibleToShelf(s.getCapacity(), resType1,1);
                    }

                faithTrackController.movePlayerOfTurn(1);
                table.turnOf().setMacroTurnType(MacroTurnType.NONE);
                table.turnOf().setMicroTurnType(MicroTurnType.NONE);

                for (RealPlayer player : table.getPlayers())
                    player.sendMessage(new StartMessage());

                table.nextTurn();
            }
        } else {
            table.addAllIfPossibleToShelf(capacityShelf1, resType1, 1);

            if ((table.getPlayers().length > 2) && (table.turnOf() == table.getPlayers()[2]))
                faithTrackController.movePlayerOfTurn(1);
            table.turnOf().setMacroTurnType(MacroTurnType.NONE);
            table.turnOf().setMicroTurnType(MicroTurnType.NONE);

            if(table.getPlayers()[table.getPlayers().length - 1].equals(table.turnOf())){
                for (RealPlayer player : table.getPlayers())
                    player.sendMessage(new StartMessage());
            }
            table.nextTurn();
        }
    }

    /**
     * Changes turn and checks if the game endend;
     * in case the game is single-player, it also makes Lorenzo Il Magnifico's move
     */
    public void endTurn(){
        if (table.turnOf().getMacroTurnType() != MacroTurnType.DONE){
            table.turnOf().setErrorMessage("Cannot skip turn");
            return;
        }

        table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        table.turnOf().setMicroTurnType(MicroTurnType.NONE);
        table.nextTurn();
        if(table.isSinglePlayer() && !table.isLastLap()){
            playActionToken(table.drawToken());
            //msg (al single player): LorenzoTurnMessage(actionTokenType scartato)
            if(anEntireLineIsEmpty()){
                table.setLastLap();
            }
        }
        if(table.isSinglePlayer()){
            //MEGA DEBUG PLS LEVARE STA COSA
            table.addToPlayerOfTurnStrongbox(new EnumMap<>(Resource.class){{
                put(Resource.COIN, 50);
                put(Resource.SERVANT, 50);
                put(Resource.SHIELD, 50);
                put(Resource.STONE, 50);
            }});

            table.nextTurn();
        }
        if((table.isLastLap()) && (table.getPlayers()[0] == table.turnOf()))
            endGame();
    }

    /**
     * Finds the winner and sends it to the players
     */
    public void endGame(){
        ArrayList<Player> winners = new ArrayList<>();
        if(table.isSinglePlayer()){
            if(anEntireLineIsEmpty() || table.getFaithTrack().finished(table.getLorenzo().getPosition()))
                winners.add(table.getLorenzo());
            else{
                winners.add(table.getPlayers()[0]);
            }
        } else {
            int maxPoints = 0;
            int maxNumOfResources = 0;
            for(RealPlayer player: table.getPlayers()){
                int[] points = calculatePoints(player);
                if(points[0] > maxPoints || (points[0] == maxPoints && points[1] > maxNumOfResources)){
                    maxPoints = points[0];
                    maxNumOfResources = points[1];
                    winners.clear();
                    winners.add(player);
                } else if (points[0] == maxPoints && points[1] == maxNumOfResources){
                    winners.add(player);
                }
            }
        }
        table.addWinners(winners);
    }

    /**
     * Calculate the total points scored by a player and his total resources
     * @param player the player whose points are being calculated
     * @return array of integers containing the total points and the number of resources
     */
    private int[] calculatePoints(RealPlayer player){
        int sum = 0;

        for(DevSlot slot: player.getDevSlots())
            sum += slot.totalPoints();

        sum += table.getFaithTrack().victoryPoints(player.getPosition());

        for(PopeFavorCard card: player.getPopeFavorCards())
            if(card.getState() == PopeFavorCardState.FACEUP)
                sum += card.getVictoryPoints();

        int totalResources = player.getStrongBox().countAll();
        for(Shelf shelf: player.getShelves())
            totalResources += shelf.getUsage();

        for(LeaderCard card: player.getLeaderCards())
            if(card.hasBeenPlayed()){
                sum += card.getVictoryPoints();
                try{
                    totalResources += card.getAbility().countAll();
                } catch (WrongLeaderCardType e){

                }
            }
        sum += totalResources/5;
        return new int[]{sum, totalResources};
    }

    /**
     * Applies the effect of an Action Token
     * @param token Action Token that has been drawn
     */
    private void playActionToken(ActionToken token){
        switch (token.getType()){
            case TWOFP:
                faithTrackController.movePlayerOfTurn(2);
                break;
            case RESETDECKONEFP:
                faithTrackController.movePlayerOfTurn(1);
                table.getLorenzo().getActionTokenDeck().reset();
                break;
            case DISCARDGREEN:
                table.discardTwoDevCards(Colour.GREEN);
                break;
            case DISCARDYELLOW:
                table.discardTwoDevCards(Colour.YELLOW);
                break;
            case DISCARDBLUE:
                table.discardTwoDevCards(Colour.BLUE);
                break;
            case DISCARDPURPLE:
                table.discardTwoDevCards(Colour.PURPLE);
                break;
        }
    }

    /**
     * Checks if all the decks of the same color are empty
     * @return true if all the decks of the same color are empty, false otherwise
     */
    private boolean anEntireLineIsEmpty(){
        for(int i = 0; i < 4; i++){
            if (getLineOfDecks(i).isEmpty()) return true;
        }
        return false;
    }

    /**
     * Line of decks of the same color getter
     * @param color integer indicating a color
     * @return a list of decks of the same color
     */
    private ArrayList<DevDeck> getLineOfDecks(int color){
        ArrayList<DevDeck> lineOfDecks = new ArrayList<>();
        lineOfDecks.add(table.getDevDecks()[color]);
        lineOfDecks.add(table.getDevDecks()[color + 4]);
        lineOfDecks.add(table.getDevDecks()[color + 8]);
        return lineOfDecks;
    }
}