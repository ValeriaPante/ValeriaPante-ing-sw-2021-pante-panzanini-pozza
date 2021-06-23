package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ThreeTextInput extends TwoTextInput{

    private Text thirdTextField;
    private TextField thirdInputField;

    public ThreeTextInput(){
        super();
        this.thirdTextField = new Text();
        this.thirdInputField = new TextField();
    }
}
