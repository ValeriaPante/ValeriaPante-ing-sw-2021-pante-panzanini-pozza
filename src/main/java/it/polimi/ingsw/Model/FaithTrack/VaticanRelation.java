package it.polimi.ingsw.Model.FaithTrack;

/**
 * Represent a Vatican Relation on the FaithTrack
 */
public class VaticanRelation implements Cloneable{
    private final int id;
    private final int posStart;
    private final int posPope;
    private boolean alreadyDone;

    /**
     * VaticanRelation constructor
     * @param start start position (inclusive)
     * @param posPope end/pope position (inclusive)
     * @param id represent the order
     */
    public VaticanRelation(int start, int posPope, int id){
        this.id = id;
        this.posStart = start;
        this.posPope = posPope;
        this.alreadyDone = false;
    }

    /**
     * Getter
     * @return the id of this VaticanRelation
     */
    public int getId(){
        return id;
    }

    /**
     * Evaluator of a position
     * @param pos position to evaluate
     * @return true if inside or over this VaticanRelation
     */
    public boolean isInOrOver(int pos){
        return pos>=posStart;
    }

    /**
     * Evaluator of a position
     * @param pos position to evaluate
     * @return true if on the Pope position or over
     */
    public boolean isOnPopePositionOrOver(int pos){
        return pos>= posPope;
    }

    /**
     * Getter
     * @return true if this VaticanRelation is done
     */
    public boolean isAlreadyDone(){
        return alreadyDone;
    }

    /**
     * Sets this Vatican Relation as done
     */
    public void done(){
        this.alreadyDone = true;
    }

    /**
     * Clones this Vatican Relation
     * @return a copy of this Vatican Relation
     */
    @Override
    public VaticanRelation clone(){
        VaticanRelation clone = new VaticanRelation(this.posStart, this.posPope, this.id);
        if (this.alreadyDone){
            clone.alreadyDone = true;
        }
        return clone;
    }
}
