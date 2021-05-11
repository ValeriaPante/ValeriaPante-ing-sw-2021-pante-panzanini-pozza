package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.*;

public class ControllerSwitch {
    private BuyDevCardController buyDevCardController;
    private GameController gameController;
    private LeaderController leaderController;
    private MarketController marketController;
    private ProductionController productionController;

    public void actionOnMessage(AllProductionPowerSelectionMessage message){
        productionController.selectAllProductionPowers();
    }

    public void actionOnMessage(AnySelectionMessage message){
        productionController.anySelection(message.getResource());
    }

    public void actionOnMessage(BackFromAnySelectionMessage message){
        productionController.backFromAnySelection();
    }

    public void actionOnMessage(BuyDevCardMessage message){
        buyDevCardController.buyDevCard();
    }

    public void actionOnMessage(CardProductionSelectionMessage message){
        productionController.selectCardProduction(message.getInteger());
    }

    public void actionOnMessage(ChooseDevCardMessage message){
        buyDevCardController.chooseDevCard(message.getInteger());
    }

    public void actionOnMessage(ChooseDevSlotMessage message){
        buyDevCardController.chooseDevSlot(message.getInteger());
    }

    public void actionOnMessage(DiscountAbilityMessage message){
        buyDevCardController.applyDiscountAbility(message.getInteger());
    }

    public void actionOnMessage(EndTurnMessage message){
        gameController.endTurn();
    }

    public void actionOnMessage(ExchangeMessage message){
        marketController.exchange();
    }

    public void actionOnMessage(LeaderCardActionMessage message){
        leaderController.actionOnLeaderCard(message.getInteger(), message.isaBoolean());
    }

    public void actionOnMessage(LeaderDiscardMessage message){
        gameController.discardLeaderCard(message.getInteger());
    }

    public void actionOnMessage(LeaderStorageSelectionMessage message){}

    public void actionOnMessage(MarketSelectionMessage message){
        marketController.selectFromMarket(message.getInteger(), message.isaBoolean());
    }

    public void actionOnMessage(MoveToLeaderStorageMessage message){
        marketController.moveToLeaderStorage(message.getInteger());
    }

    public void actionOnMessage(MoveToShelfMessage message){
        marketController.moveToShelf(message.getInteger());
    }

    public void actionOnMessage(MoveToSupportContainerMessage message){
        marketController.moveSelectedToSupportContainer();
    }

    public void actionOnMessage(PaySelectedMessage message){
        buyDevCardController.paySelected();
    }

    public void actionOnMessage(ProductionActivationMessage message){
        productionController.activateProduction();
    }

    public void actionOnMessage(QuitFromMarketMessage message){
        marketController.quit();
    }

    public void actionOnMessage(SelectResourceMessage message){
        gameController.selectResource(message.getInteger(), message.getResource());
    }
    
    public void actionOnMessage(ShelfDeselectionMessage message){ }

    public void actionOnMessage(ShelfSelectionMessage message){ }

    public void actionOnMessage(StrongBoxDeselectionMessage message){ }

    public void actionOnMessage(StrongBoxSelectionMessage message){ }

    public void actionOnMessage(SupportContainerDeselectionMessage message){
        marketController.deselectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public void actionOnMessage(SupportContainerSelectionMessage message){
        marketController.selectFromSupportContainer(message.getResource(), message.getInteger());
    }

    public void actionOnMessage(TakeFromMarketMessage message){
        marketController.takeFromMarket();
    }

    public void actionOnMessage(TransmutationMessage message){
        marketController.selectTransmutation(message.getSerial1(), message.getQuantity1(), message.getSerial2(), message.getQuantity2());
    }
}
