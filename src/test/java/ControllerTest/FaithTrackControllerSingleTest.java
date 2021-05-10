package ControllerTest;

import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FaithTrackControllerSingleTest {

    private FaithTrackController faithTrackController;
    private Table table;

    @BeforeEach
    @DisplayName("Setup")
    public void init(){
        this.table = new Table(1){{
            addPlayer(new RealPlayer("A"));
        }};
        this.faithTrackController = new FaithTrackController(this.table);
    }

    @Test
    @DisplayName("Player move forward without relation being activated")
    public void movePlayerOfTurnCase1(){
        this.faithTrackController.movePlayerOfTurn(4);
        assertEquals(5, this.table.turnOf().getPosition());
        assertEquals(1, this.table.getLorenzo().getPosition());
    }

    @Test
    @DisplayName("Lorenzo move forward without relation being activated")
    public void movePlayerOfTurnCase2(){
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(4);
        assertEquals(1, this.table.turnOf().getPosition());
        assertEquals(5, this.table.getLorenzo().getPosition());
    }

    @Test
    @DisplayName("Lorenzo activates vatican relation and the player is inside that")
    public void movePlayerOfTurnCase3(){
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(8);

        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("Lorenzo activates vatican relation and the player is inside that")
    public void movePlayerOfTurnCase4(){
        //A -> 6
        this.faithTrackController.movePlayerOfTurn(5);

        //Lorenzo turn
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(7);

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("Lorenzo activates more than only one vatican relation and the player is inside the first")
    public void movePlayerOfTurnCase5(){
        //A -> 6
        this.table.getPlayers()[0].moveForward(5);

        //Lorenzo turn
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(16);

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("check that nothing happens if a specific vatican relation already occurred")
    public void movePlayerOfTurnCase6(){
        //A -> 6
        this.faithTrackController.movePlayerOfTurn(5);

        //Lorenzo Turn
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(7);

        this.checkCase6();

        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(3);

        this.checkCase6();
    }

    private void checkCase6(){
        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("activation of 1 vatican relations by Lorenzo and then another by the player")
    public void movePlayerOfTurnCase7(){
        //A -> 6
        this.faithTrackController.movePlayerOfTurn(5);

        //Lorenzo turn -> 9
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(8);

        //A turn -> 16
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(10);

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("Lorenzo reaches the end")
    public void movePlayerOfTurnCase8(){

        //Lorenzo turn -> 24
        this.table.nextTurn();
        assertThrows(GameOver.class, () ->this.faithTrackController.movePlayerOfTurn(24));

        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
        assertEquals(24, this.table.getLorenzo().getPosition());

        assertTrue(this.table.isLastLap());
    }

    @Test
    @DisplayName("Player reaches the end")
    public void movePlayerOfTurnCase9(){

        assertThrows(GameOver.class, ()-> this.faithTrackController.movePlayerOfTurn(24));

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
        assertEquals(24, this.table.turnOf().getPosition());

        assertTrue(this.table.isLastLap());
    }

    @Test
    @DisplayName("Player move all the others forward without relation being activated")
    public void moveAllTheOthersCase1(){
        this.faithTrackController.moveAllTheOthers(4);
        assertEquals(1, this.table.turnOf().getPosition());
        assertEquals(5, this.table.getLorenzo().getPosition());

        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());

    }

    //lorenzo never moves the others forward -> nothing changes
    @Test
    @DisplayName("Lorenzo move all the others forward")
    public void moveAllTheOthersCase2(){
        this.table.nextTurn();
        this.faithTrackController.moveAllTheOthers(4);
        assertEquals(1, this.table.turnOf().getPosition());
        assertEquals(1, this.table.getLorenzo().getPosition());
    }


    @Test
    @DisplayName("Lorenzo activates vatican relation and the player is inside that")
    public void moveAllTheOthersCase4(){
        //A -> 7
        this.faithTrackController.movePlayerOfTurn(6);
        //Lorenzo -> 6
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(5);
        //A turn
        this.table.nextTurn();
        this.faithTrackController.moveAllTheOthers(2);

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("Lorenzo activates more than only one vatican relation and the player is inside the first")
    public void moveAllTheOthersCase5(){
        //A -> 6
        this.faithTrackController.movePlayerOfTurn(5);
        this.faithTrackController.moveAllTheOthers(16);

        assertEquals(PopeFavorCardState.FACEUP, this.table.getPlayers()[0].getPopeFavorCards()[0].getState());
        assertEquals(PopeFavorCardState.DISABLED, this.table.getPlayers()[0].getPopeFavorCards()[1].getState());
        assertEquals(PopeFavorCardState.FACEDOWN, this.table.getPlayers()[0].getPopeFavorCards()[2].getState());
    }

    @Test
    @DisplayName("Lorenzo reaches the end")
    public void moveAllTheOthersCase6(){
        assertThrows(GameOver.class, ()-> this.faithTrackController.moveAllTheOthers(30));

        assertTrue(this.table.isLastLap());
    }
}
