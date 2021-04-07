package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Enums.*;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.Player.*;
import it.polimi.ingsw.Cards.*;

import java.util.*;

public class Controller {
    private Table table;
    private FaithTrack faithTrack;
    private List<String> players;

    public void addNewPlayer(String playerName){
        players.add(playerName);
    }

    public void removePlayer(String playerName){
        players.remove(playerName);
    }

    public void startGame() {

    }

    public void playTurn(RealPlayer playerOfTurn){

    }

    private void chooseTurnType(TurnType typeOfTurn){
        switch(typeOfTurn) {
            case BUYNEWCARD:
                playBuyCardTurn(table.turnOf());
                break;
            case GETFROMMARKET:
                playMarketTurn(table.turnOf());
                break;
            case PRODUCITON:
                playProductionTurn(table.turnOf());
                break;
        }
    }

    private void playMarketTurn(RealPlayer playerOfTurn){
        boolean rowOrColumn;
        int number;
        EnumMap<Resource, Integer> stillToBeSet;

        //scegliere se riga o se colonna (suppongo boolean: 1 riga, 0 colonna)

        if (rowOrColumn){       //Sceglie riga
            stillToBeSet = table.getMarket().pickRow();
        }
        else{           //Sceglie colonna
            stillToBeSet = table.getMarket().pickColumn();
        }


    }

    private void playBuyCardTurn(RealPlayer playerOfTurn) throws ActionNotDone {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a DevDeck.");
        int numberOfDeck = input.nextInt();
        try {
            if(!table.getDevDecks()[numberOfDeck - 1].isEmpty()) {
                if (atLeastOneDevSlotIsAvailable(playerOfTurn, table.getDevDecks()[numberOfDeck - 1].getTopCard())){
                    //messaggio: non hai dove mettere questa carta
                    throw new ActionNotDone();
                } else {
                    System.out.println("Do you want to use discounts? 0 = no, else = yes");
                    int in = input.nextInt();
                    TransactionCatalyst catalyst;
                    EnumMap<Resource, Integer> toBePaid = table.getDevDecks()[numberOfDeck - 1].getTopCard().getCost();
                    if (in == 0) {
                        System.out.println("Choose a Leader Card.");
                        in = input.nextInt();
                        applyDiscountAbility(playerOfTurn.getLeaderCards()[in], toBePaid);
                    }
                    catalyst = new TransactionCatalyst(toBePaid);
                    createWallet(playerOfTurn, catalyst);
                    try {
                        catalyst.commit();
                        chooseDevSlot(playerOfTurn, table.drawDevDeck(numberOfDeck - 1));
                    } catch (IndexOutOfBoundsException e) {
                        //messaggio: non hai le risorse necessarie
                        throw new ActionNotDone();
                    }
                }
            } else {
                throw new ActionNotDone();
            }
        } catch (IndexOutOfBoundsException e){
            //messaggio: non esiste questo mazzetto
            throw new ActionNotDone();
        } finally {
            input.close();
        }
    }

    private boolean atLeastOneDevSlotIsAvailable(RealPlayer playerOfTurn, DevCard card){
        boolean result = false;
        for(int i = 0; i < playerOfTurn.getDevSlots().length; i++)
            result = result || playerOfTurn.getDevSlots()[i].isInsertable(card);
        return result;
    }

    private void chooseDevSlot(RealPlayer playerOfTurn, DevCard toBePlaced){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a Slot.");
        int numberOfSlot = input.nextInt();
        while(true){
            try {
                playerOfTurn.getDevSlots()[numberOfSlot - 1].addCard(toBePlaced);
                break;
            } catch (CantPutThisHere e) {
                //messaggio: non puoi mettere questa carta qui
                System.out.println("Choose a Slot.");
                numberOfSlot = input.nextInt();
            } catch (IndexOutOfBoundsException e) {
                //messaggio: non esiste questo numero di slot
                System.out.println("Choose a Slot.");
                numberOfSlot = input.nextInt();
            }
        }
        input.close();
    }

    private void createWallet(RealPlayer playerOfTurn, TransactionCatalyst catalyst){
        //senza controlli, solo per avere qualcosa che crei il wallet in questa fase della progettazione
        Scanner input = new Scanner(System.in);
        System.out.println("What do you pay from the first Shelf? First resource, then quantity");
        Resource resource = resourceConverter(input.nextInt());
        int quantity = input.nextInt();
        EnumMap<Resource, Integer> map = new EnumMap<>(Resource.class);
        map.put(resource, quantity);
        catalyst.put(playerOfTurn.getShelves()[0], map);
        System.out.println("What do you pay from the second Shelf? First resource, then quantity");
        resource = resourceConverter(input.nextInt());
        quantity = input.nextInt();
        map.clear();
        map.put(resource, quantity);
        catalyst.put(playerOfTurn.getShelves()[1], map);
        System.out.println("What do you pay from the third Shelf? First resource, then quantity");
        resource = resourceConverter(input.nextInt());
        quantity = input.nextInt();
        map.clear();
        map.put(resource, quantity);
        catalyst.put(playerOfTurn.getShelves()[2], map);
        System.out.println("What do you pay from the StrongBox? First resource, then quantity");
        int i = input.nextInt();
        map.clear();
        while(i>= 0 && i < 4){
            resource = resourceConverter(i);
            quantity = input.nextInt();
            map.put(resource, quantity);
            i = input.nextInt();
        }
        catalyst.put(playerOfTurn.getDepot(), map);
        map.clear();
        for(LeaderCard card: playerOfTurn.getLeaderCards()){
            if (card.getType() == LeaderCardType.STORAGE){
                System.out.println("What do you pay from the LeaderCard? First resource, then quantity");
                resource = resourceConverter(i);
                quantity = input.nextInt();
                map.put(resource, quantity);
            }
        }
        input.close();
    }

