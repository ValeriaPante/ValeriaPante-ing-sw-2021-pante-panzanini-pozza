package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Network.RequestHandlers.InGameRequestHandler;
import it.polimi.ingsw.PreGameModel.*;

import java.util.Random;

/**
 * Controller for the Pre game state: (creation lobby, changing lobby etc..)
 */
public class PreGameControllerSwitch {

    private final RemotePreGameModel preGameModel;
    private final Random random;

    /**
     * Constructor
     */
    public PreGameControllerSwitch(){
        this.preGameModel = new RemotePreGameModel();
        this.random = new Random();
    }

    /**
     * Creation of a new lobby
     * @param creationLobbyMessage message to evaluate
     */
    public synchronized void actionOnMessage(CreationLobbyMessage creationLobbyMessage){
        int id = random.nextInt(200);
        while (this.preGameModel.getAllLobbiesId().contains(id)){
            id = random.nextInt(200);
        }
        this.preGameModel.createLobby(id);
        User user = this.preGameModel.getAndRemoveUser(creationLobbyMessage.getSenderId());
        if (user == null){
            return;
        }
        this.preGameModel.addUserToLobby(user, id);
    }

    /**
     * User disconnection
     * @param disconnectMessage message to evaluate
     */
    public synchronized void actionOnMessage(DisconnectMessage disconnectMessage){
        User userDisconnected = this.preGameModel.getAndRemoveUser(disconnectMessage.getSenderId());
        if (userDisconnected != null){
            userDisconnected.closeConnection();
        }
    }

    /**
     * Moving from a lobby to another
     * @param moveToLobbyMessage message to evaluate
     */
    public synchronized void actionOnMessage(MoveToLobbyMessage moveToLobbyMessage){
        User user = this.preGameModel.getAndRemoveUser(moveToLobbyMessage.getSenderId());
        if (user==null){
            return;
        }
        if (this.preGameModel.isLobbyFull(moveToLobbyMessage.getLobbyId())){
            return;
        }
        this.preGameModel.addUserToLobby(user, moveToLobbyMessage.getLobbyId());
    }

    /**
     * Starting a game for a lobby
     * @param startGameMessage message to evaluate
     */
    public synchronized void actionOnMessage(StartGameMessage startGameMessage){
        int lobbyId = this.preGameModel.getUserLobbyId(startGameMessage.getSenderId());
        if (lobbyId == 0){
            this.preGameModel.notifyError(startGameMessage.getSenderId(), "You can't start the game, you are not the first");
            return;
        }
        Lobby lobby = this.preGameModel.getAndRemoveLobby(lobbyId);
        if (lobby == null){
            return;
        }

        new Thread(() ->{
            InGameRequestHandler inGameRequestHandler = new InGameRequestHandler(lobby);
            for (User user: lobby.getUsers()){
                user.setRequestHandler(inGameRequestHandler);
            }
        }).start();
    }

    /**
     * Adding a new user
     * @param user user to add
     */
    public synchronized void addNewUser(User user){
        int id = this.random.nextInt(100000);
        while (this.preGameModel.getAllUsersIds().contains(id)){
            id = this.random.nextInt(100000);
        }
        user.setId(id);
        this.preGameModel.addNewUser(user);
    }
}
