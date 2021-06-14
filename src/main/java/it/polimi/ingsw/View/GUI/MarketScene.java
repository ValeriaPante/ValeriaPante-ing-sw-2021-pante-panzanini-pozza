package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.MarketSelectionMessage;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.TakeFromMarketMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class MarketScene extends ObservableByGUI{
    private Pane root;
    private final ArrayList<Region> rowsAndColumns;

    public MarketScene(GUI gui){
        addObserver(gui);
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/marketScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Resource[][] market = observer.getModel().getGrid();
        Resource slide = observer.getModel().getSlide();
        for(int i = 0; i < 12; i++ ){
            Circle circle = (Circle) root.getChildren().get(i+3);
            circle.setFill(getColorFromResource(market[i/4][i%4]));
        }
        Circle circle = (Circle) root.getChildren().get(15);
        circle.setFill(getColorFromResource(slide));

        Button sendButton = (Button) root.lookup("#buyButton");
        sendButton.setOnAction(event -> {
            new Thread(() -> sendMessage(new TakeFromMarketMessage())).start();
            observer.toMarketState();
            Platform.runLater(Transition::hideDialog);
        });

        root.lookup("#quit").setOnMouseClicked(mouseEvent -> Transition.hideDialog());

        rowsAndColumns = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            Region region = (Region) root.getChildren().get(i+23);
            region.setVisible(false);
            rowsAndColumns.add(region);
            Region arrow = (Region) root.getChildren().get(i+16);
            arrow.setId(String.valueOf(i));
            arrow.setOnMouseClicked(mouseEvent -> {
                deselectAll();
                int index = Integer.parseInt(((Region) mouseEvent.getSource()).getId());
                rowsAndColumns.get(index).setVisible(true);
                if(index < 3) new Thread(() -> sendMessage(new MarketSelectionMessage(index, true))).start();
                else new Thread(() -> sendMessage(new MarketSelectionMessage(index-3, false))).start();
                mouseEvent.consume();
            });
        }
    }

    private void deselectAll(){
        for (Region rowsAndColumn : rowsAndColumns) rowsAndColumn.setVisible(false);
    }

    private Color getColorFromResource(Resource resource){
        switch (resource){
            case SHIELD:
                return new Color(37.0/255,172.0/255,225.0/255,1.0);
            case SERVANT:
                return new Color(100.0/255, 88.0/255, 164.0/255, 1.0);
            case STONE:
                return new Color(128.0/255,129.0/255,126.0/255,1.0);
            case COIN:
                return new Color(239.0/255,207.0/255,54.0/255,1.0);
            case WHITE:
                return new Color(250.0/255,250.0/255,250.0/255,1.0);
            case FAITH:
                return new Color(154.0/255,25.0/255,39.0/255,1.0);
            default:
                return null;
        }
    }

    public Pane getRoot() {
        return root;
    }
}
