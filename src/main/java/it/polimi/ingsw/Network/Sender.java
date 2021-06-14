package it.polimi.ingsw.Network;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Sender {

    OutputStream outputStream;

    public Sender(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    private String buildHeader(String name, int size){
        return "{ " +
                "\"type\": \""+ "assetFile" +"\"" + ", " +
                "\"name\": \"" + name + "\"" + ", " +
                "\"size\": " + size +
                " }\n";
    }

    private String buildHeader(int size){
        return "{ " +
                "\"type\": \"message\"" + ", " +
                "\"size\": " + size +
                " }\n";
    }

    private String buildAssetDescriptionHeader(String folderPath, String hashAlg, int size){
        return "{" +
                "\"type\": \""+ "assetsDesc" +"\"" + ", " +
                "\"folderPath\": \"" + folderPath + "\"" + ", " +
                "\"hashAlg\": \"" + hashAlg + "\"" + ", " +
                "\"size\": " + size +
                "}\n";
    }

    private String buildAssetFinishMessage(){
        return "{ " +
                "\"type\": \"assetsEnd\", " +
                "\"name\": \"null\", " +
                "\"format\": \"null\", " +
                "\"size\": 0" +
                " }\n";
    }

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
    public boolean sendAssetMessage(String assetDesc, String path, String shaAlg){
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        try{
            byteArrayOutStream.write(this.buildAssetDescriptionHeader(path.replace("\\", "\\\\"), shaAlg, assetDesc.getBytes().length).getBytes());
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
    public boolean send(String message){
        System.out.println("Sending: " + message);
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        try{
            byteArrayOutStream.write(this.buildHeader(message.getBytes().length).getBytes());
            byteArrayOutStream.write(message.getBytes());

            byteArrayOutStream.writeTo(this.outputStream);
            this.outputStream.flush();
            byteArrayOutStream.close();
            System.out.println("Sent: " + message);
        }catch (IOException e){
            return false;
        }
        return true;
    }

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
