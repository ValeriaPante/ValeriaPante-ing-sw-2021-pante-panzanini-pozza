package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.RequestHandlers.PreGameRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server{

    private final int port;
    PreGameRequestHandler requestHandler;

    public Server(int port, PreGameRequestHandler requestHandler){
        this.port = port;
        this.requestHandler = requestHandler;
    }

    public void start(){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(this.port);
        }
        catch (IOException e){
            return;
        }

        boolean isServerAccepting = true;

        while(isServerAccepting) {
            try {
                Socket newClient = serverSocket.accept();
                newClient.setKeepAlive(true);

                new Thread(() -> {
                    try {
                        ConnectionHandler connectionHandler = new ConnectionHandler(newClient, requestHandler);
                        connectionHandler.update();
                        String nickname = connectionHandler.waitForNickname();
                        requestHandler.addNewSocket(nickname, connectionHandler);
                        new Thread(connectionHandler).start();
                    } catch (IOException e) {
                        try {
                            newClient.close();
                        } catch (IOException ignored) {
                        }
                    }
                }).start();

            } catch (IOException e) {
                isServerAccepting = false;
            }
        }

        try {
            serverSocket.close();
        }
        catch (IOException ignored){
        }

    }
}
