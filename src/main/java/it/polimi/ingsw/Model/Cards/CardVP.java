package it.polimi.ingsw.Model.Cards;

public abstract class CardVP {
    private final int victoryPoints;

    public CardVP(int points) throws IllegalArgumentException{
        if (points < 0){
            throw new IllegalArgumentException();
        }
        this.victoryPoints = points;
    }

    public int getVictoryPoints(){
        return this.victoryPoints;
    }
}
