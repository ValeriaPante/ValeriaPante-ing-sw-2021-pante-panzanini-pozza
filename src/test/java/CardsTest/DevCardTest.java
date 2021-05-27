package CardsTest;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevCardTest {

    DevCard card;

    @BeforeEach
    public void init(){
        EnumMap<Resource, Integer> cost = new EnumMap<>(Resource.class);
        cost.put(Resource.SHIELD, 2);
        this.card = new DevCard(1, cost, new DevCardType(1, Colour.GREEN), new ProductionPower(
                new Resource[]  {Resource.COIN},
                new int[]       {1},
                new Resource[]  {Resource.FAITH},
                new int[]       {1}),1);
    }

    @Test
    public void checksCreation(){
        assertNotNull(card.getCost());
        assertNotNull(card.getType());
        assertNotNull(card.getProdPower());
    }

    @Test
    public void checksClone(){
        DevCard clonedCard = this.card.clone();
        assertTrue(this.card.equals(clonedCard));
    }
}