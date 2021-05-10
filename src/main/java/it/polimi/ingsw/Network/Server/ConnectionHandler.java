package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Controller.PreGameController.RequestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionHandler implements Runnable{

    private int id;
    private final Scanner fromClient;
    private final PrintWriter toClient;
    private RequestHandler requestHandler;

    public ConnectionHandler(Socket socket, RequestHandler requestHandler) throws IOException {
        this.fromClient = new Scanner(socket.getInputStream());
        this.toClient = new PrintWriter(socket.getOutputStream());
        this.requestHandler = requestHandler;
    }

    public String waitForNickname(){
        String nickname = "";
        while (nickname.isEmpty()){
            nickname = this.fromClient.nextLine();
            nickname = nickname.trim();
        }
        return nickname;
    }

    public void send(String message){
        this.toClient.println(message);
        this.toClient.flush();
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public void run() {
        String request;
        while (true) {
            request = fromClient.nextLine();

            System.out.println(request); //
            if (request.equals("quit")) {
                this.requestHandler.connectionClosed(this.id);
                break;
            } else {
                this.requestHandler.requestEvaluator(this.id, request);
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
