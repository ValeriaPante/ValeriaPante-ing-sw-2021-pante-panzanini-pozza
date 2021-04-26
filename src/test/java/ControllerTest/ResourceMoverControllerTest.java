package ControllerTest;

import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Controller.ResourceMoverController;
import it.polimi.ingsw.Decks.LeaderDeck;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceMoverControllerTest {
    private RealPlayer player;
    private ResourceMoverController controller;

    @Test
    @DisplayName("Swap testing 2L")
    public void testSwap2L(){
        player = new RealPlayer("testingPlayer");

        LeaderDeck deck = new LeaderDeck();
        deck.shuffle();
        int numOfCards = 0;
        LeaderCard card;
        while (numOfCards < 2){
            card = deck.draw();
            try{
                card.getAbility().getCapacity();
                player.addLeaderCard(card);
                numOfCards++;
            } catch (WeDontDoSuchThingsHere ignored){}
        }

        Shelf[] arrayOfShelves = new Shelf[3];
        for (Shelf s: player.getShelves()){
            if (s.getCapacity() == 1) {
                s.singleAdd(Resource.SERVANT);
                arrayOfShelves[0] = s;
            }

            if (s.getCapacity() == 2) {
                s.addAllIfPossible(Resource.STONE, 2);
                arrayOfShelves[1] = s;
            }

            if (s.getCapacity() == 3){
                s.addAllIfPossible(Resource.SHIELD, 3);
                arrayOfShelves[2] = s;
            }
        }

        controller = new ResourceMoverController();
        controller.update(player, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 2);
        }});

        // <----
        for (Shelf s: player.getShelves()){
            if (s.getCapacity() == 3){
                s.singleSelection();
                s.pay();
            }
        }

        for (Shelf s: player.getShelves()){
        if (s.getCapacity() == 2)
                s.multiSelection(2);

            if (s.getCapacity() == 3)
                s.multiSelection(2);
        }

        controller.exchange();

        assertEquals();
    }

    @Test
    @DisplayName("Swap testing 0L")
    public void testSwap0L(){
        player = new RealPlayer("testingPlayer");

        LeaderDeck deck = new LeaderDeck();
        deck.shuffle();
        int numOfCards = 0;
        LeaderCard card;
        while (numOfCards < 2){
            card = deck.draw();
            try{
                card.getAbility().getCapacity();
            } catch (WeDontDoSuchThingsHere e){
                player.addLeaderCard(card);
                numOfCards++;
            }
        }

        Shelf[] arrayOfShelves = new Shelf[3];
        for (Shelf s: player.getShelves()){
            if (s.getCapacity() == 1) {
                s.singleAdd(Resource.SERVANT);
                arrayOfShelves[0] = s;
            }

            if (s.getCapacity() == 2) {
                s.addAllIfPossible(Resource.STONE, 2);
                arrayOfShelves[1] = s;
            }

            if (s.getCapacity() == 3){
                s.addAllIfPossible(Resource.SHIELD, 3);
                arrayOfShelves[2] = s;
            }
        }

        controller = new ResourceMoverController();
        controller.update(player, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 2);
        }});


    }

    @Test
    @DisplayName("Swap testing 1L")
    public void testSwap1L(){
        player = new RealPlayer("testingPlayer");

        LeaderDeck deck = new LeaderDeck();
        deck.shuffle();
        int numOfCards = 0;
        int numOfLeaderCards = 0;
        LeaderCard card;
        while ((numOfCards < 1) || (numOfLeaderCards < 1)){
            card = deck.draw();
            try{
                if (numOfLeaderCards == 0){
                    card.getAbility().getCapacity();
                    player.addLeaderCard(card);
                    numOfLeaderCards++;
                }
            } catch (WeDontDoSuchThingsHere e){
                if (numOfCards == 0) {
                    player.addLeaderCard(card);
                    numOfCards++;
                }
            }
        }

        Shelf[] arrayOfShelves = new Shelf[3];
        for (Shelf s: player.getShelves()){
            if (s.getCapacity() == 1) {
                s.singleAdd(Resource.SERVANT);
                arrayOfShelves[0] = s;
            }

            if (s.getCapacity() == 2) {
                s.addAllIfPossible(Resource.STONE, 2);
                arrayOfShelves[1] = s;
            }

            if (s.getCapacity() == 3){
                s.addAllIfPossible(Resource.SHIELD, 3);
                arrayOfShelves[2] = s;
            }
        }

        controller = new ResourceMoverController();
        controller.update(player, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 2);
            put(Resource.SHIELD, 2);
        }});


    }
}
