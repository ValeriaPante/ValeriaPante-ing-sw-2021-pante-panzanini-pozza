package it.polimi.ingsw.Controller;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.PreGameModel.Lobby;

import java.util.List;

/**
 * This class is used for visitor pattern as visitor.
 * It is implemented in order to avoid the usage of a "switch" made of multiples "instanceof".
 * It extracts the content of each message and passes it to the right controller.
 * Since some messages can correspond to calls of methods on different controller that cannot be
 * decided before, since it is up to the business logic of the game, whe used the pattern "chain of responsibility":
 * an example is the implementation of the "actionOnMessage" method for a message which type is "shelfSelection".
 * All the possible methods, on the different controllers, are called and tried to be performed and according to the
 * result, different calls on methods are considered.
 */
public class InGameControllerSwitch {
    private final BuyDevCardController buyDevCardController;
    private final GameController gameController;
    private final LeaderController leaderController;
    private final MarketController marketController;
    private final ProductionController productionController;

    /**
     * Creates all the controllers and all the game elements
     * @param lobby lobby of the players that will play this match
     */
    public InGameControllerSwitch(Lobby lobby){
        gameController = new GameController(lobby);
        buyDevCardController = new BuyDevCardController(gameController.getFaithTrackController());
        productionController = new ProductionController(gameController.getFaithTrackController());
        marketController = new MarketController(gameController.getFaithTrackController());
        leaderController = new LeaderController(gameController.getFaithTrackController());
    }

    private boolean isActionFromTurnOf(InGameMessage message){
        Gson gson = new Gson();
        System.out.println("----"+gson.toJson(message));

        Table table = gameController.getTable();

        RealPlayer senderPlayer = null;
        for (RealPlayer player : table.getPlayers()){
            if (player.getId() == message.getSenderId()){
                senderPlayer = player;
            }
        }
        if (table.isSinglePlayer() && table.isLorenzoTurn()){
            if (senderPlayer != null){
                senderPlayer.setErrorMessage("It is not your turn!");
            }
            return false;
        } else if (table.isSinglePlayer()){

        }
        if (message.getSenderId() != table.turnOf().getId()){
            System.out.println(table.turnOf().getId());
            if (senderPlayer != null){
                senderPlayer.setErrorMessage("It is not your turn!");
            }
            return false;
        }

        return true;
    }

    public synchronized void actionOnMessage(AllProductionPowerSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        productionController.selectAllProductionPowers();
    }

    public synchronized void actionOnMessage(AnySelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            productionController.anySelection(message.getResource());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(BackFromAnySelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        productionController.backFromAnySelection();
    }

    public synchronized void actionOnMessage(BuyDevCardMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        buyDevCardController.buyDevCard();
    }

    public synchronized void actionOnMessage(CardProductionSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        productionController.selectCardProduction(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevCardMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        buyDevCardController.chooseDevCard(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevSlotMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        buyDevCardController.chooseDevSlot(message.getInteger());
    }

    public synchronized void actionOnMessage(DiscountAbilityMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        buyDevCardController.applyDiscountAbility(message.getInteger());
    }

    public synchronized void actionOnMessage(EndTurnMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        gameController.endTurn();
    }

    public synchronized void actionOnMessage(ExchangeMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.exchange();
    }

    public synchronized void actionOnMessage(LeaderCardActionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            leaderController.actionOnLeaderCard(message.getInteger(), message.isaBoolean());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(LeaderDiscardMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        gameController.discardLeaderCard(message.getInteger());
    }

    public synchronized void actionOnMessage(LeaderStorageSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        boolean bool;
        bool = marketController.selectionFromLeaderStorage(message.getResource(), message.getId(), message.getResPosition());
        if (!bool){
            bool = buyDevCardController.selectionFromLeaderStorage(message.getResource(), message.getId(), message.getResPosition());
            if (!bool) {
                bool = productionController.selectionFromLeaderStorage(message.getResource(), message.getId(), message.getResPosition());
                if (!bool)
                    gameController.getTable().turnOf().setErrorMessage("You cannot perform this action now!");
            }
        }
    }

    public synchronized void actionOnMessage(MarketSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.selectFromMarket(message.getInteger(), message.isaBoolean());
    }

    public synchronized void actionOnMessage(MoveToLeaderStorageMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.moveToLeaderStorage(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToShelfMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.moveToShelf(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToSupportContainerMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.moveSelectedToSupportContainer();
    }

    public synchronized void actionOnMessage(PaySelectedMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        buyDevCardController.paySelected();
    }

    public synchronized void actionOnMessage(ProductionActivationMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            productionController.activateProduction();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(QuitFromMarketMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            marketController.quit();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(SelectResourceMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        gameController.selectResource(message.getInteger(), message.getResource());
    }
    
    public synchronized void actionOnMessage(ShelfDeselectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        boolean bool;
        bool = marketController.deselectionFromShelf(message.getResource(), message.getInteger());
        if (!bool){
            bool = buyDevCardController.deselectionFromShelf(message.getResource(), message.getInteger());
            if (!bool) {
                bool = productionController.deselectionFromShelf(message.getResource(), message.getInteger());
                if (!bool)
                    gameController.getTable().turnOf().setErrorMessage("You cannot perform this deselection now!");
            }
        }
    }

    public synchronized void actionOnMessage(ShelfSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        boolean bool;
        bool = marketController.selectionFromShelf(message.getResource(), message.getInteger());
        if (!bool){
            bool = buyDevCardController.selectionFromShelf(message.getResource(), message.getInteger());
            if (!bool) {
                bool = productionController.selectionFromShelf(message.getResource(), message.getInteger());
                if (!bool)
                    gameController.getTable().turnOf().setErrorMessage("You cannot perform this selection now!");
            }
        }
    }

    public synchronized void actionOnMessage(StrongBoxDeselectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        boolean bool;
        bool = buyDevCardController.deselectionFromStrongBox(message.getResource(), message.getInteger());
            if (!bool) {
                bool = productionController.deselectionFromStrongBox(message.getResource(), message.getInteger());
                if (!bool)
                    gameController.getTable().turnOf().setErrorMessage("You cannot perform this deselection now!");
            }
    }

    public synchronized void actionOnMessage(StrongBoxSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        boolean bool;
        bool = buyDevCardController.selectionFromStrongBox(message.getResource(), message.getInteger());
        if (!bool) {
            bool = productionController.selectionFromStrongBox(message.getResource(), message.getInteger());
            if (!bool)
                gameController.getTable().turnOf().setErrorMessage("You cannot perform this selection now!");
        }
    }

    public synchronized void actionOnMessage(SupportContainerDeselectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.deselectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(SupportContainerSelectionMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        marketController.selectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(TakeFromMarketMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            marketController.takeFromMarket();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(TransmutationMessage message){
        if (!isActionFromTurnOf(message)){
            return;
        }
        try{
            marketController.selectTransmutation(message.getSerial1(), message.getQuantity1(), message.getSerial2(), message.getQuantity2());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public void connectionClosed(int playerId){
        int id = 0;
        RealPlayer[] players = gameController.getTable().getPlayers();
        for (int i=0; i<players.length; i++){
            if (players[i].getId() == playerId){
                id = i+1;
                break;
            }
        }
        gameController.getTable().closeAll(id);
    }
}
