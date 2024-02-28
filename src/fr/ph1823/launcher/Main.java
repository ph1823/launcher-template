package fr.ph1823.launcher;

import fr.ph1823.launcher.frame.MainFrame;

import java.awt.*;
import java.io.IOException;

public class Main {
    public static final String URL = "http://montana.datahosting.fr/launcher";
    public static final String NAME = "MontanaRP";
    public static final String PREFIX = "/launcher/game";
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double ratioWidth = (screenSize.getWidth() / 960) ;
    public static double rationHeight = ((screenSize.getHeight()) / 508);


    public static void main(String[] args) throws IOException {
        if(ratioWidth > 1) ratioWidth = 1;
        if(rationHeight > 1) rationHeight = 1;

        //ratioWidth = rationHeight;
        new MainFrame();
    }
}