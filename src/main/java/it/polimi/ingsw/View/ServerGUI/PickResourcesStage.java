package it.polimi.ingsw.View.ServerGUI;

import it.polimi.ingsw.Enums.Resource;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.EnumMap;

public class PickResourcesStage{

    private Stage pickResourceScene;
    private Pane root;
    private Text coin;
    private Text servant;
    private Text stone;
    private Text shield;
    private TextField coinNum;
    private TextField servantNum;
    private TextField stoneNum;
    private TextField shieldNum;
    private Button confirmButton;

    private EnumMap<Resource, Integer> result;


    public PickResourcesStage(){
        this.result = new EnumMap<>(Resource.class);

        this.pickResourceScene = new Stage();
        this.pickResourceScene.setResizable(false);
        this.root = new Pane();
        this.root.setPadding(new Insets(20));
        this.coin = new Text("COIN:");
        this.coin.setX(20);
        this.coin.setY(coin.getLayoutBounds().getHeight()+20);
        this.servant = new Text(coin.getX(), coin.getY()+50,"SERVANT:");
        this.stone = new Text(coin.getX(),coin.getY()+100,"STONE:");
        this.shield = new Text(coin.getX(),coin.getY()+150,"SHIELD: ");
        this.coinNum = new TextField();
        this.coinNum.setLayoutX(coin.getX()+75);
        this.coinNum.setLayoutY(coin.getY() - coin.getLayoutBounds().getHeight());
        this.coinNum.setPrefWidth(50);
        this.coinNum.setText("0");
        this.servantNum = new TextField();
        this.servantNum.setLayoutX(coinNum.getLayoutX());
        this.servantNum.setLayoutY(coinNum.getLayoutY()+50);
        this.servantNum.setPrefWidth(50);
        this.servantNum.setText("0");
        this.stoneNum = new TextField();
        this.stoneNum.setLayoutX(coinNum.getLayoutX());
        this.stoneNum.setLayoutY(coinNum.getLayoutY()+100);
        this.stoneNum.setPrefWidth(50);
        this.stoneNum.setText("0");
        this.shieldNum = new TextField();
        this.shieldNum.setLayoutX(coinNum.getLayoutX());
        this.shieldNum.setLayoutY(coinNum.getLayoutY()+150);
        this.shieldNum.setPrefWidth(50);
        this.shieldNum.setText("0");

        this.confirmButton = new Button("Ok");
        this.confirmButton.setLayoutX(coinNum.getLayoutX());
        this.confirmButton.setLayoutY(coinNum.getLayoutY()+200);
        this.confirmButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    modifyResult();
                    pickResourceScene.close();
                }catch (NumberFormatException e){
                    //pass
                }
            }
        });
        this.root.getChildren().addAll(this.coin, this.servant, this.stone, this.shield,
                this.coinNum, this.servantNum, this.stoneNum, this.shieldNum, this.confirmButton);
        this.pickResourceScene.setScene(new Scene(root));
    }

    private void modifyResult(){
/*                        if (Integer.parseInt(coinNum.getCharacters().toString()) != 0) {
                    put(Resource.COIN, Integer.parseInt(coinNum.getCharacters().toString()));
                }
                if (Integer.parseInt(servantNum.getCharacters().toString()) != 0) {
                    put(Resource.SERVANT, Integer.parseInt(servantNum.getCharacters().toString()));
                }
                if (Integer.parseInt(stoneNum.getCharacters().toString()) != 0) {
                    put(Resource.STONE, Integer.parseInt(stoneNum.getCharacters().toString()));
                }
                if (Integer.parseInt(shieldNum.getCharacters().toString()) != 0) {
                    put(Resource.SHIELD, Integer.parseInt(shieldNum.getCharacters().toString()));
                }*/
        this.result.put(Resource.COIN, Integer.parseInt(coinNum.getCharacters().toString()));
        this.result.put(Resource.SERVANT, Integer.parseInt(servantNum.getCharacters().toString()));
        this.result.put(Resource.STONE, Integer.parseInt(stoneNum.getCharacters().toString()));
        this.result.put(Resource.SHIELD, Integer.parseInt(shieldNum.getCharacters().toString()));
    }

    public void show(){
        this.pickResourceScene.show();
    }

    public void showAndWait(){
        this.pickResourceScene.showAndWait();
    }

    public EnumMap<Resource, Integer> showAndWaitResults(){
        //non mi viene in mente come questa cosa possa tirare eccezione
        this.modifyResult();
        this.pickResourceScene.showAndWait();
        return this.result;
    }

    public void setValues(int coins, int servants, int stones, int shields){
        this.coinNum.setText(Integer.toString(coins));
        this.servantNum.setText(Integer.toString(servants));
        this.stoneNum.setText(Integer.toString(stones));
        this.shieldNum.setText(Integer.toString(shields));
    }

    public void setValues(String coins, String servants, String stones, String shields){
        this.coinNum.setText(coins);
        this.servantNum.setText(servants);
        this.stoneNum.setText(stones);
        this.shieldNum.setText(shields);
    }
}
