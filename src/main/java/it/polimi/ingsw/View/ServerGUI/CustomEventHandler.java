package it.polimi.ingsw.View.ServerGUI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CustomEventHandler implements EventHandler<MouseEvent> {

    private final Text toUpdate;

    public CustomEventHandler(Text toUpdate){
        this.toUpdate = toUpdate;
    }

    private void plusOne(){
        int num = Integer.parseInt(toUpdate.getText());
        if (num<99){
            this.toUpdate.setText(Integer.toString(num+1));
        }
    }

    private void minusOne(){
        int num = Integer.parseInt(toUpdate.getText());
        if (num>0){
            this.toUpdate.setText(Integer.toString(num-1));
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()){
            this.plusOne();
        }
        else if (mouseEvent.isSecondaryButtonDown()){
            this.minusOne();
        }
    }
}
