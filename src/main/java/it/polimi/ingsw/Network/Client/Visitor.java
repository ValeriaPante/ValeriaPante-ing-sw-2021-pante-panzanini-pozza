package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Network.Client.Messages.*;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import it.polimi.ingsw.View.View;
import it.polimi.ingsw.View.ClientModel.Game;

public class Visitor {

    private final View view;
    private final Game model;

    public Visitor(View view){
        this.view = view;
        this.model = view.getModel();
    }

    public void updateModel(ActionOnLeaderCardMessage m){
        if(m.isDiscard()){
            model.getPlayerFromId(m.getId()).getLeaderCards().remove(Integer.valueOf(m.getCardId()));
            view.discardLeaderCard(m.getId(), m.getCardId());
        }
        else{
            if(m.getId() != model.getLocalPlayerId()) model.getPlayerFromId(m.getId()).getLeaderCards().add(m.getCardId());
            view.activateLeaderCard(m.getId(), m.getCardId());
        }
    }

    public void updateModel(ChangedLeaderStorageMessage m){
        model.getPlayerFromId(m.getId()).setLeaderStorage(m.getCardId(), m.getOwned());
        view.updateLeaderStorage(m.getId(), m.getCardId());
    }

    public void updateModel(ChangedLobbyMessage m){
        System.out.println("changed lobby method Visitor"); //Debug
        if(m.isMine()) model.setLocalPlayerLobbyId(m.getId());
        if(m.getPlayers().length == 0) {
            model.removeLobby(m.getId());
            view.removeLobby(m.getId());
        }
        else{
            model.addLobby(m.getId(), m.getPlayers());
            System.out.println("modified model in Visitor class"); //Debug
            view.updateLobbyState(m.getId());
        }
    }

    public void updateModel(StartMessage m){
        view.startGame();
    }

    public void updateModel(ChangedShelfMessage m){
        model.getPlayerFromId(m.getId()).setShelf(m.getResourceType(), m.getQuantity(), m.getNumberOfShelf());
        view.updateShelves(m.getId(), m.getNumberOfShelf());
    }

    public void updateModel(ChangedStrongboxMessage m){
        model.getPlayerFromId(m.getId()).setStrongbox(m.getInside());
        view.updateStrongbox(m.getId());
    }

    public void updateModel(ChangedSupportContainerMessage m){
        model.getPlayerFromId(m.getId()).setSupportContainer(m.getInside());
        view.updateSupportContainer(m.getId());
    }

    public void updateModel(InitMessage m){
        model.updateMarketState(m.getMarket(), m.getSlide());
        model.initialiseDevDecks(m.getDevDecks());
        model.setLocalPlayerId(m.getId());
        for(int i = 0; i < m.getPlayersId().length; i++){
            SimplifiedPlayer p = new SimplifiedPlayer();
            p.setUsername(m.getPlayersUsernames()[i]);
            if(m.getPlayersId()[i] == m.getId()){
                for(int j = 0; j < m.getLocalPlayerLeaderCards().length; j++){
                    p.addLeaderCard(m.getLocalPlayerLeaderCards()[j]);
                }
            }

            model.addPlayer(m.getPlayersId()[i], p);
        }
    }

    public void updateModel(NewDevCardMessage m){
        view.addDevCardInSlot(m.getId(), m.getCardId(), m.getNumberOfSlot());
    }

    public void updateModel(NewMarketStateMessage m){
        model.updateMarketState(m.getGrid(), m.getSlide());
    }

    public void updateModel(NewPlayerPositionMessage m){
        view.updatePositions(m.getId(), m.getPosition());
    }

    public void updateModel(NewTopCardMessage m){
        model.updateDevDeck(m.getNumberOfDeck(), m.getId());
    }

    public void updateModel(PopeFavourCardStateMessage m){
        view.updatePopeFavourCard(m.getId(), m.getCards());
    }

    public void updateModel(TurnOfMessage m){
        view.nextTurn(m.getId());
    }

    public void updateModel(WinnerMessage m){
        view.showWinner(model.getPlayerFromId(m.getId()).getUsername());
    }

    public void updateModel(ErrorMessage m){
        view.showErrorMessage(m.getError());
    }

}
