package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;

public class MessageConverterToJSON {
    //the following static methods are used for creating the JSON through visitor pattern
    public static String convertToJson(AllProductionPowerSelectionMessage message){
        return "{ \"type\": \"AllProductionPowerSelection\"}";
    }

    public static String convertToJson(AnySelectionMessage message){
        return "{ \"type\": \"AnySelection\", " +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(BackFromAnySelectionMessage message){
        return "{ \"type\": \"BackFromAnySelection\"}";
    }

    public static String convertToJson(BuyDevCardMessage message){
        return "{ \"type\": \"BuyDevCard\"}";
    }

    public static String convertToJson(CardProductionSelectionMessage message){
        return "{ \"type\": \"CardProductionSelection\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(ChooseDevCardMessage message){
        return "{ \"type\": \"ChooseDevCard\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(ChooseDevSlotMessage message){
        return "{ \"type\": \"ChooseDevSlot\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(DiscountAbilityMessage message){
        return "{ \"type\": \"DiscountAbility\", " +
                "\"integer\":"+message.getInteger()+"}";
    }

    public static String convertToJson(EndTurnMessage message){
        return "{ \"type\": \"EndTurn\"}";
    }

    public static String convertToJson(ExchangeMessage message){
        return "{ \"type\": \"Exchange\"}";
    }

    public static String convertToJson(LeaderCardActionMessage message){
        return "{ \"type\": \"LeaderCardAction\", " +
                "\"integer\":"+ message.getInteger() +", " +
                "\"aBoolean\":"+ message.isaBoolean() +"}";
    }

    public static String convertToJson(LeaderDiscardMessage message){
        return "{ \"type\": \"LeaderDiscard\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(LeaderStorageSelectionMessage message){
        return "{ \"type\": \"LeaderStorageSelection\", " +
                "\"id\":"+ message.getId() +", "  +
                "\"resPosition\":"+ message.getResPosition() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(MarketSelectionMessage message){
        return "{ \"type\": \"MarketSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"aBoolean\":"+ message.isaBoolean() +"}";
    }

    public static String convertToJson(MoveToLeaderStorageMessage message){
        return "{ \"type\": \"MoveToLeaderStorage\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(MoveToShelfMessage message){
        return "{ \"type\": \"MoveToShelf\", " +
                "\"integer\":"+ message.getInteger() +"}";
    }

    public static String convertToJson(MoveToSupportContainerMessage message){
        return "{ \"type\": \"MoveToSupportContainer\"}";
    }

    public static String convertToJson(NoActionMessage message){
        return "{ \"type\": \"NoAction\"}";
    }

    public static String convertToJson(PaySelectedMessage message){
        return "{ \"type\": \"PaySelected\"}";
    }

    public static String convertToJson(ProductionActivationMessage message){
        return "{ \"type\": \"ProductionActivation\"}";
    }

    public static String convertToJson(QuitFromMarketMessage message){
        return "{ \"type\": \"QuitFromMarket\"}";
    }

    public static String convertToJson(SelectResourceMessage message){
        return "{ \"type\": \"SelectResource\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(ShelfDeselectionMessage message){
        return "{ \"type\": \"ShelfDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(ShelfSelectionMessage message){
        return "{ \"type\": \"ShelfSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(StrongBoxDeselectionMessage message){
        return "{ \"type\": \"StrongBoxDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(StrongBoxSelectionMessage message){
        return "{ \"type\": \"StrongBoxSelection\", " +
                "\"integer\":" + message.getInteger() + ", " +
                "\"resource\":" + message.getResource().toString() + "}";
    }

    public static String convertToJson(SupportContainerDeselectionMessage message){
        return "{ \"type\": \"SupportContainerDeselection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(SupportContainerSelectionMessage message){
        return "{ \"type\": \"SupportContainerSelection\", " +
                "\"integer\":"+ message.getInteger() +", "  +
                "\"resource\":"+ message.getResource().toString() +"}";
    }

    public static String convertToJson(TakeFromMarketMessage message){
        return "{ \"type\": \"TakeFromMarket\"}";
    }

    public static String convertToJson(DeselectAllResources message){
        return "{ \"type\": \"DeselectAll\"}";
    }

    public static String convertToJson(TransmutationMessage message){
        return "{ \"type\": \"Transmutation\", " +
                "\"serial1\":"+ message.getSerial1() +", "  +
                "\"serial1\":"+ message.getSerial2() +", "  +
                "\"quantity1\":"+ message.getQuantity1() +", "  +
                "\"quantity2\":"+ message.getQuantity2() +"}";
    }
}
