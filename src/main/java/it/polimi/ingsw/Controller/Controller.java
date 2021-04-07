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
                    EnumMap<Resource, Integer> toBePaid = table.getDevDecks()[numberOfDeck - 1].getTopCard().getCost();
                    if (in == 0) {
                        System.out.println("Choose a Leader Card.");
                        in = input.nextInt();
                        try {
                            applyDiscountAbility(playerOfTurn.getLeaderCards()[in], toBePaid);
                        } catch (IndexOutOfBoundsException e) {
                            //messaggio: indice di card non corretto
                        }
                    }
                    Payment howToPay = new Payment(playerOfTurn, toBePaid);
                    createWallet(howToPay);
                    try {
                        //committa tramite il transaction catalyst di howToPay (?)
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

    private void createWallet(Payment howToPay){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose how to pay.");
        String in = input.nextLine();
        while(!in.equals('\n')){
            howToPay.select(in);
            in = input.nextLine();
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

    private EnumMap<Resource, Integer> applyStorageAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toPlace){
        Depot remainedResources = new Depot();
        for (EnumMap.Entry<Resource, Integer> entry : toPlace.entrySet())
            for(int i = 0; i < entry.getValue(); i++)
                if (!leaderCardForAction.getAbility().isFull(entry.getKey())){
                    leaderCardForAction.getAbility().add(entry.getKey());
                } else {
                    remainedResources.singleAdd(entry.getKey());
                }
        return remainedResources.content();
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
}