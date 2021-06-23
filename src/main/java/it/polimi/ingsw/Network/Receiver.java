package it.polimi.ingsw.Network;

import com.google.gson.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Receiver {

    InputStream inputStream;

    public Receiver(InputStream inputStream){
        this.inputStream = inputStream;
    }

    private JsonObject toJsonObjectIfPossible(String toEvaluate){
        JsonParser parser = new JsonParser();
        JsonElement element;
        try{
            element = parser.parse(toEvaluate);
        } catch (JsonParseException e){
            System.out.println("failing parsing");
            return null;
        }
        if (!element.isJsonObject()){
            System.out.println("not object");
            return null;
        }
        return element.getAsJsonObject();
    }

    private boolean isAssetHeaderJsonValid(String arrived){
        JsonObject header = toJsonObjectIfPossible(arrived);
        if (header==null){
            System.out.println("wrong header");
            return false;
        }
        JsonElement type = header.get("type");
        JsonElement name = header.get("name");
        JsonElement size = header.get("size");
        if (type == null || name == null || size == null){
            return false;
        }
        return (type.isJsonPrimitive() && type.getAsJsonPrimitive().isString()) &&
                (name.isJsonPrimitive() && name.getAsJsonPrimitive().isString()) &&
                (size.isJsonPrimitive() && size.getAsJsonPrimitive().isNumber());
    }

    private boolean isAssetHeaderFinish(String arrived){
        JsonObject header = toJsonObjectIfPossible(arrived);
        return (header.get("type").getAsString().equals("assetsEnd")) &&
                (header.get("name").getAsString().equals("null")) &&
                (header.get("format").getAsString().equals("null")) &&
                (header.get("size").getAsInt() == 0);
    }


    private boolean isAssetDescriptorsHeaderJsonValid(String fromServer){
        JsonObject header = toJsonObjectIfPossible(fromServer);
        if (header==null){
            return false;
        }
        JsonElement type = header.get("type");
        JsonElement path = header.get("folderPath");
        JsonElement hashAlg = header.get("hashAlg");
        JsonElement size = header.get("size");

        if (type == null || path == null || hashAlg == null){
            return false;
        }
        return (type.isJsonPrimitive() && type.getAsJsonPrimitive().isString() && type.getAsJsonPrimitive().getAsString().equals("assetsDesc")) &&
                (path.isJsonPrimitive() && path.getAsJsonPrimitive().isString()) &&
                (hashAlg.isJsonPrimitive() && hashAlg.getAsJsonPrimitive().isString()) &&
                (size.isJsonPrimitive() && size.getAsJsonPrimitive().isNumber());
    }

    private boolean isMessageHeaderValid(String fromServer){
        JsonObject header = this.toJsonObjectIfPossible(fromServer);
        if (header==null){
            return false;
        }
        JsonElement type = header.get("type");
        JsonElement size = header.get("size");
        if (type==null || size==null){
            return false;
        }
        return (type.isJsonPrimitive() && type.getAsJsonPrimitive().isString() && type.getAsJsonPrimitive().getAsString().equals("message")) &&
                (size.isJsonPrimitive() && size.getAsJsonPrimitive().isNumber());
    }

    private String getHeader() throws IOException {
        ArrayList<Byte> header = new ArrayList<>();
        Integer byteArrived = this.inputStream.read();

        while (byteArrived!=10){        //(int)'\n' = 10
            header.add(byteArrived.byteValue());
            byteArrived = this.inputStream.read();
        }

        byte[] headerByteArray = new byte[header.size()];

        for (int i=0; i<header.size(); i++){
            headerByteArray[i] = header.get(i);
        }
        return new String(headerByteArray, 0, headerByteArray.length);
    }

    private byte[] getByteArrayMessage(int nbrToRead) throws IOException {
        byte[] byteArray = new byte[nbrToRead];
        int nbrRd = 0;
        int nbrLeftToRead = nbrToRead;
        while (nbrLeftToRead>0){
            int rd = this.inputStream.read(byteArray, nbrRd, nbrLeftToRead);
            if (rd<0){
                break;
            }
            nbrRd += rd;
            nbrLeftToRead -= rd;
        }
        return byteArray;
    }

    private String readMessage(int nbrToRead) throws IOException {
        byte[] byteArray = getByteArrayMessage(nbrToRead);
        System.out.println("Arrived: " + new String(byteArray, 0, byteArray.length));
        return new String(byteArray,0,byteArray.length);
    }

    //sever side
    public String[] readAssetsDescription() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String fromClient = this.getHeader();
        System.out.println(fromClient); //DEBUG
        if(this.isAssetHeaderFinish(fromClient)){
            System.out.println("Ricevuto messaggio fine");
            return null;
        }
        else if (!this.isAssetDescriptorsHeaderJsonValid(fromClient)){
            System.out.println("wrong header");
            return null;
        }
        String folderPath;
        String hashAlg;

        JsonObject header = jsonParser.parse(fromClient).getAsJsonObject();
        folderPath = header.get("folderPath").getAsString();
        hashAlg = header.get("hashAlg").getAsString();
        String msg = this.readMessage(header.get("size").getAsInt());

        return new String[]{msg, folderPath, hashAlg};
    }

    public String readMessage() throws IOException{
        JsonParser jsonParser = new JsonParser();
        String fromServer = this.getHeader();
        if (!this.isMessageHeaderValid(fromServer)){
            return null;
        }
        JsonObject header = jsonParser.parse(fromServer).getAsJsonObject();
        return this.readMessage(header.get("size").getAsInt());
    }

    public AssetDescriptor getAssetDescriptor() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String arrived = this.getHeader();
        if (!this.isAssetHeaderJsonValid(arrived)){
            //qualcosa è stato mandato sbagliato dal server
            System.out.println("sintassi sbagliata");
            System.out.println("arrivato: " + arrived);
            return null;
        }
        else if(this.isAssetHeaderFinish(arrived)){
            System.out.println("Ricevuto messaggio fine");
            return null;
        }
        JsonObject header = jsonParser.parse(arrived).getAsJsonObject();
        return new AssetDescriptor(new ByteArrayInputStream(this.getByteArrayMessage(header.get("size").getAsInt())), header.get("name").getAsString());
    }

    public void close(){
        while (true){
            try{
                this.inputStream.close();
                break;
            }catch (IOException e){

            }
        }
    }
}
