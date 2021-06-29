package it.polimi.ingsw.View.CLI.Printers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.CLI.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to print on terminal the leader cards written down in the ".json" file named: "LeaderCardsConfig.json"
 */
public class LeaderCardPrinter extends MapPrinter {
    private final Gson gson;
    private final JsonArray array;
    private JsonObject currentCard;

    /**
     * Opens the ".json" file containing the leader cards
     */
    public LeaderCardPrinter(){
        InputStream in = getClass().getResourceAsStream("/accessible/JSONs/LeaderCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String config = reader.lines().collect(Collectors.joining());

        gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(config);
        if (!elements.isJsonArray())
            throw new IllegalArgumentException("Check the config file and his syntax");

        array = elements.getAsJsonArray();
        currentCard = null;
    }

    /**
     * Prints the leader card with the "id" specified as parameter
     * @param id unique identifier of the leader card that has to be printed (it must be ranging from 49 to 64)
     * @param content it is an array of the resources contained in the leader card:
     *                so it is null if the leader card has not got a leader storage ability or
     *                if it has got but it is empty. Otherwise, it is not null
     */
    public void printFromID(int id, Resource[] content){
        for (int i=0; i < array.size(); i++) {
            JsonObject card = array.get(i).getAsJsonObject();
            if (card.get("id").getAsInt() == id) {
                currentCard = card;
                System.out.print("\n" +
                        "     |¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n" +
                        "     | "+ Color.colourText(" LEADER CARD ", "YELLOW") +" |\n" +
                        "     |               |\n" +
                        "     |             ID: " + Color.colourInt( id, "YELLOW") + "\n" +
                        "     |           COST: ");

                printCost();

                System.out.println(
                        "     | VICTORY POINTS: " + card.get("victoryPoints").getAsString());

                printAbility(content);

                System.out.println(
                        "     |_______________|");

                currentCard = null;
                break;
            }
        }
    }

    private void printAbility(Resource[] content){
        String leaderCardType = currentCard.get("LeaderCardType").getAsString();

        System.out.println(
                "     |   ABILITY TYPE: " + leaderCardType);
        switch (leaderCardType){
            case "DISCOUNT":
                System.out.print("     |       PAY LESS: ");
                printMapCompact(gson.fromJson(currentCard.get("discount"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                break;

            case "PRODPOWER":
                System.out.print("     |          INPUT: ");
                printMapCompact(gson.fromJson(currentCard.get("prodpower").getAsJsonObject().get("input"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                System.out.print("     |         OUTPUT: ");
                printMapCompact(gson.fromJson(currentCard.get("prodpower").getAsJsonObject().get("output"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                break;

            case "TRANSMUTATION":
                System.out.print("     |TURN WHITE INTO: ");
                printMapCompact(gson.fromJson(currentCard.get("transmutation"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                break;

            case "STORAGE":
                System.out.print("     |       CAPACITY: ");
                printMapCompact(gson.fromJson(currentCard.get("storage"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                System.out.print("     |        CONTENT: ");
                if (content == null || content.length == 0)
                    System.out.println("nothing");
                else{
                    boolean commaNeeded = false;
                    for (int i=0; i< content.length; i++){
                        if (commaNeeded)
                            System.out.print(",");
                        if (!commaNeeded)
                            commaNeeded = true;
                        if (content[i] == null)
                            System.out.print(" __");
                        else
                            System.out.print(Resource.toAliasCompact(content[i]));
                    }
                    System.out.print("\n");
                }
                break;
        }
    }

    private void printCost(){
        Map<Resource, Integer> cost = gson.fromJson(currentCard.get("resourceReq"), new TypeToken<HashMap<Resource, Integer>>(){}.getType());
        if (!cost.isEmpty()){
            printMapCompact(cost);
            return;
        }

        boolean commaNeeded = false;
        JsonArray devCost = currentCard.get("devCardReq").getAsJsonArray();
        JsonObject devCard;
        for (int i=0; i < devCost.size(); i++){
            devCard= devCost.get(i).getAsJsonObject();
            if (commaNeeded)
                System.out.print(", ");
            else
                commaNeeded = true;
            System.out.print(devCard.get("quantity")+ " x (");
            JsonObject devCardType = devCard.get("devCardType").getAsJsonObject();
            System.out.print(devCardType.get("color").getAsString() + " development card");
            if (devCardType.get("level").getAsInt() != 0)
                System.out.print(" of level " + devCardType.get("level"));
            System.out.print(")");
        }
        System.out.print("\n");
    }
}
