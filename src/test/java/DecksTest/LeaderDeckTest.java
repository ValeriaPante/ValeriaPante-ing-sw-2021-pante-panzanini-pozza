package DecksTest;

import it.polimi.ingsw.Model.Decks.LeaderDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LeaderDeckTest {

    @Test
    public void drawingEmptyDeck(){
        LeaderDeck deck = new LeaderDeck();
        for(int i = 0; i < 16; i++)
            deck.draw();
        assertThrows(IndexOutOfBoundsException.class, deck::draw);
    }

}