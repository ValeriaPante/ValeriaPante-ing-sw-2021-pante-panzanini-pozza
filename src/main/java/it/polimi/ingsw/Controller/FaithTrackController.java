package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.PopeFavorCard;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.VaticanRelation;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.Player;

public class FaithTrackController {
    private static FaithTrackController instance;
    private final FaithTrack faithTrack;

    private void checkVaticanRelation(VaticanRelation vaticanRelation, Player[] players){
        if (vaticanRelation != null){
            vaticanRelation.done();

            for (Player player : players){
                this.updateCard(vaticanRelation, player);
            }
        }
    }

    private void updateCard(VaticanRelation vaticanRelation, Player player){
        PopeFavorCard popeFavorCard = player.getPopeFavorCards()[vaticanRelation.getId()];
        if (vaticanRelation.isInOrOver(player.getPosition())){
            popeFavorCard.toFaceUp();
        }
        else{
            popeFavorCard.discard();
        }
    }

    private boolean moveForward(Player player, int faithPoints){
        //need to check if player exceed the faith track length
        if (this.faithTrack.finished(player.getPosition() + faithPoints)) {
            player.moveForward(FaithTrack.length - player.getPosition());
            return true;
        }
        player.moveForward(faithPoints);
        return false;
    }

    private FaithTrackController(){
        this.faithTrack = FaithTrack.getInstance();
    }

    public static FaithTrackController getInstance(){
        if (instance == null){
            instance = new FaithTrackController();
        }
        return instance;
    }

    public void movePlayerOfTurn(Table table, int faithPoints){
        if (table.isSinglePlayer()){
            if (table.isLorenzoTurn()){
                if (this.moveForward(table.getLorenzo(), faithPoints)){
                    table.setLastLap();
                }
                VaticanRelation vaticanRelation =this.faithTrack.popeRelation(table.getLorenzo().getPosition());
                this.checkVaticanRelation(vaticanRelation, table.getPlayers());
            }
            else{
                if (this.moveForward(table.turnOf(), faithPoints)){
                    table.setLastLap();
                }
                //nella partita single player se il giocatore attiva il rapporto in vaticano
                //non cambia nulla per lorenzo
                VaticanRelation vaticanRelation = this.faithTrack.popeRelation(table.turnOf().getPosition());
                if (vaticanRelation != null){
                    //la ha attivata il giocatore stesso quindi modifico solo la carta senza fare ulteriori controlli
                    table.turnOf().getPopeFavorCards()[vaticanRelation.getId()].toFaceUp();
                }
            }
        }
        else{
            //partita multi
            if (this.moveForward(table.turnOf(), faithPoints)){
                table.setLastLap();
            }
            //controllo se ha attivato un rapporto in vaticano
            VaticanRelation vaticanRelation = this.faithTrack.popeRelation(table.turnOf().getPosition());
            this.checkVaticanRelation(vaticanRelation, table.getPlayers());
        }
    }

    //move all players except the one in turn
    public void moveAllTheOthers(Table table, int faithPoints){
        if (table.isSinglePlayer()){
            if (!table.isLorenzoTurn()){
                if (this.moveForward(table.getLorenzo(), faithPoints)){
                    table.setLastLap();
                }
                //a questo punto lorenzo potrebbe far partire un rapporto in vaticano
                VaticanRelation vaticanRelation = this.faithTrack.popeRelation(table.getLorenzo().getPosition());
                this.checkVaticanRelation(vaticanRelation, table.getPlayers());
            }
            else{
                //non è possibile arrivare qui per le regole del gioco
                //lorenzo non fa mai avanzare gli altri, sto egoista
            }
        }
        else{
            //partita multi

            for (Player player : table.getPlayers()){
                if (!(player == table.turnOf())){
                    if (this.moveForward(player, faithPoints)){
                        table.setLastLap();
                    }
                }
            }

            //ora tutti i giocatori hanno la posizione aggiornata
            for (Player player : table.getPlayers()){
                VaticanRelation vaticanRelation = this.faithTrack.popeRelation(player.getPosition());
                this.checkVaticanRelation(vaticanRelation, table.getPlayers());
            }
        }
    }
}
