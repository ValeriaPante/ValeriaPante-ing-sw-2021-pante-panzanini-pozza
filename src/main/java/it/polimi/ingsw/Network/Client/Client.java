package it.polimi.ingsw.Network.Client;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Messages.*;
import it.polimi.ingsw.View.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client implements Runnable{

    private Scanner fromServer;
    private PrintWriter toServer;
    private final Visitor visitor;
    private final Gson gson;
    private final JsonParser parser;

    public Client(View view){
        this.visitor = new Visitor(view);
        this.gson = new Gson();
        this. parser = new JsonParser();
    }

    //ritorna true se la partita è finita
    private boolean convertInput(String input){
        boolean result = false;

        ArrayList<FromServerMessage> messages = interpret(input);
        for (FromServerMessage message : messages) result = result || message.visit(visitor);

        return result;
    }

    private ArrayList<FromServerMessage> interpret(String input){
        ArrayList<FromServerMessage> result = new ArrayList<>();

        JsonElement elements = parser.parse(input);
        if(!elements.isJsonArray()){
            throw new IllegalArgumentException("Check the config file and his syntax");
        } else {
            JsonArray array = elements.getAsJsonArray();
            for(int i = 0; i < array.size(); i++){
                JsonObject toEvaluate = array.get(i).getAsJsonObject();

                try{
                    switch(toEvaluate.get("type").getAsString()){
                        case "changedLobby":
                            result.add(new ChangedLobbyMessage(toEvaluate.get("id").getAsInt(), gson.fromJson(toEvaluate.get("players"), String[].class)));
                            break;
                        case "init":
                            JsonArray playersInfo = toEvaluate.get("players").getAsJsonArray();
                            int[] playersId = new int[playersInfo.size()];
                            String[] playersUsernames = new String[playersInfo.size()];

                            for(int j = 0; j < playersInfo.size(); j++){
                                JsonObject player = array.get(j).getAsJsonObject();
                                playersId[j] = player.get("userId").getAsInt();
                                playersUsernames[j] = player.get("username").getAsString();
                            }

                            int[] clientLeaderCards = gson.fromJson(toEvaluate.get("initialLeaderCards"), int[].class);

                            result.add(new InitMessage(toEvaluate.get("clientId").getAsInt(),gson.fromJson(toEvaluate.get("market"), Resource[].class), gson.fromJson(toEvaluate.get("slide"), Resource.class),gson.fromJson(toEvaluate.get("devDecks"), int[].class), playersId, playersUsernames, clientLeaderCards));
                            break;
                        case "start":
                            result.add(new StartMessage());
                            break;
                        case "actionOnLeaderCard":
                            result.add(new ActionOnLeaderCardMessage(toEvaluate.get("playedId").getAsInt(), toEvaluate.get("discard").getAsBoolean(), toEvaluate.get("id").getAsInt()));
                            break;
                        case "newDevCard":
                            result.add(new NewDevCardMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("newPlayerCardId").getAsInt(), toEvaluate.get("numberOfSlot").getAsInt()));
                            break;
                        case "newTopCard":
                            result.add(new NewTopCardMessage(toEvaluate.get("id").getAsInt(), toEvaluate.get("numberOfDeck").getAsInt()));
                            break;
                        case "changedShelf":
                            result.add(new ChangedShelfMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("numberOfShelf").getAsInt(), gson.fromJson(toEvaluate.get("resourceType"), Resource.class),toEvaluate.get("quantity").getAsInt()));
                            break;
                        case "changedStrongbox":
                            result.add(new ChangedStrongboxMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("inside"), new TypeToken<HashMap<Resource, Integer>>(){}.getType())));
                            break;
                        case "changedSupportContainer":
                            result.add(new ChangedSupportContainerMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("inside"), new TypeToken<HashMap<Resource, Integer>>(){}.getType())));
                            break;
                        case "changedLeaderStorage":
                            result.add(new ChangedLeaderStorageMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("cardId").getAsInt(), gson.fromJson(toEvaluate.get("owned"), Resource[].class))) ;
                            break;
                        case "newMarketState":
                            result.add(new NewMarketStateMessage(gson.fromJson(toEvaluate.get("grid"), Resource[].class), gson.fromJson(toEvaluate.get("slide"), Resource.class)));
                            break;
                        case "popeFavourCardState":
                            result.add(new PopeFavourCardStateMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("cards"), PopeFavorCardState[].class)));
                            break;
                        case "newPlayerPosition":
                            result.add(new NewPlayerPositionMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("pos").getAsInt()));
                            break;
                        case "winner":
                            result.add(new WinnerMessage(toEvaluate.get("id").getAsInt()));
                            break;
                        case "error":
                            result.add(new ErrorMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("error").getAsString()));
                            break;
                        default:
                            break;
                    }
                } catch (NullPointerException e){
                    //there is some format error inside the message
                }
            }
        }
        return result;
    }

    public void run(){
        if(fromServer != null && toServer != null) {
            while (true) {
                String input = fromServer.nextLine();
                //break se è finita la partita
                if(convertInput(input))
                    break;
            }

            fromServer.close();
            toServer.close();
        }
    }

    public void update(String inputLine) {
        toServer.println(inputLine);
        toServer.flush();
    }

    public void connect(String ip, String port, String username) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));
            fromServer = new Scanner(socket.getInputStream());
            toServer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Server unreachable");
            System.exit(0);
            return;
        }
        this.update(username);
        System.out.println("Connection established");
    }
}
