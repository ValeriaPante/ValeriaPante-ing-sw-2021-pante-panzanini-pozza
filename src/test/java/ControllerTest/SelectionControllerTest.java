package ControllerTest;

import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.SelectionController;
import it.polimi.ingsw.Decks.LeaderDeck;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionControllerTest {
    SelectionController selectionController;

    @Test
    @DisplayName("leader cards test")
    public void testLC(){
        RealPlayer player = new RealPlayer("testingPlayer");
        Table table = new Table(1);
        table.addPlayer(player);
        selectionController = new SelectionController(new FaithTrackController(table));

        LeaderDeck deck = new LeaderDeck();
        deck.shuffle();
        LeaderCard pickedCard;
        LeaderCard[] arrayOfLC = new LeaderCard[4];
        int storageLC = 0;
        int notStorageLC = 0;
        while ((storageLC + notStorageLC) < 4){
            pickedCard = deck.draw();
            try{
                pickedCard.getAbility().getCapacity();
                if (storageLC < 2){
                    arrayOfLC[storageLC] = pickedCard;
                    storageLC ++;
                }
            } catch (WeDontDoSuchThingsHere e) {
                if (notStorageLC < 2){
                    arrayOfLC[notStorageLC + 2] = pickedCard;
                    notStorageLC ++;
                }
            }
        }

        player.addLeaderCard(arrayOfLC[0]);
        player.addLeaderCard(arrayOfLC[2]);
        arrayOfLC[0].play();
        int count = 0;
        LeaderCard[] playersLC = table.turnOf().getLeaderCards();
        for (LeaderCard lc : playersLC){
            if (lc == arrayOfLC[0]) {
                assertTrue(lc.hasBeenPlayed());
                count++;
            }
            else assertFalse(lc.hasBeenPlayed());
        }
        assertEquals(count, 1);
    }
}
