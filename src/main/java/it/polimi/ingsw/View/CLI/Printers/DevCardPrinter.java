package it.polimi.ingsw.View.CLI.Printers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.View.CLI.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This class is used to print on terminal everything linked to development cards:
 * development slots, the grill of development cards on the table and development cards themselves.
 * The cards will be read from a ".json" file named "DevCardsConfig.json"
 */
public class DevCardPrinter extends MapPrinter {
    private final Gson gson;
    private final JsonObject devCards;
    private int level;
    private String color;

    /**
     * Opens the ".json" file containing the development cards
     */
    public DevCardPrinter(){
        InputStream in = getClass().getResourceAsStream("/accessible/JSONs/DevCardsConfig.json");
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

    /**
     * Prints on terminal the development card with "id" corresponding to "id"
     * @param id unique identifier of the development card that has to be printed (it must be ranging from 1 to 48)
     */
    public void printFromID(int id){
        JsonObject card = getCardFromID(id);
        System.out.print("\n" +
                "     |¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯| \n" +
                "     | " + Color.colourText("DEVELOPMENT CARD", "YELLOW")+ " |\n" +
                "     |                  |\n" +
                "     |                ID: "+ Color.colourInt(id, "YELLOW") + "\n" +
                "     |              COST: " );
        printMapCompact(gson.fromJson(card.get("cost"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
        System.out.print(
                "     |    VICTORY POINTS: " + card.get("victoryPoints").getAsInt() +"\n" +
                "     |             COLOR: "+ color + "\n" +
                "     |             LEVEL: "+ level + "\n" +
                "     |             INPUT: ");
        JsonObject power = card.get("prodpower").getAsJsonObject();
        printMapCompact(gson.fromJson(power.get("input"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
        System.out.print(
                "     |            OUTPUT: ");
        printMapCompact(gson.fromJson(power.get("output"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
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

    /**
     * Prints on terminal the development slots passed as parameter
     * @param devSlots it is a matrix 3x3: each row is referred to the content of a development slot
     *                 (the ids of the development cards contained in it). The slots are considered
     *                 having a positional number starting from zero and increasing from left to right
     *                 on the player's dashboard
     * @param topCardOnly if "true" this method will print only the top card, if present,
     *                    for each development slot. If "false" it will print the whole content of each slot
     */
    public void printDevSlots(int[][] devSlots, boolean topCardOnly){
        for(int i=0; i < 3; i++){
            System.out.println("Development slot number " + (i+1) + ":");
            printSingleDevSlot(devSlots[i], topCardOnly);
        }
    }

    /**
     * Prints on terminal only the development slot passed as parameter
     * @param singleDevSlot it is an array of length equals to 3 containing the ids of the development cards contained in it
     * @param topCardOnly if "true" this method will print only the top card, if present,
     *                    for the development slot specified. If "false" it will print the whole content of the slot
     */
    public void printSingleDevSlot(int[] singleDevSlot, boolean topCardOnly){
        if(singleDevSlot[0] == 0){
            printEmptySlot();
        } else {
            for (int i=2; i>=0; i--){
                if (singleDevSlot[i] != 0) {
                    printFromID(singleDevSlot[i]);
                    if (topCardOnly)
                        return;
                }
            }
        }
    }

    private void printEmptySlot(){
        System.out.println("\n" +
                "     + - + - + - + - + -\n" +
                "     -                 +\n" +
                "     +                 -\n" +
                "     -      EMPTY      +\n" +
                "     +                 -\n" +
                "     -                 +\n" +
                "     +      SLOT       - \n" +
                "     -                 +\n" +
                "     +                 -\n" +
                "     - + - + - + - + - +\n");
    }

    /**
     * Prints on terminal the decks of development cards placed n the table
     * @param devDecks it is a 3x4 matrix containing the ids of the development cards
     *                 as displayed on the table
     */
    public void printDevDecks(int[][] devDecks){
        System.out.println("\n" + Color.colourText("HERE ARE THE DEVELOPMENT CARDS PLACED ON THE TABLE!", "YELLOW"));
        for (int i=0; i <= 2; i++){
            System.out.println("\n" + "Level " + (i+1) + " development cards:");
            for (int j=0; j <= 3; j++){
                if (devDecks[i][j] == 0){
                    printEmptySlot();
                } else {
                    printFromID(devDecks[i][j]);
                }
            }
        }
    }
}
