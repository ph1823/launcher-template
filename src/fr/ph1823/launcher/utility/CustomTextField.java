package fr.ph1823.launcher.utility;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField {

    public CustomTextField(int columns) {
        super(columns);
    }


    @Override
    protected void paintComponent(Graphics g) {
        // Load the image
        ImageIcon ii = new ImageIcon(getClass().getResource("/input.png"));
        Image i = ii.getImage();

        // Draw the image
        g.drawImage(i, 0, 0, this);

        // Call the superclass's paintComponent method
        super.paintComponent(g);
    }
}
