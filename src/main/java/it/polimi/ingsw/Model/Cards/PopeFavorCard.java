package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.PopeFavorCardState;

/**
 * Representation of a Pope Favour Card
 */
public class PopeFavorCard extends CardVP {
    private PopeFavorCardState state;

    /**
     * Constructor
     * @param points victory points of this specific pope favour card
     */
    public PopeFavorCard(int points){
        super(points);
        this.state = PopeFavorCardState.FACEDOWN;
    }

    /**
     * Getter
     * @return this card state
     */
    public PopeFavorCardState getState(){
        return state;
    }

    /**
     * Setter: sets this cars to DISABLED
     */
    public void discard(){
        this.state = PopeFavorCardState.DISABLED;
    }

    /**
     * Setter: sets this cars to FACEUP
     */
    public void toFaceUp(){
        this.state = PopeFavorCardState.FACEUP;
    }
}

