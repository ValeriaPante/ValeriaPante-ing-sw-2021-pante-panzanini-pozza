package it.polimi.ingsw.View.CLI.Printers;

import com.google.gson.*;
import it.polimi.ingsw.View.CLI.Color;
import it.polimi.ingsw.View.CLI.SimplifiedFaithTrack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

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
                        System.out.print("        " + (j+1) + ": face down");
                        break;
                    case FACEUP:
                        System.out.print("        " + (j+1) + ": face up");
                        break;
                    case DISABLED:
                        System.out.print("        " + (j+1) + ": discarded");
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
