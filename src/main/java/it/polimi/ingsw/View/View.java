package it.polimi.ingsw.View;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.View.ClientModel.Game;

public interface View {

    void showNewLobby(int lobbyId, String firstPlayer);
    void updateLobbyState(int lobbyId, String[] players);
    void removeLobby(int lobbyId);

    void chooseLeaderCards();
    void chooseInitialResources();
    void startGame();

    void showMarket();
    void showDevDecks();

    void updatePositions(int playerId, int pos);

    void updatePopeFavourCard(int playerId, PopeFavorCardState[] states);

    void updateStrongbox(int playerId);
    void updateShelves(int playerId, int numShelf);
    void updateSupportContainer(int playerId);
    void updateLeaderStorage(int playerId, int cardId);

    void activateLeaderCard(int playerId, int cardId);
    void discardLeaderCard(int playerId, int cardId);

    void addDevCardInSlot(int playerId, int cardId, int slot);

    void showWinner(String username);

    void showErrorMessage(String message);

    Game getModel();
}
