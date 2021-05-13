package it.polimi.ingsw.CustomDeserializers;

import com.google.gson.*;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.*;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;

import java.lang.reflect.Type;

public class InGameDeserializer implements JsonDeserializer<InGameMessage> {
    @Override
    public InGameMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        try{
            switch (jsonObject.get("type").getAsString()){
                case "AllProductionPowerSelection":
                    return new AllProductionPowerSelectionMessage();

                case "AnySelection":
                    return new AnySelectionMessage(Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "BackFromAnySelection":
                    return new BackFromAnySelectionMessage();

                case "BuyDevCard":
                    return new BuyDevCardMessage();

                case "CardProductionSelection":
                    return new CardProductionSelectionMessage(jsonObject.get("integer").getAsInt());

                case "ChooseDevCard":
                    return new ChooseDevCardMessage(jsonObject.get("integer").getAsInt());

                case "ChooseDevSlot":
                    return new ChooseDevSlotMessage(jsonObject.get("integer").getAsInt());

                case "DiscountAbility":
                    return new DiscountAbilityMessage(jsonObject.get("integer").getAsInt());

                case "EndTurn":
                    return new EndTurnMessage();

                case "Exchange":
                    return new ExchangeMessage();

                case "LeaderCardAction":
                    return new LeaderCardActionMessage(jsonObject.get("integer").getAsInt(), jsonObject.get("boolean").getAsBoolean());

                case "LeaderDiscard":
                    return new LeaderDiscardMessage(jsonObject.get("integer").getAsInt());

                case "LeaderStorageSelection":
                    return new LeaderStorageSelectionMessage(jsonObject.get("id").getAsInt(), jsonObject.get("resPosition").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "MarketSelection":
                    return new MarketSelectionMessage(jsonObject.get("integer").getAsInt(), jsonObject.get("boolean").getAsBoolean());

                case "MoveToLeaderStorage":
                    return new MoveToLeaderStorageMessage(jsonObject.get("integer").getAsInt());

                case "MoveToShelf":
                    return new MoveToShelfMessage(jsonObject.get("integer").getAsInt());

                case "MoveToSupportContainer":
                    return new MoveToSupportContainerMessage();

                case "PaySelected":
                    return new PaySelectedMessage();

                case "ProductionActivation":
                    return new ProductionActivationMessage();

                case "QuitFromMarket":
                    return new QuitFromMarketMessage();

                case "SelectResource":
                    return new SelectResourceMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "ShelfDeselection":
                    return new ShelfDeselectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "ShelfSelection":
                    return new ShelfSelectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "StrongBoxDeselection":
                    return new StrongBoxDeselectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "StrongBoxSelection":
                    return new StrongBoxSelectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "SupportContainerDeselection":
                    return new SupportContainerDeselectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "SupportContainerSelection":
                    return new SupportContainerSelectionMessage(jsonObject.get("integer").getAsInt(), Resource.fromAlias(jsonObject.get("resource").getAsString()));

                case "TakeFromMarket":
                    return new TakeFromMarketMessage();

                case "Transmutation":
                    return new TransmutationMessage(jsonObject.get("serial1").getAsInt(), jsonObject.get("serial2").getAsInt(), jsonObject.get("quantity1").getAsInt(), jsonObject.get("quantity2").getAsInt());

                default:
                    //Error InGameMessage: "Incorrect message type for in-game"
                    return new NoActionMessage();
            }
        } catch (NullPointerException e) {
            //Error InGameMessage: "Incorrect message syntax"
            return new NoActionMessage();
        }
    }
}
