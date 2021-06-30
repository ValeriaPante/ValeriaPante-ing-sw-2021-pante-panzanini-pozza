package it.polimi.ingsw.View.ServerGUI;

import com.google.gson.*;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.Resource;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DevCardPersonalizationScene extends CustomScenes{

    private final String[] order;
    private final Resource[] resourcesOrder;
    private String pathImageCard;

    private Image coinImage;
    private Image servantImage;
    private Image stoneImage;
    private Image shieldImage;
    private Image faithImage;
    private Image anyImage;

    private JsonObject devCardsJson;
    private int devCardLv;
    private int devCardType;
    private int devCardPos;
    private LinkedHashMap<String, JsonArray> modifiedDevCards;
    private JsonArray pathNewCards;

    private Scene devCardPersonalizationScene;
    private Pane root;
    private ImageView imageSlot;
    private Button changeImage;
    private Text victoryPoints;
    private TextField victoryPointsNum;
    private Text resourceRequired;
    private ImageDescription[] cost;

    private Text productionPower;
    private Text inputText;
    private ImageDescription[] input;
    private Text outputText;
    private ImageDescription[] output;

    private Button nextButton;


    private void positionImageDescription(ImageDescription[] imageDescriptions, double xPosStart, double yPosStart, int xSpacing, int ySpacing){
        for (int i=0; i<imageDescriptions.length; i++){
            if (i==0){
                imageDescriptions[i].setPosition(xPosStart, yPosStart + ySpacing);
            }
            else{
                imageDescriptions[i].setPosition(imageDescriptions[i-1].getImageX() + imageDescriptions[i-1].getImageFitWidth() + xSpacing, yPosStart + ySpacing);
            }
        }
    }

    private void modifyCost(JsonObject card){
        JsonObject resourcesReq = card.get("cost").getAsJsonObject();
        JsonElement amount;
        for (int i=0; i<this.cost.length; i++){
            amount = resourcesReq.get(this.resourcesOrder[i].toString());
            this.cost[i].setText((amount == null) ? "0" : amount.getAsString());
        }
    }

    private void modifyInOut(JsonObject input, ImageDescription[] descriptors){
        JsonElement amount;
        for (int i=0; i<descriptors.length; i++){
            amount = input.get(this.resourcesOrder[i].toString());
            descriptors[i].setText((amount == null) ? "0" : amount.getAsString());
        }
    }

    private void modifyScene(){
        this.pathImageCard = "default";
        JsonArray cards = this.devCardsJson.getAsJsonArray(this.devCardLv+this.order[this.devCardType]);
        JsonObject card = cards.get(this.devCardPos).getAsJsonObject();
        this.imageSlot.setImage(new Image(this.getClass().getResourceAsStream("/accessible/assets/imgs/" + card.get("id").getAsInt() + ".png")));
        this.victoryPointsNum.setText(card.get("victoryPoints").getAsString());
        this.modifyCost(card);
        this.modifyInOut(card.get("prodpower").getAsJsonObject().get("input").getAsJsonObject(), this.input);
        this.modifyInOut(card.get("prodpower").getAsJsonObject().get("output").getAsJsonObject(), this.output);
    }

    private JsonObject enumMapExtractor(ImageDescription[] descriptions, Enum[] map){
        JsonObject enumMap = new JsonObject();
        int amount;
        for (int i=0; i<descriptions.length; i++){
            amount = Integer.parseInt(descriptions[i].getText());
            if (amount>0){
                enumMap.add(map[i].toString(), new JsonPrimitive(amount));
            }
        }
        return enumMap;
    }

    private void update(){
        String deckType = this.devCardLv + this.order[devCardType];
        int cardId = this.devCardsJson.getAsJsonArray(deckType).get(this.devCardPos).getAsJsonObject().get("id").getAsInt();
        JsonObject pathDescriptor = new JsonObject();
        pathDescriptor.add("name", new JsonPrimitive(cardId+".png"));
        pathDescriptor.add("path", new JsonPrimitive(this.pathImageCard));
        this.pathNewCards.add(pathDescriptor);

        //building json object for card
        JsonObject card = new JsonObject();
        card.add("id", new JsonPrimitive(cardId));
        card.add("victoryPoints", new JsonPrimitive(Integer.parseInt(this.victoryPointsNum.getCharacters().toString())));
        card.add("cost", this.enumMapExtractor(this.cost, this.resourcesOrder));
        JsonObject prodPower = new JsonObject();
        prodPower.add("input", this.enumMapExtractor(this.input, this.resourcesOrder));
        prodPower.add("output", this.enumMapExtractor(this.output, this.resourcesOrder));
        card.add("prodpower", prodPower);

        JsonArray cards = this.modifiedDevCards.get(deckType);
        if (cards == null){
            cards = new JsonArray();
        }
        cards.add(card);
        this.modifiedDevCards.put(deckType, cards);
    }

    private void finish(){
        String serverPath = null;
        try {
            serverPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        }catch (URISyntaxException e){
            //pass
        }

        for (int i=0; i<this.pathNewCards.size(); i++){
            String relativePath = File.separator + "accessible" + File.separator + "assets" + File.separator + "imgs" + File.separator;
            File dir = new File(serverPath + relativePath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            JsonObject pathDescriptor = this.pathNewCards.get(i).getAsJsonObject();
            String name = pathDescriptor.get("name").getAsString();
            String path = pathDescriptor.get("path").getAsString();
            byte[] buffer = new byte[1024];
            InputStream imageStream;
            int count;
            try {
                if ("default".equals(path)) {
                    imageStream = this.getClass().getResourceAsStream("/accessible/assets/imgs/" + name);
                } else {
                    imageStream = new FileInputStream(path);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(serverPath + relativePath + name);
                while ((count = imageStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
                imageStream.close();
            }catch (IOException e){
                //sincero bho
                e.printStackTrace();
            }
        }

        //copiare il json
        Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();
        String output = prettyPrinting.toJson(this.modifiedDevCards);
        File outputDir = new File(serverPath + File.separator + "accessible" + File.separator + "JSONs" + File.separator);
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }
        try {
            FileOutputStream jsonFile = new FileOutputStream(outputDir.getAbsolutePath() + File.separator + "DevCardsConfig.json");
            jsonFile.write(output.getBytes());
            jsonFile.close();
        }catch (IOException e){
            //sincero bho
        }

        super.setNextScene(new FaithTrackPersonalizationScene(super.mainStage).getScene());
    }

    private boolean allCorrect(){
        try {
            int vp = Integer.parseInt(this.victoryPointsNum.getCharacters().toString());
            return vp <= 99 && vp >= 1;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private void setOnActions() {
        Arrays.stream(this.cost).forEach(ImageDescription::setDefaultOnClickHandler);
        Arrays.stream(this.input).forEach(ImageDescription::setDefaultOnClickHandler);
        Arrays.stream(this.output).forEach(ImageDescription::setDefaultOnClickHandler);

        this.nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (allCorrect()) {
                    update();
                    devCardPos++;
                    //System.out.println(devCardsJson.getAsJsonArray(devCardLv + order[devCardType]));
                    if (devCardPos >= devCardsJson.getAsJsonArray(devCardLv + order[devCardType]).size()){
                        devCardPos = 0;
                        devCardType ++;
                        if (devCardType >= order.length){
                            devCardType = 0;
                            devCardLv ++;
                        }
                    }

                    if (devCardsJson.get(devCardLv + order[devCardType]) == null){
                        finish();
                    }
                    else{
                        modifyScene();
                    }
                }
            }
        });
        this.changeImage.setOnAction(new EventHandler<ActionEvent>() {
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
                        pathImageCard = selectedFile.getAbsolutePath();
                        String path = selectedFile.toURI().toURL().toExternalForm();
                        //System.out.println(path);
                        imageSlot.setImage(new Image(path));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                root.setDisable(false);
            }
        });
    }

    private void positionGraphic(){
        this.imageSlot.setX(20); this.imageSlot.setY(20); this.imageSlot.setFitWidth((2*462)/3f); this.imageSlot.setFitHeight((2*698)/3f);
        this.imageSlot.setPreserveRatio(true);

        this.changeImage.setFont(new Font(14));
        this.changeImage.setLayoutX(this.imageSlot.getX() + this.imageSlot.getFitWidth()/2 - 50);
        this.changeImage.setLayoutY(this.imageSlot.getY()+this.imageSlot.getFitHeight() + 20);

        this.victoryPoints.setFont(new Font(19));
        this.victoryPoints.setX(this.imageSlot.getX() + this.imageSlot.getFitWidth() + 30);
        this.victoryPoints.setY(this.imageSlot.getY() + this.victoryPoints.getLayoutBounds().getHeight());

        this.victoryPointsNum.setLayoutX(this.victoryPoints.getX() + this.victoryPoints.getLayoutBounds().getWidth() + 20);
        this.victoryPointsNum.setLayoutY(this.victoryPoints.getLayoutBounds().getHeight());
        this.victoryPointsNum.setPrefWidth(50);

        this.resourceRequired.setFont(new Font(19));
        this.resourceRequired.setX(this.imageSlot.getX() + this.imageSlot.getFitWidth() + 30);
        this.resourceRequired.setY(this.victoryPoints.getY()+this.resourceRequired.getLayoutBounds().getHeight() + 10);

        Arrays.stream(this.cost).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.cost, this.resourceRequired.getX(), this.resourceRequired.getY(), 10, 20);

        this.productionPower.setFont(new Font(19));
        this.productionPower.setX(this.cost[0].getImageX());
        this.productionPower.setY(this.cost[0].getTextY() + 2*this.cost[0].getTextFontSize());

        this.inputText.setFont(new Font(19));
        this.inputText.setX(this.productionPower.getX());
        this.inputText.setY(this.productionPower.getY() + 2*this.productionPower.getFont().getSize());
        Arrays.stream(this.input).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.input, this.inputText.getX(), this.inputText.getY(), 10, 20);
        this.outputText.setFont(new Font(19));
        this.outputText.setX(this.inputText.getX());
        this.outputText.setY(this.input[0].getTextY() + 2*this.input[0].getTextFontSize());

        Arrays.stream(this.output).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.output, this.outputText.getX(), this.outputText.getY(), 10, 20);

        this.nextButton.setFont(new Font(14));
        this.nextButton.setLayoutX(this.output[0].getImageX() + (this.output[this.output.length-1].getImageX() - this.output[0].getImageX())/2);
        this.nextButton.setLayoutY(this.changeImage.getLayoutY());
    }

    private void buildGraphic(){
        this.root = new Pane();
        this.root.setPadding(new Insets(20));
        this.imageSlot = new ImageView();
        this.victoryPoints = new Text("Victory Points:");
        this.victoryPointsNum = new TextField();
        this.changeImage = new Button("Change Image");
        this.resourceRequired = new Text("Resources Required:");

        this.cost = new ImageDescription[4];
        this.cost[0] = new ImageDescription(this.coinImage);
        this.cost[1] = new ImageDescription(this.servantImage);
        this.cost[2] = new ImageDescription(this.stoneImage);
        this.cost[3] = new ImageDescription(this.shieldImage);

        this.productionPower = new Text("Production Power:");
        this.inputText = new Text("Input:");
        this.input = new ImageDescription[6];
        this.input[0] = new ImageDescription(this.coinImage);
        this.input[1] = new ImageDescription(this.servantImage);
        this.input[2] = new ImageDescription(this.stoneImage);
        this.input[3] = new ImageDescription(this.shieldImage);
        this.input[4] = new ImageDescription(this.faithImage); this.input[4].setDisable(true);
        this.input[5] = new ImageDescription(this.anyImage);
        this.outputText = new Text("Output:");
        this.output = new ImageDescription[6];
        this.output[0] = new ImageDescription(this.coinImage);
        this.output[1] = new ImageDescription(this.servantImage);
        this.output[2] = new ImageDescription(this.stoneImage);
        this.output[3] = new ImageDescription(this.shieldImage);
        this.output[4] = new ImageDescription(this.faithImage);
        this.output[5] = new ImageDescription(this.anyImage);

        this.nextButton = new Button("Next");

        this.root.getChildren().addAll(this.imageSlot, this.changeImage, this.victoryPoints, this.victoryPointsNum, this.resourceRequired);
        Arrays.stream(this.cost).forEach(imageDescription -> imageDescription.addToPane(this.root));
        this.root.getChildren().addAll(this.productionPower, this.inputText, this.outputText);
        Arrays.stream(this.input).forEach(imageDescription -> imageDescription.addToPane(this.root));
        Arrays.stream(this.output).forEach(imageDescription -> imageDescription.addToPane(this.root));
        this.root.getChildren().add(this.nextButton);
        this.devCardPersonalizationScene = new Scene(this.root);
    }

    private void loadImages(){
        this.coinImage = new Image(this.getClass().getResourceAsStream("/constantAssets/coin.png"));
        this.servantImage = new Image(this.getClass().getResourceAsStream("/constantAssets/servant.png"));
        this.stoneImage = new Image(this.getClass().getResourceAsStream("/constantAssets/stone.png"));
        this.shieldImage = new Image(this.getClass().getResourceAsStream("/constantAssets/shield.png"));
        this.faithImage = new Image(this.getClass().getResourceAsStream("/constantAssets/faith.png"));
        this.anyImage = new Image(this.getClass().getResourceAsStream("/constantAssets/any.png"));
    }

    private void readJson(){
        JsonParser parser = new JsonParser();
        this.modifiedDevCards = new LinkedHashMap<>();
        this.pathNewCards = new JsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/accessible/JSONs/DevCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.devCardsJson = parser.parse(reader.lines().collect(Collectors.joining())).getAsJsonObject();
        try{
            reader.close();
        }catch (IOException e){
            //
        }
        this.devCardLv = 1;
        this.devCardType = 0;
        this.devCardPos = 0;
    }

    public DevCardPersonalizationScene(Stage stage) {
        super(stage);
        this.order = Arrays.stream(Colour.values()).map(Enum::toString).toArray(String[]::new);
        this.resourcesOrder = new Resource[]{
                Resource.COIN,
                Resource.SERVANT,
                Resource.STONE,
                Resource.SHIELD,
                Resource.FAITH,
                Resource.ANY
        };
        this.readJson();
        this.loadImages();
        this.buildGraphic();
        this.positionGraphic();
        this.setOnActions();
        this.modifyScene();
    }

    @Override
    public Scene getScene(){
        return this.devCardPersonalizationScene;
    }
}
