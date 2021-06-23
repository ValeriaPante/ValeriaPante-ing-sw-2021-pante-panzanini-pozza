package it.polimi.ingsw.View.ServerGUI;

import it.polimi.ingsw.Network.RequestHandlers.PreGameRequestHandler;
import it.polimi.ingsw.Network.Server.Server;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ServerGUI extends Application {

    private boolean startServer = true;

    @Override
    public void start(Stage stage){
        stage.close();
        Stage myStage = new Stage();
        myStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                myStage.close();
                startServer = false;
            }
        });
        Scene leaderScene = new LeaderCardsPersonalizationScene(myStage).getScene();
        //Scene leaderScene = new DevCardPersonalizationScene(myStage).getScene();
        myStage.setResizable(false);
        myStage.setScene(leaderScene);
        myStage.showAndWait();
        myStage.close();
        if (this.startServer){
            startServer();
        }
    }

    public static void startServer(){
        PreGameRequestHandler controller = new PreGameRequestHandler();
        Server server = new Server(42000, controller);
        server.start();
    }


}
