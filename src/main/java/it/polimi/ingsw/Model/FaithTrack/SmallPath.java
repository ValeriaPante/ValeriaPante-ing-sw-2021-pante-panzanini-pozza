package it.polimi.ingsw.Model.FaithTrack;

//rappresenta una porzione del percorso fede
class SmallPath {
    private final int posStart;
    private final int posEnd;
    private final int victoryPoints;

    //costruttore, victory points associati a questo particolare paercorso
    SmallPath(int posStart, int posEnd, int victoryPoints){
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.victoryPoints = victoryPoints;
    }

    //ritorna true sse l'intero che rappresenta la posizione passato come parametro
    // è interno a questo percorso
    boolean isIn(int pos){
        return posStart<=pos && pos<=posEnd;
    }

    //getter di victory points
    int getVictoryPoints(){
        return this.victoryPoints;
    }
}