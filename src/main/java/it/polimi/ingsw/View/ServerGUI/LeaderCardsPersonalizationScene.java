package it.polimi.ingsw.View.ServerGUI;

import com.google.gson.*;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.Resource;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LeaderCardsPersonalizationScene extends CustomScenes{

    private final Resource[] resourcesOrder;
    private final Colour[] coloursOrder;
    private String pathImageCard;
    private final Gson gson;

    private Image coinImage;
    private Image servantImage;
    private Image stoneImage;
    private Image shieldImage;
    private Image faithImage;
    private Image anyImage;

    private JsonArray leaderCardsJson;
    private int leaderCardPos;
    private JsonArray modifiedLeaderCards;
    private JsonArray pathNewCards;

    private Scene leaderCardsPersonalizationScene;
    private ImageView imageSlot;
    private Pane root;
    private Button nextButton;
    private Button changeImage;
    private Text victoryPoints;
    private TextField victoryPointsNum;
    private Text resourceRequired;

    //coin - servant - stone - shield
    private ImageDescription[] resourceReq;

    private Line separator;

    private Text devCardsReqTitle;
    //yellow - purple - green - blue
    //yellow1 - purple1 - green1 - blue1
    //yellow2 - purple2 - green2 - blue2
    //yellow3 - purple3 - green3 - blue3
    private ImageDescription[][] devCardsReq;

    private Text abilityText;
    private ChoiceBox<LeaderCardType> abilityType;
    //coin - servant - stone - shield - faith - any
    private Text abilityText1;
    private ImageDescription[] abilityIn1;
    private Text abilityText2;
    private ImageDescription[] abilityIn2;

    private void resetLvDevCardsNum(){
        Arrays.stream(this.devCardsReq).forEach(array -> Arrays.stream(array).forEach(text -> text.setText("0")));
    }

    private void modifyVictoryPoints(JsonObject elem){
        this.victoryPointsNum.setText(elem.get("victoryPoints").getAsString());
    }

    private void modifyResourcesRequired(JsonObject elem){
        JsonObject resourcesReq = elem.get("resourceReq").getAsJsonObject();
        JsonElement amount;
        for (int i=0; i<this.resourceReq.length; i++){
            amount = resourcesReq.get(this.resourcesOrder[i].toString());
            this.resourceReq[i].setText((amount == null) ? "0" : amount.getAsString());
        }
    }

    private void modifyAbilityIn(JsonObject input, ImageDescription[] descriptors){
        JsonElement amount;
        for (int i=0; i<descriptors.length; i++){
            amount = input.get(this.resourcesOrder[i].toString());
            descriptors[i].setText((amount == null) ? "0" : amount.getAsString());
        }
    }

    private void modifyAbility(JsonObject elem){
        LeaderCardType type = this.gson.fromJson(elem.get("LeaderCardType"), LeaderCardType.class);
        this.abilityType.setValue(type);

        //updates quantity
        if (type==LeaderCardType.DISCOUNT){
            JsonObject discount = elem.get("discount").getAsJsonObject();
            this.modifyAbilityIn(discount, this.abilityIn1);
        }
        else if(type==LeaderCardType.STORAGE){
            JsonObject storage = elem.get("storage").getAsJsonObject();
            this.modifyAbilityIn(storage, this.abilityIn1);

        }
        else if(type==LeaderCardType.TRANSMUTATION){
            JsonObject transmutation = elem.get("transmutation").getAsJsonObject();
            this.modifyAbilityIn(transmutation, this.abilityIn1);

        }
        else if(type==LeaderCardType.PRODPOWER){
            JsonObject productionPower = elem.get("prodpower").getAsJsonObject();
            this.modifyAbilityIn(productionPower.get("input").getAsJsonObject(), this.abilityIn1);
            this.modifyAbilityIn(productionPower.get("output").getAsJsonObject(), this.abilityIn2);
        }

    }


    private void modifyDevCardsRequired(JsonObject elem){
        this.resetLvDevCardsNum();
        JsonArray devCardsReq = elem.get("devCardReq").getAsJsonArray();
        for (int i=0; i<devCardsReq.size(); i++){
            JsonObject cardDescriptor = devCardsReq.get(i).getAsJsonObject();
            String quantity = cardDescriptor.get("quantity").getAsString();
            JsonObject devCardType = cardDescriptor.get("devCardType").getAsJsonObject();
            int level = devCardType.get("level").getAsInt();
            String color = devCardType.get("color").getAsString();

            switch (color){
                case ("YELLOW"):
                    this.devCardsReq[level][0].setText(quantity);
                    break;
                case ("PURPLE"):
                    this.devCardsReq[level][1].setText(quantity);
                    break;
                case ("GREEN"):
                    this.devCardsReq[level][2].setText(quantity);
                    break;
                case ("BLUE"):
                    this.devCardsReq[level][3].setText(quantity);
                    break;
            }
        }
    }

    private void resetAbility(){
        Arrays.stream(abilityIn1).forEach(imageDescription -> {
            imageDescription.setDisable(false);
            imageDescription.setText("0");
        });
        Arrays.stream(abilityIn2).forEach(imageDescription -> {
            imageDescription.setDisable(false);
            imageDescription.setText("0");
        });
    }

    private void discountAbility(){
        this.abilityText1.setText("Discount:");
        this.abilityIn1[4].setDisable(true);
        this.abilityIn1[5].setDisable(true);
        Arrays.stream(this.abilityIn2).forEach(imageDescription -> imageDescription.setDisable(true));
    }

    private void storageAbility(){
        this.abilityText1.setText("Storage:");
        this.abilityIn1[4].setDisable(true);
        this.abilityIn1[5].setDisable(true);
        Arrays.stream(this.abilityIn2).forEach(imageDescription -> imageDescription.setDisable(true));
    }

    private void transmutationAbility(){
        this.abilityText1.setText("Transmutation:");
        this.abilityIn1[4].setDisable(true);
        this.abilityIn1[5].setDisable(true);
        Arrays.stream(this.abilityIn2).forEach(imageDescription -> imageDescription.setDisable(true));
    }

    private void productionAbility(){
        this.abilityText1.setText("Production Power:");
        this.abilityIn1[4].setDisable(true);
    }

    private void modifyScene(){
        this.pathImageCard = "default";
        JsonObject elem = this.leaderCardsJson.get(this.leaderCardPos).getAsJsonObject();
        this.imageSlot.setImage(new Image(this.getClass().getResourceAsStream("/accessible/assets/imgs/" + elem.get("id").getAsInt() + ".png")));
        this.modifyVictoryPoints(elem);
        this.modifyResourcesRequired(elem);
        this.modifyAbility(elem);
        this.modifyDevCardsRequired(elem);
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
        int cardId = this.leaderCardsJson.get(this.leaderCardPos).getAsJsonObject().get("id").getAsInt();
        JsonObject pathDescriptor = new JsonObject();
        pathDescriptor.add("name", new JsonPrimitive(cardId+".png"));
        pathDescriptor.add("path", new JsonPrimitive(this.pathImageCard));
        this.pathNewCards.add(pathDescriptor);

        JsonObject card = new JsonObject();
        card.add("id", new JsonPrimitive(cardId));
        card.add("victoryPoints", new JsonPrimitive(Integer.parseInt(this.victoryPointsNum.getCharacters().toString())));
        card.add("resourceReq", this.enumMapExtractor(this.resourceReq, this.resourcesOrder));
        JsonArray devCardReq = new JsonArray();
        int amount;
        for (int i=0; i<this.devCardsReq.length; i++){
            for (int j=0; j<this.devCardsReq[i].length; j++){
                amount = Integer.parseInt(this.devCardsReq[i][j].getText());
                if (amount > 0){
                    JsonObject devCardDescAmount = new JsonObject();
                    JsonObject devCardDesc = new JsonObject();
                    devCardDesc.add("level", new JsonPrimitive(i));
                    devCardDesc.add("color", new JsonPrimitive(this.coloursOrder[j].toString()));
                    devCardDescAmount.add("devCardType", devCardDesc);
                    devCardDescAmount.add("quantity", new JsonPrimitive(amount));
                    devCardReq.add(devCardDescAmount);
                }
            }
        }
        card.add("devCardReq", devCardReq);
        LeaderCardType leaderCardType = this.abilityType.getValue();
        card.add("LeaderCardType", new JsonPrimitive(leaderCardType.toString()));
        if (leaderCardType == LeaderCardType.DISCOUNT){
            card.add("discount", this.enumMapExtractor(this.abilityIn1, this.resourcesOrder));
        }
        else if(leaderCardType == LeaderCardType.STORAGE){
            card.add("storage", this.enumMapExtractor(this.abilityIn1, this.resourcesOrder));
        }
        else if(leaderCardType == LeaderCardType.TRANSMUTATION){
            card.add("transmutation", this.enumMapExtractor(this.abilityIn1, this.resourcesOrder));
        }
        else if(leaderCardType == LeaderCardType.PRODPOWER){
            JsonObject prodpower = new JsonObject();
            prodpower.add("input", this.enumMapExtractor(this.abilityIn1, this.resourcesOrder));
            prodpower.add("output", this.enumMapExtractor(this.abilityIn2, this.resourcesOrder));
            card.add("prodpower", prodpower);
        }

        this.modifiedLeaderCards.add(card);
    }

    private void finish(){
        String serverPath = null;
        try {
            serverPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        }catch (URISyntaxException e){
            //pass
        }

        for (int i=0; i<this.pathNewCards.size(); i++){
            String relativePath = "\\accessible\\assets\\imgs\\";
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
        String output = prettyPrinting.toJson(this.modifiedLeaderCards);
        File outputDir = new File(serverPath + "\\accessible\\JSONs\\");
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }
        try {
            FileOutputStream jsonFile = new FileOutputStream(serverPath + "\\accessible\\JSONs\\LeaderCardsConfig.json");
            jsonFile.write(output.getBytes());
            jsonFile.close();
        }catch (IOException e){
            //sincero bho
        }

        super.setNextScene(new DevCardPersonalizationScene(super.mainStage).getScene());
    }

    private boolean allCorrect(){
        try {
            int vp = Integer.parseInt(this.victoryPointsNum.getCharacters().toString());
            return vp <= 99 && vp >= 1;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private void setOnActions(){
        Arrays.stream(this.resourceReq).forEach(ImageDescription::setDefaultOnClickHandler);
        Arrays.stream(this.devCardsReq).forEach(array -> Arrays.stream(array).forEach(ImageDescription::setDefaultOnClickHandler));
        Arrays.stream(this.abilityIn1).forEach(ImageDescription::setDefaultOnClickHandler);
        Arrays.stream(this.abilityIn2).forEach(ImageDescription::setDefaultOnClickHandler);

        this.nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (allCorrect()){
                    update();
                    leaderCardPos ++;
                    if (leaderCardPos>=leaderCardsJson.size()){
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
                        new ExtensionFilter("Image Files", "*.png")
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

        this.abilityType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (LeaderCardType.DISCOUNT == abilityType.getValue()){
                    resetAbility();
                    discountAbility();
                }
                else if (LeaderCardType.STORAGE == abilityType.getValue()){
                    resetAbility();
                    storageAbility();
                }
                else if (LeaderCardType.TRANSMUTATION == abilityType.getValue()){
                    resetAbility();
                    transmutationAbility();
                }
                else if (LeaderCardType.PRODPOWER == abilityType.getValue()){
                    resetAbility();
                    productionAbility();
                }
            }
        });
    }

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

    private void positionImageDescription(ImageDescription[][] imageDescriptions, double xPosStart, double yPosStart, int xSpacing, int ySpacing){
        for (int i=0; i<imageDescriptions.length; i++){
            if (i==0){
                this.positionImageDescription(imageDescriptions[i], xPosStart, yPosStart, xSpacing, ySpacing);
            }
            else{
                this.positionImageDescription(imageDescriptions[i], imageDescriptions[i-1][0].getImageX(), imageDescriptions[i-1][0].getTextY(), xSpacing, ySpacing);
            }
        }
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

        Arrays.stream(this.resourceReq).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.resourceReq, this.resourceRequired.getX(), this.resourceRequired.getY(), 10, 20);

        this.abilityText.setFont(new Font(19));
        this.abilityText.setX(this.resourceReq[0].getImageX());
        this.abilityText.setY(this.resourceReq[0].getTextY() + 2*this.resourceReq[0].getTextFontSize());
        this.abilityType.setLayoutX(this.abilityText.getX() + this.abilityText.getLayoutBounds().getWidth() + 20);
        this.abilityType.setLayoutY(this.abilityText.getY() - this.abilityText.getFont().getSize());

        this.abilityText1.setFont(new Font(19));
        this.abilityText1.setX(this.abilityText.getX());
        this.abilityText1.setY(this.abilityText.getY() + 2*this.abilityText.getFont().getSize());

        Arrays.stream(this.abilityIn1).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.abilityIn1, this.abilityText1.getX(), this.abilityText1.getY(), 10, 20);

        this.abilityText2.setFont(new Font(19));
        this.abilityText2.setX(this.abilityText1.getX());
        this.abilityText2.setY(this.abilityIn1[0].getTextY() + 2*this.abilityIn1[0].getTextFontSize());

        Arrays.stream(this.abilityIn2).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.abilityIn2, this.abilityText2.getX(), this.abilityText2.getY(), 10, 20);

        this.separator.setStrokeWidth(3);
        this.separator.setStartX(this.abilityIn1[this.abilityIn1.length-1].getImageX() + 75);
        this.separator.setStartY(this.imageSlot.getY());
        this.separator.setEndX(this.abilityIn1[this.abilityIn1.length-1].getImageX() + 75);
        this.separator.setEndY(this.imageSlot.getY() + this.imageSlot.getLayoutBounds().getHeight());

        this.devCardsReqTitle.setFont(new Font(19));
        this.devCardsReqTitle.setX(this.separator.getStartX() + 20);
        this.devCardsReqTitle.setY(this.victoryPoints.getY());


        Arrays.stream(this.devCardsReq).forEach(array -> Arrays.stream(array).forEach(imageDescription -> {
            imageDescription.setImageFitSize(55, 55);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        }));
        this.positionImageDescription(this.devCardsReq, this.devCardsReqTitle.getX(), this.devCardsReqTitle.getY(), 10, 20);

        this.nextButton.setFont(new Font(14));
        this.nextButton.setLayoutX(this.devCardsReq[0][devCardsReq[0].length-1].getImageX() - (this.devCardsReq[0][devCardsReq[0].length-1].getImageX() - this.devCardsReq[0][0].getImageX())/2);
        this.nextButton.setLayoutY(this.separator.getEndY());
    }

    private void buildGraphic(){
        this.root = new Pane();
        this.root.setPadding(new Insets(20));
        this.imageSlot = new ImageView();
        this.victoryPoints = new Text("Victory Points:");
        this.resourceRequired = new Text("Resources Required:");
        this.victoryPointsNum = new TextField();
        this.changeImage = new Button("Change Image");

        this.resourceReq = new ImageDescription[4];
        this.resourceReq[0] = new ImageDescription(this.coinImage);
        this.resourceReq[1] = new ImageDescription(this.servantImage);
        this.resourceReq[2] = new ImageDescription(this.stoneImage);
        this.resourceReq[3] = new ImageDescription(this.shieldImage);

        this.separator = new Line();

        this.devCardsReqTitle = new Text("Development Cards Required:");

        this.devCardsReq = new ImageDescription[4][4];
        this.devCardsReq[0][0] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/onlyYellow.png"));
        this.devCardsReq[0][1] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/onlyPurple.png"));
        this.devCardsReq[0][2] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/onlyGreen.png"));
        this.devCardsReq[0][3] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/onlyBlue.png"));
        this.devCardsReq[1][0] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/yellow1.png"));
        this.devCardsReq[1][1] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/purple1.png"));
        this.devCardsReq[1][2] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/green1.png"));
        this.devCardsReq[1][3] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/blue1.png"));
        this.devCardsReq[2][0] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/yellow2.png"));
        this.devCardsReq[2][1] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/purple2.png"));
        this.devCardsReq[2][2] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/green2.png"));
        this.devCardsReq[2][3] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/blue2.png"));
        this.devCardsReq[3][0] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/yellow3.png"));
        this.devCardsReq[3][1] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/purple3.png"));
        this.devCardsReq[3][2] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/green3.png"));
        this.devCardsReq[3][3] = new ImageDescription(this.getClass().getResourceAsStream("/accessible/assets/imgs/blue3.png"));

        this.abilityText = new Text("Ability:");
        this.abilityType = new ChoiceBox<>(FXCollections.observableArrayList(LeaderCardType.PRODPOWER, LeaderCardType.STORAGE, LeaderCardType.DISCOUNT, LeaderCardType.TRANSMUTATION));

        this.abilityText1 = new Text();
        this.abilityIn1 = new ImageDescription[6];
        this.abilityIn1[0] = new ImageDescription(this.coinImage);
        this.abilityIn1[1] = new ImageDescription(this.servantImage);
        this.abilityIn1[2] = new ImageDescription(this.stoneImage);
        this.abilityIn1[3] = new ImageDescription(this.shieldImage);
        this.abilityIn1[4] = new ImageDescription(this.faithImage);
        this.abilityIn1[5] = new ImageDescription(this.anyImage);

        this.abilityText2 = new Text();
        this.abilityIn2 = new ImageDescription[6];
        this.abilityIn2[0] = new ImageDescription(this.coinImage);
        this.abilityIn2[1] = new ImageDescription(this.servantImage);
        this.abilityIn2[2] = new ImageDescription(this.stoneImage);
        this.abilityIn2[3] = new ImageDescription(this.shieldImage);
        this.abilityIn2[4] = new ImageDescription(this.faithImage);
        this.abilityIn2[5] = new ImageDescription(this.anyImage);

        this.nextButton = new Button("Next");

        this.root.getChildren().addAll(this.imageSlot, this.changeImage, this.nextButton, this.victoryPoints, this.victoryPointsNum, this.resourceRequired, this.separator);
        this.root.getChildren().addAll(this.devCardsReqTitle);
        this.root.getChildren().addAll(this.abilityText, this.abilityType);
        this.root.getChildren().add(this.abilityText1);
        Arrays.stream(this.abilityIn1).forEach(imageDescription -> imageDescription.addToPane(this.root));
        this.root.getChildren().add(this.abilityText2);
        Arrays.stream(this.abilityIn2).forEach(imageDescription -> imageDescription.addToPane(this.root));
        Arrays.stream(this.resourceReq).forEach(imageDescription -> imageDescription.addToPane(this.root));
        Arrays.stream(this.devCardsReq).forEach(array -> Arrays.stream(array).forEach(imageDescription -> imageDescription.addToPane(this.root)));
        this.leaderCardsPersonalizationScene = new Scene(this.root);
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
        this.modifiedLeaderCards = new JsonArray();
        this.pathNewCards = new JsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/accessible/JSONs/LeaderCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.leaderCardsJson = parser.parse(reader.lines().collect(Collectors.joining())).getAsJsonArray();
        try{
            reader.close();
        }catch (IOException e){
            //
        }
        this.leaderCardPos = 0;
    }

    public LeaderCardsPersonalizationScene(Stage stage){
        super(stage);
        this.gson = new Gson();
        //coin - servant - stone - shield - faith - any
        this.resourcesOrder = new Resource[]{
                Resource.COIN,
                Resource.SERVANT,
                Resource.STONE,
                Resource.SHIELD,
                Resource.FAITH,
                Resource.ANY
        };
        //yellow - purple - green - blue
        this.coloursOrder = new Colour[]{
                Colour.YELLOW,
                Colour.PURPLE,
                Colour.GREEN,
                Colour.BLUE
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
        return this.leaderCardsPersonalizationScene;
    }

}
