package it.polimi.ingsw.Model.Decks;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DevDeck implements Deck{
    private List<DevCard> deck;
    private final DevCardType type;

    public DevDeck(DevCardType t) throws IllegalArgumentException{
        deck = new ArrayList<>();
        type = new DevCardType(t.getLevel(), t.getColor());

        if(t == null || t.getColor() == null || (t.getLevel() <= 0 || t.getLevel() > 3)){
            throw new IllegalArgumentException();
        }

        Path path = Paths.get(this.getClass().getResource("/JSONs/DevCardsConfig.json").toString().substring(6));
        String config;

        try {
            config = Files.readString(path, StandardCharsets.UTF_8);
        }
        catch (IOException e){
            throw new IllegalArgumentException("Error during the reading of the config file");
        }

        EnumMap<Resource, Integer> cost = new EnumMap<>(Resource.class);
        EnumMap<Resource, Integer> input = new EnumMap<>(Resource.class);
        EnumMap<Resource, Integer> output = new EnumMap<>(Resource.class);

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(config);
        if (!elements.isJsonObject()){
            throw new IllegalArgumentException("Check the config file and his syntax");
        }
        else{
            JsonObject object = elements.getAsJsonObject();
            JsonArray cards = object.getAsJsonArray(t.getLevel()+t.getColor().toString());
            for (int i = 0; i < cards.size(); i++) {
                JsonObject card = cards.get(i).getAsJsonObject();

                HashMap<Resource, Integer>map = gson.fromJson(card.get("cost"),new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                    cost.put(entry.getKey(), entry.getValue());
                }

                JsonObject prodpower = card.getAsJsonObject("prodpower");

                map = gson.fromJson(prodpower.get("input"),new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                    input.put(entry.getKey(), entry.getValue());
                }

                map = gson.fromJson(prodpower.get("output"),new TypeToken<HashMap<Resource, Integer>>(){}.getType());
                for(Map.Entry<Resource, Integer> entry: map.entrySet()){
                    output.put(entry.getKey(), entry.getValue());
                }

                deck.add(new DevCard(card.get("victoryPoints").getAsInt(), cost.clone(), type, new ProductionPower(input.clone(), output.clone()), card.get("id").getAsInt()));

                cost.clear();
                input.clear();
                output.clear();
            }
        }

        Collections.shuffle(deck);
    }

    public DevCardType getType(){
        return new DevCardType(type.getLevel(), type.getColor());
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public DevCard draw() {
        return deck.remove(0);
    }

    public DevCard getTopCard(){
        return this.deck.get(0).clone();
    }

    public boolean isEmpty(){
        return this.deck.isEmpty();
    }

    public int size(){ return this.deck.size(); }

    public void selectTopCard() throws IndexOutOfBoundsException{
        deck.get(0).select();
    }
}
