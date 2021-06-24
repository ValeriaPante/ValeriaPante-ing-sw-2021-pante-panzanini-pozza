package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.PopeFavorCardState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplifiedFaithTrack {
    private final Map<Integer, Integer> idInternalIndexMapping;
    private final Map<Integer, Integer> positions;
    private final PopeFavorCardState[][] cardsState;

    public SimplifiedFaithTrack(List<Integer> playersID){
        this.cardsState = new PopeFavorCardState[playersID.size()][3];
        this.idInternalIndexMapping = new HashMap<>();
        this.positions = new HashMap<>();

        int i=0;
        for (int id : playersID){
            idInternalIndexMapping.put(id, i);
            positions.put(id,0);
            i++;
        }

        for (int j=0; j<playersID.size(); j++)
            for (int k=0; k<3; k++)
                cardsState[j][k] = PopeFavorCardState.FACEDOWN;
    }

    public synchronized void changePosition(int playerID, int newPosition){
        positions.put(playerID, newPosition);
    }

    public synchronized void changeState(int playerID, PopeFavorCardState[] newStates){
        cardsState[idInternalIndexMapping.get(playerID)] = newStates;
    }

    public synchronized int getPosition(int playerID){
        return positions.get(playerID);
    }

    public synchronized PopeFavorCardState[] getCardsState(int playerID){
        return cardsState[idInternalIndexMapping.get(playerID)];
    }
}
