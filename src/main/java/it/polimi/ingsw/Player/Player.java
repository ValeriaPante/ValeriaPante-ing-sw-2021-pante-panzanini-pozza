package it.polimi.ingsw.Player;

import it.polimi.ingsw.Cards.PopeFavorCard;
import java.util.Arrays;

public abstract class Player {
    private int position;
    private final String nickname;
    private PopeFavorCard[] popeFavorCards;

    private void initialisePopeFavorCards(){
        this.popeFavorCards = new PopeFavorCard[]{
                new PopeFavorCard(2),
                new PopeFavorCard(3),
                new PopeFavorCard(4),
        };
    }

    Player(String nickname){
        this.position = 0;
        this.nickname = nickname;
        this.initialisePopeFavorCards();
    }

    public int getPosition() {
        return this.position;
    }
    public String getNickname(){
        return this.nickname;
    }
    public PopeFavorCard[] getPopeFavorCards(){
        return Arrays.copyOf(this.popeFavorCards, this.popeFavorCards.length);
    }

    public void moveForward(int amount){
        this.position += amount;
    }
}
