package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Enums.PopeFavorCardState;

public class PopeFavorCard extends CardVP {
    private PopeFavorCardState state;

    //costruttore, prende i punti di punti di quella particolare carta
    public PopeFavorCard(int points){
        super(points);
        this.state = PopeFavorCardState.FACEDOWN;
    }

    //getter dello stato
    public PopeFavorCardState getState(){
        return state;
    }

    //cambia lo stato della carta in DISABLED
    public void discard(){
        this.state = PopeFavorCardState.DISABLED;
    }

    //cambia lo stato della carta in FACEUP
    public void toFaceUp(){
        this.state = PopeFavorCardState.FACEUP;
    }
}

