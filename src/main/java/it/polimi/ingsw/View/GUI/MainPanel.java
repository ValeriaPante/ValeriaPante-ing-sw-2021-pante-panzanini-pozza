package it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainPanel extends JPanel {

    public MainPanel(){
        super();
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        ImageIcon colored = null;
        ImageIcon blackAndWhite = null;
        try {
            colored = new ImageIcon(ImageIO.read(new File("C:\\Users\\valer\\Desktop\\provagui2\\src\\colored.jpg")));
            blackAndWhite = new ImageIcon(ImageIO.read(new File("C:\\Users\\valer\\Desktop\\provagui2\\src\\ba.jpg")));
        } catch (IOException e){

        }

        Image image = colored.getImage(); // transform it
        Image newimg = image.getScaledInstance(1000, 700,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        colored = new ImageIcon(newimg);  // transform it back

        image = blackAndWhite.getImage(); // transform it
        newimg = image.getScaledInstance(300, 200,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        blackAndWhite = new ImageIcon(newimg);  // transform it back

        panel1.add(new JLabel(colored));


        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(0, 1));
        for(int i= 0; i < 3; i++)
            panel2.add(new JLabel(blackAndWhite));

        panel2.setSize(30, 100);

        this.add(panel1);
        this.add(panel2);
        setVisible(true);
    }
}
