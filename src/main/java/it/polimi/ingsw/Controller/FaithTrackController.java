package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.PopeFavorCard;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.VaticanRelation;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.Player;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.ArrayList;

public class FaithTrackController {
    private static FaithTrackController instance;

    //in base all posizione del giocatore ritorno una array di VaticanRelation che devono essere attivati
    //un giocatore si potrebbe spostare talmente avanti che ne attiva 2+ in una volta sola
    private VaticanRelation[] toActivate(VaticanRelation[] vaticanRelations, int playerPos){
        ArrayList<VaticanRelation> toActivate = new ArrayList<>();
        for (int i = 0; vaticanRelations[i].isOnPopePositionOrOver(playerPos); i++){
            if (!vaticanRelations[i].isAlreadyDone()){
                toActivate.add(vaticanRelations[i]);
            }
        }
        return toActivate.toArray(new VaticanRelation[0]);
    }

    //modifico la faithTrack associata a quella partita
    //e modifico le cate favore papale ai giocatori
    private void updateCardV2(VaticanRelation[] vaticanRelations, FaithTrack faithTrack, RealPlayer[] players){
        if (vaticanRelations.length == 0){
            return;
        }
        for (VaticanRelation vaticanRelation: vaticanRelations){
            faithTrack.doneVaticanRelation(vaticanRelation.getId());
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

    private boolean moveForward(Player player, int faithPoints, FaithTrack faithTrack){
        //need to check if player exceed the faith track length
        if (faithTrack.finished(player.getPosition() + faithPoints)) {
            player.moveForward(faithTrack.getLength() - player.getPosition());
            return true;
        }
        player.moveForward(faithPoints);
        return false;
    }

    private FaithTrackController(){
    }

    public static FaithTrackController getInstance(){
        if (instance == null){
            instance = new FaithTrackController();
        }
        return instance;
    }

    public void movePlayerOfTurn(Table table, int faithPoints){
        VaticanRelation[] toActivate;
        if (table.isSinglePlayer() && table.isLorenzoTurn()){
            if (this.moveForward(table.getLorenzo(), faithPoints, table.getFaithTrack())){
                table.setLastLap();
            }
            toActivate = this.toActivate(table.getFaithTrack().getVaticanRelations(), table.getLorenzo().getPosition());
        }
        else {
            //partita multi or singleplayer (Non turno di lorenzo)
            if (this.moveForward(table.turnOf(), faithPoints, table.getFaithTrack())) {
                table.setLastLap();
            }
            toActivate = this.toActivate(table.getFaithTrack().getVaticanRelations(), table.turnOf().getPosition());
        }

        if (toActivate.length != 0){
            this.updateCardV2(toActivate, table.getFaithTrack(), table.getPlayers());
        }
    }

    //move all players except the one in turn
    public void moveAllTheOthers(Table table, int faithPoints){
        VaticanRelation[] toActivate;
        if (table.isSinglePlayer()){
            if (!table.isLorenzoTurn()){
                if (this.moveForward(table.getLorenzo(), faithPoints, table.getFaithTrack())){
                    table.setLastLap();
                }
                //a questo punto lorenzo potrebbe far partire un rapporto in vaticano
                toActivate = this.toActivate(table.getFaithTrack().getVaticanRelations(), table.getLorenzo().getPosition());
                if (toActivate.length != 0){
                    this.updateCardV2(toActivate, table.getFaithTrack(), table.getPlayers());
                }
            }
            else{
                //non è possibile arrivare qui per le regole del gioco
                //lorenzo non fa mai avanzare gli altri, sto egoista
            }
        }
        else{
            //partita multi
            for (Player player : table.getPlayers()){
                if (player != table.turnOf()){
                    if (this.moveForward(player, faithPoints, table.getFaithTrack())){
                        table.setLastLap();
                    }
                }
            }

            //ora tutti i giocatori hanno la posizione aggiornata
            for (Player player : table.getPlayers()){
                toActivate = this.toActivate(table.getFaithTrack().getVaticanRelations(), player.getPosition());
                if (toActivate.length != 0){
                    this.updateCardV2(toActivate, table.getFaithTrack(), table.getPlayers());
                }
            }
        }
    }
}
