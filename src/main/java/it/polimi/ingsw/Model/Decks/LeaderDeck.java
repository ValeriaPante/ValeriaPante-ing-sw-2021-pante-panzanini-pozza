package it.polimi.ingsw.Model.Decks;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Representaion of Leader Deck
 */
public class LeaderDeck implements Deck{
    private final List<LeaderCard> deck;

    /**
     * Constucts a leader deck from a JSON file
     */
    public LeaderDeck() {
        deck = new ArrayList<>();

        InputStream in = getClass().getResourceAsStream("/accessible/JSONs/LeaderCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String config = reader.lines().collect(Collectors.joining());

        EnumMap<Resource, Integer> resourceReq = new EnumMap<>(Resource.class);
        Map<DevCardType, Integer> devCardReq = new HashMap<>();
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);
        EnumMap<Resource, Integer> output = new EnumMap<>(Resource.class);

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

                Map<Resource, Integer> map;
                if(!s.equals("prodpower")){
                    map = gson.fromJson(card.get(s), new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                    for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                        input.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    map = gson.fromJson(card.get(s).getAsJsonObject().get("input"), new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                    for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                        input.put(entry.getKey(), entry.getValue());
                    }
                    map = gson.fromJson(card.get(s).getAsJsonObject().get("output"), new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                    for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                        output.put(entry.getKey(), entry.getValue());
                    }
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

                deck.add(new LeaderCard(card.get("victoryPoints").getAsInt(), resourceReq, devCardReq, type, input, output, card.get("id").getAsInt()));

                resourceReq.clear();
                devCardReq.clear();
                input.clear();
                output.clear();

            }
        }
    }

    /**
     * Shuffles the deck
     */
    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Draws from the deck
     * @return Leader Card on top of the deck
     */
    public LeaderCard draw() {
        return deck.remove(0);
    }
}
