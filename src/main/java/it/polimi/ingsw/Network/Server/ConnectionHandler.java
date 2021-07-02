package it.polimi.ingsw.Network.Server;

import com.google.gson.*;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.Receiver;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Handler of the incoming connections server side
 */
public class ConnectionHandler implements Runnable, MessageSenderInterface{

    private final Gson messageToJson;
    private int id;
    private final Receiver fromClient;
    private final Sender toClient;
    private RequestHandler requestHandler;

    /**
     * Constructor
     * @param socket socket
     * @param requestHandler a request handler that will take care of all the messages
     * @throws IOException if an error occurs
     */
    public ConnectionHandler(Socket socket, RequestHandler requestHandler) throws IOException {
        this.messageToJson = new Gson();
        this.fromClient = new Receiver(socket.getInputStream());
        this.toClient = new Sender(socket.getOutputStream());
        this.requestHandler = requestHandler;
    }

    /**
     * Runs the logic to update (send) all the missing assets (images, etc..)
     */
    public void update(){
        String[] assetInfo;
        while ((assetInfo = this.waitForAssetsDescription())!=null){
            this.evaluate(assetInfo);
        }
    }

    /**
     * Evaluates an array of assets with their hashing
     * If a file is different, it will be sent to the other side
     */
    private void evaluate(String[] assetInfo){
        JsonParser jsonParser = new JsonParser();
        File mainDir;
        try {
            if (assetInfo[1].charAt(0) != File.separator.charAt(0)){
                assetInfo[1] = assetInfo[1].replace(assetInfo[1].charAt(0), File.separator.charAt(0));
            }
            mainDir = new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath() + File.separator + "accessible" + File.separator + assetInfo[1]); //non mi convince
        }catch (URISyntaxException e){
            return;
        }
        if (!mainDir.exists() || !mainDir.isDirectory()){
            return;
        }
        if (!isAssetsDescriptionValid(assetInfo[0])){
            return;
        }

        JsonArray array = jsonParser.parse(assetInfo[0]).getAsJsonArray();
        File[] files = mainDir.listFiles();
        files = (files == null) ? new File[]{} : files;
        boolean isFileDifferent = true;
        for (File file : files){
            if (file.isFile()){
                for (int i=0; i<array.size(); i++){
                    JsonObject elem = array.get(i).getAsJsonObject();
                    if(file.getName().equals(elem.get("name").getAsString())){
                        String fileHash = this.getFileSha(assetInfo[2], file);
                        if (fileHash.equals(elem.get("hash").getAsString())){
                            isFileDifferent = false;
                        }
                        break;
                    }
                }
                if (isFileDifferent){
                    try {
                        FileInputStream fileToSend = new FileInputStream(file.getAbsolutePath());
                        this.toClient.send(fileToSend, file.getName());
                        fileToSend.close();
                    }catch (IOException ignored){
                    }
                }
            }
            isFileDifferent = true;
        }

        this.toClient.sendMessageEndAssets();
    }

    /**
     * Checking is a string is in the correct format
     * @param fromServer string to evaluate
     * @return true if the string is in the correct format, false otherwise
     */
    private boolean isAssetsDescriptionValid(String fromServer){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(fromServer);
        if (!element.isJsonArray()){
            return false;
        }
        JsonArray array = element.getAsJsonArray();
        for (int i=0; i< array.size(); i++){
            if (array.get(i).isJsonObject()){
                JsonObject elem = array.get(i).getAsJsonObject();
                JsonElement name = elem.get("name");
                JsonElement hash = elem.get("hash");

                if (name==null || hash == null){
                    return false;
                }
                if (!(name.isJsonPrimitive() && name.getAsJsonPrimitive().isString() && hash.isJsonPrimitive() && hash.getAsJsonPrimitive().isString())){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Calculate the hash of a file
     * @param shaAlg hash algorithm to use
     * @param file tile to evaluate
     * @return the file hash
     */
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
            strBuilder.append(Integer.toString((singleByte & 0xff) + 0x100, 16).substring(1));
        }
        return strBuilder.toString();
    }

    /**
     * Convert the next message on the stream as String[3]
     * @return the files infos or null if received an end message (client doesn't need more updates)
     */
    public String[] waitForAssetsDescription(){
        String[] assetDescription = null;
        try {
            //bloccante
            assetDescription = this.fromClient.readAssetsDescription();
            if (assetDescription == null){
                //endMessage
                return null;
            }
            else if (assetDescription[0] == null){
                //message not correct
                return null;
            }
        }catch (IOException e){
            this.requestHandler.connectionClosed(this.id);
        }
        return assetDescription;
    }

    /**
     * Waits a string on the network
     * @return the string read
     */
    public String waitForNickname(){
        String nickname = "";
        while (nickname.isEmpty()){
            try{
                nickname = this.fromClient.readMessage();
                nickname = nickname.trim();
            }catch (IOException e){
                this.requestHandler.connectionClosed(this.id);
            }

        }
        return nickname;
    }

    /**
     * Sending a message to client
     * @param message message to send
     */
    public void send(FromServerMessage message){
        int i=0;
        while (!this.toClient.send(this.messageToJson.toJson(message))){
            i++;
            if (i==50){
                break;
            }
        }
    }

    /**
     * Sets the if of this connection (must be unique)
     * @param id the id this connection will be identified with
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Starts the loop to keep listening
     */
    @Override
    public void run() {
        new Thread(() -> {
            while (true) {
                if (!this.toClient.ping()){
                    break;
                }
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        String request;
        while (true) {
            try {
                request = fromClient.readMessage();

                this.requestHandler.requestEvaluator(this.id, request);
            }
            catch (IOException e){
                this.requestHandler.connectionClosed(this.id);
                break;
            }
        }
    }

    /**
     * Setter
     * @param requestHandler the implementation of a RequestHandler that will handle the incoming requests
     */
    public void setRequestHandler(RequestHandler requestHandler){
        this.requestHandler = requestHandler;
    }

    /**
     * Getter
     * @return this connection id
     */
    public int getId(){
        return this.id;
    }

    /**
     * Close the connection
     */
    public void closeConnection(){
        this.fromClient.close();
        this.toClient.close();
    }
}
