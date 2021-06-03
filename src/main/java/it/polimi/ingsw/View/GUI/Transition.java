package it.polimi.ingsw.View.GUI;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Transition {
    private static Stage primaryStage;
    private static Stage dialogStage;
    private static Scene welcomeScene;
    private static Scene usernameScene;
    private static Scene lobbiesScene;
    private static Scene waitingToStartScene;
    private static Scene loadingScene;
    private static Scene leaderCardsScene;
    private static Scene initialResourcesScene;
    private static Scene mainScene;

    public static void setPrimaryStage(Stage primaryStage) {
        Transition.primaryStage = primaryStage;
    }

    public static void setDialogStage(Stage dialogStage){
        Transition.dialogStage = dialogStage;
    }

    public static void setDialogScene(Scene scene){
        dialogStage.setScene(scene);
    }

    public static void setWelcomeScene(WelcomeScene welcomeScene) {
        Transition.welcomeScene = new Scene(welcomeScene.getRoot());
    }

    public static void setUsernameScene(UsernameScene usernameScene) {
        Transition.usernameScene = new Scene(usernameScene.getRoot());
    }

    public static void setLobbiesScene(LobbiesScene lobbiesScene) {
        Transition.lobbiesScene = new Scene(lobbiesScene.getRoot());
    }

    public static void setWaitingToStartScene(WaitingToStartScene waitingToStartScene) {
        Transition.waitingToStartScene = new Scene(waitingToStartScene.getRoot());
    }

    public static void setLoadingScene(LoadingScene loadingScene){
        Transition.loadingScene = new Scene(loadingScene.getRoot());
    }

    public static void setLeaderCardsScene(LeaderCardScene leaderCardsScene){
        Transition.leaderCardsScene = new Scene(leaderCardsScene.getRoot());
    }

    public static void setInitialResourcesScene(InitialResourcesScene initialResourcesScene){
        Transition.initialResourcesScene = new Scene(initialResourcesScene.getRoot());
    }

    public static void setMainScene(MainScene mainScene){
        Transition.mainScene = new Scene(mainScene.getRoot());
    }

    public static void setMainScene(SinglePlayerMainScene mainScene){
        Transition.mainScene = new Scene(mainScene.getRoot());
    }

    public static void toWelcomeScene(){
        primaryStage.setScene(welcomeScene);
    }

    public static void toUsernameScene(){
        primaryStage.setScene(usernameScene);
    }

    public static void toLobbiesScene(){
        if(!WaitingToStartScene.isReady()) primaryStage.setScene(lobbiesScene);
    }

    public static void toWaitingToStartScene(){
        if(loadingScene == null) primaryStage.setScene(waitingToStartScene);
    }

    public static void toLoadingScene(){
        primaryStage.setScene(loadingScene);
    }

    public static void toLeaderCardScene(){
        primaryStage.setScene(leaderCardsScene);
    }

    public static void toInitialResourcesScene(){
        primaryStage.setScene(initialResourcesScene);
    }

    public static void toMainScene(){
        primaryStage.setScene(mainScene);
    }

    public static void showDialog(){
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.showAndWait();
    }

    public static void setUnclosableDialog(){

    }

    public static void hideDialog(){
        dialogStage.close();
    }

    public static void updateLeaderCards(int index){
        GridPane gridPane = (GridPane) leaderCardsScene.getRoot().lookup("#gridPane");
        gridPane.getChildren().remove(index);
        gridPane.add(new Pane(),index,0);
        if(LeaderCardScene.getCount() == 2){

        }
    }
}
