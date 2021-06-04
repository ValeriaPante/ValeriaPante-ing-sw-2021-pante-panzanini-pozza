package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DevDecksScene extends ObservableByGUI{
    private Pane root;
    private final HashMap<Integer, Region> selection;

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
                if(devDecks[i][j] != 0){
                    Region region = (Region) root.getChildren().get(2+i*4+j);
                    region.setVisible(false);
                    selection.put(devDecks[i][j], region);
                    InputStream in = getClass().getResourceAsStream("/Images/"+devDecks[i][j]+".png");
                    ImageView image = new ImageView();
                    image.setImage(new Image(in));
                    image.setFitWidth(120);
                    image.setPreserveRatio(true);
                    image.setId(String.valueOf(devDecks[i][j]));
                    image.setOnMouseClicked(mouseEvent -> {
                        deselectAll();
                        int cardId = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId());
                        selection.get(cardId).setVisible(true);
                        //sendMessageToServer(MessageToServerCreator.createChooseDevCardMessage(cardId));
                        mouseEvent.consume();
                    });
                    grid.add(image, j, i);
                }
            }


        Button sendButton = (Button) root.lookup("#buyButton");
        sendButton.setOnAction(event -> {
            //sendMessageToServer(MessageToServerCreator.createBuyDevCardMessage());
            observer.toBuyDevCardState();
            observer.getCurrentState().next();
        });

        root.lookup("#quit").setOnMouseClicked(mouseEvent -> {
            Transition.hideDialog();
        });
    }

    private void deselectAll(){
        for(Map.Entry<Integer, Region> card: selection.entrySet())
            card.getValue().setVisible(false);
    }

    public Pane getRoot() {
        return root;
    }
}
