package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TwoTextInput {

    protected Text firstTextField;
    protected Text secondTextField;
    protected TextField firstInputField;
    protected TextField secondInputField;

    public TwoTextInput(String firstText, String secondText){
        this.firstTextField = new Text(firstText);
        this.secondTextField = new Text(secondText);
        this.firstInputField = new TextField();
        this.secondInputField = new TextField();
        this.firstInputField.setPrefWidth(50);
        this.secondInputField.setPrefWidth(50);
    }

    public void setFontSizes(int size){
        this.firstTextField.setFont(new Font(size));
        //this.firstInputField.setFont(new Font(size));
        this.secondTextField.setFont(new Font(size));
        //this.secondInputField.setFont(new Font(size));
    }

    protected double maxTextLength(){
        return Math.max(this.firstTextField.getLayoutBounds().getWidth(), this.secondTextField.getLayoutBounds().getWidth());
    }

    public void setFirstInput(String text){
        this.firstInputField.setText(text);
    }

    public void setSecondInput(String text){
        this.secondInputField.setText(text);
    }

    protected void setPosition(double xPos, double yPos, double offset){
        this.firstTextField.setX(xPos);
        this.firstTextField.setY(yPos);
        this.firstInputField.setLayoutX(xPos + offset + 20);
        this.firstInputField.setLayoutY(yPos - this.firstTextField.getFont().getSize());
        this.secondTextField.setX(xPos);
        this.secondTextField.setY(yPos + this.firstTextField.getLayoutBounds().getHeight() + 10);
        this.secondInputField.setLayoutX(xPos + offset + 20);
        this.secondInputField.setLayoutY(this.secondTextField.getY() - this.secondTextField.getFont().getSize());
    }

    public void setPosition(double xPos, double yPos){
        double offset = this.maxTextLength();
        this.setPosition(xPos, yPos, offset);
    }

    public void addToPane(Pane pane){
        pane.getChildren().addAll(this.firstTextField, this.firstInputField, this.secondTextField, this.secondInputField);
    }

    public String getFirstInput(){
        return this.firstInputField.getCharacters().toString();
    }

    public String getSecondInput(){
        return this.secondInputField.getCharacters().toString();
    }

    public double getHeight(){
        return (this.secondTextField.getY() - this.firstTextField.getY()) + this.secondTextField.getLayoutBounds().getHeight();
    }

    public double getWidth(){
        return this.maxTextLength() + 20 + this.firstInputField.getPrefWidth();
    }

    public double getY(){
        return this.firstTextField.getY();
    }

    public double getX(){
        return this.firstTextField.getX();
    }
}
