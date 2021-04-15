package DecksTest;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Enums.Colour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevDeckTest {
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
        deck.selectCard(i);
        assertTrue(deck.getTopCard().isSelected());

        deck.selectCard(i);
        assertFalse(deck.getTopCard().isSelected());
    }

    @Test
    public void checksSelectingRemovedCard(){
        int i = deck.getTopCard().getId();
        deck.draw();

        assertThrows(IndexOutOfBoundsException.class, () -> deck.selectCard(i));
    }
}