package it.polimi.ingsw.Controller;

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

    public void playTurn(Player playerOfTurn){

    }

    private void chooseTurnType(TurnType typeOfTurn){

    }

    private void playMarketTurn(Player playerOfTurn){

    }

    private void playBuyCardTurn(Player playerOfTurn){

    }

    private void playProductionTurn(Player playerOfTurn) {

    }

    public void actionOnLeaderCard(Player playerOfTurn, LeaderCard leaderCardForAction, Boolean playOrDiscard){

    }
}
