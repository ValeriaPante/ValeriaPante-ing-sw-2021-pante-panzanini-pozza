package it.polimi.ingsw.View.ServerGUI;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FaithTrackPersonalizationScene extends CustomScenes{

    private Scene faithTrackPersonalizationScene;
    private Pane root;
    private Text smallPathsText;
    private ThreeTextInput[] smallPaths;
    private Text vaticanRelationText;
    private TwoTextInput[] vaticanRelations;
    private Line separator;
    private Button nextButton;

    private Button showImageButton;
    private Button changeImageButton;

    private JsonObject faithTrackInfo;
    private String pathFaithTrackImage;

    private boolean allCorrect(){
        int one;
        int two;

        for (int i=0; i<this.smallPaths.length; i++){
            try{
                one = Integer.parseInt(this.smallPaths[i].getFirstInput());
                two = Integer.parseInt(this.smallPaths[i].getSecondInput());
                Integer.parseInt(this.smallPaths[i].getThirdInput());
            } catch (NumberFormatException e){
                return false;
            }
            if (i==0){
                if (one != 0){
                    return false;
                }
            }
            else{
                if (one != Integer.parseInt(smallPaths[i-1].getSecondInput()) + 1){
                    return false;
                }
            }
            if (two<one){
                return false;
            }
        }

        for (int i=0; i<this.vaticanRelations.length; i++){
            try{
                one = Integer.parseInt(vaticanRelations[i].getFirstInput());
                two = Integer.parseInt(vaticanRelations[i].getSecondInput());
            } catch (NumberFormatException e){
                return false;
            }
            if (two<one){
                return false;
            }
        }

        return true;
    }

    private void finish(){
        JsonObject faithTrackModified = new JsonObject();
        //length
        faithTrackModified.add("length", this.faithTrackInfo.get("length").getAsJsonPrimitive());

        //smallPaths
        JsonArray smallPaths = new JsonArray();
        for (ThreeTextInput smallPath : this.smallPaths){
            JsonObject smallPathJson = new JsonObject();
            smallPathJson.add("posStart", new JsonPrimitive(Integer.parseInt(smallPath.getFirstInput())));
            smallPathJson.add("posEnd", new JsonPrimitive(Integer.parseInt(smallPath.getSecondInput())));
            smallPathJson.add("victoryPoints", new JsonPrimitive(Integer.parseInt(smallPath.getThirdInput())));
            smallPaths.add(smallPathJson);
        }
        faithTrackModified.add("smallPaths", smallPaths);

        //vatican Relations
        JsonArray vaticanRelations = new JsonArray();
        for (int i=0; i<this.vaticanRelations.length; i++){
            JsonObject vaticanRelation = new JsonObject();
            vaticanRelation.add("posStart", new JsonPrimitive(Integer.parseInt(this.vaticanRelations[i].getFirstInput())));
            vaticanRelation.add("posPope", new JsonPrimitive(Integer.parseInt(this.vaticanRelations[i].getSecondInput())));
            vaticanRelation.add("id", new JsonPrimitive(i));
            vaticanRelations.add(vaticanRelation);
        }
        faithTrackModified.add("vaticanRelations", vaticanRelations);

        //copiare il json
        String serverPath = null;
        try {
            serverPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        }catch (URISyntaxException e){
            //pass
        }

        Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();
        String output = prettyPrinting.toJson(faithTrackModified);
        File outputDir = new File(serverPath + File.separator + "accessible" + File.separator + "JSONs" + File.separator );
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }
        try {
            FileOutputStream jsonFile = new FileOutputStream(outputDir.getAbsolutePath() + File.separator + "FaithTrackConfig.json");
            jsonFile.write(output.getBytes());
            jsonFile.close();
        }catch (IOException e){
            //sincero bho
        }

        try {
            InputStream imgInputStream;
            if (this.pathFaithTrackImage.equals("default")){
                imgInputStream = this.getClass().getResourceAsStream("/accessible/assets/imgs/colored.png");
            }else{
                imgInputStream = new FileInputStream(this.pathFaithTrackImage);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(serverPath + File.separator + "accessible" + File.separator + "assets" + File.separator + "imgs" + File.separator + "colored.png");
            int count;
            byte[] buffer = new byte[1024];
            try {
                while ((count = imgInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }
            } catch (IOException ignored) {
            }
            imgInputStream.close();
            fileOutputStream.close();
        }catch (IOException ignored){}
        super.closeStage();
    }

    private void setOnActions(){
        this.nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (allCorrect()){
                    finish();
                }
            }
        });
        this.showImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setDisable(true);
                Stage imagStage = new Stage();
                Pane pane = new Pane();
                ImageView faithTrackImage = new ImageView();
                faithTrackImage.setPreserveRatio(true);
                faithTrackImage.setFitWidth(1000);
                faithTrackImage.setFitHeight(1000);
                InputStream imageIS;
                if (pathFaithTrackImage.equals("default")){
                    imageIS = this.getClass().getResourceAsStream("/accessible/assets/imgs/colored.png");
                }else{
                    try {
                        imageIS = new FileInputStream(pathFaithTrackImage);
                    }catch (IOException e){
                        pathFaithTrackImage = "default";
                        imageIS = this.getClass().getResourceAsStream("/accessible/assets/imgs/colored.png");
                    }
                }
                faithTrackImage.setImage(new Image(imageIS));
                pane.getChildren().add(faithTrackImage);
                Scene faithTrackImageScene = new Scene(pane);
                imagStage.setScene(faithTrackImageScene);
                imagStage.showAndWait();
                root.setDisable(false);
            }
        });
        this.changeImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setDisable(true);
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose new image");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Image Files", "*.png")
                );
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile!=null){
                    try {
                        pathFaithTrackImage = selectedFile.getAbsolutePath();
                        String path = selectedFile.toURI().toURL().toExternalForm();
                        //System.out.println(path);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                root.setDisable(false);
            }
        });
    }

    private void modifyScene(){
        JsonArray smallPaths = this.faithTrackInfo.getAsJsonArray("smallPaths");
        for (int i=0; i<smallPaths.size(); i++){
            JsonObject smallPath = smallPaths.get(i).getAsJsonObject();
            this.smallPaths[i].setFirstInput(smallPath.get("posStart").getAsString());
            this.smallPaths[i].setSecondInput(smallPath.get("posEnd").getAsString());
            this.smallPaths[i].setThirdInput(smallPath.get("victoryPoints").getAsString());
        }

        JsonArray vaticanRelations = this.faithTrackInfo.getAsJsonArray("vaticanRelations");
        for (int i=0; i<vaticanRelations.size(); i++){
            JsonObject vaticanRelation = vaticanRelations.get(i).getAsJsonObject();
            this.vaticanRelations[i].setFirstInput(vaticanRelation.get("posStart").getAsString());
            this.vaticanRelations[i].setSecondInput(vaticanRelation.get("posPope").getAsString());
        }
    }

    private void readJson(){
        this.pathFaithTrackImage = "default";
        JsonParser parser = new JsonParser();
        InputStream inputStream = this.getClass().getResourceAsStream("/accessible/JSONs/FaithTrackConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.faithTrackInfo = parser.parse(reader.lines().collect(Collectors.joining())).getAsJsonObject();
        try{
            reader.close();
        }catch (IOException e){
            //
        }
    }

    private void positionTextInput(TwoTextInput[] elements, double xPos, double yPos){
        for (int i=0; i<elements.length; i++){
            if (i==0){
                elements[i].setPosition(xPos, yPos);
            }
            else if (i%5 == 0){
                elements[i].setPosition(xPos + elements[i-1].getWidth() + 50, yPos);
            }
            else{
                elements[i].setPosition(elements[i-1].getX(), elements[i-1].getY() + elements[i-1].getHeight() + 20);
            }
        }
    }

    private void positionGraphics(){
        this.smallPathsText.setFont(new Font(20));
        this.smallPathsText.setX(20); this.smallPathsText.setY(30);

        Arrays.stream(smallPaths).forEach(threeTextInput -> threeTextInput.setFontSizes(17));
        this.positionTextInput(this.smallPaths, this.smallPathsText.getX(), this.smallPathsText.getY()+40);

        this.separator.setStrokeWidth(3);
        this.separator.setStartX(this.smallPaths[this.smallPaths.length-1].getX() + this.smallPaths[this.smallPaths.length-1].getWidth() + 40);
        this.separator.setStartY(this.smallPaths[0].getY());
        this.separator.setEndX(this.smallPaths[this.smallPaths.length-1].getX() + this.smallPaths[this.smallPaths.length-1].getWidth() + 40);
        this.separator.setEndY(this.smallPaths[4].getY() + this.smallPaths[4].getHeight());
        this.separator.setTranslateY(-20);

        this.vaticanRelationText.setFont(new Font(20));
        this.vaticanRelationText.setX(this.separator.getStartX() + 40);
        this.vaticanRelationText.setY(this.smallPathsText.getY());

        Arrays.stream(vaticanRelations).forEach(twoTextInput -> twoTextInput.setFontSizes(17));
        this.positionTextInput(this.vaticanRelations, this.vaticanRelationText.getX(), this.vaticanRelationText.getY()+40);

        this.nextButton.setFont(new Font(14));
        this.nextButton.setLayoutX(this.vaticanRelations[0].getX() + this.vaticanRelations[0].getWidth());
        this.nextButton.setLayoutY(this.separator.getEndY());

        this.showImageButton.setFont(new Font(14));
        this.showImageButton.setLayoutX(this.smallPaths[0].getX());
        this.showImageButton.setLayoutY(this.smallPaths[4].getY() + this.smallPaths[4].getHeight() + 20);
        this.changeImageButton.setFont(new Font(14));
        this.changeImageButton.setLayoutX(this.showImageButton.getLayoutX() + this.showImageButton.getText().length()*this.showImageButton.getFont().getSize() );
        this.changeImageButton.setLayoutY(this.showImageButton.getLayoutY());
    }

    private void buildGraphic(){
        this.root = new Pane();
        this.root.setPadding(new Insets(20));

        this.smallPathsText = new Text("Faith Track sections:");
        int smallPathsAmount = this.faithTrackInfo.getAsJsonArray("smallPaths").size();
        this.smallPaths = new ThreeTextInput[smallPathsAmount];
        for (int i=0; i<smallPathsAmount; i++){
            this.smallPaths[i] = new ThreeTextInput((i+1)+"° Start", (i+1)+"° End", (i+1)+"° Victory points");
        }

        this.separator = new Line();

        this.vaticanRelationText = new Text("Vatican Relation sections:");
        int vaticanRelationsAmount = this.faithTrackInfo.getAsJsonArray("vaticanRelations").size();
        this.vaticanRelations = new TwoTextInput[vaticanRelationsAmount];
        for (int i=0; i<vaticanRelationsAmount; i++){
            this.vaticanRelations[i] = new TwoTextInput((i+1)+"° Start", (i+1)+"° Pope position");
        }

        this.nextButton = new Button("Next");

        this.showImageButton = new Button("Show image");
        this.changeImageButton = new Button("Change Image");

        this.root.getChildren().addAll(this.smallPathsText, this.separator, this.vaticanRelationText, this.nextButton);
        Arrays.stream(this.smallPaths).forEach(threeTextInput -> threeTextInput.addToPane(this.root));
        Arrays.stream(this.vaticanRelations).forEach(twoTextInput -> twoTextInput.addToPane(this.root));
        this.root.getChildren().addAll(this.showImageButton, this.changeImageButton);
        this.faithTrackPersonalizationScene = new Scene(this.root);
    }

    public FaithTrackPersonalizationScene(Stage stage) {
        super(stage);
        this.readJson();
        this.buildGraphic();
        this.positionGraphics();
        this.setOnActions();
        this.modifyScene();
    }

    @Override
    public Scene getScene() {
        return this.faithTrackPersonalizationScene;
    }
}
