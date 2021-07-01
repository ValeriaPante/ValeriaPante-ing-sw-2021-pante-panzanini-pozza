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
            //lancia IOException se succede un errore nell'aprire il socket
            //prob la porta non è disponibile
            return;
        }

        boolean isServerAccepting = true;

        while(isServerAccepting) {
            try {
                Socket newClient = serverSocket.accept();
                newClient.setKeepAlive(true);
                System.out.println("Connection"); //--------

                //forse a questo punto syncronised su this

                //devo passare questo socket a qualcosa che lo gestisce
                //crea tutto quello che mi serve da questo socket
                new Thread(() -> {
                    try {
                        ConnectionHandler connectionHandler = new ConnectionHandler(newClient, requestHandler);
                        connectionHandler.update();
                        String nickname = connectionHandler.waitForNickname();
                        System.out.println("Nickname received: " + nickname);
                        requestHandler.addNewSocket(nickname, connectionHandler);
                        System.out.println("AddNewSocket ended");
                        //a questo punto se che tutto è settato correttamente, posso far partire il thread su connectionHandler
                        new Thread(connectionHandler).start();
                    } catch (IOException e) {
                        //problema nella creazione del connection handler
                        try {
                            newClient.close();
                        } catch (IOException ioException) {
                            //problema nella chiusura del socket
                            //per ora non ho idea di come gestire questa cosa
                            ioException.printStackTrace();
                        }
                    }
                }).start();

            } catch (IOException e) {
                //lancia IOException se avviene un errore mentre aspetta la connessione
                //in sostanza quando il server viene chiuso e lui è bloccato nella accept()
                //NB SocketException extends IOException
                isServerAccepting = false;
            }
        }

        //non vorrei mai arrivare qui ma se succede
        while (true) {
            try {
                serverSocket.close();
                break;
            }
            catch (IOException e){
                //pass
            }
        }
    }
}
