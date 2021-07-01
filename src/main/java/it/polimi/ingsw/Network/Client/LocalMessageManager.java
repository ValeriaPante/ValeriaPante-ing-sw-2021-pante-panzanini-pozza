package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.Server.LocalMessageSender;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;
import it.polimi.ingsw.View.View;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Manages the messages from/to the player in an offline game
 */
public class LocalMessageManager implements MessageManager{

    private final InGameControllerSwitch inGameControllerSwitch;

    private void copyFile(File fileToCopy, String outputDirPath){
        if (fileToCopy.isDirectory()){
            File dir = new File(outputDirPath + File.separator + fileToCopy.getName());
            if (!dir.exists()){
                dir.mkdirs();
            }
            for (File file : fileToCopy.listFiles()){
                this.copyFile(file, dir.getAbsolutePath());
            }
        }
        else if (fileToCopy.isFile()){
            try {
                FileInputStream fileInputStream = new FileInputStream(fileToCopy);
                FileOutputStream fileOutputStream = new FileOutputStream(outputDirPath + File.separator + fileToCopy.getName(), false);

                try {
                    int count;
                    byte[] buffer = new byte[1024];
                    while ((count = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                }catch (IOException ignored){}

                fileInputStream.close();
                fileOutputStream.close();
            }catch (IOException ignored){}
        }
    }

    private void settingUpAssets(){
        try {
            File jarFolder = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File outputDir = new File(jarFolder.getAbsolutePath() + File.separator + "accessible");
            if (!outputDir.exists()){
                outputDir.mkdirs();
            }
            for (File file : jarFolder.listFiles()){
                if (file.isDirectory() && !file.getAbsolutePath().equals(outputDir.getAbsolutePath())){
                    this.copyFile(file, outputDir.getAbsolutePath());
                }
            }
        }catch (URISyntaxException ignored){}
    }

    /**
     * Constructor
     * @param view view modality chosen by the player
     */
    public LocalMessageManager(View view) {
        this.settingUpAssets();
        Lobby lobby = new Lobby(0);
        User user = new User("you", new LocalMessageSender(view));
        user.setId(1);
        lobby.addUser(user);
        this.inGameControllerSwitch = new InGameControllerSwitch(lobby);
    }

    /**
     * Sends a message to the server
     * @param message message to send
     */
    @Override
    public synchronized void update(InGameMessage message) {
        message.setSenderId(1);
        message.readThrough(inGameControllerSwitch);
    }

    /**
     * Sends a message to the server
     * @param message message to send
     */
    @Override
    public synchronized void update(PreGameMessage message) {
    }

    /**
     * Starts the connection with the server if possible
     * @param ip IP address of the server
     * @param port port used by the server
     * @param username username chosen by the player
     */
    @Override
    public void connect(String ip, String port, String username) {

    }
}
