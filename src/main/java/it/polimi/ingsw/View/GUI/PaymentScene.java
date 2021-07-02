package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.*;

/**
 * Scenes displaying what he needs to pay and where he can select the resources for the payment
 */
public class PaymentScene extends Initializable{
    private HashMap<Resource, Integer> leaderStorages1;
    private HashMap<Resource, Integer> leaderStorages2;
    private ArrayList<Resource[]> resources;
    private ArrayList<boolean[]> selected;
    private HashMap<Resource, Integer> strongbox;
    private HashMap<Resource, Integer> shelves;

    private EnumMap<Resource, Integer> shelfNumbers;

    public PaymentScene(){
        try {
            root = FXMLLoader.load(getClass().getResource("/Scenes/paymentScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button pay = (Button) root.lookup("#buyButton");
        pay.setOnAction(event -> {
            Transition.setOnContainersScene(false);
            observer.getCurrentState().next();
            new Thread(() -> sendMessage(new PaySelectedMessage())).start();

        });


    }

    @Override
    public void initialise(){
        AnchorPane cost = (AnchorPane) root.getChildren().get(2);
        HashMap<Resource, Integer> supportContainer = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getSupportContainer();
        ((Label) cost.getChildren().get(4)).setText(supportContainer.containsKey(Resource.COIN)? supportContainer.get(Resource.COIN).toString(): "0");
        ((Label) cost.getChildren().get(5)).setText(supportContainer.containsKey(Resource.SHIELD)? supportContainer.get(Resource.SHIELD).toString(): "0");
        ((Label) cost.getChildren().get(6)).setText(supportContainer.containsKey(Resource.SERVANT)? supportContainer.get(Resource.SERVANT).toString(): "0");
        ((Label) cost.getChildren().get(7)).setText(supportContainer.containsKey(Resource.STONE)? supportContainer.get(Resource.STONE).toString(): "0");

        initialiseButtons();
    }

    private void completeMap(HashMap<Resource, Integer> container){
        if(!container.containsKey(Resource.COIN)) container.put(Resource.COIN, 0);
        if(!container.containsKey(Resource.SHIELD)) container.put(Resource.SHIELD, 0);
        if(!container.containsKey(Resource.SERVANT)) container.put(Resource.SERVANT, 0);
        if(!container.containsKey(Resource.STONE)) container.put(Resource.STONE, 0);
    }

    public void initialiseButtons(){

        resources = new ArrayList<>();
        selected = new ArrayList<>();

        shelfNumbers = new EnumMap<>(Resource.class);
        HashMap<Resource, Integer> shelf = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getShelf(0);
        ArrayList<Resource> inside = new ArrayList<>(shelf.keySet());
        if (!inside.isEmpty()) shelfNumbers.put(inside.get(0), 1);
        shelves= new HashMap<>(shelf);
        shelf = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getShelf(1);
        inside = new ArrayList<>(shelf.keySet());
        if(!inside.isEmpty()) shelfNumbers.put(inside.get(0), 2);
        shelves.putAll(shelf);
        shelf = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getShelf(2);
        inside = new ArrayList<>(shelf.keySet());
        if(!inside.isEmpty())shelfNumbers.put(new ArrayList<>(shelf.keySet()).get(0), 3);
        shelves.putAll(shelf);

        completeMap(shelves);

        strongbox = new HashMap<>(observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getStrongbox());
        completeMap(strongbox);

        leaderStorages1 = new HashMap<>();
        leaderStorages2 = new HashMap<>();
        int[] leaderStoragesID = new int[2];
        ArrayList<Integer> lc = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderCards();
        for( int i = 0; i < lc.size(); i++){
            if(lc.get(i) > 52 && lc.get(i) < 57){
                leaderStoragesID[i] = lc.get(i);
                Resource[] storage = observer.getModel().getPlayerFromId(observer.getModel().getLocalPlayerId()).getLeaderStorage(lc.get(i));
                if(storage != null){
                    resources.add(storage);
                    selected.add(new boolean[]{false, false});
                    for (Resource resource : storage)
                        if( i == 0) leaderStorages1.put(resource, (leaderStorages1.containsKey(resource)) ? leaderStorages1.get(storage[i]) + 1 : 1);
                        else leaderStorages2.put(resource, (leaderStorages2.containsKey(resource)) ? leaderStorages2.get(storage[i]) + 1 : 1);

                }
            }
        }

        completeMap(leaderStorages1);
        completeMap(leaderStorages2);

        ((Button) root.lookup("#minusButton0")).setOnAction(event -> {
            new Thread(() -> sendMessage(new ShelfDeselectionMessage(shelfNumbers.get(Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count0");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < shelves.get(Resource.COIN)){
                root.lookup("#plusButton0").setDisable(false);
            }
            if(currentCount == 0){
                root.lookup("#minusButton0").setDisable(true);
            }
        });
        root.lookup("#minusButton0").setDisable(true);
        ((Button) root.lookup("#plusButton0")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new ShelfSelectionMessage(shelfNumbers.get(Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count0");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                root.lookup("#minusButton0").setDisable(false);
            }
            if(currentCount == shelves.get(Resource.COIN)){
                root.lookup("#plusButton0").setDisable(true);
            }
        });
        if(shelves.get(Resource.COIN) == 0) root.lookup("#plusButton0").setDisable(true);

        ((Button) root.lookup("#minusButton1")).setOnAction(event -> {
            new Thread(() -> sendMessage(new ShelfDeselectionMessage(shelfNumbers.get(Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count1");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < shelves.get(Resource.SHIELD)){
                root.lookup("#plusButton1").setDisable(false);
            }
            if(currentCount == 0){
                root.lookup("#minusButton1").setDisable(true);
            }
        });
        root.lookup("#minusButton1").setDisable(true);
        ((Button) root.lookup("#plusButton1")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new ShelfSelectionMessage(shelfNumbers.get(Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count1");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                root.lookup("#minusButton1").setDisable(false);
            }
            if(currentCount == shelves.get(Resource.SHIELD)){
                ( root.lookup("#plusButton1")).setDisable(true);
            }
        });
        if(shelves.get(Resource.SHIELD) == 0) ( root.lookup("#plusButton1")).setDisable(true);

        ((Button) root.lookup("#minusButton2")).setOnAction(event -> {
            new Thread(() -> sendMessage(new ShelfDeselectionMessage(shelfNumbers.get(Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count2");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < shelves.get(Resource.SERVANT)){
                ( root.lookup("#plusButton2")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton2")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton2")).setDisable(true);
        ((Button) root.lookup("#plusButton2")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new ShelfSelectionMessage(shelfNumbers.get(Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count2");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton2")).setDisable(false);
            }
            if(currentCount == shelves.get(Resource.SERVANT)){
                ( root.lookup("#plusButton2")).setDisable(true);
            }
        });
        if(shelves.get(Resource.SERVANT) == 0) ( root.lookup("#plusButton2")).setDisable(true);

        ((Button) root.lookup("#minusButton3")).setOnAction(event -> {
            new Thread(() -> sendMessage(new ShelfDeselectionMessage(shelfNumbers.get(Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count3");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < shelves.get(Resource.STONE)){
                ( root.lookup("#plusButton3")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton3")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton3")).setDisable(true);
        ((Button) root.lookup("#plusButton3")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage( new ShelfSelectionMessage(shelfNumbers.get(Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count3");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton3")).setDisable(false);
            }
            if(currentCount == shelves.get(Resource.STONE)){
                ( root.lookup("#plusButton3")).setDisable(true);
            }
        });
        if(shelves.get(Resource.STONE) == 0) ( root.lookup("#plusButton3")).setDisable(true);




        ((Button) root.lookup("#minusButton4")).setOnAction(event -> {
            new Thread(() -> sendMessage(new StrongBoxDeselectionMessage(1, Resource.COIN))).start();
            Label count = (Label) root.lookup("#count4");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < strongbox.get(Resource.COIN)){
                ( root.lookup("#plusButton4")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton4")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton4")).setDisable(true);
        ((Button) root.lookup("#plusButton4")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new StrongBoxSelectionMessage(1, Resource.COIN))).start();
            Label count = (Label) root.lookup("#count4");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton4")).setDisable(false);
            }
            if(currentCount == strongbox.get(Resource.COIN)){
                ( root.lookup("#plusButton4")).setDisable(true);
            }
        });
        if(strongbox.get(Resource.COIN) == 0) ( root.lookup("#plusButton4")).setDisable(true);

        ((Button) root.lookup("#minusButton5")).setOnAction(event -> {
            new Thread(() -> sendMessage(new StrongBoxDeselectionMessage(1, Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count5");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < strongbox.get(Resource.SHIELD)){
                ( root.lookup("#plusButton5")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton5")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton5")).setDisable(true);
        ((Button) root.lookup("#plusButton5")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new StrongBoxSelectionMessage(1, Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count5");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton5")).setDisable(false);
            }
            if(currentCount == strongbox.get(Resource.SHIELD)){
                ( root.lookup("#plusButton5")).setDisable(true);
            }
        });
        if(strongbox.get(Resource.SHIELD) == 0) ( root.lookup("#plusButton5")).setDisable(true);

        ((Button) root.lookup("#minusButton6")).setOnAction(event -> {
            new Thread(() -> sendMessage(new StrongBoxDeselectionMessage(1, Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count6");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < strongbox.get(Resource.SERVANT)){
                ( root.lookup("#plusButton6")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton6")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton6")).setDisable(true);
        ((Button) root.lookup("#plusButton6")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new StrongBoxSelectionMessage(1, Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count6");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton6")).setDisable(false);
            }
            if(currentCount == strongbox.get(Resource.SERVANT)){
                ( root.lookup("#plusButton6")).setDisable(true);
            }
        });
        if(strongbox.get(Resource.SERVANT) == 0) ( root.lookup("#plusButton6")).setDisable(true);

        ((Button) root.lookup("#minusButton7")).setOnAction(event -> {
            new Thread(() -> sendMessage(new StrongBoxDeselectionMessage(1, Resource.STONE))).start();
            Label count = (Label) root.lookup("#count7");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < strongbox.get(Resource.STONE)){
                ( root.lookup("#plusButton7")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton7")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton7")).setDisable(true);
        ((Button) root.lookup("#plusButton7")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new StrongBoxSelectionMessage(1, Resource.STONE))).start();
            Label count = (Label) root.lookup("#count7");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                (root.lookup("#minusButton7")).setDisable(false);
            }
            if(currentCount == strongbox.get(Resource.STONE)){
                (root.lookup("#plusButton7")).setDisable(true);
            }
        });
        if(strongbox.get(Resource.STONE) == 0) ( root.lookup("#plusButton7")).setDisable(true);

        ((Button) root.lookup("#minusButton8")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], deselectNextPosition(0, Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count8");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages1.get(Resource.COIN)){
                ( root.lookup("#plusButton8")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton8")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton8")).setDisable(true);
        ((Button) root.lookup("#plusButton8")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], selectNextPosition(0, Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count8");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton8")).setDisable(false);
            }
            if(currentCount == leaderStorages1.get(Resource.COIN)){
                ( root.lookup("#plusButton8")).setDisable(true);
            }
        });
        if(leaderStorages1.get(Resource.COIN) == 0) ( root.lookup("#plusButton8")).setDisable(true);

        ((Button) root.lookup("#minusButton9")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], deselectNextPosition(0, Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count9");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages1.get(Resource.SHIELD)){
                ( root.lookup("#plusButton9")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton9")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton9")).setDisable(true);
        ((Button) root.lookup("#plusButton9")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], selectNextPosition(0, Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count9");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton9")).setDisable(false);
            }
            if(currentCount == leaderStorages1.get(Resource.SHIELD)){
                ( root.lookup("#plusButton9")).setDisable(true);
            }
        });
        if(leaderStorages1.get(Resource.SHIELD) == 0) ( root.lookup("#plusButton9")).setDisable(true);

        ((Button) root.lookup("#minusButton10")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], deselectNextPosition(0, Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count10");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages1.get(Resource.SERVANT)){
                ( root.lookup("#plusButton10")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton10")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton10")).setDisable(true);
        ((Button) root.lookup("#plusButton10")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], selectNextPosition(0, Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count10");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton10")).setDisable(false);
            }
            if(currentCount == leaderStorages1.get(Resource.SERVANT)){
                ( root.lookup("#plusButton10")).setDisable(true);
            }
        });
        if(leaderStorages1.get(Resource.SERVANT) == 0) ( root.lookup("#plusButton10")).setDisable(true);

        ((Button) root.lookup("#minusButton11")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], deselectNextPosition(0, Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count11");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages1.get(Resource.STONE)){
                ( root.lookup("#plusButton11")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton11")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton11")).setDisable(true);
        ((Button) root.lookup("#plusButton11")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[0], selectNextPosition(0, Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count11");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton11")).setDisable(false);
            }
            if(currentCount == leaderStorages1.get(Resource.STONE)){
                ( root.lookup("#plusButton11")).setDisable(true);
            }
        });
        if(leaderStorages1.get(Resource.STONE) == 0) ( root.lookup("#plusButton11")).setDisable(true);

        ((Button) root.lookup("#minusButton12")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], deselectNextPosition(1, Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count12");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages2.get(Resource.COIN)){
                ( root.lookup("#plusButton12")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton12")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton12")).setDisable(true);
        ((Button) root.lookup("#plusButton12")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], selectNextPosition(1, Resource.COIN), Resource.COIN))).start();
            Label count = (Label) root.lookup("#count12");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton12")).setDisable(false);
            }
            if(currentCount == leaderStorages2.get(Resource.COIN)){
                ( root.lookup("#plusButton12")).setDisable(true);
            }
        });
        if(leaderStorages2.get(Resource.COIN) == 0) ( root.lookup("#plusButton12")).setDisable(true);

        ((Button) root.lookup("#minusButton13")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], deselectNextPosition(1, Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#coun13");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages2.get(Resource.SHIELD)){
                ( root.lookup("#plusButton13")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton13")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton13")).setDisable(true);
        ((Button) root.lookup("#plusButton13")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], selectNextPosition(1, Resource.SHIELD), Resource.SHIELD))).start();
            Label count = (Label) root.lookup("#count13");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton13")).setDisable(false);
            }
            if(currentCount == leaderStorages2.get(Resource.SHIELD)){
                ( root.lookup("#plusButton13")).setDisable(true);
            }
        });
        if(leaderStorages2.get(Resource.SHIELD) == 0) ( root.lookup("#plusButton13")).setDisable(true);

        ((Button) root.lookup("#minusButton14")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], deselectNextPosition(1, Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count14");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages2.get(Resource.SERVANT)){
                ( root.lookup("#plusButton14")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton14")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton14")).setDisable(true);
        ((Button) root.lookup("#plusButton14")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], selectNextPosition(1, Resource.SERVANT), Resource.SERVANT))).start();
            Label count = (Label) root.lookup("#count14");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton14")).setDisable(false);
            }
            if(currentCount == leaderStorages2.get(Resource.SERVANT)){
                ( root.lookup("#plusButton14")).setDisable(true);
            }
        });
        if(leaderStorages2.get(Resource.SERVANT) == 0) ( root.lookup("#plusButton14")).setDisable(true);

        ((Button) root.lookup("#minusButton15")).setOnAction(event -> {
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], deselectNextPosition(1, Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count15");
            int currentCount = Integer.parseInt(count.getText())-1;
            count.setText(String.valueOf(currentCount));
            if(currentCount < leaderStorages2.get(Resource.STONE)){
                ( root.lookup("#plusButton15")).setDisable(false);
            }
            if(currentCount == 0){
                ( root.lookup("#minusButton15")).setDisable(true);
            }
        });
        ( root.lookup("#minusButton15")).setDisable(true);
        ((Button) root.lookup("#plusButton15")).setOnAction(event -> {
            if(root.lookup("#quit") != null) root.lookup("#quit").setVisible(false);
            new Thread(() -> sendMessage(new LeaderStorageSelectionMessage(leaderStoragesID[1], selectNextPosition(1, Resource.STONE), Resource.STONE))).start();
            Label count = (Label) root.lookup("#count15");
            int currentCount = Integer.parseInt(count.getText())+1;
            count.setText(String.valueOf(currentCount));
            if(currentCount > 0){
                ( root.lookup("#minusButton15")).setDisable(false);
            }
            if(currentCount == leaderStorages2.get(Resource.STONE)){
                ( root.lookup("#plusButton15")).setDisable(true);
            }
        });
        if(leaderStorages2.get(Resource.STONE) == 0) ( root.lookup("#plusButton15")).setDisable(true);
    }

    private int selectNextPosition(int numberOfStorage, Resource toSelect){
        Resource[] res = resources.get(numberOfStorage);
        boolean[] sel = selected.get(numberOfStorage);

        for(int i = 1; i >= 0; i--){
            if(res[i] == toSelect && !sel[i]){
                sel[i] = true;
                return i + 1;
            }
        }

        return -1;
    }

    private int deselectNextPosition(int numberOfStorage, Resource toDeselect){
        Resource[] res = resources.get(numberOfStorage);
        boolean[] sel = selected.get(numberOfStorage);

        for(int i = 0; i < 2; i++){
            if(res[i] == toDeselect && sel[i]){
                sel[i] = false;
                return i + 1;
            }
        }

        return -1;
    }


}
