package it.polimi.ingsw.Model.Decks;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Representation of development deck
 */
public class DevDeck implements Deck{
    private List<DevCard> deck;
    private final DevCardType type;

    /**
     * Constructs the deck from a JSON file
     * @param t type of all the card contained in the deck
     * @throws IllegalArgumentException if the type is null or its attributes are not allowed
     */
    public DevDeck(DevCardType t) throws IllegalArgumentException{
        deck = new ArrayList<>();
        type = new DevCardType(t.getLevel(), t.getColor());

        if(t == null || t.getColor() == null || (t.getLevel() <= 0 || t.getLevel() > 3)){
            throw new IllegalArgumentException();
        }

        Path path;
        String config;
        try {
            path = Paths.get(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + File.separator + "accessible" + File.separator + "JSONs" + File.separator + "DevCardsConfig.json");
        }catch (URISyntaxException e){
            throw new IllegalArgumentException("Unable to find the file Path");
        }
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

    /**
     * Type of the deck getter
     * @return type (color-level) of the deck
     */
    public DevCardType getType(){
        return new DevCardType(type.getLevel(), type.getColor());
    }

    /**
     * Shuffles the deck
     */
    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Draws a card from the deck
     * @return development card on top of the deck
     */
    public DevCard draw() {
        return deck.remove(0);
    }

    /**
     * Top card of the deck
     * @return a copy of the development card on top of the deck
     */
    public DevCard getTopCard(){
        if(this.isEmpty()) return null;
        return this.deck.get(0).clone();
    }

    /**
     * Checks if the deck is empty
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty(){
        return this.deck.isEmpty();
    }

    /**
     * Size of the deck getter
     * @return number of card contained by the deck
     */
    public int size(){ return this.deck.size(); }

    /**
     * Selects top card of the deck
     * @throws IndexOutOfBoundsException if the deck is empty
     */
    public void selectTopCard() throws IndexOutOfBoundsException{
        deck.get(0).select();
    }
}
