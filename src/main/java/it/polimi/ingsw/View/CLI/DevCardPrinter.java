package it.polimi.ingsw.View.CLI;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Enums.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DevCardPrinter extends mapPrinter{
    Gson gson;
    JsonObject devCards;
    int level;
    String color;

    public DevCardPrinter(){
        InputStream in = getClass().getResourceAsStream("/JSONs/DevCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String config = reader.lines().collect(Collectors.joining());

        gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(config);
        if (!elements.isJsonObject()){
            throw new IllegalArgumentException("Check the config file and his syntax");
        }
        devCards = elements.getAsJsonObject();
    }

    public void printFromID(int id){
        JsonObject card = getCardFromID(id);
        System.out.print("\n" +
                "     |¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯| \n" +
                "     | " + Color.colourText("DEVELOPMENT CARD", "YELLOW")+ " |\n" +
                "     |                  |\n" +
                "     |                ID: "+ Color.colourInt(id, "YELLOW") + "\n" +
                "     |              COST: " );
        printMap(gson.fromJson(card.get("cost"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
        System.out.print(
                "     |    VICTORY POINTS: " + card.get("victoryPoints").getAsInt() +"\n" +
                "     |             COLOR: "+ color + "\n" +
                "     |             LEVEL: "+ level + "\n" +
                "     |             INPUT: ");
        JsonObject power = card.get("prodpower").getAsJsonObject();
        printMap(gson.fromJson(power.get("input"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
        System.out.print(
                "     |            OUTPUT: ");
        printMap(gson.fromJson(power.get("output"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
        System.out.println(
                "     |__________________|" + "\n");
    }

    private JsonObject getCardFromID(int id){
        String levelAndColor;

        // cards with id from 1 to 16 are level 1, from 17 to 32 are level 2 and from 33 to 48 are level 3
        level = ((id -1) /16) +1;

        switch (id % 4){
            case 0:
                color = "YELLOW";
                  break;
            case 1:
                color = "GREEN";
                break;
            case 2:
                color = "PURPLE";
                break;
            case 3:
                color = "BLUE";
                break;
            }
        levelAndColor = level + color;


        JsonArray cards = devCards.getAsJsonArray(levelAndColor);
        JsonObject card;
        for (int i = 0; i < cards.size(); i++) {
            card = cards.get(i).getAsJsonObject();
            if(card.get("id").getAsInt() == id)
                return card;
        }

        return null;
    }
}
