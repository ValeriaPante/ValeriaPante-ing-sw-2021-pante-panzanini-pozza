package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.Model;
import it.polimi.ingsw.PreGameModel.User;

import java.util.Random;
import java.util.concurrent.ExecutorService;

public class PreGameControllerSwitch {

    private final Model model;
    private final Random random;
    private final ExecutorService executor;

    public PreGameControllerSwitch(ExecutorService executor){
        this.model = new Model();
        this.random = new Random();
        this.executor = executor;
    }

    public synchronized void actionOnMessage(CreationLobbyMessage message){
        int id = random.nextInt(200);
        while (this.model.getAllLobbiesId().contains(id)){
            id = random.nextInt(200);
        }
        this.model.createLobby(id);
        User user = this.model.getAndRemoveUser(message.getSenderId());
        if (user == null){
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        this.model.addUserToLobby(user, id);
    }

    public synchronized void actionOnMessage(DisconnectMessage message){
        this.model.playerDisconnect(message.getSenderId());
    }

    public synchronized void actionOnMessage(MoveToLobbyMessage message){
        User user = this.model.getAndRemoveUser(message.getSenderId());
        if (user==null){
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        if (this.model.isLobbyFull(message.getLobbyId())){
            //notifica il player che ha provato ad accedere ad una lobby piena sbagliato
            return;
        }
        this.model.addUserToLobby(user, message.getLobbyId());
    }

    public synchronized void actionOnMessage(StartGameMessage message){
        int lobbyId = this.model.getUserLobbyId(message.getSenderId());
        if (lobbyId == 0){
            //notifica il player che ha provato a fare una cosa non permessa
            return;
        }
        Lobby lobby = this.model.getAndRemoveLobby(lobbyId);
        if (lobby == null){
            //stranissimo, vuol dire che il giocatore ha richiesto l'inizio della partita
            //di una lobby in cui era presente prima ma ora la lobby non esiste più;
            return;
        }

        executor.submit(()->{
            //a questo punto di faccio un po quello che voglio,
            //lo passo a chi di dovere che gestisce l'inizio della partita
        });
    }

    public synchronized void addNewUser(User user){
        //qui creo il nuovo clientHandler e nel costruttore gli passo this
        int id = random.nextInt(100000);
        while (this.model.getAllUsersIds().contains(id)){
            id = random.nextInt(100000);
        }
        user.setId(id);
        this.model.addNewUser(user);
    }
}
