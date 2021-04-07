package it.polimi.ingsw.Decks;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.util.*;

public class LeaderDeck implements Deck{
    private List<LeaderCard> deck;

    public LeaderDeck(){
        deck = new ArrayList<>();

        //49
        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        input.put(Resource.SERVANT, 1);
        deck.add(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input));

        //50
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.BLUE), 1);
        devCardReq.put(new DevCardType(0, Colour.PURPLE), 1);
        input.clear();
        input.put(Resource.SHIELD, 1);
        deck.add(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input));

        //51
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        devCardReq.put(new DevCardType(0, Colour.BLUE), 1);
        input.clear();
        input.put(Resource.STONE, 1);
        deck.add(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input));

        //52
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        devCardReq.put(new DevCardType(0, Colour.PURPLE), 1);
        input.clear();
        input.put(Resource.COIN, 1);
        deck.add(new LeaderCard(2, resourceReq, devCardReq, LeaderCardType.DISCOUNT, input));

        //53
        resourceReq.put(Resource.COIN, 5);
        devCardReq.clear();
        input.clear();
        input.put(Resource.STONE, 2);
        deck.add(new LeaderCard(3, resourceReq, devCardReq, LeaderCardType.STORAGE, input));

        //54
        resourceReq.clear();
        resourceReq.put(Resource.STONE, 5);
        input.clear();
        input.put(Resource.SERVANT, 2);
        deck.add(new LeaderCard(3, resourceReq, devCardReq, LeaderCardType.STORAGE, input));

        //55
        resourceReq.clear();
        resourceReq.put(Resource.SERVANT, 5);
        input.clear();
        input.put(Resource.SHIELD, 2);
        deck.add(new LeaderCard(3, resourceReq, devCardReq, LeaderCardType.STORAGE, input));

        //56
        resourceReq.clear();
        resourceReq.put(Resource.SHIELD, 5);
        input.clear();
        input.put(Resource.COIN, 2);
        deck.add(new LeaderCard(3, resourceReq, devCardReq, LeaderCardType.STORAGE, input));

        //57
        resourceReq.clear();
        devCardReq.put(new DevCardType(0, Colour.YELLOW), 2);
        devCardReq.put(new DevCardType(0, Colour.BLUE), 1);
        input.clear();
        input.put(Resource.SERVANT, 1);
        deck.add(new LeaderCard(5, resourceReq, devCardReq, LeaderCardType.TRANSMUTATION, input));

        //58
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.GREEN), 2);
        devCardReq.put(new DevCardType(0, Colour.PURPLE), 1);
        input.clear();
        input.put(Resource.SHIELD, 1);
        deck.add(new LeaderCard(5, resourceReq, devCardReq, LeaderCardType.TRANSMUTATION, input));

        //59
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.BLUE), 2);
        devCardReq.put(new DevCardType(0, Colour.YELLOW), 1);
        input.clear();
        input.put(Resource.STONE, 1);
        deck.add(new LeaderCard(5, resourceReq, devCardReq, LeaderCardType.TRANSMUTATION, input));

        //60
        devCardReq.clear();
        devCardReq.put(new DevCardType(0, Colour.PURPLE), 2);
        devCardReq.put(new DevCardType(0, Colour.GREEN), 1);
        input.clear();
        input.put(Resource.COIN, 1);
        deck.add(new LeaderCard(5, resourceReq, devCardReq, LeaderCardType.TRANSMUTATION, input));

        //61
        devCardReq.clear();
        devCardReq.put(new DevCardType(2, Colour.YELLOW), 1);
        input.clear();
        input.put(Resource.SHIELD, 1);
        deck.add(new LeaderCard(4, resourceReq, devCardReq, LeaderCardType.PRODPOWER, input));

        //62
        devCardReq.clear();
        devCardReq.put(new DevCardType(2, Colour.BLUE), 1);
        input.clear();
        input.put(Resource.SERVANT, 1);
        deck.add(new LeaderCard(4, resourceReq, devCardReq, LeaderCardType.PRODPOWER, input));

        //63
        devCardReq.clear();
        devCardReq.put(new DevCardType(2, Colour.PURPLE), 1);
        input.clear();
        input.put(Resource.STONE, 1);
        deck.add(new LeaderCard(4, resourceReq, devCardReq, LeaderCardType.PRODPOWER, input));

        //64
        devCardReq.clear();
        devCardReq.put(new DevCardType(2, Colour.GREEN), 1);
        input.clear();
        input.put(Resource.COIN, 1);
        deck.add(new LeaderCard(4, resourceReq, devCardReq, LeaderCardType.PRODPOWER, input));

        Collections.shuffle(deck);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public LeaderCard draw() {
        return deck.remove(0);
    }
}
