package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThreeTextInput extends TwoTextInput{

    private Text thirdTextField;
    private TextField thirdInputField;

    public ThreeTextInput(){
        super();
        this.thirdTextField = new Text();
        this.thirdInputField = new TextField();
    }

    @Override
    public void setFontSizes(int size){
        super.setFontSizes(size);
        this.thirdTextField.setFont(new Font(size));
        this.thirdInputField.setFont(new Font(size));
    }

    @Override
    public void addToPane(Pane pane){
        super.addToPane(pane);
        pane.getChildren().addAll(this.thirdTextField, this.thirdInputField);
    }
}
