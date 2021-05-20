package it.polimi.ingsw.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsernamePanel extends ObservablePanel implements ActionListener {
    private JTextArea textInput;

    public UsernamePanel() {
        super();
        setLayout(new BorderLayout());
        add(new JLabel("Choose your username."), BorderLayout.NORTH);
        textInput = new JTextArea();
        add(textInput, BorderLayout.CENTER);

        JButton button = new JButton("Send");
        button.setVisible(true);
        button.addActionListener(this);
        add(button, BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        observer.updateToLobbyFrame(textInput.getText());
    }
}
