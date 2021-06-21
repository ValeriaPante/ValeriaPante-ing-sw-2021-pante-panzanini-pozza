package it.polimi.ingsw.View.ServerGUI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DevCardPersonalizationScene extends CustomScenes{

    String[] order;
    private String pathImageCard;

    private Image coinImage;
    private Image servantImage;
    private Image stoneImage;
    private Image shieldImage;
    private Image faithImage;
    private Image anyImage;

    private JsonObject devCardsJson;
    private int devCardPos;
    private JsonObject modifiedDevCards;
    private JsonArray pathNewCards;

    private Scene devCardPersonalizationScene;
    private Pane root;
    private ImageView imageSlot;
    private Button changeImage;
    private Text victoryPoints;
    private TextField victoryPointsNum;
    private Text resourceRequired;
    private ImageDescription[] resourceReq;

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

    private void modifyScene(){

    }

    private void update(){

    }

    private void finish(){

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
        this.nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (allCorrect()) {
                    update();
                    devCardPos++;
                    //if (leaderCardPos >= leaderCardsJson.size()) {
                    if (false){
                        finish();
                    } else {
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

        Arrays.stream(this.resourceReq).forEach(imageDescription -> {
            imageDescription.setImageFitSize(50,50);
            imageDescription.setPreservedRatio(true);
            imageDescription.setTextFontSize(17);
        });
        this.positionImageDescription(this.resourceReq, this.resourceRequired.getX(), this.resourceRequired.getY(), 10, 20);

        this.productionPower.setFont(new Font(19));
        this.productionPower.setX(this.resourceReq[0].getImageX());
        this.productionPower.setY(this.resourceReq[0].getTextY() + 2*this.resourceReq[0].getTextFontSize());

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

        this.resourceReq = new ImageDescription[4];
        this.resourceReq[0] = new ImageDescription(this.coinImage);
        this.resourceReq[1] = new ImageDescription(this.servantImage);
        this.resourceReq[2] = new ImageDescription(this.stoneImage);
        this.resourceReq[3] = new ImageDescription(this.shieldImage);

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
        Arrays.stream(this.resourceReq).forEach(imageDescription -> imageDescription.addToPane(this.root));
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
        this.modifiedDevCards = new JsonObject();
        this.pathNewCards = new JsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/accessible/JSONs/DevCardsConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.devCardsJson = parser.parse(reader.lines().collect(Collectors.joining())).getAsJsonObject();
        this.devCardPos = 0;
    }

    public DevCardPersonalizationScene(Stage stage) {
        super(stage);
        this.order = new String[]{
                "GREEN",
                "YELLOW",
                "BLUE",
                "PURPLE",
        };
        this.loadImages();
        this.buildGraphic();
        this.positionGraphic();
        this.setOnActions();
        this.modifyScene();
    }

    public Scene getScene(){
        return this.devCardPersonalizationScene;
    }
}