    private void playProductionTurn(RealPlayer playerOfTurn) {

    }

    public void actionOnLeaderCard(RealPlayer playerOfTurn, LeaderCard leaderCardForAction, Boolean discard) throws UnsatisfiedRequirements {
        if (discard){
            playerOfTurn.discardLeaderCard(leaderCardForAction);
            playerOfTurn.moveForward(1);
        } else if (!leaderCardForAction.hasBeenPlayed()){
            if (!checkRequirements(playerOfTurn, leaderCardForAction))
                throw new UnsatisfiedRequirements();
            leaderCardForAction.play();
        }
    }

    private boolean checkRequirements(RealPlayer playerOfTurn, LeaderCard leaderCardForAction){
        return checkResourceReq(playerOfTurn, leaderCardForAction) && checkDevCardTypeReq(playerOfTurn, leaderCardForAction);
    }

    private boolean checkResourceReq(RealPlayer playerOfTurn, LeaderCard leaderCardForAction){
        // prende il contenuto che hai da tutti
        Depot allResourceOwned = new Depot();
        allResourceOwned.addEnumMap(playerOfTurn.resourcesOwned());
        return allResourceOwned.contains(leaderCardForAction.getResourceReq());
    }

    private boolean checkDevCardTypeReq(RealPlayer playerOfTurn, LeaderCard leaderCardForAction){
        boolean devCardReq = true;
        if(!leaderCardForAction.getDevCardReq().isEmpty()){
            ArrayList<DevCardType> ownedDevCard = new ArrayList<>();
            for(int i = 0; i < playerOfTurn.getDevSlots().length; i++)
                ownedDevCard.addAll(playerOfTurn.getDevSlots()[i].getDevCardTypeContained());
            int sum;
            for (Map.Entry<DevCardType, Integer> entry : leaderCardForAction.getDevCardReq().entrySet()) {
                sum = 0;
                for (DevCardType devCardType : ownedDevCard) {
                    if ((entry.getKey().getLevel() == 0 && devCardType.getColor() == entry.getKey().getColor()) || (entry.getKey().getLevel() == devCardType.getLevel() && devCardType.getColor() == entry.getKey().getColor()))
                        sum++;
                }
                if (sum != entry.getValue()){
                    devCardReq = false;
                    break;
                }
            }
        }
        return devCardReq;
    }

    private void applyStorageAbility(LeaderCard leaderCardForAction){
        System.out.println("Choose the resource to put.");
        //si deve gestire anche da dove prenderla
        System.out.println("0 = stone\n1 = servant\n2 = shield\n3 = coin");
        Scanner input = new Scanner(System.in);
        Resource resource = resourceConverter(input.nextInt());
        input.close();
        if (resource != null && leaderCardForAction.getAbility().getCapacity().containsKey(resource) && !leaderCardForAction.getAbility().isFull(resource)){
            leaderCardForAction.getAbility().add(resource);
        } else {
            //messaggio: non puoi inserire qui questa risorsa
        }
    }

    private void applyDiscountAbility (LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePaid){
        for (EnumMap.Entry<Resource, Integer> entry : toBePaid.entrySet())
            toBePaid.put(entry.getKey(), entry.getValue() - ((leaderCardForAction.getAbility().getDiscount().get(entry.getKey()) != null)? leaderCardForAction.getAbility().getDiscount().get(entry.getKey()): 0));
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced){
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
                toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*toBePlaced.get(Resource.WHITE));
            toBePlaced.remove(Resource.WHITE);
        }
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced, int quantity){
        for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
            toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*quantity);
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction1, LeaderCard leaderCardForAction2, EnumMap<Resource, Integer> toBePlaced){
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            Scanner input = new Scanner(System.in);
            System.out.println("How many whites do you want to transmute with the first Leader Card?");
            int firstQuantity = input.nextInt();
            System.out.println("How many whites do you want to transmute with the second Leader Card?");
            int secondQuantity = input.nextInt();
            while(firstQuantity + secondQuantity != toBePlaced.get(Resource.WHITE)){
                System.out.println("The sum doesn't match with the number of whites you gained.");
                System.out.println("How many whites do you want to transmute with the first Leader Card?");
                firstQuantity = input.nextInt();
                System.out.println("How many whites do you want to transmute with the second Leader Card?");
                secondQuantity = input.nextInt();
            }
            input.close();

            applyTransmutationAbility(leaderCardForAction1, toBePlaced, firstQuantity);
            applyTransmutationAbility(leaderCardForAction2, toBePlaced, secondQuantity);
            toBePlaced.remove(Resource.WHITE);

        }
    }

    //creato per gestire l'input testuale di una risorsa
    private Resource resourceConverter(int i){
        switch(i) {
            case 0:
                return Resource.STONE;
            case 1:
                return Resource.SERVANT;
            case 2:
                return Resource.SHIELD;
            case 3:
                return Resource.COIN;
            default:
                return null;
        }
    }
}