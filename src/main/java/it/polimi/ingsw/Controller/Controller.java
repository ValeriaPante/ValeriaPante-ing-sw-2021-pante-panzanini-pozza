package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Player.*;
import it.polimi.ingsw.Cards.*;

import java.util.EnumMap;
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
        switch(typeOfTurn) {
            case BUYNEWCARD:
                playBuyCardTurn(table.turnOf());
                break;
            case GETFROMMARKET:
                playMarketTurn(table.turnOf());
                break;
            case PRODUCITON:
                playProductionTurn(table.turnOf());
                break;
        }
    }

    private void playMarketTurn(RealPlayer playerOfTurn){
        boolean rowOrColumn;
        int number;
        EnumMap<Resource, Integer> stillToBeSet;

        //scegliere se riga o se colonna (suppongo boolean: 1 riga, 0 colonna)

        if (rowOrColumn){       //Sceglie riga
            stillToBeSet = table.getMarket().pickRow();
        }
        else{           //Sceglie colonna
            stillToBeSet = table.getMarket().pickColumn();
        }


    }

    private void playBuyCardTurn(RealPlayer playerOfTurn){

    }

    private void playProductionTurn(RealPlayer playerOfTurn) {

    }

    public void actionOnLeaderCard(RealPlayer playerOfTurn, LeaderCard leaderCardForAction, Boolean playOrDiscard){

    }
}