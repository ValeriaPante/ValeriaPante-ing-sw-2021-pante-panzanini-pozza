package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Network.Client.Messages.*;

public class Visitor {

    public void updateModel(ActionOnLeaderCardMessage m){ }

    public void updateModel(ChangedLeaderStorageMessage m){ }

    public void updateModel(ChangedLobbyMessage m){ }

    public void updateModel(ChangedShelfMessage m){ }

    public void updateModel(ChangedStrongboxMessage m){ }

    public void updateModel(InitMessage m){ }

    public void updateModel(NewDevCardMessage m){ }

    public void updateModel(NewLobbyMessage m){ }

    public void updateModel(NewMarketStateMessage m){ }

    public void updateModel(NewPlayerPositionMessage m){ }

    public void updateModel(NewTopCardMessage m){ }

    public void updateModel(PopeFavourCardStateMessage m){ }

    public void updateModel(RemoveLobbyMessage m){ }

    public void updateModel(WinnerMessage m){ }

}
