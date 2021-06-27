package it.polimi.ingsw.Network.JsonToClient;

import com.google.gson.Gson;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Cards.PopeFavorCard;
import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Deposit.Market;
import it.polimi.ingsw.Model.Deposit.Shelf;
import it.polimi.ingsw.Model.Deposit.StrongBox;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;

import java.util.ArrayList;
import java.util.List;

public class JsonToClientInGame {

    Gson gson;

    private String formatting(String key, String value, boolean isValueString){
        if (isValueString){
            return "\""+key+"\": \"" + value + "\"";
        }
        else{
            return "\""+key+"\": " + value;
        }
    }

    private String formatting(String key, int value){
        return "\""+key+"\": \"" + value + "\"";
    }

    private String strongBoxFormatting(StrongBox strongBox){
        if (strongBox.isEmpty()){
            return "{}";
        }
        return gson.toJson(strongBox.content());
    }

    public JsonToClientInGame(){
        this.gson = new Gson();
    }

    public String initMessage(Market market, int playerId, LeaderCard[] leaderCards, DevDeck[] devDecks){
        ArrayList<Integer> leaderCardsId = new ArrayList<>();
        for (LeaderCard leaderCard : leaderCards){
            leaderCardsId.add(leaderCard.getId());
        }

        ArrayList<Integer> topCardsId = new ArrayList<>();
        for (DevDeck devDeck : devDecks){
            topCardsId.add(devDeck.getTopCard().getId());
        }

        return "{ " +
                this.formatting("type", "init", true) + "," +
                this.formatting("market", gson.toJson(market.getState()), false) + "," +
                this.formatting("slide", gson.toJson(market.getSlide()), false) + "," +
                this.formatting("devDecks", gson.toJson(topCardsId), false) + "," +


                this.formatting("initialLeaderCards", gson.toJson(leaderCardsId), false) + "," +
                this.formatting("clientId", playerId) +
                " }";
    }

    public String startGameMessage(){
        return "{ " +
                this.formatting("type", "start", true) +
                " }";
    }

    public String leaderCardActionMessage(int playerId, boolean isDiscard, int leaderCardId){
        return "{ " +
                this.formatting("type", "actionOnLeaderCard", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("discard", (isDiscard ? "true" : "false"), true) + "," +
                this.formatting("id", leaderCardId) +
                " }";
    }

    public String newDevCardMessage(int playerId, int cardId, int slotNumber){
        return "{ " +
                this.formatting("type", "newDevCard", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("newPlayerCardId", cardId) + "," +
                this.formatting("numberOfSlot", slotNumber) +
                " }";
    }

    public String newTopCardMessage(int numberOfDeck, int newTopId){
        return "{ " +
                this.formatting("type", "newTopCard", true) + "," +
                this.formatting("numberOfDeck", numberOfDeck) + "," +
                this.formatting("id", newTopId) +
                " }";
    }

    public String shelfChangeMessage(Shelf shelf, int playerId) {
        boolean isEmpty = false;
        if (shelf.isEmpty()) {
            isEmpty = true;
        }
        return "{ " +
                this.formatting("type", "changedShelf", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("numberOfShelf", shelf.getCapacity()) + "," +
                this.formatting("resourceType", (isEmpty ? "\"\"" : gson.toJson(shelf.getResourceType())), false) + "," +
                this.formatting("quantity", (isEmpty ? 0 : shelf.getUsage())) +
                " }";
    }

   public String strongboxChangeMessage(StrongBox strongBox, int playerId){
        return "{ " +
                this.formatting("type", "changedStrongbox", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("inside", this.strongBoxFormatting(strongBox), false) +
                " }";
   }

   public String leaderStorageChangeMessage(LeaderCard leaderCard,  int playerId){
        return "{ " +
                this.formatting("type", "changedLeaderStorage", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("cardId", leaderCard.getId()) + "," +
                this.formatting("owned", gson.toJson(leaderCard.getAbility().getContent()),false) +
                " }";
   }

   public String supportContainerChangeMessage(StrongBox supportContainer, int playerId){
        return "{ " +
                this.formatting("type", "changedSupportContainer", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("inside", this.strongBoxFormatting(supportContainer) ,false) +
                " }";
   }

   public String newMarketStateMessage(Market market){
        return "{ " +
                this.formatting("type", "newMarketState", true) + "," +
                this.formatting("grid", gson.toJson(market.getState()), false) + "," +
                this.formatting("slide", gson.toJson(market.getSlide()), false) +
                " }";
   }

   public String popeCardsMessage(PopeFavorCard[] popeFavorCards, int playerId){
        ArrayList<PopeFavorCardState> states = new ArrayList<>();

        for (PopeFavorCard popeFavorCard : popeFavorCards){
            states.add(popeFavorCard.getState());
        }

        return "{ " +
                this.formatting("type", "popeFavourCardState", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("cards", gson.toJson(states), false) +
                " }";
   }

   public String newPlayerPosMessage(int playerId, int faithPos){
        return "{ " +
                this.formatting("type", "newPlayerPosition", true) + "," +
                this.formatting("playerId", playerId) + "," +
                this.formatting("pos", faithPos) +
                " }";
   }

   public String winnerMessage(int playerId){
        return "{ " +
                this.formatting("type", "winner", true) + "," +
                this.formatting("playerId", playerId) +
                " }";
   }

   public String errorMessage(String errorText){
        return "{ " +
                this.formatting("type", "error", true) + "," +
                this.formatting("error", errorText, true) +
                " }";
   }

}
