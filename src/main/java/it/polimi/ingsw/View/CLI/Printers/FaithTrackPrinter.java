package it.polimi.ingsw.View.CLI.Printers;

import com.google.gson.*;
import it.polimi.ingsw.View.CLI.Color;
import it.polimi.ingsw.View.CLI.SimplifiedFaithTrack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to print on the terminal the faith track and additional information
 */
public class FaithTrackPrinter {
    private final JsonArray smallPaths;
    private final JsonArray vaticanRelations;

    public FaithTrackPrinter(){
        InputStream in = getClass().getResourceAsStream("/accessible/JSONs/FaithTrackConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String config = reader.lines().collect(Collectors.joining());

        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(config);
        if (!elements.isJsonObject())
            throw new IllegalArgumentException("Check the config file and his syntax");
        smallPaths = elements.getAsJsonObject().get("smallPaths").getAsJsonArray();
        vaticanRelations = elements.getAsJsonObject().get("vaticanRelations").getAsJsonArray();
    }

    /**
     * Prints on terminal the faith tack and the positions of the players on it.
     * For each player will be displayed both the username and the id
     * so it will be clare tha position of each player even if two of them have the same username
     * @param playersID a list of the ids of the players (the order is not important)
     * @param playersUsernames a list of the usernames of the players (the order must be tha same used in "playersID" parameter)
     * @param simplifiedFaithTrack is the simplified faith track used in the game
     */
    public void printTrack(List<Integer> playersID, List<String> playersUsernames, SimplifiedFaithTrack simplifiedFaithTrack){
        System.out.println( "\n" +
                " _____  ___  ___  ___  ___  ___  ___  ___  ___  ___  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____  ____   \n" +
                "|"+ Color.colourText("START", "YELLOW") +"|| 1 || 2 || 3 || 4 || 5 || 6 || 7 || 8 || 9 || 10 || 11 || 12 || 13 || 14 || 15 || 16 || 17 || 18 || 19 || 20 || 21 || 22 || 23 || 24 |\n" +
                " ¯¯¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯  ¯¯¯¯");
        System.out.println("Here are the positions on the faith track:");
        for (int i=0; i< playersID.size(); i++){
            System.out.println(Color.colourText(playersUsernames.get(i),"CYAN") +
                    " (ID:" + Color.colourInt(playersID.get(i),"PURPLE") + ") : " +
                    (simplifiedFaithTrack.getPosition(playersID.get(i)) == 0 ? "BEGINNING" : simplifiedFaithTrack.getPosition(playersID.get(i))) );
        }
    }

    /**
     * Prints on terminal the mapping of the points for the positions on the faith track
     * used in this precise game
     */
    public void printPointsForPosition(){
        System.out.println("\n" + "VICTORY POINTS: (not cumulative)");
        for (int i=0; i<smallPaths.size(); i++){
            System.out.println("+ " +
                    Color.colourInt(smallPaths.get(i).getAsJsonObject().get("victoryPoints").getAsInt(), "YELLOW") +
                    " from position " +
                    Color.colourInt(smallPaths.get(i).getAsJsonObject().get("posStart").getAsInt(), "YELLOW") +
                    " to " +
                    Color.colourInt(smallPaths.get(i).getAsJsonObject().get("posEnd").getAsInt(), "YELLOW") + ";");
        }
        System.out.print("\n");
    }

    /**
     * Prints on terminal the the mapping of the points for the vatican relations for this precise game
     * and the state of the Pope favor card of each player, displaying both their usernames and their ids
     * @param playersID a list of the ids of the players (the order is not important)
     * @param playersUsernames a list of the usernames of the players (the order must be tha same used in "playersID" parameter)
     *  @param simplifiedFaithTrack is the simplified faith track used in the game
     */
    public void printVaticanRelations(List<Integer> playersID, List<String> playersUsernames, SimplifiedFaithTrack simplifiedFaithTrack){
        System.out.println("\n" + "VATICAN RELATIONS: ");
        for (int i=0; i<vaticanRelations.size(); i++){
            System.out.println(" - number " +
                    Color.colourInt((vaticanRelations.get(i).getAsJsonObject().get("id").getAsInt()+1), "YELLOW") +
                    ": starts at " +
                    Color.colourInt(vaticanRelations.get(i).getAsJsonObject().get("posStart").getAsInt(), "YELLOW") +
                    ", ends at " +
                    Color.colourInt(vaticanRelations.get(i).getAsJsonObject().get("posPope").getAsInt(), "YELLOW") + ";");
        }

        System.out.print("\n");

        System.out.println("Here are the pope favor cards of the player" + (playersID.size() == 1 ? ":" : "s:"));
        for (int i=0; i< playersID.size(); i++){
            System.out.println(Color.colourText(playersUsernames.get(i),"CYAN") +
                    " (ID:" + Color.colourInt(playersID.get(i),"PURPLE") + ") : ");
            System.out.print("\t");
            for (int j=0; j<3; j++){
                switch(simplifiedFaithTrack.getCardsState(playersID.get(i))[j]){
                    case FACEDOWN:
                        System.out.print("\t\t" + (j+1) + ": face down");
                        break;
                    case FACEUP:
                        System.out.print("\t\t" + (j+1) + ": face up  ");
                        break;
                    case DISABLED:
                        System.out.print("\t\t" + (j+1) + ": discarded");
                        break;
                    default:
                        break;
                }
            }
            System.out.println("\n");
        }
        System.out.print("\n");
    }
}
