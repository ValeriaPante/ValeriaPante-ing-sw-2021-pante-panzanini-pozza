package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.View;

public class CLI implements View {
    @Override
    public void showNewLobby(int lobbyId, String firstPlayer) {

    }

    @Override
    public void updateLobbyState(int lobbyId, String[] players) {

    }

    @Override
    public void removeLobby(int lobbyId) {

    }

    @Override
    public void chooseLeaderCards() {

    }

    @Override
    public void chooseInitialResources() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void showMarket() {

    }

    @Override
    public void showDevDecks() {

    }

    @Override
    public void updatePositions(int playerId, int pos) {

    }

    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {

    }

    @Override
    public void updateStrongbox(int playerId) {

    }

    @Override
    public void updateShelves(int playerId, int numShelf) {

    }

    @Override
    public void updateSupportContainer(int playerId) {

    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {

    }

    @Override
    public void activateLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void discardLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {

    }

    @Override
    public void showWinner(String username) {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public Game getModel() {
        return null;
    }
}
