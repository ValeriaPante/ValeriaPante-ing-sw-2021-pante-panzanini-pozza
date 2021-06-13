package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

public class MessageToJsonConverter {

    public static String create(InGameMessage message){
        return null;
    }

    public static String create(PreGameMessage message){
        return null;
    }

    public static String create(CreationLobbyMessage message){
        return "{ \"type\": \"CreationLobby\"} " ;
    }

    public static String create(DisconnectMessage message){
        return "{ \"type\": \"Disconnect\"}";
    }

    public static String create(MoveToLobbyMessage message){
        return "{ \"type\": \"MoveToLobby\", " +
                "\"lobbyId\":"+ message.getLobbyId() +"}";
    }

    public static String create(StartGameMessage message){
        return "{ \"type\": \"StartGame\"}";
    }

    public static String create(AllProductionPowerSelectionMessage message){
        return "{ \"type\": \"AllProductionPowerSelection\"}";
    }

    public static String create(AnySelectionMessage message){
        return "{ \"type\": \"AnySelection\", " +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(BackFromAnySelectionMessage message){
        return "{ \"type\": \"BackFromAnySelection\"}";
    }

    public static String create(BuyDevCardMessage message){
        return "{ \"type\": \"BuyDevCard\"}";
    }

    public static String create(CardProductionSelectionMessage message){
        return "{ \"type\": \"CardProductionSelection\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(ChooseDevCardMessage message){
        return "{ \"type\": \"ChooseDevCard\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(ChooseDevSlotMessage message){
        return "{ \"type\": \"ChooseDevSlot\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(DiscountAbilityMessage message){
        return "{ \"type\": \"DiscountAbility\", " +
                "\"integer\":"+ message.getInteger()+"}";
    }

    public static String create(EndTurnMessage message){
        return "{ \"type\": \"EndTurn\"}";
    }

    public static String create(ExchangeMessage message){
        return "{ \"type\": \"Exchange\"}";
    }

    public static String create(LeaderCardActionMessage message){
        return "{ \"type\": \"LeaderCardAction\", " +
                "\"integer\":"+ message.getInteger() +", " +
                "\"aBoolean\":"+ message.isaBoolean() +"}";
    }

    public static String create(LeaderDiscardMessage message){
        return "{ \"type\": \"LeaderDiscard\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(LeaderStorageSelectionMessage message){
        return "{ \"type\": \"LeaderStorageSelection\", " +
                "\"id\":"+ message.getId() +", "  +
                "\"resPosition\":"+ message.getResPosition() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(MarketSelectionMessage message){
        return "{ \"type\": \"MarketSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"aBoolean\":"+ message.isaBoolean() +"}";
    }

    public static String create(MoveToLeaderStorageMessage message){
        return "{ \"type\": \"MoveToLeaderStorage\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(MoveToShelfMessage message){
        return "{ \"type\": \"MoveToShelf\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String create(MoveToSupportContainerMessage message){
        return "{ \"type\": \"MoveToSupportContainer\"}";
    }

    public static String create(PaySelectedMessage message){
        return "{ \"type\": \"PaySelected\"}";
    }

    public static String create(ProductionActivationMessage message){
        return "{ \"type\": \"ProductionActivation\"}";
    }

    public static String create(QuitFromMarketMessage message){
        return "{ \"type\": \"QuitFromMarket\"}";
    }

    public static String create(SelectResourceMessage message){
        return "{ \"type\": \"SelectResource\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(ShelfDeselectionMessage message){
        return "{ \"type\": \"ShelfDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(ShelfSelectionMessage message){
        return "{ \"type\": \"ShelfSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(StrongBoxDeselectionMessage message){
        return "{ \"type\": \"StrongBoxDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(StrongBoxSelectionMessage message){
        return "{ \"type\": \"StrongBoxSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(SupportContainerDeselectionMessage message){
        return "{ \"type\": \"SupportContainerDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(SupportContainerSelectionMessage message){
        return "{ \"type\": \"SupportContainerSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String create(TakeFromMarketMessage message){
        return "{ \"type\": \"TakeFromMarket\"}";
    }

    public static String create(TransmutationMessage message){
        return "{ \"type\": \"Transmutation\", " +
                "\"serial1\":"+ message.getSerial1() +", "  +
                "\"serial1\":"+ message.getSerial2() +", "  +
                "\"quantity1\":"+ message.getQuantity1() +", "  +
                "\"quantity2\":"+ message.getQuantity2() +"}";
    }
}
