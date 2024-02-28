package fr.ph1823.launcher.frame;

import fr.ph1823.launcher.Main;
import fr.ph1823.launcher.panel.MainPanel;
import fr.ph1823.launcher.utility.FrameDragListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

public class MainFrame extends JFrame{

    public MainFrame() throws IOException {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(Main.NAME + " - Launcher");

        // Set name in system menubar for Gnome (and Linux)
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            try {
                Toolkit xToolkit = Toolkit.getDefaultToolkit();
                Field awtAppClassNameField = xToolkit.getClass().getDeclaredField("awtAppClassName");
                awtAppClassNameField.setAccessible(true);
                awtAppClassNameField.set(xToolkit, Main.NAME + " - Démarrage");
            } catch (Exception e) {
                System.out.println("Errorr on set linux title");
            }
        }

        setResizable(false);
        setSize((int) (960 * Main.ratioWidth), (int) (508 * Main.rationHeight));

        FrameDragListener frameDragListener = new FrameDragListener(this);
        addMouseListener(frameDragListener);
        addMouseMotionListener(frameDragListener);

        setLocationRelativeTo(null);
        setUndecorated(true);
        setContentPane(new MainPanel(this));


        setVisible(true);

    }

}
