package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.FaithTrack.VaticanRelation;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

public class FaithTrackController {
    private Table table;
    private FaithTrack faithTrack;

    /**
     * Position evaluator
     * @param playerPos position to evaluate
     * @return all the vatican relations that must be activated
     */
    private VaticanRelation[] toActivate(int playerPos){
        VaticanRelation[] vaticanRelations = this.table.getFaithTrack().getVaticanRelations();
        ArrayList<VaticanRelation> toActivate = new ArrayList<>();
        for (int i = 0; i<vaticanRelations.length && vaticanRelations[i].isOnPopePositionOrOver(playerPos); i++){
            if (!vaticanRelations[i].isAlreadyDone()){
                toActivate.add(vaticanRelations[i]);
            }
        }
        return toActivate.toArray(new VaticanRelation[0]);
    }

    /**
     * Updates the faith track and the players card based on which vatican relations are passed
     * @param vaticanRelations vatican relations to activate
     */
    private void updateCard(VaticanRelation[] vaticanRelations){
        if (vaticanRelations.length == 0){
            return;
        }
        for (VaticanRelation vaticanRelation: vaticanRelations){
            this.table.getFaithTrack().doneVaticanRelation(vaticanRelation.getId());
            this.table.updatePlayersPopeCards(vaticanRelation.getId());
        }
    }

    /**
     * Updates the player position on the Faith Track
     * @param player player to move
     * @param faithPoints amount to move
     * @return true is someone reached the end
     */
    private boolean moveForward(Player player, int faithPoints){
        if (this.faithTrack.finished(player.getPosition() + faithPoints)) {
            this.table.moveForwardOnFaithTrack(player.getId(), this.table.getFaithTrack().getLength() - player.getPosition());
            return true;
        }
        this.table.moveForwardOnFaithTrack(player.getId(), faithPoints);
        return false;
    }

    /**
     * Constructor
     * @param table Table associated to this controller
     */
    public FaithTrackController(Table table){
        this.table = table;
        this.faithTrack = this.table.getFaithTrack();
    }

    /**
     * Getter
     * @return the table associated to this controller
     */
    public Table getTable(){
        return this.table;
    }

    /**
     * Move the player of turn forward
     * Updates all the cards if needed
     * @param faithPoints amount to move forward
     */
    public void movePlayerOfTurn(int faithPoints){
        VaticanRelation[] toActivate;
        if (this.table.isSinglePlayer() && this.table.isLorenzoTurn()){
            if (this.moveForward(this.table.getLorenzo(), faithPoints)){
                this.table.setLastLap();
            }
            toActivate = this.toActivate(this.table.getLorenzo().getPosition());
        }
        else {
            if (this.moveForward(this.table.turnOf(), faithPoints)) {
                this.table.setLastLap();
            }
            toActivate = this.toActivate(this.table.turnOf().getPosition());
        }

        if (toActivate.length != 0){
            this.updateCard(toActivate);
        }
        if (this.table.isSinglePlayer() && this.table.isLastLap()){
            throw new GameOver();
        }
    }

    /**
     * Move all the player forward on the faith track except the player of turn
     * Updates all the cards if needed
     * @param faithPoints amount to move forward
     */
    public void moveAllTheOthers(int faithPoints){
        VaticanRelation[] toActivate;
        if (this.table.isSinglePlayer()){
            if (!this.table.isLorenzoTurn()){
                if (this.moveForward(this.table.getLorenzo(), faithPoints)){
                    this.table.setLastLap();
                }
                //a questo punto lorenzo potrebbe far partire un rapporto in vaticano
                toActivate = this.toActivate(this.table.getLorenzo().getPosition());
                if (toActivate.length != 0){
                    this.updateCard(toActivate);
                }
                if (this.table.isLastLap()) {
                    throw new GameOver();
                }
            }
        }
        else{
            //partita multi
            for (Player player : this.table.getPlayers()){
                if (player != table.turnOf()){
                    if (this.moveForward(player, faithPoints)){
                        table.setLastLap();
                    }
                }
            }

            for (Player player : table.getPlayers()){
                toActivate = this.toActivate(player.getPosition());
                if (toActivate.length != 0){
                    this.updateCard(toActivate);
                }
            }
        }
    }
}
