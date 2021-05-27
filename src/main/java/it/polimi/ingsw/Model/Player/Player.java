package it.polimi.ingsw.Model.Player;

public abstract class Player {
    private int position;
    private final String nickname;

    Player(String nickname){
        this.position = 1;
        this.nickname = nickname;
    }

    public int getPosition() {
        return this.position;
    }
    public String getNickname(){
        return this.nickname;
    }

    public void moveForward(int amount){
        this.position += amount;
    }
}
