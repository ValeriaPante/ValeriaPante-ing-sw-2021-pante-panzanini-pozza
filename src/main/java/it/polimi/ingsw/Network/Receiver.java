package it.polimi.ingsw.Network;

import com.google.gson.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Utility class that read on a InputStream
 */
public class Receiver {

    private final InputStream inputStream;

    /**
     * Constructor
     * @param inputStream to read on
     */
    public Receiver(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * Convert a string in a json object if possible
     * @param toEvaluate string to evaluate
     * @return the JsonObject associated to this string or null if the format was not correct
     */
    private JsonObject toJsonObjectIfPossible(String toEvaluate){
        JsonParser parser = new JsonParser();
        JsonElement element;
        try{
            element = parser.parse(toEvaluate);
        } catch (JsonParseException e){
            return null;
        }
        if (!element.isJsonObject()){
            return null;
        }
        return element.getAsJsonObject();
    }

    /**
     * Checking is a string is in the correct format
     * @param arrived string to evaluate
     * @return true if the string is in the correct format, false otherwise
     */
    private boolean isAssetHeaderJsonValid(String arrived){
        JsonObject header = toJsonObjectIfPossible(arrived);
        if (header==null){
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

    /**
     * Checking is a string is in the correct format
     * @param arrived string to evaluate
     * @return true if the string is in the correct format, false otherwise
     */
    private boolean isAssetHeaderFinish(String arrived){
        JsonObject header = toJsonObjectIfPossible(arrived);
        if (header==null){
            return false;
        }
        return (header.get("type").getAsString().equals("assetsEnd")) &&
                (header.get("name").getAsString().equals("null")) &&
                (header.get("format").getAsString().equals("null")) &&
                (header.get("size").getAsInt() == 0);
    }

    /**
     * Checking is a string is in the correct format
     * @param fromServer string to evaluate
     * @return true if the string is in the correct format, false otherwise
     */
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

    /**
     * Checking is a string is in the correct format
     * @param fromServer string to evaluate
     * @return true if the string is in the correct format, false otherwise
     */
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

    /**
     * Reads on the inputs stream waiting for the header of the next message
     * @return the header read
     * @throws IOException if an error occurs while reading or if the input stream has been closed
     */
    private String getHeader() throws IOException {
        ArrayList<Byte> header = new ArrayList<>();
        Integer byteArrived = this.inputStream.read();

        while (byteArrived!=10){        //(int)'\n' = 10
            if (byteArrived == -1){
                throw new IOException();
            }
            header.add(byteArrived.byteValue());
            byteArrived = this.inputStream.read();
        }

        byte[] headerByteArray = new byte[header.size()];

        for (int i=0; i<header.size(); i++){
            headerByteArray[i] = header.get(i);
        }
        return new String(headerByteArray, 0, headerByteArray.length);
    }

    /**
     * Reads on the input stream a specific amount of bytes an returns it as a byte array
     * @param nbrToRead amount of bytes to read
     * @return bytes read
     * @throws IOException if an error occurs while reading
     */
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
        byte[] byteArray = this.getByteArrayMessage(nbrToRead);
        return new String(byteArray,0,byteArray.length);
    }

    /**
     * Convert the next message on the stream as String[3]
     * @return the files infos: (message received, folder evaluated client side, hash algorithm for hashing files client side)
     * @throws IOException if there are problems reading on the stream or the header is not correct
     */
    public String[] readAssetsDescription() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String fromClient = this.getHeader();
        if(this.isAssetHeaderFinish(fromClient)){
            return null;
        }
        else if (!this.isAssetDescriptorsHeaderJsonValid(fromClient)){
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

    /**
     * Convert the next message on the stream to a string
     * @return the next message arrived on the stream as String
     * @throws IOException if there are problems reading on the stream or the header is not correct
     */
    public String readMessage() throws IOException{
        JsonParser jsonParser = new JsonParser();
        String fromServer = this.getHeader();
        if (!this.isMessageHeaderValid(fromServer)){
            return null;
        }
        JsonObject header = jsonParser.parse(fromServer).getAsJsonObject();
        return this.readMessage(header.get("size").getAsInt());
    }

    /**
     * Convert the next message on the stream as AssetDescriptor
     * @return the file information that i need inside a AssetDescriptor
     * @throws IOException if there are problems reading on the stream or the header is not correct
     */
    public AssetDescriptor getAssetDescriptor() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String arrived = this.getHeader();
        if (!this.isAssetHeaderJsonValid(arrived)){
            return null;
        }
        else if(this.isAssetHeaderFinish(arrived)){
            return null;
        }
        JsonObject header = jsonParser.parse(arrived).getAsJsonObject();
        return new AssetDescriptor(new ByteArrayInputStream(this.getByteArrayMessage(header.get("size").getAsInt())), header.get("name").getAsString());
    }

    /**
     * Closes the input stream
     */
    public void close(){
        try{
            this.inputStream.close();
        }catch (IOException ignored){
        }

    }
}
