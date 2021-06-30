package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.BuyDevCardMessage;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.ChooseDevCardMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Scene displaying all the decks of development cards
 */
public class DevDecksScene extends ObservableByGUI{
    private Pane root;
    private static HashMap<Integer, Region> selection;

    public DevDecksScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/devDecksScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane grid = (GridPane) root.lookup("#grid");
        int[][] devDecks = observer.getModel().getDevDecks();
        selection = new HashMap<>();
        for(int i = 0; i < devDecks.length; i++ )
            for(int j = 0; j < devDecks[i].length; j++){
                Region region = (Region) root.getChildren().get(2+i*4+j);
                region.setVisible(false);
                selection.put(i*4+j+1, region);
                if(devDecks[i][j] != 0){
                    ImageView image = new ImageView();
                    try {
                        File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                        FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +devDecks[i][j]+".png");
                        image.setImage(new Image(fileInputStream));

                    } catch(Exception e) {
                    }
                    image.setFitWidth(120);
                    image.setPreserveRatio(true);
                    image.setId(String.valueOf(i*4+j+1));
                    image.setOnMouseClicked(mouseEvent -> {
                        deselectAll();
                        int deckNumber = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId());
                        selection.get(deckNumber).setVisible(true);
                        new Thread(() -> sendMessage(new ChooseDevCardMessage(deckNumber))).start();
                        mouseEvent.consume();
                    });
                    grid.add(image, j, i);
                }
            }


        Button sendButton = (Button) root.lookup("#buyButton");
        sendButton.setOnAction(event -> {
            observer.toBuyDevCardState();
            Platform.runLater(Transition::hideDialog);
            new Thread(() -> sendMessage(new BuyDevCardMessage())).start();
        });

        root.lookup("#quit").setOnMouseClicked(mouseEvent -> Transition.hideDialog());
    }

    public static void deselectAll(){
        if(selection != null){
            for(Map.Entry<Integer, Region> card: selection.entrySet())
                card.getValue().setVisible(false);
        }
    }

    public static void deselectIfSelected(int cardId){
        if(selection != null && selection.containsKey(cardId)) selection.get(cardId+1).setVisible(false);
        deselectAll();
    }

    public Pane getRoot() {
        return root;
    }
}
