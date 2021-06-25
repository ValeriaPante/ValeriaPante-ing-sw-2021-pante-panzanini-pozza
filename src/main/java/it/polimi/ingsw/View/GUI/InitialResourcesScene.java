package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.SelectResourceMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.io.InputStream;

/**
 * Scenes displaying the resource to choose as initial resources
 */
public class InitialResourcesScene extends ObservableByGUI{

    private static int count;
    private static int toChoose;
    private Pane root;

    public InitialResourcesScene(GUI gui){
        addObserver(gui);
        if(observer.getModel().getLocalPlayerIndex() < 3) toChoose = 1;
        else toChoose = 2;

        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/initialResourcesScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node coin = root.lookup("#coin");
        coin.setOnDragDetected(dragEvent -> {
            Dragboard db = ((Node) dragEvent.getSource()).startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("COIN");
            db.setContent(content);
            dragEvent.consume();
        });
        Node shield = root.lookup("#shield");
        shield.setOnDragDetected(dragEvent -> {
            Dragboard db = ((Node) dragEvent.getSource()).startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("SHIELD");
            db.setContent(content);
            dragEvent.consume();
        });
        Node stone = root.lookup("#stone");
        stone.setOnDragDetected(dragEvent -> {
            Dragboard db = ((Node) dragEvent.getSource()).startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("STONE");
            db.setContent(content);
            dragEvent.consume();
        });
        Node servant = root.lookup("#servant");
        servant.setOnDragDetected(dragEvent -> {
            Dragboard db = ((Node) dragEvent.getSource()).startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("SERVANT");
            db.setContent(content);
            dragEvent.consume();
        });


        Region shelf1 = (Region) root.lookup("#shelf1");
        shelf1.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != shelf1 && dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            dragEvent.consume();
        });
        shelf1.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                new Thread(() -> sendMessage(new SelectResourceMessage(1, Resource.valueOf(db.getString())))).start();
                count++;
                success = true;
            }
            if(toChoose == 2 && count == 1){
                AnchorPane region = (AnchorPane) root.lookup("#resource1");
                region.getChildren().add(getImageFromResource(db.getString()));
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
            if(count == toChoose) Platform.runLater(Transition::toLoadingScene);
        });


        Region shelf2 = (Region) root.lookup("#shelf2");
        shelf2.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != shelf2 && dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            dragEvent.consume();
        });
        shelf2.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                new Thread(() -> sendMessage(new SelectResourceMessage(2, Resource.valueOf(db.getString())))).start();
                count++;
                success = true;
            }
            if(toChoose == 2 && count == 1){
                AnchorPane region = (AnchorPane) root.lookup("#resource2");
                region.getChildren().add(getImageFromResource(db.getString()));
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
            if(count == toChoose) Platform.runLater(Transition::toLoadingScene);
        });


        Region shelf3 = (Region) root.lookup("#shelf3");
        shelf3.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != shelf3 && dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            dragEvent.consume();
        });
        shelf3.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                new Thread(() -> sendMessage(new SelectResourceMessage(3, Resource.valueOf(db.getString())))).start();
                count++;
                success = true;
            }
            if(toChoose == 2 && count == 1){
                AnchorPane region = (AnchorPane) root.lookup("#resource3");
                region.getChildren().add(getImageFromResource(db.getString()));
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
            if(count == toChoose) Platform.runLater(Transition::toLoadingScene);
        });

    }

    private ImageView getImageFromResource(String resource){
        InputStream in = getClass().getResourceAsStream("/constantAssets/" +resource.toLowerCase()+".png");
        ImageView image = new ImageView();
        image.setImage(new Image(in));
        image.setFitWidth(70);
        image.setPreserveRatio(true);
        return image;
    }

    public static void wrongSelection(){
        count--;
    }

    public Pane getRoot() {
        return root;
    }
}
