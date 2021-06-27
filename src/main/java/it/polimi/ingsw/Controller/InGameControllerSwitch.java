package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
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

    public synchronized void actionOnMessage(AllProductionPowerSelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        productionController.selectAllProductionPowers();
    }

    public synchronized void actionOnMessage(AnySelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            productionController.anySelection(message.getResource());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(BackFromAnySelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        productionController.backFromAnySelection();
    }

    public synchronized void actionOnMessage(BuyDevCardMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        buyDevCardController.buyDevCard();
    }

    public synchronized void actionOnMessage(CardProductionSelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        productionController.selectCardProduction(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevCardMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        buyDevCardController.chooseDevCard(message.getInteger());
    }

    public synchronized void actionOnMessage(ChooseDevSlotMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        buyDevCardController.chooseDevSlot(message.getInteger());
    }

    public synchronized void actionOnMessage(DiscountAbilityMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        buyDevCardController.applyDiscountAbility(message.getInteger());
    }

    public synchronized void actionOnMessage(EndTurnMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        gameController.endTurn();
    }

    public synchronized void actionOnMessage(ExchangeMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.exchange();
    }

    public synchronized void actionOnMessage(LeaderCardActionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            leaderController.actionOnLeaderCard(message.getInteger(), message.isaBoolean());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(LeaderDiscardMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        gameController.discardLeaderCard(message.getInteger());
    }

    public synchronized void actionOnMessage(LeaderStorageSelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
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
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.selectFromMarket(message.getInteger(), message.isaBoolean());
    }

    public synchronized void actionOnMessage(MoveToLeaderStorageMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.moveToLeaderStorage(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToShelfMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.moveToShelf(message.getInteger());
    }

    public synchronized void actionOnMessage(MoveToSupportContainerMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.moveSelectedToSupportContainer();
    }

    public synchronized void actionOnMessage(PaySelectedMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        buyDevCardController.paySelected();
    }

    public synchronized void actionOnMessage(ProductionActivationMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            productionController.activateProduction();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(QuitFromMarketMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            marketController.quit();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(SelectResourceMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        gameController.selectResource(message.getInteger(), message.getResource());
    }
    
    public synchronized void actionOnMessage(ShelfDeselectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
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
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
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
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
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
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
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
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.deselectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(SupportContainerSelectionMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        marketController.selectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public synchronized void actionOnMessage(TakeFromMarketMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            marketController.takeFromMarket();
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }

    public synchronized void actionOnMessage(TransmutationMessage message){
        if (message.getSenderId() != gameController.getTable().turnOf().getId() || (gameController.getTable().isSinglePlayer() && gameController.getTable().isLorenzoTurn())){
            for (RealPlayer player : gameController.getTable().getPlayers())
                if (player.getId() == message.getSenderId()){
                    player.setErrorMessage("It is not your turn!");
                }
        }
        try{
            marketController.selectTransmutation(message.getSerial1(), message.getQuantity1(), message.getSerial2(), message.getQuantity2());
        } catch (GameOver gameOver) {
            gameController.endGame();
        }
    }
}
