package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TwoTextInput {

    private Text firstTextField;
    private Text secondTextField;
    private TextField firstInputField;
    private TextField secondInputField;

    public TwoTextInput(){
        this.firstTextField = new Text();
        this.secondTextField = new Text();
        this.firstInputField = new TextField();
        this.secondInputField = new TextField();
    }

    public void addToPane(Pane pane){
        pane.getChildren().addAll(this.firstTextField, this.firstInputField, this.secondTextField, this.firstInputField);
    }
}
