package it.polimi.ingsw.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SingleLobbyPanel extends ObservablePanel implements ActionListener{
    private static HashMap<Integer, SingleLobbyPanel> lobbys = new HashMap<>();
    private int lobbyId;

    public SingleLobbyPanel(int lobbyId, String[] players){
        setLayout(new GridLayout(0, 1));
        add(new JLabel(Integer.toString(lobbyId)));
        for (int i = 0; i < players.length; i++)
            add(new JLabel(players[i]));
        Button button = new Button("Go");
        button.addActionListener(this);
        add(button);
        lobbys.put(lobbyId, this);
        this.lobbyId = lobbyId;
    }

    public static SingleLobbyPanel removeLobby(int lobbyId){
        return lobbys.remove(lobbyId);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        observer.chooseLobby(this.lobbyId);
    }
}
