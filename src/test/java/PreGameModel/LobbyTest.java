package PreGameModel;

import ControllerTest.FakeConnectionHandler;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    private Lobby lobby;

    @BeforeEach
    @DisplayName("Init")
    private void init(){
        this.lobby = new Lobby(4);
    }

    @Test
    @DisplayName("getFirstUserId method test")
    void getFirstUserId() {
        User user1 = new User("A", new FakeConnectionHandler(55));
        User user2 = new User("B", new FakeConnectionHandler(44));
        this.lobby.addUser(user1);
        this.lobby.addUser(user2);
        assertEquals(55, this.lobby.getFirstUserId());
    }

    @Test
    @DisplayName("getId method test")
    void getId() {
        assertEquals(4, this.lobby.getId());
    }

    @Test
    @DisplayName("addUser method test")
    void addUser() {
        User user = new User("A", new FakeConnectionHandler(1));
        assertAll( () -> lobby.addUser(user));
        assertFalse(this.lobby.isEmpty());
    }

    @Test
    @DisplayName("removeUser method test")
    void removeUser() {
        User user = new User("A", new FakeConnectionHandler(1));
        this.lobby.addUser(user);
        assertAll(()->this.lobby.removeUser(user));
        assertTrue(this.lobby.isEmpty());

        //the user is not present -> nothing changes
        this.lobby.removeUser(user);
        assertTrue(this.lobby.isEmpty());
    }

    @Test
    @DisplayName("getUsers method test")
    void getUsers() {
        this.lobby.addUser(new User("A", new FakeConnectionHandler(1)));
        this.lobby.addUser(new User("B", new FakeConnectionHandler(2)));

        assertEquals(2, this.lobby.getUsers().size());
    }

    @Test
    @DisplayName("isFull method test")
    void isFull() {
        assertFalse(this.lobby.isFull());
    }

    @Test
    @DisplayName("isEmpty method test")
    void isEmpty() {
        assertTrue(this.lobby.isEmpty());
    }
}