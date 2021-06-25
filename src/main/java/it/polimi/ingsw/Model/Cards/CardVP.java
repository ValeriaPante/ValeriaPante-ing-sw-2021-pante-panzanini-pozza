package it.polimi.ingsw.Model.Cards;

/**
 * Representation of a card with some victory points
 */
public abstract class CardVP {
    private final int victoryPoints;

    /**
     * Constructor
     * @param points of this card
     * @throws IllegalArgumentException if points < 0
     */
    public CardVP(int points) throws IllegalArgumentException{
        if (points < 0){
            throw new IllegalArgumentException();
        }
        this.victoryPoints = points;
    }

    /**
     * Getter
     * @return victory point of this card
     */
    public int getVictoryPoints(){
        return this.victoryPoints;
    }
}
