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

    private void startGame(){
        this.table = new Table(players.size());
        this.faithTrackController = new FaithTrackController(table);

        //Players' playing order is random
        Collections.shuffle(players);
        for (User player : players)
            table.addPlayer(new RealPlayer(player));

        table.initLeaderCards();
    }

    public Table getTable(){
        return this.table;
    }

    public FaithTrackController getFaithTrackController(){
        return this.faithTrackController;
    }

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
                else
                    table.nextTurn();
            }
        }
    }

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
                        s.singleAdd(resType1);
                    }

                faithTrackController.movePlayerOfTurn(1);
                table.turnOf().setMacroTurnType(MacroTurnType.NONE);
                table.turnOf().setMicroTurnType(MicroTurnType.NONE);

                for (RealPlayer player : table.getPlayers())
                    player.sendMessage(new StartMessage());

                table.nextTurn();
            }
        } else {
            for (Shelf s : table.turnOf().getShelves())
                if (s.getCapacity() == capacityShelf1)
                    s.singleAdd(resType1);

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
            //table.drawToken(); //ritorna un token
            playActionToken(table.getLorenzo().getActionTokenDeck().draw());
            //msg (al single player): LorenzoTurnMessage(actionTokenType scartato)
            if(anEntireLineIsEmpty()){
                table.setLastLap();
            }
        }
        if(table.isSinglePlayer()) table.nextTurn();
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
                discardDevCards(0);
                break;
            case DISCARDYELLOW:
                discardDevCards(1);
                break;
            case DISCARDBLUE:
                discardDevCards(2);
                break;
            case DISCARDPURPLE:
                discardDevCards(3);
                break;
        }
    }

    /**
     * Discards 2 development cards of a certain color
     * @param color integer indicating the color of the development cards to discard
     */
    private void discardDevCards(int color){
        //array di int [-1,-1,-1] top cards id
        int cardsToDiscard = 2;
        int level = 0;
        ArrayList<DevDeck> lineOfDecks = getLineOfDecks(color);
        while(cardsToDiscard > 0 && level < 3){
            if (lineOfDecks.get(level).size() > 1){
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
            } else if (lineOfDecks.get(level).size() == 1) {
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
                level++;
                //notifica cambiamento mazzetto
            } else {
                level++;
            }
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
    //--------------------------------------------------------------------------------------------
}