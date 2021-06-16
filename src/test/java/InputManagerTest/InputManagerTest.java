package InputManagerTest;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.CreationLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.MoveToLobbyMessage;
import it.polimi.ingsw.View.CLI.InputManager;
import it.polimi.ingsw.View.ClientModel.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputManagerTest {
    @Test
    @DisplayName("content() test")
    public void test() {
        Game model = new Game();
        InputManager inputManager = new InputManager(model);
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("CREATE LOBBY")));
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("                 CREATE LOBBY          ")));
        assertDoesNotThrow(() -> assertInstanceOf(CreationLobbyMessage.class, inputManager.preGameInput("         crEate lObby          ")));
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("CREATE LBBY"));
        try{
            inputManager.preGameInput("CREATE LBBY");
        } catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "Your input was not correct! Please, retry...");
        }

        assertDoesNotThrow(() -> assertInstanceOf(MoveToLobbyMessage.class, inputManager.preGameInput("move to lobby 10")));
        MoveToLobbyMessage messageMTL = (MoveToLobbyMessage) inputManager.preGameInput("move to lobby 10");
        assertEquals(10, messageMTL.getLobbyId());
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("move to lobby a"));
        try{
            inputManager.preGameInput("move to lobby a");
        } catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "You entered a wrong integer! Please, retry...");
        }
        assertThrows(IllegalArgumentException.class, () -> inputManager.preGameInput("Throw exception"));
    }
}
