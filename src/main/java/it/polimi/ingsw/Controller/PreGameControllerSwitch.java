package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.PreGameModel.*;

import java.util.Random;

public class PreGameControllerSwitch {

    private final PreGameModel preGameModel;
    private final Random random;

    public PreGameControllerSwitch(boolean isLocal){
        this.preGameModel = (isLocal) ? new LocalPreGameModel() : new RemotePreGameModel();
        this.random = new Random();
    }

    public synchronized void actionOnMessage(CreationLobbyMessage message){
        int id = random.nextInt(200);
        while (this.preGameModel.getAllLobbiesId().contains(id)){
            id = random.nextInt(200);
        }
        this.preGameModel.createLobby(id);
        User user = this.preGameModel.getAndRemoveUser(message.getSenderId());
        if (user == null){
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        this.preGameModel.addUserToLobby(user, id);
    }

    public synchronized void actionOnMessage(DisconnectMessage message){
        this.preGameModel.getAndRemoveUser(message.getSenderId());
    }

    public synchronized void actionOnMessage(MoveToLobbyMessage message){
        User user = this.preGameModel.getAndRemoveUser(message.getSenderId());
        if (user==null){
            //non ho associato nessun user con questo id, non me lo spiego
            //ma per evitare che cada tutto
            return;
        }
        if (this.preGameModel.isLobbyFull(message.getLobbyId())){
            //notifica il player che ha provato ad accedere ad una lobby piena sbagliato
            return;
        }
        this.preGameModel.addUserToLobby(user, message.getLobbyId());
    }

    public synchronized void actionOnMessage(StartGameMessage message){
        int lobbyId = this.preGameModel.getUserLobbyId(message.getSenderId());
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
            System.out.println("Partita iniziata");
        }).start();
    }

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
