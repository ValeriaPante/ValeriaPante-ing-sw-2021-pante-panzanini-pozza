package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.RequestHandlers.PreGameRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

    private final int port;
    PreGameRequestHandler requestHandler;
    ExecutorService executor;

    public Server(int port, PreGameRequestHandler requestHandler, ExecutorService executor){
        this.port = port;
        this.requestHandler = requestHandler;
        this.executor = executor;
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
        ExecutorService executor = Executors.newCachedThreadPool();

        boolean isServerAccepting = true;

        while(isServerAccepting) {
            try {
                Socket newClient = serverSocket.accept();
                System.out.println("Connection"); //--------

                //forse a questo punto syncronised su this

                //devo passare questo socket a qualcosa che lo gestisce
                //crea tutto quello che mi serve da questo socket
                this.executor.submit(() -> {
                    try {
                        ConnectionHandler connectionHandler = new ConnectionHandler(newClient, requestHandler);
                        connectionHandler.update();
                        String nickname = connectionHandler.waitForNickname();
                        requestHandler.addNewSocket(nickname, connectionHandler);
                        //a questo punto se che tutto è settato correttamente, posso far partire il thread su connectionHandler
                        executor.submit(connectionHandler);
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
                });

            } catch (IOException e) {
                //lancia IOException se avviene un errore mentre aspetta la connessione
                //in sostanza quando il server viene chiuso e lui è bloccato nella accept()
                //NB SocketException extends IOException
                isServerAccepting = false;
            }
        }

        //non vorrei mai arrivare qui ma se succede
        executor.shutdown();
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
