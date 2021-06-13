package it.polimi.ingsw.Network.Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class ConnectionHandler implements Runnable{

    private int id;
    private final Receiver fromClient;
    private final Sender toClient;
    private RequestHandler requestHandler;

    public ConnectionHandler(Socket socket, RequestHandler requestHandler) throws IOException {
        this.fromClient = new Receiver(socket.getInputStream());
        this.toClient = new Sender(socket.getOutputStream());
        this.requestHandler = requestHandler;
    }

    public void update(){
        String[] assetInfo;
        while ((assetInfo = this.waitForAssetsDescription())!=null){
            this.evaluate(assetInfo);
        }
    }

    private void evaluate(String[] assetInfo){
        JsonParser jsonParser = new JsonParser();
        File mainDir;
        try {
            mainDir = new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/accessible/" + assetInfo[1]); //non mi convince
        }catch (URISyntaxException e){
            return;
        }
        System.out.println(mainDir.getAbsolutePath()); //DEBUG
        if (!mainDir.exists() || !mainDir.isDirectory()){
            System.out.println("Folder not present"); //DEBUG
            return;
        }
        if (!isAssetsDescriptionValid(assetInfo[0])){
            //mi sa che non faccio nulla
            System.out.println("Json not valid"); //DEBUG
            return;
        }

        JsonArray array = jsonParser.parse(assetInfo[0]).getAsJsonArray();
        File[] files = mainDir.listFiles();
        files = (files == null) ? new File[]{} : files;
        System.out.println(files.length); //DEBUG
        boolean isFileDifferent = true;
        for (File file : files){
            if (file.isFile()){
                System.out.println("Evaluating file: " + file.getName());
                for (int i=0; i<array.size(); i++){
                    JsonObject elem = array.get(i).getAsJsonObject();
                    if(file.getName().equals(elem.get("name").getAsString())){
                        String fileHash = this.getFileSha(assetInfo[2], file);
                        System.out.println("hash my file: " + fileHash + ", " + elem.getAsJsonPrimitive("hash").getAsString());
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
                        System.out.println("sent asset " + file.getName()); //DEBUG*/
                    }catch (IOException e){

                    }
                }
            }
            isFileDifferent = true;
        }

        this.toClient.sendMessageEndAssets();
        System.out.println("Sent end message"); //debug
    }

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

    public String[] waitForAssetsDescription(){
        String[] assetDescription;
        try {
            //bloccante
            assetDescription = this.fromClient.readAssetsDescription();
            if (assetDescription == null){
                //messaggio fine
                return null;
            }
            else if (assetDescription[0] == null){
                //messaggio non conforme
                return null;
            }
            System.out.println(assetDescription[0]); //DEBUG
            System.out.println(assetDescription[1]); //DEBUG
            System.out.println(assetDescription[2]); //DEBUG
        }catch (IOException e){
            //Non è riuscito a leggere
            return null;
        }
        return assetDescription;
    }

    public String waitForNickname(){
        String nickname = "";
        while (nickname.isEmpty()){
            try{
                nickname = this.fromClient.readMessage();
                nickname = nickname.trim();
            }catch (IOException e){
                //vediamo un attimo insieme cosa fare
            }

        }
        return nickname;
    }

    public void send(String message){
        this.toClient.send(message);
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public void run() {
        String request;
        while (true) {
            try {
                request = fromClient.readMessage();

                System.out.println(request); //
                if (request.equals("quit")) {
                    this.requestHandler.connectionClosed(this.id);
                    break;
                } else {
                    this.requestHandler.requestEvaluator(this.id, request);
                }
            }
            catch (IOException e){
                //vediamo un attimo insieme cosa fare
            }
        }
    }

    public void setRequestHandler(RequestHandler requestHandler){
        this.requestHandler = requestHandler;
    }

    public int getId(){
        return this.id;
    }
}
