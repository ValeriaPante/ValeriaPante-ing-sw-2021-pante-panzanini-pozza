package DecksTest;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Enums.Colour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DevDeckTest {
    private DevDeck deck;

    @BeforeEach
    public void init(){
        deck = new DevDeck(new DevCardType(1, Colour.BLUE));
    }

    @Test
    public void drawsCorrectly(){
        DevCard topCard = deck.getTopCard();
        DevCard drawnCard = deck.draw();
        assertEquals(topCard.getType(), drawnCard.getType());
        assertEquals(topCard.getVictoryPoints(), drawnCard.getVictoryPoints());
        assertEquals(topCard.getCost(), drawnCard.getCost());
        assertEquals(topCard.getProdPower().toString(), drawnCard.getProdPower().toString());
    }

    @Test
    public void illegalArgument(){
        assertThrows(IllegalArgumentException.class, () -> new DevDeck(new DevCardType(5, Colour.BLUE)));
    }

    @Test
    public void checksSelect(){
        int i = deck.getTopCard().getId();
        deck.selectTopCard();
        assertTrue(deck.getTopCard().isSelected());

        deck.selectTopCard();
        assertFalse(deck.getTopCard().isSelected());
    }

    @Test
    public void checksSelectingEmptyDeck(){
        deck.draw();
        deck.draw();
        deck.draw();
        deck.draw();

        assertThrows(IndexOutOfBoundsException.class, () -> deck.selectTopCard());
    }
}