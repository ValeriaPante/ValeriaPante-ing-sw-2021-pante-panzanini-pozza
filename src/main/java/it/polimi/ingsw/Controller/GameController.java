package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Decks.LeaderDeck;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Enums.*;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Player.*;
import it.polimi.ingsw.Cards.*;

import java.util.*;

public class GameController {
    private Table table;
    private FaithTrackController faithTrackController;
    private final List<String> players;

    public GameController(){
        this.players = new ArrayList<>();
    }

    public void addNewPlayer(String playerName){
        players.add(playerName);
    }

    public void removePlayer(String playerName){
        players.remove(playerName);
    }



    private void initializePlayersLeaderCard(){
        LeaderDeck leaderDeck = new LeaderDeck();
        leaderDeck.shuffle();
        RealPlayer[] lisOfPlayers = table.getPlayers();

        for (RealPlayer player : lisOfPlayers)
                for (int i=0; i<4; i++)
                    player.addLeaderCard(leaderDeck.draw());

        waitForDiscarding();
    }

    //I need a way to receive in input the card that the player want to discard
    private void waitForDiscarding(){
        int totalNumberOfLCs;
        RealPlayer[] listOfPlayers = table.getPlayers();
        do{
            totalNumberOfLCs = 0;
            for (RealPlayer player : listOfPlayers)
                totalNumberOfLCs += player.getLeaderCards().length;
        } while(totalNumberOfLCs > ((listOfPlayers.length) * 2) );
    }

    private void initializePlayersResources(){
        RealPlayer[] listOfPlayers = table.getPlayers();

        if(listOfPlayers.length > 2)
            for(int i=2; i < listOfPlayers.length; i++)
                listOfPlayers[i].moveForward(1);

        for(int i=1; i < listOfPlayers.length; i++)
            setInitialResources(listOfPlayers[i], (i == 3) ? 2 : 1);
    }

    private void setInitialResources(RealPlayer player, int numberOfResources){
        Scanner input = new Scanner(System.in);

        //scelta delle risorse

        //posizionamento delle risorse nelle shelf

    }

    public void startGame() {
        this.table = new Table(players.size());
        this.faithTrackController = new FaithTrackController(this.table);

        Collections.shuffle(players);  //Players' playing order is random
            for (String nickName : players)
                table.addPlayer(new RealPlayer(nickName));

        initializePlayersLeaderCard();

        //assegnamento delle risorse ai giocatori
        if (!table.isSinglePlayer())
            initializePlayersResources();

    }



    //End turn
    //--------------------------------------------------------------------------------------------
    public void endTurn(){
        table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        table.turnOf().setMicroTurnType(MicroTurnType.NONE);
        if(table.isSinglePlayer()){
            table.nextTurn();
            if(anEntireLineIsEmpty()){
                table.setLastLap();
            } else {
                playActionToken(table.getLorenzo().getActionTokenDeck().draw());
                if(anEntireLineIsEmpty()){
                    table.setLastLap();
                }
            }
        }
        table.nextTurn();
        if((table.isLastLap()) && (table.getPlayers()[0] == table.turnOf()))
            endGame();
    }

    private void endGame(){
        if(table.isSinglePlayer()){
            if(anEntireLineIsEmpty() || table.getFaithTrack().finished(table.getLorenzo().getPosition()))
                table.addWinner(table.getLorenzo());
            else{
                table.addWinner(table.getPlayers()[0]);
                calculatePoints(table.getPlayers()[0]); //dove li memorizziamo i punti fatti?
            }
        } else {
            int maxPoints = 0;
            int maxNumOfResources = 0;
            for(RealPlayer player: table.getPlayers()){
                int[] points = calculatePoints(player);
                if(points[0] > maxPoints || (points[0] == maxPoints && points[1] > maxNumOfResources)){
                    maxPoints = points[0];
                    maxNumOfResources = points[1];
                    table.clearWinners();
                    table.addWinner(player);
                } else if (points[0] == maxPoints && points[1] > maxNumOfResources){
                    table.addWinner(player);
                }
            }
        }
    }

    private int[] calculatePoints(RealPlayer player){
        int sum = 0;

        //victory points from dev card
        for(DevSlot slot: player.getDevSlots())
            sum += slot.totalPoints();

        //victory points from faith track
        sum += table.getFaithTrack().victoryPoints(player.getPosition());

        //vp from pope favor cards
        for(PopeFavorCard card: player.getPopeFavorCards())
            if(card.getState() == PopeFavorCardState.FACEUP)
                sum += card.getVictoryPoints();

        //vp from resources
        int totalResources = player.getStrongBox().countAll();
        for(Shelf shelf: player.getShelves())
            totalResources += shelf.getUsage();

        //vp from leader cards
        for(LeaderCard card: player.getLeaderCards())
            if(card.hasBeenPlayed()){
                sum += card.getVictoryPoints();
                try{
                    totalResources += card.getAbility().countAll();
                } catch (WeDontDoSuchThingsHere e){

                }
            }
        sum += totalResources/5;
        return new int[]{sum, totalResources};
    }

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

    private void discardDevCards(int color){
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
            } else {
                level++;
            }
        }
    }

    private boolean anEntireLineIsEmpty(){
        for(int i = 0; i < 4; i++){
            if (getLineOfDecks(i).isEmpty()) return true;
        }
        return false;
    }

    private ArrayList<DevDeck> getLineOfDecks(int color){
        ArrayList<DevDeck> lineOfDecks = new ArrayList<>();
        lineOfDecks.add(table.getDevDecks()[color]);
        lineOfDecks.add(table.getDevDecks()[color + 4]);
        lineOfDecks.add(table.getDevDecks()[color + 8]);
        return lineOfDecks;
    }
    //--------------------------------------------------------------------------------------------
}