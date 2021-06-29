package it.polimi.ingsw.Network.Client;

import com.google.gson.Gson;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Messages.*;
import it.polimi.ingsw.View.ClientModel.SimplifiedPlayer;
import it.polimi.ingsw.View.View;
import it.polimi.ingsw.View.ClientModel.Game;

/**
 * Makes the right calls depending on the message received (visitor pattern)
 */
public class Visitor {

    private final View view;
    private final Game model;

    private Gson gson;

    public Visitor(View view){
        this.view = view;
        this.model = view.getModel();

        gson = new Gson();
    }

    public void updateModel(ActionOnLeaderCardMessage m){
        System.out.println(gson.toJson(m));

        if(m.isDiscard()){
            if(m.getId() == model.getLocalPlayerId() || model.getPlayerFromId(m.getId()).getLeaderCards().contains(m.getCardId())) model.getPlayerFromId(m.getId()).getLeaderCards().remove(Integer.valueOf(m.getCardId()));
            else if (m.getId() == model.getLocalPlayerId() && m.getCardId() > 52 && m.getCardId() <57) model.getPlayerFromId(m.getId()).getAllLeaderStorages().remove(m.getCardId());
            view.discardLeaderCard(m.getId(), m.getCardId());
        }
        else{
            if(m.getId() != model.getLocalPlayerId()) model.getPlayerFromId(m.getId()).getLeaderCards().add(m.getCardId());
            else if (m.getCardId() > 52 && m.getCardId() <57) model.getPlayerFromId(m.getId()).getAllLeaderStorages().put(m.getCardId(), new Resource[]{null, null});
            view.activateLeaderCard(m.getId(), m.getCardId());
        }
    }

    public void updateModel(ChangedLeaderStorageMessage m){
        System.out.println(gson.toJson(m));

        model.getPlayerFromId(m.getId()).setLeaderStorage(m.getCardId(), m.getOwned());
        view.updateLeaderStorage(m.getId(), m.getCardId());
    }

    public void updateModel(ChangedLobbyMessage m){
        System.out.println(gson.toJson(m));

        if(m.isMine()) model.setLocalPlayerLobbyId(m.getId());
        if(m.getPlayers().length == 0) {
            model.removeLobby(m.getId());
            view.removeLobby(m.getId());
        }
        else{
            model.addLobby(m.getId(), m.getPlayers());
            view.updateLobbyState(m.getId());
        }
    }

    public void updateModel(StartMessage m){
        System.out.println(gson.toJson(m));

        view.startGame();
    }

    public void updateModel(ChangedShelfMessage m){
        System.out.println(gson.toJson(m));

        model.getPlayerFromId(m.getId()).setShelf(m.getResourceType(), m.getQuantity(), m.getNumberOfShelf());
        view.updateShelves(m.getId(), m.getNumberOfShelf());
    }

    public void updateModel(ChangedStrongboxMessage m){
        System.out.println(gson.toJson(m));

        model.getPlayerFromId(m.getId()).setStrongbox(m.getInside());
        view.updateStrongbox(m.getId());
    }

    public void updateModel(ChangedSupportContainerMessage m){
        System.out.println(gson.toJson(m));

        model.getPlayerFromId(m.getId()).setSupportContainer(m.getInside());
        view.updateSupportContainer(m.getId());
    }

    public void updateModel(InitMessage m){
        System.out.println(gson.toJson(m));

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
        if(model.getLocalPlayerId() == m.getPlayersId()[0]) view.chooseLeaderCards();
        else view.startInitialisation();
    }

    public void updateModel(NewDevCardMessage m){
        System.out.println(gson.toJson(m));

        model.getPlayerFromId(m.getId()).addDevCardInSlot(m.getCardId(), m.getNumberOfSlot());
        view.addDevCardInSlot(m.getId(), m.getCardId(), m.getNumberOfSlot());
    }

    public void updateModel(NewMarketStateMessage m){
        System.out.println(gson.toJson(m));

        model.updateMarketState(m.getGrid(), m.getSlide());
    }

    public void updateModel(NewPlayerPositionMessage m){
        System.out.println(gson.toJson(m));

        System.out.println(gson.toJson(m));
        view.updatePositions(m.getId(), m.getPosition());
    }

    public void updateModel(NewTopCardMessage m){
        System.out.println(gson.toJson(m));

        model.updateDevDeck(m.getNumberOfDeck(), m.getId());
    }

    public void updateModel(PopeFavourCardStateMessage m){
        System.out.println(gson.toJson(m));

        view.updatePopeFavourCard(m.getId(), m.getCards());
    }

    public void updateModel(TurnOfMessage m){
        System.out.println(gson.toJson(m));

        view.nextTurn(m.getId());
    }

    public void updateModel(WinnerMessage m){
        System.out.println(gson.toJson(m));

        view.showWinner(m.getId());
    }

    public void updateModel(ErrorMessage m){
        System.out.println(gson.toJson(m));

        view.showErrorMessage(m.getError());
    }

    public void updateModel(SelectionErrorMessage m){
        System.out.println(gson.toJson(m));

        view.showSelectionError(m.getError(), m.getId());
    }

    public void updateModel(LorenzoTurnMessage m) {
        System.out.println(gson.toJson(m));

        view.showLorenzoTurn(m.getActionToken());
    }

    public void updateModel(DisconnectionMessage m){
        System.out.println(gson.toJson(m));

        view.showErrorMessage(m.getError());
        try{
            Thread.sleep(3000);
        } catch (Exception e){

        } finally {
            System.exit(0);
        }
    }

}
