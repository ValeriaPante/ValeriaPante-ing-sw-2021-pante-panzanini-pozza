package PreGameModel;

import ControllerTest.FakeConnectionHandler;
import it.polimi.ingsw.PreGameModel.RemotePreGameModel;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RemotePreGameModelTest {

    private RemotePreGameModel preGameModel;

    @BeforeEach
    void init(){
        this.preGameModel = new RemotePreGameModel();
    }

    @Test
    @DisplayName("getAllUsersIds method test")
    void getAllUsersIds() {
        this.preGameModel.addNewUser(new User("A", new FakeConnectionHandler(){{setId(1);}}));
        this.preGameModel.addNewUser(new User("B", new FakeConnectionHandler(){{setId(2);}}));
        this.preGameModel.createLobby(1);
        this.preGameModel.addUserToLobby(new User("C", new FakeConnectionHandler(){{setId(3);}}) ,1);
        assertTrue(this.preGameModel.getAllUsersIds().contains(1) && this.preGameModel.getAllUsersIds().contains(2) && this.preGameModel.getAllUsersIds().contains(3));
    }

    @Test
    @DisplayName("getAllLobbiesId method test")
    void getAllLobbiesId() {
        this.preGameModel.createLobby(1);
        this.preGameModel.createLobby(2);
        this.preGameModel.createLobby(3);
        assertTrue(this.preGameModel.getAllLobbiesId().contains(1) && this.preGameModel.getAllLobbiesId().contains(2) && this.preGameModel.getAllLobbiesId().contains(3));
    }

    @Test
    @DisplayName("createLobby method test")
    void createLobby() {
        assertAll(() -> this.preGameModel.createLobby(15));
    }

    @Test
    @DisplayName("getUserLobbyId method test")
    void getUserLobbyId() {
        assertEquals(0,this.preGameModel.getUserLobbyId(2));
        this.preGameModel.createLobby(15);
        this.preGameModel.addUserToLobby(new User("A", new FakeConnectionHandler(){{setId(1);}}), 15);
        assertEquals(15, this.preGameModel.getUserLobbyId(1));
    }

    @Test
    @DisplayName("getAndRemoveLobby method test")
    void getAndRemoveLobby() {
        this.preGameModel.createLobby(1);
        assertTrue(this.preGameModel.getAllLobbiesId().contains(1));
        this.preGameModel.getAndRemoveLobby(1);
        assertFalse(this.preGameModel.getAllLobbiesId().contains(1));
    }

    @Test
    @DisplayName("getAndRemoveUser method test")
    void getAndRemoveUser() {
        assertNull(this.preGameModel.getAndRemoveUser(1));

        User user = new User("A", new FakeConnectionHandler(){{setId(14);}});
        this.preGameModel.addNewUser(user);
        assertTrue(this.preGameModel.getAllUsersIds().contains(14));
        this.preGameModel.getAndRemoveUser(14);
        assertFalse(this.preGameModel.getAllUsersIds().contains(14));

        this.preGameModel.createLobby(1);
        this.preGameModel.addUserToLobby(user, 1);
        assertTrue(this.preGameModel.getAllUsersIds().contains(14));
        this.preGameModel.getAndRemoveUser(14);
        assertFalse(this.preGameModel.getAllUsersIds().contains(14));
        assertFalse(this.preGameModel.getAllLobbiesId().contains(1));
    }

    @Test
    @DisplayName("isLobbyFull method test")
    void isLobbyFull() {
        this.preGameModel.createLobby(1);
        assertFalse(this.preGameModel.isLobbyFull(1));
        this.preGameModel.addUserToLobby(new User("A", new FakeConnectionHandler(){{setId(1);}}), 1);
        this.preGameModel.addUserToLobby(new User("B", new FakeConnectionHandler(){{setId(2);}}), 1);
        this.preGameModel.addUserToLobby(new User("C", new FakeConnectionHandler(){{setId(3);}}), 1);
        this.preGameModel.addUserToLobby(new User("D", new FakeConnectionHandler(){{setId(4);}}), 1);
        assertTrue(this.preGameModel.isLobbyFull(1));
    }

    @Test
    @DisplayName("addUserToLobby method test")
    void addUserToLobby() {
        this.preGameModel.createLobby(1);
        assertAll(() -> this.preGameModel.addUserToLobby(new User("A", new FakeConnectionHandler(){{setId(1);}}), 1));
    }

    @Test
    @DisplayName("addNewUser method test")
    void addNewUser() {
        User user = new User("A", new FakeConnectionHandler());
        assertAll(() -> this.preGameModel.addNewUser(user));
    }
}