package it.polimi.ingsw.Model.Decks;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LeaderDeck implements Deck{
    private List<LeaderCard> deck;

    public LeaderDeck() {
        deck = new ArrayList<>();

        Path path = Paths.get(this.getClass().getResource("/JSONs/LeaderCardsConfig.json").toString().substring(6));
        String config;

        try {
            config = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error during the reading of the config file");
        }

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(config);
        if (!elements.isJsonArray()) {
            throw new IllegalArgumentException("Check the config file and his syntax");
        } else {
            JsonArray array = elements.getAsJsonArray();
            String s;
            for (int i = 0; i < array.size(); i++) {
                JsonObject card = array.get(i).getAsJsonObject();

                s = card.get("LeaderCardType").getAsString().toLowerCase();
                LeaderCardType type = gson.fromJson(card.get("LeaderCardType"), LeaderCardType.class);

                Map<Resource, Integer> map = gson.fromJson(card.get(s), new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                    input.put(entry.getKey(), entry.getValue());
                }

                map = gson.fromJson(card.get("resourceReq"),new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                    resourceReq.put(entry.getKey(), entry.getValue());
                }

                JsonArray devMap = card.get("devCardReq").getAsJsonArray();
                for(int j = 0; j < devMap.size(); j++){
                    JsonObject obj = devMap.get(j).getAsJsonObject();
                    devCardReq.put(gson.fromJson(obj.get("devCardType"), DevCardType.class), obj.get("quantity").getAsInt());
                }

                deck.add(new LeaderCard(card.get("victoryPoints").getAsInt(), resourceReq, devCardReq, type, input, card.get("id").getAsInt()));

                resourceReq.clear();
                devCardReq.clear();
                input.clear();

            }
        }
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public LeaderCard draw() {
        return deck.remove(0);
    }
}
