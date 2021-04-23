package ControllerTest;

import it.polimi.ingsw.Controller.ResourceMoverController;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceMoverControllerTest {
    private RealPlayer player;
    private ResourceMoverController controller;

    @Test
    @DisplayName("Swap testing")
    public void testSwap(){
        player = new RealPlayer("testingPlayer");
        for (Shelf s: player.getShelves()){
            if (s.getCapacity() == 1)
                s.singleAdd(Resource.SERVANT);

            if (s.getCapacity() == 2)
                s.addAllIfPossible(Resource.STONE, 2);

            if (s.getCapacity() == 3)
                s.addAllIfPossible(Resource.SHIELD, 3);
        }
    }
}
