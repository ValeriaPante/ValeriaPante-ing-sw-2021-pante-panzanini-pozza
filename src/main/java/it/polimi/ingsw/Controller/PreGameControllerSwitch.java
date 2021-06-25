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
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        this.preGameModel.addUserToLobby(user, id);
    }

    /**
     * User disconnection
     * @param disconnectMessage message to evaluate
     */
    public synchronized void actionOnMessage(DisconnectMessage disconnectMessage){
        this.preGameModel.getAndRemoveUser(disconnectMessage.getSenderId());
    }

    /**
     * Moving from a lobby to another
     * @param moveToLobbyMessage message to evaluate
     */
    public synchronized void actionOnMessage(MoveToLobbyMessage moveToLobbyMessage){
        User user = this.preGameModel.getAndRemoveUser(moveToLobbyMessage.getSenderId());
        if (user==null){
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        if (this.preGameModel.isLobbyFull(moveToLobbyMessage.getLobbyId())){
            //notifica il player che ha provato ad accedere ad una lobby piena sbagliato
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
            //notifica il player che ha provato a fare una cosa non permessa
            return;
        }
        Lobby lobby = this.preGameModel.getAndRemoveLobby(lobbyId);
        if (lobby == null){
            //stranissimo, vuol dire che il giocatore ha richiesto l'inizio della partita
            //di una lobby in cui era presente prima ma ora la lobby non esiste più;
            return;
        }

        new Thread(() ->{
            //a questo punto di faccio un po quello che voglio,
            //lo passo a chi di dovere che gestisce l'inizio della partita
            InGameRequestHandler inGameRequestHandler = new InGameRequestHandler(lobby);
            for (User user: lobby.getUsers()){
                user.setRequestHandler(inGameRequestHandler);
            }
            System.out.println("Partita iniziata");
        }).start();
    }

    /**
     * Adding a new user
     * @param user user to add
     */
    public synchronized void addNewUser(User user){
        //qui creo il nuovo clientHandler e nel costruttore gli passo this
        int id = this.random.nextInt(100000);
        while (this.preGameModel.getAllUsersIds().contains(id)){
            id = this.random.nextInt(100000);
        }
        user.setId(id);
        this.preGameModel.addNewUser(user);
    }
}
