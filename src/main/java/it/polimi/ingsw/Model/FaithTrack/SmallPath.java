package it.polimi.ingsw.Model.FaithTrack;

//rappresenta una porzione del percorso fede

/**
 * Represent a little segment of the FaithTrack
 */
class SmallPath {
    private final int posStart;
    private final int posEnd;
    private final int victoryPoints;

    /**
     * SmallPath constructor
     * @param posStart start position (inclusive)
     * @param posEnd end position (inclusive)
     * @param victoryPoints victory points associates to this SmallPath
     */
    public SmallPath(int posStart, int posEnd, int victoryPoints){
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.victoryPoints = victoryPoints;
    }

    /**
     * Evaluator of a position
     * @param pos position to evaluate
     * @return true if inside this SmallPath
     */
    boolean isIn(int pos){
        return posStart<=pos && pos<=posEnd;
    }

    /**
     * Getter
     * @return the victory points associates to this SmallPath
     */
    int getVictoryPoints(){
        return this.victoryPoints;
    }
}