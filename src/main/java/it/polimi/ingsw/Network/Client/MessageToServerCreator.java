package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Enums.Resource;

public abstract class MessageToServerCreator {

    public static String createCreationLobbyMessage(){
        return "{ \"type\": \"CreationLobby\"} " ;
    }

    public static String createDisconnectMessage(){
        return "{ \"type\": \"Disconnect\"}";
    }

    public static String createMoveToLobbyMessage(int lobbyId){
        return "{ \"type\": \"MoveToLobby\", " +
                "\"lobbyId\":"+ lobbyId +"}";
    }

    public static String createStartGameMessage(){
        return "{ \"type\": \"StartGame\"}";
    }

    public static String createAllProductionPowerSelectionMessage(){
        return "{ \"type\": \"AllProductionPowerSelection\"}";
    }

    public static String createAnySelectionMessage(Resource resource){
        return "{ \"type\": \"AnySelection\", " +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createBackFromAnySelectionMessage(){
        return "{ \"type\": \"BackFromAnySelection\"}";
    }

    public static String createBuyDevCardMessage(){
        return "{ \"type\": \"BuyDevCard\"}";
    }

    public static String createCardProductionSelectionMessage(int cardId){
        return "{ \"type\": \"CardProductionSelection\", " +
                "\"integer\":"+ cardId +"}";
    }

    public static String createChooseDevCardMessage(int devDeckNumber){
        return "{ \"type\": \"ChooseDevCard\", " +
                "\"integer\":"+ devDeckNumber +"}";
    }

    public static String createChooseDevSlotMessage(int devSlotNumber){
        return "{ \"type\": \"ChooseDevSlot\", " +
                "\"integer\":"+ devSlotNumber +"}";
    }

    public static String createDiscountAbilityMessage(int cardId){
        return "{ \"type\": \"DiscountAbility\", " +
                "\"integer\":"+ cardId+"}";
    }

    public static String createEndTurnMessage(){
        return "{ \"type\": \"EndTurn\"}";
    }

    public static String createExchangeMessage(){
        return "{ \"type\": \"Exchange\"}";
    }

    public static String createLeaderCardActionMessage(int leaderCardId, boolean discard){
        return "{ \"type\": \"LeaderCardAction\", " +
                "\"integer\":"+ leaderCardId +", " +
                "\"aBoolean\":"+ discard +"}";
    }

    public static String createLeaderDiscardMessage(int leaderCardId){
        return "{ \"type\": \"LeaderDiscard\", " +
                "\"integer\":"+ leaderCardId +"}";
    }

    public static String createLeaderStorageSelectionMessage(Resource resource, int resPosition, int cardId){
        return "{ \"type\": \"LeaderStorageSelection\", " +
                "\"id\":"+ cardId +", "  +
                "\"resPosition\":"+ resPosition +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createMarketSelectionMessage(int number, boolean isRow){
        return "{ \"type\": \"MarketSelection\", " +
                "\"integer\":"+ number +", "  +
                "\"aBoolean\":"+ isRow +"}";
    }

    public static String createMoveToLeaderStorageMessage(int serial){
        return "{ \"type\": \"MoveToLeaderStorage\", " +
                "\"integer\":"+ serial +"}";
    }

    public static String createMoveToShelfMessage(int numberOfShelf){
        return "{ \"type\": \"MoveToShelf\", " +
                "\"integer\":"+ numberOfShelf +"}";
    }

    public static String createMoveToSupportContainerMessage(){
        return "{ \"type\": \"MoveToSupportContainer\"}";
    }

    public static String createPaySelectedMessage(){
        return "{ \"type\": \"PaySelected\"}";
    }

    public static String createProductionActivationMessage(){
        return "{ \"type\": \"ProductionActivation\"}";
    }

    public static String createQuitFromMarketMessage(){
        return "{ \"type\": \"QuitFromMarket\"}";
    }

    public static String createSelectResourceMessage(int n, Resource resource){
        return "{ \"type\": \"SelectResource\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createShelfDeselectionMessage(int n, Resource resource){
        return "{ \"type\": \"ShelfDeselection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createShelfSelectionMessage(int n, Resource resource){
        return "{ \"type\": \"ShelfSelection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createStrongBoxDeselectionMessage(int n, Resource resource){
        return "{ \"type\": \"StrongBoxDeselection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createStrongBoxSelectionMessage(int n, Resource resource){
        return "{ \"type\": \"StrongBoxSelection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createSupportContainerDeselectionMessage(int n, Resource resource){
        return "{ \"type\": \"SupportContainerDeselection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createSupportContainerSelectionMessage(int n, Resource resource){
        return "{ \"type\": \"SupportContainerSelection\", " +
                "\"integer\":"+ n +", "  +
                "\"resource\":"+ resource.toString() +"}";
    }

    public static String createTakeFromMarketMessage(){
        return "{ \"type\": \"TakeFromMarket\"}";
    }

    public static String createTransmutationMessage(int serial1, int serial2, int quantity1, int quantity2){
        return "{ \"type\": \"Transmutation\", " +
                "\"serial1\":"+ serial1 +", "  +
                "\"serial1\":"+ serial2 +", "  +
                "\"quantity1\":"+ quantity1 +", "  +
                "\"quantity2\":"+ quantity2 +"}";
    }
}
