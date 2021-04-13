package it.polimi.ingsw.FaithTrack;

import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FaithTrack {

    private final int length;
    private final SmallPath[] smallPaths;
    private final VaticanRelation[] vaticanRelations;

    //chiedo alla faith track quanti punti sono associati ad una determinata posizione
    public int victoryPoints(int pos){
        for (SmallPath elem:smallPaths){
            if (elem.isIn(pos)){
                return elem.getVictoryPoints();
            }
        }
        return 20;
    }

    public void doneVaticanRelation(int idVaticanRelation) throws IllegalArgumentException{
        for (VaticanRelation vaticanRelation: this.vaticanRelations){
            if (vaticanRelation.getId() == idVaticanRelation){
                vaticanRelation.done();
                return;
            }
        }
        throw new IllegalArgumentException("No vatican relation selected");
    }

    public VaticanRelation[] getVaticanRelations(){
        ArrayList<VaticanRelation> copy = new ArrayList<>();
        for (VaticanRelation vaticanRelation : this.vaticanRelations){
            copy.add(vaticanRelation.clone());
        }
        return copy.toArray(new VaticanRelation[0]);
    }

    public int getLength(){
        return this.length;
    }

    //ritorna true se la posizione supera la lunghezza del percorso
    public boolean finished(int pos){
        return pos >= length;
    }

    //costruttore
    public FaithTrack() throws IllegalArgumentException{
        Path path = Paths.get("C:\\Users\\Daniel\\IdeaProjects\\ing-sw-2021-pante-panzanini-pozza\\src\\main\\java\\it\\polimi\\ingsw\\Configs\\FaithTrackConfig.json");
        String config;

        try {
            config = Files.readString(path, StandardCharsets.UTF_8);
        }
        catch (IOException e){
            throw new IllegalArgumentException("Error during the reading of the config file");
        }

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(config);
        if (!element.isJsonObject()){
            throw new IllegalArgumentException("Check the config file and his syntax");
        }
        else{
            JsonObject FaithTrack = element.getAsJsonObject();
            this.length = FaithTrack.get("length").getAsInt();
            JsonArray jsonArray = FaithTrack.getAsJsonArray("smallPaths");
            this.smallPaths = gson.fromJson(jsonArray, SmallPath[].class);
            this.vaticanRelations = gson.fromJson(FaithTrack.getAsJsonArray("vaticanRelations"), VaticanRelation[].class);
        }
    }
}
