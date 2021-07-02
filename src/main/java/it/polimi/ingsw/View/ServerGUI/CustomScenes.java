package it.polimi.ingsw.View.ServerGUI;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class CustomScenes {

    protected Stage mainStage;

    public CustomScenes(Stage stage){
        this.mainStage = stage;
    }

    public void setNextScene(Scene nextScene){
        this.mainStage.setScene(nextScene);
    }

    public void closeStage(){
        //this.mainStage.getOnCloseRequest().handle(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        this.mainStage.close();
    }

    abstract Scene getScene();
}
