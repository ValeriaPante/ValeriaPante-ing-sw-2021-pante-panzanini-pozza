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

public class GameController {
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

        //scelta delle risorse

        //posizionamento delle risorse nelle shelf

    }

    public void startGame() {
        this.table = new Table(players.size());
        this.faithTrackController = FaithTrackController.getInstance();

        Collections.shuffle(players);  //Players' playing order is random
            for (String nickName : players)
                table.addPlayer(new RealPlayer(nickName));

        initializePlayersLeaderCard();

        //assegnamento delle risorse ai giocatori
        if (!table.isSinglePlayer())
            initializePlayersResources();

    }


    private int moveToShelves(EnumMap<Resource, Integer> mapToBePlaced, RealPlayer player){
        Scanner inputPLayer = new Scanner(System.in);
        Depot toBePlaced = new Depot();
        String inputString;

        toBePlaced.addEnumMap(mapToBePlaced);

        inputString = inputPLayer.nextLine();
        //write STOP to stop placing
        while ( (toBePlaced.countAll() == 0) || (inputString.equals("STOP"))){
            if (notDepot(player, inputString)){

            }
            inputString = inputPLayer.nextLine();
        }

        return toBePlaced.countAll();
    }

    private boolean notDepot(RealPlayer player, String inputPLayer){
        InputManager input = InputManager.getInstance();
        SelectResourceOutput output = input.selectResourcesInStorages(inputPLayer, player);
        if (output.getStorage() == player.getStrongBox())
            return false;

        return true;
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
        //if (0 == number){       //Sceglie riga
            //do
            //    number = playerInput.nextInt();
            //while(number > 2);
            //stillToBeSet.addEnumMap(table.getMarket().pickRow(number));
        //}
        //else{           //Sceglie colonna
            //do
            //    number = playerInput.nextInt();
            //while(number > 3);
            //stillToBeSet.addEnumMap(table.getMarket().pickColumn(number));
        //}

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


    public GameController(){
        this.players = new ArrayList<>();
    }

    //End turn
    //--------------------------------------------------------------------------------------------
    public void endTurn(){
        if(table.isSinglePlayer()){
            table.nextTurn();
            if(anEntireLineIsEmpty()){
                table.setLastLap();
            } else {
                playActionToken(table.getLorenzo().getActionTokenDeck().draw());
                if(anEntireLineIsEmpty()) table.setLastLap();
            }
        }
        table.nextTurn();
        if((table.isLastLap()) && (table.getPlayers()[0] == table.turnOf()))
            endGame();
    }

    public void endGame(){
        //calcola il vincitore e mostra il messaggio
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
        ArrayList<DevDeck> lineOfDecks = getLineOfDecks(color);
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

    private boolean anEntireLineIsEmpty(){
        for(int i = 0; i < 4; i++){
            if (getLineOfDecks(i).isEmpty()) return true;
        }
        return false;
    }

    private ArrayList<DevDeck> getLineOfDecks(int color){
        ArrayList<DevDeck> lineOfDecks = new ArrayList<>();
        lineOfDecks.add(table.getDevDecks()[color]);
        lineOfDecks.add(table.getDevDecks()[color + 4]);
        lineOfDecks.add(table.getDevDecks()[color + 8]);
        return lineOfDecks;
    }
    //--------------------------------------------------------------------------------------------
}