package it.polimi.ingsw.View.ServerGUI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FaithTrackPersonalizationScene extends CustomScenes{

    private Scene faithTrackPersonalizationScene;
    private TwoTextInput[] smallPaths;
    private ThreeTextInput[] vaticanRelations;

    private JsonObject faithTrackInfo;

    private void readJson(){
        JsonParser parser = new JsonParser();
        InputStream inputStream = this.getClass().getResourceAsStream("/accessible/JSONs/LeaderCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.faithTrackInfo = parser.parse(reader.lines().collect(Collectors.joining())).getAsJsonObject();
        try{
            reader.close();
        }catch (IOException e){
            //
        }
    }

    private void buildGraphic(){
        int smallPathsAmount = this.faithTrackInfo.getAsJsonArray("smallPaths").size();
        this.smallPaths = new TwoTextInput[smallPathsAmount];
        for (int i=0; i<smallPathsAmount; i++){
            this.smallPaths[i] = new TwoTextInput();
        }
        //int vaticanRelationsAmount =
    }

    public FaithTrackPersonalizationScene(Stage stage) {
        super(stage);
        this.readJson();
        this.buildGraphic();
    }

    @Override
    public Scene getScene() {
        return this.faithTrackPersonalizationScene;
    }
}
