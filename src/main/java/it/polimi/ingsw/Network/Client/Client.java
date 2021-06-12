package it.polimi.ingsw.Network.Client;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.AssetDescriptor;
import it.polimi.ingsw.Network.Client.Messages.*;
import it.polimi.ingsw.Network.Receiver;
import it.polimi.ingsw.Network.Sender;
import it.polimi.ingsw.View.View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Client implements Runnable{

    private Receiver fromServer;
    private Sender toServer;
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
                            result.add(new ChangedLobbyMessage(toEvaluate.get("id").getAsInt(), gson.fromJson(toEvaluate.get("players"), String[].class), toEvaluate.get("itsYou").getAsBoolean()));
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
            String input;
            while (true) {
                try {
                    input = fromServer.readMessage();
                    if(convertInput(input))
                        break;
                }catch (IOException e){
                    //bho
                }
            }

            fromServer.close();
            toServer.close();
        }
    }

    public void update(String inputLine) {
        toServer.send(inputLine);
    }

    public void connect(String ip, String port, String username) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));
            fromServer = new Receiver(socket.getInputStream());
            toServer = new Sender(socket.getOutputStream());
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Server unreachable");
            System.exit(0);
            return;
        }
        this.updateAssets();
        this.update(username);
        System.out.println("Sent username");
        System.out.println("Connection established");
        this.run();
    }

    //Daniel-part-----------------------------
    private void updateAssets(){
        String[] hashingAlg = {"SHA-256"};
        MessageDigest messageDigest = null;
        for (String alg : hashingAlg){
            try{
                messageDigest = MessageDigest.getInstance(alg);
            }catch (NoSuchAlgorithmException e){
                //pass
            }
        }

        String[] paths = {"\\assets\\imgs\\", "\\JSONs\\"};
        AssetDescriptor assetDesc;
        for (String relativePath : paths){
            String fullPath ;
            try {
                fullPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath() + relativePath;
                System.out.println(fullPath);
            } catch (URISyntaxException e) {
                System.out.println("DEBUG: Come on riguarda il relativePath");
                return;
            }
            if (this.toServer.sendAssetMessage(this.getAssetsMessage(fullPath, (messageDigest==null) ? "size" : messageDigest.getAlgorithm()), relativePath, (messageDigest == null) ? "size" : messageDigest.getAlgorithm())){
                try{
                    while((assetDesc = this.fromServer.getAssetDescriptor())!=null){
                        this.saveAsset(assetDesc, fullPath);
                    }
                }catch (IOException e){
                    //raga scusate ma non avevo sbatti di gestire questa cosa
                    //si poteva con inputStream.skip(n)
                    System.exit(1);
                }
            }
        }
        this.toServer.sendMessageEndAssets();
    }

    private void saveAsset(AssetDescriptor assetDescriptor, String path){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + assetDescriptor.getName(), false);
        }catch (FileNotFoundException e){
            System.exit(500);
        }
        int count;
        byte[] buffer = new byte[1024];
        try {
            while ((count = assetDescriptor.getAssetByteArrayI().read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
        }catch (IOException e){
            //pass
        }
        try {
            fileOutputStream.close();
        }catch (IOException e){
            //
        }
    }

    private String getFileSha(String shaAlg, File file){
        MessageDigest digest = null;
        int totalBytes = 0;
        if (!shaAlg.equals("size")){
            try {
                digest = MessageDigest.getInstance(shaAlg);
            }
            catch (NoSuchAlgorithmException e){
                return "null";
            }
        }

        try {
            FileInputStream fileInputS = new FileInputStream(file);
            //chunk of data = 1Kb
            byte[] byteArr = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = fileInputS.read(byteArr)) != -1) {
                if (shaAlg.equals("size")){
                    totalBytes += bytesRead;
                }
                else {
                    digest.update(byteArr, 0, bytesRead);
                }
            }
            fileInputS.close();
        }catch (IOException e){
            return "null";
        }

        if (shaAlg.equals("size")){
            return "" + totalBytes;
        }

        byte[] hashBytes = digest.digest();
        StringBuilder strBuilder = new StringBuilder();
        for (byte singleByte : hashBytes) {
            //se qualcuno sa cosa fa questa riga sarebbe così gentile da spiegarmelo?
            strBuilder.append(Integer.toString((singleByte & 0xff) + 0x100, 16).substring(1));
        }
        return strBuilder.toString();
    }

    //size as identifier
    private String getAssetsMessage(String fullPath, String hashAlg){
        File targetDir = new File(fullPath);
        if (!targetDir.exists()){
             if(!targetDir.mkdirs()){
                 System.out.println("Debug: Grosso problema");
                 System.exit(1);
             }
        }

        File[] content = targetDir.listFiles();
        assert content != null; //l'ho creata prima
        if (content.length == 0){
            return "[]";
        }
        StringBuilder message = new StringBuilder("[");
        for (int i = 0; i< Objects.requireNonNull(content).length-1; i++){
            if (content[i].isFile()) {
                message.append("{\"name\": \"").append(content[i].getName()).append("\", ").append("\"hash\": \"").append(this.getFileSha(hashAlg, content[i])).append("\"").append("},");
            }
        }
        File lastFile = content[content.length-1];
        if (lastFile.isFile()){
            message.append("{\"name\": \"").append(lastFile.getName()).append("\", ").append("\"hash\": \"").append(this.getFileSha(hashAlg, lastFile)).append("\"").append("}");
            message.append("]");
            return message.toString();
        }
        else{
            return message.substring(0,message.toString().length()-1) + "]";
        }
    }
    //End-Daniel-part-------------------------
}
