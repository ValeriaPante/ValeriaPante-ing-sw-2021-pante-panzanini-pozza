package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Player.*;
import it.polimi.ingsw.Cards.*;

import java.util.List;

public class Controller {
    private Table table;
    private FaithTrack faithTrack;
    private List<String> players;

    public void addNewPlayer(String playerName){
        players.add(playerName);
    }

    public void removePlayer(String playerName){
        players.remove(playerName);
    }

    public void startGame() {

    }

    public void playTurn(RealPlayer playerOfTurn){

    }

    private void chooseTurnType(TurnType typeOfTurn){

    }

    private void playMarketTurn(RealPlayer playerOfTurn){

    }

    private void playBuyCardTurn(RealPlayer playerOfTurn){

    }

    private void playProductionTurn(RealPlayer playerOfTurn) {

    }

    public void actionOnLeaderCard(RealPlayer playerOfTurn, LeaderCard leaderCardForAction, Boolean playOrDiscard){

    }
}
