package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.Model.FaithTrack.VaticanRelation;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Player.RealPlayer;

import java.util.ArrayList;

public class FaithTrackController {
    private Table table;
    private FaithTrack faithTrack;

    //in base all posizione del giocatore ritorno una array di VaticanRelation che devono essere attivati
    //un giocatore si potrebbe spostare talmente avanti che ne attiva 2+ in una volta sola
    private VaticanRelation[] toActivate(VaticanRelation[] vaticanRelations, int playerPos){
        ArrayList<VaticanRelation> toActivate = new ArrayList<>();
        for (int i = 0; i<vaticanRelations.length && vaticanRelations[i].isOnPopePositionOrOver(playerPos); i++){
            if (!vaticanRelations[i].isAlreadyDone()){
                toActivate.add(vaticanRelations[i]);
            }
        }
        return toActivate.toArray(new VaticanRelation[0]);
    }

    //modifico la faithTrack associata a quella partita
    //e modifico le cate favore papale ai giocatori
    private void updateCardV2(VaticanRelation[] vaticanRelations, RealPlayer[] players){
        if (vaticanRelations.length == 0){
            return;
        }
        for (VaticanRelation vaticanRelation: vaticanRelations){
            this.table.getFaithTrack().doneVaticanRelation(vaticanRelation.getId());
            for (RealPlayer player : players){
                if (vaticanRelation.isInOrOver(player.getPosition())){
                    player.getPopeFavorCards()[vaticanRelation.getId()].toFaceUp();
                }
                else {
                    player.getPopeFavorCards()[vaticanRelation.getId()].discard();
                }
            }
        }
    }

    private boolean moveForward(Player player, int faithPoints){
        //need to check if player exceed the faith track length
        if (this.faithTrack.finished(player.getPosition() + faithPoints)) {
            player.moveForward(this.table.getFaithTrack().getLength() - player.getPosition());
            return true;
        }
        player.moveForward(faithPoints);
        return false;
    }

    public FaithTrackController(Table table){
        this.table = table;
        this.faithTrack = this.table.getFaithTrack();
    }

    public Table getTable(){
        return this.table;
    }

    public void movePlayerOfTurn(int faithPoints){
        VaticanRelation[] toActivate;
        if (this.table.isSinglePlayer() && this.table.isLorenzoTurn()){
            if (this.moveForward(this.table.getLorenzo(), faithPoints)){
                this.table.setLastLap();
            }
            toActivate = this.toActivate(this.table.getFaithTrack().getVaticanRelations(), this.table.getLorenzo().getPosition());
        }
        else {
            //partita multi or singleplayer (Non turno di lorenzo)
            if (this.moveForward(this.table.turnOf(), faithPoints)) {
                this.table.setLastLap();
            }
            toActivate = this.toActivate(this.table.getFaithTrack().getVaticanRelations(), this.table.turnOf().getPosition());
        }

        if (toActivate.length != 0){
            this.updateCardV2(toActivate, table.getPlayers());
        }
        if (this.table.isSinglePlayer() && this.table.isLastLap()){
            throw new GameOver();
        }
    }

    //move all players except the one in turn
    public void moveAllTheOthers(int faithPoints){
        VaticanRelation[] toActivate;
        if (this.table.isSinglePlayer()){
            if (!this.table.isLorenzoTurn()){
                if (this.moveForward(this.table.getLorenzo(), faithPoints)){
                    this.table.setLastLap();
                }
                //a questo punto lorenzo potrebbe far partire un rapporto in vaticano
                toActivate = this.toActivate(this.table.getFaithTrack().getVaticanRelations(), this.table.getLorenzo().getPosition());
                if (toActivate.length != 0){
                    this.updateCardV2(toActivate, table.getPlayers());
                }
                if (this.table.isLastLap()) {
                    throw new GameOver();
                }
            }
            else{
                //non è possibile arrivare qui per le regole del gioco
                //lorenzo non fa mai avanzare gli altri, sto egoista
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

            //ora tutti i giocatori hanno la posizione aggiornata
            for (Player player : table.getPlayers()){
                toActivate = this.toActivate(table.getFaithTrack().getVaticanRelations(), player.getPosition());
                if (toActivate.length != 0){
                    this.updateCardV2(toActivate, table.getPlayers());
                }
            }
        }
    }
}
