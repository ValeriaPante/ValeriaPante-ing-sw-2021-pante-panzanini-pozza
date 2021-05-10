package it.polimi.ingsw;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    private Scanner fromServer;
    private PrintWriter toServer;

    //ritorna true se la partita è finita
    private boolean convertInput(String input){
        return false;
    }

/*    public void ping() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toServer.println("ping");
            }
        }).start();
    }*/

    public void run(){
        if(fromServer != null && toServer != null) {
            while (true) {
                String input = fromServer.nextLine();
                //break se è finita la partita
                if(convertInput(input))
                    break;
            }

            fromServer.close();
            toServer.close();
        }
    }

    public void update(String inputLine) {
        toServer.println(inputLine);
        toServer.flush();
    }

    public void connect(String ip, String port, String username) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));
            fromServer = new Scanner(socket.getInputStream());
            toServer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Server unreachable");
            System.exit(0);
        }
        this.update(username);
        System.out.println("Connection established");
        //ping();
    }
}
