package it.polimi.ingsw.Model.Player;

/**
 * Super abstract representation of a player
 */
public abstract class Player {
    private int position;
    private final String nickname;

    /**
     * Constructor
     * @param nickname the nickname of the player
     */
    Player(String nickname){
        this.position = 0;
        this.nickname = nickname;
    }

    /**
     * Getter
     * @return the position of this player on the FaithTrack
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Getter
     * @return this player nickname
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * Increase the position of the player by amount
     * @param amount how much the player must be moved forward
     */
    public void moveForward(int amount){
        this.position += amount;
    }
}
