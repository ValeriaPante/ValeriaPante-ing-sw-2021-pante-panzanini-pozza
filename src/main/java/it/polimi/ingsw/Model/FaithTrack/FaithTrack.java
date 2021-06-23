package it.polimi.ingsw.Model.FaithTrack;

import com.google.gson.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Faith Track representation
 */
public class FaithTrack {

    private final int length;
    private final SmallPath[] smallPaths;
    private final VaticanRelation[] vaticanRelations;

    /**
     * Evaluator of a position
     * @param pos position to evaluate
     * @return victory points associated to this position or the max points if the position is too big or too small
     */
    public int victoryPoints(int pos){
        for (SmallPath elem:smallPaths){
            if (elem.isIn(pos)){
                return elem.getVictoryPoints();
            }
        }
        return this.smallPaths[this.smallPaths.length-1].getVictoryPoints();
    }

    /**
     * Sets a VaticanRelation to done
     * @param idVaticanRelation the id of the vatican Relation to set to done
     * @throws IllegalArgumentException if there in no VaticanRelation with that id
     */
    public void doneVaticanRelation(int idVaticanRelation) throws IllegalArgumentException{
        for (VaticanRelation vaticanRelation: this.vaticanRelations){
            if (vaticanRelation.getId() == idVaticanRelation){
                vaticanRelation.done();
                return;
            }
        }
        throw new IllegalArgumentException("No vatican relation selected");
    }

    /**
     * Getter
     * @return a nested copy of the VaticanRelations in this FaithTrack
     */
    public VaticanRelation[] getVaticanRelations(){
        ArrayList<VaticanRelation> copy = new ArrayList<>();
        for (VaticanRelation vaticanRelation : this.vaticanRelations){
            copy.add(vaticanRelation.clone());
        }
        return copy.toArray(new VaticanRelation[0]);
    }

    /**
     * Getter
     * @return this FaithTrack length
     */
    public int getLength(){
        return this.length;
    }

    /**
     * Evaluator of a position
     * @param pos position to avaluate
     * @return true if position is equals or higher of the length of this FaithTrack
     */
    public boolean finished(int pos){
        return pos >= length;
    }

    /**
     * FaithTrack constructor
     * Builds the faithTrack based on the json file inside the jar folder + /accessible/JSONs/FaithTrackConfig.json
     * @throws IllegalArgumentException if the file is not present or the file is not in the correct format
     */
    public FaithTrack() throws IllegalArgumentException{
        Path path;
        String config;
        try {
            path = Paths.get(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + File.separator + "accessible" + File.separator + "JSONs" + File.separator + "FaithTrackConfig.json");
        }catch (URISyntaxException e){
            throw new IllegalArgumentException("Unable to find the file Path");
        }
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
