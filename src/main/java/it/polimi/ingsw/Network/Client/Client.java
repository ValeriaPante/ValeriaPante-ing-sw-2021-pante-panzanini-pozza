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
import java.util.HashMap;
import java.util.Scanner;

public class Client implements Runnable{

    private Scanner fromServer;
    private PrintWriter toServer;
    private final Visitor visitor;

    public Client(View view){
        this.visitor = new Visitor(view);
    }

    //ritorna true se la partita è finita
    private boolean convertInput(String input){
        boolean result = false;

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(input);
        if(!elements.isJsonArray()){
            throw new IllegalArgumentException("Check the config file and his syntax");
        } else {
            JsonArray array = elements.getAsJsonArray();
            for(int i = 0; i < array.size(); i++){
                JsonObject toEvaluate = array.get(i).getAsJsonObject();

                FromServerMessage message = null;
                try{
                    switch(toEvaluate.get("type").getAsString()){
                        case "newLobby":
                            message = new NewLobbyMessage(toEvaluate.get("id").getAsInt(), toEvaluate.get("firstPlayer").getAsString());
                            break;
                        case "changedLobby":
                            message = new ChangedLobbyMessage(toEvaluate.get("id").getAsInt(), gson.fromJson(toEvaluate.get("players"), String[].class));
                            break;
                        case "removeLobby":
                            message = new RemoveLobbyMessage(toEvaluate.get("id").getAsInt());
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

                            message = new InitMessage(toEvaluate.get("clientId").getAsInt(),gson.fromJson(toEvaluate.get("market"), Resource[].class), gson.fromJson(toEvaluate.get("slide"), Resource.class),gson.fromJson(toEvaluate.get("devDecks"), int[].class), playersId, playersUsernames, clientLeaderCards);
                            break;
                        case "start":
                            message = new StartMessage();
                            break;
                        case "actionOnLeaderCard":
                            message = new ActionOnLeaderCardMessage(toEvaluate.get("playedId").getAsInt(), toEvaluate.get("discard").getAsBoolean(), toEvaluate.get("id").getAsInt());
                            break;
                        case "newDevCard":
                            message = new NewDevCardMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("newPlayerCardId").getAsInt(), toEvaluate.get("numberOfSlot").getAsInt());
                            break;
                        case "newTopCard":
                            message = new NewTopCardMessage(toEvaluate.get("id").getAsInt(), toEvaluate.get("numberOfDeck").getAsInt());
                            break;
                        case "changedShelf":
                            message = new ChangedShelfMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("numberOfShelf").getAsInt(), gson.fromJson(toEvaluate.get("resourceType"), Resource.class),toEvaluate.get("quantity").getAsInt());
                            break;
                        case "changedStrongbox":
                            message = new ChangedStrongboxMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("inside"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                            break;
                        case "changedSupportContainer":
                            message = new ChangedSupportContainerMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("inside"), new TypeToken<HashMap<Resource, Integer>>(){}.getType()));
                            break;
                        case "changedLeaderStorage":
                            message = new ChangedLeaderStorageMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("cardId").getAsInt(), gson.fromJson(toEvaluate.get("owned"), Resource[].class));
                            break;
                        case "newMarketState":
                            message = new NewMarketStateMessage(gson.fromJson(toEvaluate.get("grid"), Resource[].class), gson.fromJson(toEvaluate.get("slide"), Resource.class));
                            break;
                        case "popeFavourCardState":
                            message = new PopeFavourCardStateMessage(toEvaluate.get("playerId").getAsInt(), gson.fromJson(toEvaluate.get("cards"), PopeFavorCardState[].class));
                            break;
                        case "newPlayerPosition":
                            message = new NewPlayerPositionMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("pos").getAsInt());
                            break;
                        case "winner":
                            message = new WinnerMessage(toEvaluate.get("id").getAsInt());
                            result = true;
                            break;
                        case "error":
                            message = new ErrorMessage(toEvaluate.get("playerId").getAsInt(), toEvaluate.get("error").getAsString());
                            break;
                        default:
                            break;
                    }

                    if(message != null) message.visit(visitor);
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
