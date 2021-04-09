package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Decks.LeaderDeck;
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
    private FaithTrackController faithTrackController;
    private final List<String> players;

    public void addNewPlayer(String playerName){
        players.add(playerName);
    }

    public void removePlayer(String playerName){
        players.remove(playerName);
    }

    private void initializePlayersLeaderCard(){
        LeaderDeck leaderDeck = new LeaderDeck();
        leaderDeck.shuffle();
        RealPlayer[] lisOfPlayers = table.getPlayers();

        for (RealPlayer player : lisOfPlayers)
                for (int i=0; i<4; i++)
                    player.addLeaderCard(leaderDeck.draw());

        waitForDiscarding();
    }

    //I need a way to receive in input the card that the player want to discard
    private void waitForDiscarding(){
        int totalNumberOfLCs;
        RealPlayer[] listOfPlayers = table.getPlayers();
        do{
            totalNumberOfLCs = 0;
            for (RealPlayer player : listOfPlayers)
                totalNumberOfLCs += player.getLeaderCards().length;
        } while(totalNumberOfLCs > ((listOfPlayers.length) * 2) );
    }

    private controll(RealPlayer player, String inputPLayer){
        InputManager input = InputManager.getInstance();
        SelectResourceOutput output = input.selectResourcesInStorages(inputPLayer, player);
        if (output.getStorage() == player.getDepot())
            throw eccezione;
    }

    private void initializePlayersResources(){
        RealPlayer[] listOfPlayers = table.getPlayers();

        if(listOfPlayers.length > 2)
            for(int i=2; i < listOfPlayers.length; i++)
                listOfPlayers[i].moveForward(1);

        for(int i=1; i < listOfPlayers.length; i++)
            setInitialResources(listOfPlayers[i], (i == 3) ? 2 : 1);
    }

    private void setInitialResources(RealPlayer player, int numberOfResources){
        Scanner input = new Scanner(System.in);

    }

    public void startGame() {
        Scanner input = new Scanner(System.in);

        this.table = new Table(players.size());
        this.faithTrackController = FaithTrackController.getInstance();

        if (players.size()>1){
            table.setMultiPlayer();
            Collections.shuffle(players);  //Players' playing order is random

            for (String nickName : players)
                table.addPlayer(new RealPlayer(nickName));
        }
        else{
            table.setSinglePlayer();
            table.addPlayer(new RealPlayer(players.get(0)));
        }

        initializePlayersLeaderCard();

        //assegnamento delle risorse ai giocatori
        if (!table.isSinglePlayer())
            initializePlayersResources();

        //inizia turno il primo giocatore
    }

    public void playTurn(RealPlayer playerOfTurn){
        //controllare se bisogna fare azione sulle leader card (giocarle o scartarle)
        Scanner input = new Scanner(System.in);
        if(playerOfTurn.getLeaderCards().length != 0){
            chooseLeaderCard(playerOfTurn, input);
        }
        //leggere il tipo di turno e chiamare chooseturntype, se ritorna l'eccezione, bisogna scegliere di nuovo il tipo di turno (loop)
        System.out.println("Choose the type of turn you want to play.");
        String in = input.nextLine();
        while(true){
            try {
                typeOfTurnConverter(in);
                break;
            } catch (ChangeTurnException e) {
                //messaggio: non è stato possibile svolgere questo tipo di turno
            }
        }
        //controllare se bisogna fare azione sulle leader card (giocarle o scartarle)
        if(playerOfTurn.getLeaderCards().length != 0){
            chooseLeaderCard(playerOfTurn, input);
        }
        input.close();
        //finire il turno (far giocare il prossimo player, da leggere dal table)
        if(table.isSinglePlayer()){
            table.nextTurn();
            playActionToken(table.getLorenzo().getActionTokenDeck().draw());
        }
        table.nextTurn();
    }

    private void playActionToken(ActionToken token){
        switch (token.getType()){
            case TWOFP:
                faithTrackController.movePlayerOfTurn(this.table, 2);
                break;
            case RESETDECKONEFP:
                faithTrackController.movePlayerOfTurn(this.table, 1);
                table.getLorenzo().getActionTokenDeck().reset();
                break;
            case DISCARDGREEN:
                discardDevCards(0);
                break;
            case DISCARDYELLOW:
                discardDevCards(1);
                break;
            case DISCARDBLUE:
                discardDevCards(2);
                break;
            case DISCARDPURPLE:
                discardDevCards(3);
                break;
        }
    }

    private void discardDevCards(int color){
        int cardsToDiscard = 2;
        int level = 0;
        ArrayList<DevDeck> lineOfDecks = new ArrayList<>();
        lineOfDecks.add(table.getDevDecks()[color]);
        lineOfDecks.add(table.getDevDecks()[color + 4]);
        lineOfDecks.add(table.getDevDecks()[color + 8]);
        while(cardsToDiscard > 0 && level < 3){
            if (lineOfDecks.get(level).size() > 1){
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
            } else if (lineOfDecks.get(level).size() == 1) {
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
                level++;
            } else {
                level++;
            }
        }
    }

    //si potrebbe collassare in chooseTurnType passando un stringa al posto di un TurnType
    private void typeOfTurnConverter(String s) throws ChangeTurnException{
        switch (s.trim().toUpperCase()) {
            case "BUYNEWCARD":
                chooseTurnType(TurnType.BUYNEWCARD);
                break;
            case "GETFROMMARKET":
                chooseTurnType(TurnType.GETFROMMARKET);
                break;
            case "PRODUCTION":
                chooseTurnType(TurnType.PRODUCITON);
                break;
            default:
                throw new ChangeTurnException();
        }
    }

    private void chooseLeaderCard(RealPlayer playerOfTurn, Scanner input){
        System.out.println("Choose how to play your Leader Card.");
        String in = input.nextLine();
        while(!in.trim().isEmpty()){
            switch(in.trim()){
                case "L1":
                    try {
                        actionOnLeaderCard(playerOfTurn, playerOfTurn.getLeaderCards()[0], false);
                    } catch (UnsatisfiedRequirements e){
                        // non hai i requisiti necessari per questa carta
                    }
                    break;
                case "-L1":
                    try {
                        actionOnLeaderCard(playerOfTurn, playerOfTurn.getLeaderCards()[0], true);
                    } catch (UnsatisfiedRequirements e){
                        // non hai i requisiti necessari per questa carta
                    }
                    break;
                case "L2":
                    try {
                        try {
                            actionOnLeaderCard(playerOfTurn, playerOfTurn.getLeaderCards()[1], false);
                        } catch (IndexOutOfBoundsException e){
                            //hai solo una carta
                        }
                    } catch (UnsatisfiedRequirements e){
                        // non hai i requisiti necessari per questa carta
                    }
                    break;
                case "-L2":
                    try {
                        try {
                            actionOnLeaderCard(playerOfTurn, playerOfTurn.getLeaderCards()[1], true);
                        } catch (IndexOutOfBoundsException e){
                            //hai solo una carta
                        }
                    } catch (UnsatisfiedRequirements e){
                        // non hai i requisiti necessari per questa carta
                    }
                    break;
                default:
                    break;
            }
            in = input.nextLine();

        }
    }

    private void chooseTurnType(TurnType typeOfTurn) throws ChangeTurnException{
        try {
            switch (typeOfTurn) {
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
        } catch (ActionNotDone e){
            throw new ChangeTurnException();
        }
    }

    private int moveToShelves(EnumMap<Resource, Integer> mapToBePlaced, RealPlayer player){

    }

    private int numberOfPlayerTAs(RealPlayer player){
        LeaderCard[] arrayOfLCs = player.getLeaderCards();
        int accumulator = 0;

        for (LeaderCard lc : arrayOfLCs)
            if (lc.getType() == LeaderCardType.TRANSMUTATION)
                accumulator++;

        return accumulator;
    }

    private LeaderCard[] getArrayOfLCWTA(RealPlayer player){
        LeaderCard[] arrayOfLCs = player.getLeaderCards();

        if (numberOfPlayerTAs(player) == 2) //he only owns leader cards with transmutation ability
            return arrayOfLCs;

        LeaderCard[] arrayOfLCWTA = new LeaderCard[numberOfPlayerTAs(player)];

        for (LeaderCard lc : arrayOfLCs)
            if (lc.getType() == LeaderCardType.TRANSMUTATION)
                arrayOfLCWTA[0] = lc;

        return arrayOfLCWTA;
    }

    private void playMarketTurn(RealPlayer playerOfTurn){
        Scanner playerInput = new Scanner( System.in );
        int number;
        Depot stillToBeSet = new Depot();

        number = playerInput.nextInt(); //0 per riga, 1 per colonna
        if (0 == number){       //Sceglie riga
            do
                number = playerInput.nextInt();
            while(number > 2);
            stillToBeSet.addEnumMap(table.getMarket().pickRow(number));
        }
        else{           //Sceglie colonna
            do
                number = playerInput.nextInt();
            while(number > 3);
            stillToBeSet.addEnumMap(table.getMarket().pickColumn(number));
        }

        if (stillToBeSet.content().containsKey(Resource.FAITH))
            faithTrackController.movePlayerOfTurn(table, stillToBeSet.content().get(Resource.FAITH));

        if (stillToBeSet.content().containsKey(Resource.WHITE)){
            int numberOPTAs = numberOfPlayerTAs(playerOfTurn);

            if (numberOPTAs > 0){
                number = playerInput.nextInt(); //1 per attivare, 0 per non attivare

                if (number == 1){
                    LeaderCard[] arrayOfLeaderCardTransmutation = getArrayOfLCWTA(playerOfTurn);

                    if (numberOPTAs == 1){
                        for (int i=0; i < stillToBeSet.content().get(Resource.WHITE); i++){
                            stillToBeSet.singleRemove(Resource.WHITE);
                            stillToBeSet.addEnumMap(arrayOfLeaderCardTransmutation[0].getAbility().getWhiteInto());
                        }
                    }

                    else{
                        number = playerInput.nextInt(); //0 per attivare la prima, 1 per non attivare la seconda
                        for (int i=0; i < stillToBeSet.content().get(Resource.WHITE); i++){
                            stillToBeSet.singleRemove(Resource.WHITE);
                            stillToBeSet.addEnumMap(arrayOfLeaderCardTransmutation[number].getAbility().getWhiteInto());
                        }
                    }
                }

                else{
                    for (int i=0; i < stillToBeSet.content().get(Resource.WHITE); i++)
                        stillToBeSet.singleRemove(Resource.WHITE);
                }
            }

            else{
                for (int i=0; i < stillToBeSet.content().get(Resource.WHITE); i++)
                    stillToBeSet.singleRemove(Resource.WHITE);
            }
        }

        //at this point the depot contains exactly the resources the player needs to set in his shelves/ leader storages

        number = moveToShelves(stillToBeSet.content(), playerOfTurn);

        if (number != 0)
            faithTrackController.moveAllTheOthers(table, number);
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
                        howToPay.pay();
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
        while(!in.trim().isEmpty()){
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
            FaithTrackController.getInstance().movePlayerOfTurn(table, 1);
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
        allResourceOwned.addEnumMap(playerOfTurn.getResourcesOwned());
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

    public Controller(){
        this.players = new ArrayList<>();
        this.faithTrackController = FaithTrackController.getInstance();
    }
}