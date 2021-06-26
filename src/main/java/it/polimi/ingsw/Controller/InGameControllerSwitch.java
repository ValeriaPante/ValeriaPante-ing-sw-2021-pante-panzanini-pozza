package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.PreGameModel.Lobby;

import java.util.List;

public class InGameControllerSwitch {
    private final BuyDevCardController buyDevCardController;
    private final GameController gameController;
    private final LeaderController leaderController;
    private final MarketController marketController;
    private final ProductionController productionController;

    public InGameControllerSwitch(Lobby lobby){
        gameController = new GameController(lobby);
        buyDevCardController = new BuyDevCardController(gameController.getFaithTrackController());
        productionController = new ProductionController(gameController.getFaithTrackController());
        marketController = new MarketController(gameController.getFaithTrackController());
        leaderController = new LeaderController(gameController.getFaithTrackController());
    }

    public synchronized void actionOnMessage(AllProductionPowerSelectionMessage message){
        productionController.selectAllProductionPowers();
    }

    public synchronized void actionOnMessage(AnySelectionMessage message){
        try{
            productionController.anySelection(message.getResource());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(BackFromAnySelectionMessage message){
        productionController.backFromAnySelection();
    }

    public synchronized void actionOnMessage(BuyDevCardMessage message){
        buyDevCardController.buyDevCard();
    }

    public synchronized void actionOnMessage(CardProductionSelectionMessage message){
        productionController.selectCardProduction(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevCardMessage message){
        buyDevCardController.chooseDevCard(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevSlotMessage message){
        buyDevCardController.chooseDevSlot(message.getInteger());
    }

    public synchronized void actionOnMessage(DiscountAbilityMessage message){
        buyDevCardController.applyDiscountAbility(message.getInteger());
    }

    public synchronized void actionOnMessage(EndTurnMessage message){
        gameController.endTurn();
    }

    public synchronized void actionOnMessage(ExchangeMessage message){
        marketController.exchange();
    }

    public synchronized void actionOnMessage(LeaderCardActionMessage message){
        try{
            leaderController.actionOnLeaderCard(message.getInteger(), message.isaBoolean());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(LeaderDiscardMessage message){
        gameController.discardLeaderCard(message.getInteger());
    }

    public synchronized void actionOnMessage(LeaderStorageSelectionMessage message){
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
        marketController.selectFromMarket(message.getInteger(), message.isaBoolean());
    }

    public synchronized void actionOnMessage(MoveToLeaderStorageMessage message){
        marketController.moveToLeaderStorage(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToShelfMessage message){
        marketController.moveToShelf(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToSupportContainerMessage message){
        marketController.moveSelectedToSupportContainer();
    }

    public synchronized void actionOnMessage(PaySelectedMessage message){
        buyDevCardController.paySelected();
    }

    public synchronized void actionOnMessage(ProductionActivationMessage message){
        try{
            productionController.activateProduction();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(QuitFromMarketMessage message){
        try{
            marketController.quit();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(SelectResourceMessage message){
        gameController.selectResource(message.getInteger(), message.getResource());
    }
    
    public synchronized void actionOnMessage(ShelfDeselectionMessage message){
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
        boolean bool;
        bool = buyDevCardController.deselectionFromStrongBox(message.getResource(), message.getInteger());
            if (!bool) {
                bool = productionController.deselectionFromStrongBox(message.getResource(), message.getInteger());
                if (!bool)
                    gameController.getTable().turnOf().setErrorMessage("You cannot perform this deselection now!");
            }
    }

    public synchronized void actionOnMessage(StrongBoxSelectionMessage message){
        boolean bool;
        bool = buyDevCardController.selectionFromStrongBox(message.getResource(), message.getInteger());
        if (!bool) {
            bool = productionController.selectionFromStrongBox(message.getResource(), message.getInteger());
            if (!bool)
                gameController.getTable().turnOf().setErrorMessage("You cannot perform this selection now!");
        }
    }

    public synchronized void actionOnMessage(SupportContainerDeselectionMessage message){
        marketController.deselectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(SupportContainerSelectionMessage message){
        marketController.selectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(TakeFromMarketMessage message){
        try{
            marketController.takeFromMarket();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(TransmutationMessage message){
        try{
            marketController.selectTransmutation(message.getSerial1(), message.getQuantity1(), message.getSerial2(), message.getQuantity2());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }
}
