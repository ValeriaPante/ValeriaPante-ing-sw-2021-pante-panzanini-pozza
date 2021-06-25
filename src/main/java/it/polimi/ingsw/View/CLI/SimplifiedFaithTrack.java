package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.PopeFavorCardState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to represent the faith track client side, without any business rule
 */
public class SimplifiedFaithTrack {
    private final Map<Integer, Integer> idInternalIndexMapping;
    private final Map<Integer, Integer> positions;
    private final PopeFavorCardState[][] cardsState;

    /**
     * @param playersID is the list of the ids of the player, ordered in the same order the will play
     */
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

    /**
     * Changes the position of the player with "id" equals to "playerID"
     * @param playerID id of the player to be moved
     * @param newPosition new position of the player with id equals to playerID
     */
    public synchronized void changePosition(int playerID, int newPosition){
        positions.put(playerID, newPosition);
    }

    /**
     * Changes the state of the Pope favor card of the player with "id" equals to "playerID"
     * @param playerID id of the player target
     * @param newStates state of all the pope favor card of the player with "id" equals to "playerID",
     *                  ordered with the same order the player reaches the correspondent Pope favor area following the faith track
     */
    public synchronized void changeState(int playerID, PopeFavorCardState[] newStates){
        cardsState[idInternalIndexMapping.get(playerID)] = newStates;
    }

    /**
     * A request for the position of player with "id" equals to "playerID"
     * @param playerID id of the player target
     * @return the position on the faith track (if "0" it means that the player is still in the beginning position of the track) of target player
     */
    public synchronized int getPosition(int playerID){
        return positions.get(playerID);
    }

    /**
     * A request for the cards state of player with "id" equals to "playerID"
     * @param playerID id of the player target
     * @return the state of all the pope favor card of target player
     */
    public synchronized PopeFavorCardState[] getCardsState(int playerID){
        return cardsState[idInternalIndexMapping.get(playerID)];
    }
}
