package it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HomePanel extends ObservablePanel implements ActionListener {

    public HomePanel() {
        super();
        setLayout(new BorderLayout());
        try {
            add(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\valer\\Desktop\\provagui\\Immagine.jpg")))), BorderLayout.NORTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton button = new JButton("Play");
        button.addActionListener(this);
        add(button, BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        observer.updateToUsernameFrame();
    }
}
