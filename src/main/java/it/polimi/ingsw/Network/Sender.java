package it.polimi.ingsw.Network;

import java.io.*;

/**
 * Utility class that write on a InputStream
 */
public class Sender {

    private final OutputStream outputStream;

    /**
     * Constructor
     * @param outputStream to write to
     */
    public Sender(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    /**
     * Create a header for a file
     * @param name name of the file
     * @param size the size of the file in bytes
     * @return the header as string
     */
    private String buildHeader(String name, int size){
        return "{ " +
                "\"type\": \""+ "assetFile" +"\"" + ", " +
                "\"name\": \"" + name + "\"" + ", " +
                "\"size\": " + size +
                " }\n";
    }

    /**
     * Create a header for a message
     * @param size the size of the message in bytes
     * @return the header as string
     */
    private String buildHeader(int size){
        return "{ " +
                "\"type\": \"message\"" + ", " +
                "\"size\": " + size +
                " }\n";
    }

    /**
     * Create a header that specifies the hashing algorithm used to evaluate the files in a folder
     * @param folderPath relative path from .jar of the folder that has been evaluated
     * @param hashAlg hashing algorithm used
     * @param size the size of the message in bytes
     * @return the header as string
     */
    private String buildAssetDescriptionHeader(String folderPath, String hashAlg, int size){
        return "{" +
                "\"type\": \""+ "assetsDesc" +"\"" + ", " +
                "\"folderPath\": \"" + folderPath + "\"" + ", " +
                "\"hashAlg\": \"" + hashAlg + "\"" + ", " +
                "\"size\": " + size +
                "}\n";
    }

    /**
     * Create a particular message that specifies that all the files had been sent
     * @return the message as String
     */
    private String buildAssetFinishMessage(){
        return "{ " +
                "\"type\": \"assetsEnd\", " +
                "\"name\": \"null\", " +
                "\"format\": \"null\", " +
                "\"size\": 0" +
                " }\n";
    }

    /**
     * Once every file is sent this notifies that no more files for that folder will arrive
     * @return false if an error occurred sending the message
     */
    public boolean sendMessageEndAssets(){
        String header = this.buildAssetFinishMessage();
        try {
            this.outputStream.write(header.getBytes());
        }catch (IOException e){
            return false;
        }
        return true;
    }

    //only server
    /**
     * Writing a file to the outputStream
     * @param fileInputStream the file to send
     * @param name name of the file to send
     * @return false if an error occurred sending the message
     */
    public boolean send(FileInputStream fileInputStream, String name){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int count;
        byte[] buffer = new byte[1024];
        try {
            while ((count = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
        }catch (IOException e){
            return false;
        }
        String header = this.buildHeader(name, byteArrayOutputStream.size());
        ByteArrayOutputStream byteArrayOutConcat = new ByteArrayOutputStream();
        try {
            byteArrayOutConcat.write(header.getBytes());
            byteArrayOutputStream.writeTo(byteArrayOutConcat);
            byteArrayOutConcat.writeTo(this.outputStream);
            this.outputStream.flush();
            byteArrayOutputStream.close();
            byteArrayOutConcat.close();
        } catch (IOException e){
            return false;
        }
        return true;
    }

    //client
    /**
     * Builds and write a message with all the files hashes as a field, a relative path from the .jar file, and the hash algorithm used in the output stream
     * @param assetDesc all the files hashes
     * @param path the folder evaluated
     * @param hashAlg algorithm used
     * @return false if an error occurred sending the message
     */
    public boolean sendAssetMessage(String assetDesc, String path, String hashAlg){
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        try{
            //proviamo a sostituire la seguente con:
            byteArrayOutStream.write(this.buildAssetDescriptionHeader(path.replace("\\", "\\\\"), hashAlg, assetDesc.getBytes().length).getBytes());
            //byteArrayOutStream.write(this.buildAssetDescriptionHeader(path, hashAlg, assetDesc.getBytes().length).getBytes());       //<-----
            byteArrayOutStream.write(assetDesc.getBytes());
            byteArrayOutStream.writeTo(this.outputStream);
            byteArrayOutStream.writeTo(System.out);//DEBUG
            System.out.println("\n"); //DEBUG
            this.outputStream.flush();
            byteArrayOutStream.close();
        }catch (IOException e){
            return false;
        }
        return true;
    }

    //server and client
    /**
     * Write the String in the output stream
     * @param message message to send
     * @return false if an error occurred sending the message
     */
    public boolean send(String message){
        System.out.println("Sending: " + message);
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        try {
            byteArrayOutStream.write(this.buildHeader(message.getBytes().length).getBytes());
            byteArrayOutStream.write(message.getBytes());

            byteArrayOutStream.writeTo(this.outputStream);
            this.outputStream.flush();
            System.out.println("Sent: " + message);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        try {
            byteArrayOutStream.close();
        }catch (IOException e){
            return true;
        }
        return true;
    }

    /**
     * Closes the output stream
     */
    public void close(){
        while (true){
            try{
                this.outputStream.close();
                break;
            }catch(IOException e){
                //pass
            }
        }
    }



}
