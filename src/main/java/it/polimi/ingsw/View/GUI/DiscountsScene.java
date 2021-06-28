package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.DiscountAbilityMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Scene displaying leader cards of type discount, which can be chosen by the player to apply a discount
 */
public class DiscountsScene extends Initializable{

    private static ArrayList<Integer> discountLC = new ArrayList();

    public DiscountsScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/discountsScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialise(){
        Button button = (Button) root.lookup("#button");
        button.setOnAction(event -> observer.getCurrentState().next());

        root.getChildren().get(5).setVisible(false);
        root.getChildren().get(6).setVisible(false);
        root.getChildren().get(7).setVisible(false);

        if(discountLC.size() == 2){
            AnchorPane card1 = (AnchorPane) root.getChildren().get(1);
            ImageView image1 = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +discountLC.get(0)+".png");
                image1.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image1.setFitWidth(255);
            image1.setPreserveRatio(true);
            image1.setOnMouseClicked(mouseEvent -> {
                root.getChildren().get(5).setVisible(true);
                new Thread(() -> sendMessage(new DiscountAbilityMessage(discountLC.get(0)))).start();
            });
            card1.getChildren().add(image1);

            AnchorPane card2 = (AnchorPane) root.getChildren().get(2);
            ImageView image2 = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +discountLC.get(1)+".png");
                image2.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image2.setFitWidth(255);
            image2.setPreserveRatio(true);
            image2.setOnMouseClicked(mouseEvent -> {
                root.getChildren().get(6).setVisible(true);
                new Thread(() -> sendMessage(new DiscountAbilityMessage(discountLC.get(1)))).start();
            });
            card2.getChildren().add(image2);

        } else {
            AnchorPane card = (AnchorPane) root.getChildren().get(3);
            ImageView image = new ImageView();
            try {
                File fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                FileInputStream fileInputStream = new FileInputStream(fullPath.getParentFile().getPath() + "\\assets\\imgs\\" +discountLC.get(0)+".png");
                image.setImage(new Image(fileInputStream));

            } catch(Exception e) {
            }
            image.setFitWidth(255);
            image.setPreserveRatio(true);
            image.setOnMouseClicked(mouseEvent -> {
                root.getChildren().get(7).setVisible(true);
                new Thread(() -> sendMessage(new DiscountAbilityMessage(discountLC.get(0)))).start();
            });
            card.getChildren().add(image);

        }

    }

    public static int getNumberOfDiscounts(){
        return discountLC.size();
    }

    public static void putDiscount(int cardId){
        if (cardId > 48 && cardId < 53) discountLC.add(cardId);
    }

    public static void removeDiscount(int cardId){
        discountLC.remove(Integer.valueOf(cardId));
    }
}
