package ControllerTest;

import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaithTrackControllerMultiTest {

    private FaithTrackController faithTrackController;
    private Table table;

    @BeforeEach
    @DisplayName("Setup")
    public void init(){
        this.table = new Table(4){{
            addPlayer(new RealPlayer("A"));
            addPlayer(new RealPlayer("B"));
            addPlayer(new RealPlayer("C"));
            addPlayer(new RealPlayer("D"));
        }};
        this.faithTrackController = new FaithTrackController(this.table);
    }

    @Test
    @DisplayName("Player of turn move forward without relation being activated")
    public void movePlayerOfTurnCase1(){
        this.faithTrackController.movePlayerOfTurn(4);

        assertEquals(this.table.turnOf().getPosition(), 5);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("B"):
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("Player of turn activates vatican relation and nobody is inside that")
    public void movePlayerOfTurnCase2(){
        this.faithTrackController.movePlayerOfTurn(6);
        //essendo in posizone n 6 non ha attivato nessun rapporto in vaticano

        //in questo momento lo fa partire
        this.faithTrackController.movePlayerOfTurn(2);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("player of turn activates vatican relation and someone is inside that")
    public void movePlayerOfTurnCase3(){
        //A -> 3
        this.table.getPlayers()[0].moveForward(2);
        //B -> 7
        this.table.getPlayers()[1].moveForward(6);
        //C -> 6
        this.table.getPlayers()[2].moveForward(5);
        //D -> 7
        this.table.getPlayers()[3].moveForward(6);

        //D turn
        this.table.nextTurn();
        this.table.nextTurn();
        this.table.nextTurn();

        this.faithTrackController.movePlayerOfTurn(2);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("player of turn activates more than only one vatican relation and someone is inside the first")
    public void movePlayerOfTurnCase4(){
        //A -> 6
        this.table.getPlayers()[0].moveForward(5);
        //B -> 7
        this.table.getPlayers()[1].moveForward(6);
        //C -> 3
        this.table.getPlayers()[2].moveForward(2);
        //D -> 7
        this.table.getPlayers()[3].moveForward(6);

        //B turn
        this.table.nextTurn();

        this.faithTrackController.movePlayerOfTurn(10);
        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("C"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("check that nothing happens if a specific vatican relation already occurred")
    public void movePlayerOfTurnCase5(){
        this.movePlayerOfTurnCase4();

        //D turn
        this.table.nextTurn();
        this.table.nextTurn();

        this.faithTrackController.movePlayerOfTurn(2);
        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("C"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    //forse questo è ridondante
    @Test
    @DisplayName("activation of 2 vatican relations by 2 different players")
    public void movePlayerOfTurnCase6(){
        //A -> 6
        this.table.getPlayers()[0].moveForward(5);
        //B -> 7
        this.table.getPlayers()[1].moveForward(6);
        //C -> 3
        this.table.getPlayers()[2].moveForward(2);
        //D -> 7
        this.table.getPlayers()[3].moveForward(6);

        //A turn -> A starts the first
        this.faithTrackController.movePlayerOfTurn(5);

        //B turn -> B inside the second
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(6);

        //C turn -> C activates the second
        this.table.nextTurn();
        this.faithTrackController.movePlayerOfTurn(18);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("C"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("Player of turn reaches the end")
    public void movePlayerOfTurnCase7(){
        this.faithTrackController.movePlayerOfTurn(30);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }

        assertEquals(24, this.table.turnOf().getPosition());
        assertTrue(this.table.isLastLap());
    }

    @Test
    @DisplayName("Everyone except playerOfTurn move forward without vatican relation being activated")
    public void moveAllTheOthersCase1(){
        this.faithTrackController.moveAllTheOthers(4);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                    assertEquals(1, player.getPosition());
                    break;
                case("B"):
                case("C"):
                case("D"):
                    assertEquals(5, player.getPosition());
                    break;
            }
        }
    }

    @Test
    @DisplayName("One of the other Players activates vatican relation and nobody is inside that")
    public void moveAllTheOthersCase2(){
        //A -> 2
        this.table.getPlayers()[0].moveForward(1);
        //B -> 7
        this.table.getPlayers()[1].moveForward(6);
        //C -> 3
        this.table.getPlayers()[2].moveForward(2);
        //D -> 3
        this.table.getPlayers()[3].moveForward(2);

        //C -> turn
        this.table.nextTurn();
        this.table.nextTurn();

        this.faithTrackController.moveAllTheOthers(1);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("One of the other Players activates vatican relation and someone is inside that")
    public void moveAllTheOthersCase3(){
        //A -> 2
        this.table.getPlayers()[0].moveForward(1);
        //B -> 6
        this.table.getPlayers()[1].moveForward(5);
        //C -> 4
        this.table.getPlayers()[2].moveForward(3);
        //D -> 4
        this.table.getPlayers()[3].moveForward(3);

        //D turn
        this.table.nextTurn();
        this.table.nextTurn();
        this.table.nextTurn();
        this.faithTrackController.moveAllTheOthers(2);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                case("D"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                case("C"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("One of the other Players activates more than only one vatican relation and someone is inside the first")
    public void moveAllTheOthersCase4(){
        //A -> 1
        //B -> 2
        this.table.getPlayers()[1].moveForward(1);
        //C -> 7
        this.table.getPlayers()[2].moveForward(6);
        //D -> 3
        this.table.getPlayers()[3].moveForward(2);

        //B turn
        this.table.nextTurn();
        this.faithTrackController.moveAllTheOthers(9);

        for (RealPlayer player : this.table.getPlayers()){
            switch (player.getNickname()){
                case("A"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("B"):
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
                case("C"):
                case("D"):
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                    assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[1].getState());
                    assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                    break;
            }
        }
    }

    @Test
    @DisplayName("check that nothing happens if a specific vatican relation already occurred")
    public void moveAllTheOthersCase5(){
        //A -> 6
        this.table.getPlayers()[0].moveForward(5);
        //B -> 2
        this.table.getPlayers()[1].moveForward(1);
        //C -> 3
        this.table.getPlayers()[2].moveForward(2);
        //D -> 3
        this.table.getPlayers()[3].moveForward(2);

        //C turn
        this.table.nextTurn();
        this.table.nextTurn();

        //A -> activates
        this.faithTrackController.moveAllTheOthers(2);

        for (RealPlayer player : this.table.getPlayers()){
            this.doubleCheck(player);
        }

        //C -> goes on the same pope space of A
        this.faithTrackController.movePlayerOfTurn(5);

        for (RealPlayer player : this.table.getPlayers()){
            this.doubleCheck(player);
        }
    }

    private void doubleCheck(RealPlayer player){
        switch (player.getNickname()){
            case("A"):
            case("D"):
                assertEquals(PopeFavorCardState.FACEUP, player.getPopeFavorCards()[0].getState());
                assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                break;
            case("B"):
            case("C"):
                assertEquals(PopeFavorCardState.DISABLED, player.getPopeFavorCards()[0].getState());
                assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[1].getState());
                assertEquals(PopeFavorCardState.FACEDOWN, player.getPopeFavorCards()[2].getState());
                break;
        }
    }

    @Test
    @DisplayName("One of the others reaches the end")
    public void moveAllTheOthersCase6(){
        //A -> 21
        this.table.getPlayers()[0].moveForward(20);

        //B turn
        this.table.nextTurn();
        this.faithTrackController.moveAllTheOthers(5);

        //A
        assertEquals(24, this.table.getPlayers()[0].getPosition());
        assertTrue(this.table.isLastLap());
    }
}
