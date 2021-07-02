package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThreeTextInput extends TwoTextInput{

    private final Text thirdTextField;
    private final TextField thirdInputField;

    public ThreeTextInput(String firstText, String secondText, String thirdText){
        super(firstText, secondText);
        this.thirdTextField = new Text(thirdText);
        this.thirdInputField = new TextField();
        this.thirdInputField.setPrefWidth(50);
    }

    @Override
    protected double maxTextLength(){
        return Math.max(this.thirdTextField.getLayoutBounds().getWidth(), super.maxTextLength());
    }

    public void setThirdInput(String text){
        this.thirdInputField.setText(text);
    }

    @Override
    public void setPosition(double xPos, double yPos){
        double offset = this.maxTextLength();
        super.setPosition(xPos, yPos, offset);
        this.thirdTextField.setX(xPos);
        this.thirdTextField.setY(super.secondTextField.getY() + super.secondTextField.getLayoutBounds().getHeight() + 10);
        this.thirdInputField.setLayoutX(xPos + offset + 20);
        this.thirdInputField.setLayoutY(this.thirdTextField.getY() - this.thirdTextField.getFont().getSize());
    }

    public String getThirdInput(){
        return this.thirdInputField.getCharacters().toString();
    }

    @Override
    public void setFontSizes(int size){
        super.setFontSizes(size);
        this.thirdTextField.setFont(new Font(size));
    }

    @Override
    public void addToPane(Pane pane){
        super.addToPane(pane);
        pane.getChildren().addAll(this.thirdTextField, this.thirdInputField);
    }

    @Override
    public double getHeight(){
        return (this.thirdTextField.getY() - super.firstTextField.getY()) + this.firstTextField.getLayoutBounds().getHeight();
    }
}
