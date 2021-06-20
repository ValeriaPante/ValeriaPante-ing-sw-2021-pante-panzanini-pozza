package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.InputStream;

public class ImageDescription{
    ImageView imageSlot;
    Text amount;

    public ImageDescription(InputStream imageInputStream){
        this.imageSlot = new ImageView(new Image(imageInputStream));
        this.amount = new Text("0");
    }

    public ImageDescription(Image image){
        this.imageSlot = new ImageView(image);
        this.amount = new Text("0");
    }

    public void setDefaultOnClickHandler(){
        this.imageSlot.setOnMousePressed(new CustomEventHandler(this.amount));
        this.imageSlot.setCursor(Cursor.HAND);
    }

    public ImageView getImageSlot() {
        return this.imageSlot;
    }

    public Text getAmount(){
        return this.amount;
    }

    public void setText(String txt){
        this.amount.setText(txt);
    }

    public void setPreservedRatio(boolean ratioPreserved){
        this.imageSlot.setPreserveRatio(ratioPreserved);
    }

    public void setImageFitSize(int width, int height){
        this.imageSlot.setFitWidth(width);
        this.imageSlot.setFitHeight(height);
    }

    public void setTextFontSize(int size){
        this.amount.setFont(new Font(size));
    }

    public void addToPane(Pane pane){
        pane.getChildren().addAll(this.imageSlot, this.amount);
    }

    public double getImageFitWidth(){
        return this.imageSlot.getFitWidth();
    }

    public double getImageX(){
        return this.imageSlot.getX();
    }

    public double getImageY(){
        return this.imageSlot.getY();
    }

    public String getText(){
        return this.amount.getText();
    }

    public double getTextX(){
        return this.amount.getX();
    }

    public double getTextY(){
        return this.amount.getY();
    }

    public void setDisable(boolean bool){
        if (bool){
            this.imageSlot.setDisable(true);
            this.imageSlot.setOpacity(0.3);
            this.amount.setOpacity(0.3);
        }
        else{
            this.imageSlot.setDisable(false);
            this.imageSlot.setOpacity(1);
            this.amount.setOpacity(1);
        }
    }

    public double getTextFontSize(){
        return this.amount.getFont().getSize();
    }

    public void setPosition(double xPos, double yPos){
        this.imageSlot.setX(xPos);
        this.imageSlot.setY(yPos);
        this.amount.setX(this.imageSlot.getX() + this.imageSlot.getFitWidth()/2 - this.amount.getFont().getSize()/2);
        this.amount.setY(this.imageSlot.getY() + this.imageSlot.getFitHeight() + this.amount.getFont().getSize());
    }
}
