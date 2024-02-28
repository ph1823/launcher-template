package fr.ph1823.launcher.panel;

import fr.ph1823.launcher.Main;
import fr.ph1823.launcher.utility.CustomTextField;
import fr.ph1823.launcher.utility.DownloadUtility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MainPanel extends JPanel {

    Image backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background.png"));

    JLabel infoText = new JLabel(), downloadText = new JLabel();
    JProgressBar infoProgress = new JProgressBar(), downloadProgress = new JProgressBar();
    public static Properties properties;


    public MainPanel(JFrame frame) throws IOException {
        //setVisible(true);
        setLayout(null);
        setBounds(0, 0, (int) (960 * Main.ratioWidth), (int) (508 * Main.rationHeight));

        DownloadUtility.getInstance().attach(this);
        DownloadUtility.getInstance().checkLauncherDir();
        properties = this.loadProperties();

        //Add close utton
        JButton close = createButton("close", 18, 22, 38, 38);
        add(close);
        close.addActionListener(e -> System.exit(0));

        //Add reduce button
        /*JButton reduce = createButton("reduce", 506, 22, 38, 38);
        add(reduce);
        reduce.addActionListener((e) -> frame.setState(Frame.ICONIFIED));*/

        Font font1 = new Font("Default", Font.PLAIN, 40);

        //Add text
        JTextField username = new CustomTextField(20);
        username.setFont(font1);
        username.setBorder(null);
        username.setOpaque(false);
        username.setText(properties.getProperty("username", ""));
        username.setBounds((int) (this.getWidth() / 2 - ((326 * Main.ratioWidth) / 2)), this.getHeight() - 238, (int) (334 * Main.ratioWidth), (int) (53 * Main.rationHeight));
        add(username);

        /*JTextField password = new JPasswordField(20);
        password.setBounds((this.getWidth() / 2 - ((326 * Main.ratioWidth) / 2)), this.getHeight() - 118,(int) (326 * Main.ratioWidth) , (int) (44* Main.rationHeight));
        password.setFont(font1);
        password.setBorder(null);
        password.setOpaque(false);
        add(password);*/

        JPopupMenu popupMenu = new JPopupMenu();
        add(popupMenu);
        //Play: 414x103 x: 101 y: 690
        JButton play = createButton("play", (int) (this.getWidth() / 2 -  (113*Main.ratioWidth) / 2), this.getHeight()  - 118,113, 44);
        play.addActionListener((e) -> {
            new Thread(() -> {
                try {
                    properties.setProperty("username", username.getText());
                    saveProperties(properties);
                    System.out.println("fun checkLauncherDir");
                    DownloadUtility.getInstance().checkLauncherDir();
                    System.out.println("fun checkUpdate");
                    DownloadUtility.getInstance().checkUpdate();
                    System.out.println("fun update");
                    DownloadUtility.getInstance().update();
                    System.out.println("fun launchGame");
                    DownloadUtility.getInstance().launchGame(username.getText(), properties.getProperty("ram"));
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Une erreur est survenue.\n" + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            }).start();
            //Lancement du jeu, en téléchargant les ressources
        });
        add(play);

        //Add settiong button and create action (open new window)
        JButton settings = createButton("settings", 58, 22, 38, 38);
        settings.addActionListener((e) -> {
            JPanel panel = new SettingsPanel(properties);
            panel.setVisible(true);
            JFrame settingsFrame = new JFrame("Settings");
            settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the settings frame
            settingsFrame.getContentPane().add(panel);

            // Configure the settings frame
            settingsFrame.pack();
            settingsFrame.setLocationRelativeTo(null); // Center on the screen
            settingsFrame.setVisible(true);
        });
        add(settings);
        //volume:32x32 x: 73 y: 25
        //Add volume settings button


        add(infoText);
        add(infoProgress);
        add(downloadText);
        add(downloadProgress);
    }

    private JButton createButton(String name, int x, int y, int widht, int height) {
        Image icon = new ImageIcon(getClass().getResource("/" + name + ".png")).getImage();
        JButton button = new JButton(new ImageIcon(icon.getScaledInstance(widht, height, Image.SCALE_SMOOTH)));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBounds((int) (x * Main.ratioWidth), (int) (y * Main.rationHeight), (int) (widht * Main.ratioWidth), (int) (height * Main.rationHeight));

        return button;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void updateState(String infoMessage) {
        Font font1 = new Font("Default", Font.PLAIN, 20);
        infoText.setBounds(getRatioX(40), getRatiY(800), getRatioX(580), getRatiY(24));
        infoText.setFont(font1);

        infoText.setText(infoMessage);
    }

    public int getRatiY(int y) {
        return (int) (y * Main.rationHeight);
    }

    public int getRatioX(int x) {
        return (int) (x * Main.rationHeight);
    }

    public void initProgressbar(int totalFile, int totalBytes) {
        Font font1 = new Font("Default", Font.PLAIN, 20);
        infoText.setBounds(getRatioX(80),  getRatiY(800), getRatioX(520), getRatiY(24));
        infoText.setFont(font1);
        infoText.setHorizontalAlignment(JLabel.CENTER);
        infoText.setHorizontalTextPosition(JLabel.CENTER);
        infoProgress.setBounds(getRatioX(40),  getRatiY(831), getRatioX(540), getRatiY(30));
        infoProgress.setMaximum(totalFile);
        infoProgress.setMinimum(0);

        downloadText.setFont(font1);
        downloadText.setBounds(getRatioX(80), getRatiY(876), getRatioX(520), getRatiY(24));
        downloadText.setHorizontalAlignment(JLabel.CENTER);
        downloadText.setHorizontalTextPosition(JLabel.CENTER);
        downloadProgress.setBounds(getRatioX(40), getRatiY(906), getRatioX(540), getRatiY(30));
        downloadProgress.setMaximum(totalBytes);
        downloadProgress.setMinimum(0);
    }
    public void updateState(int countFile, int downloadedByte) {
        infoText.setText("Téléchargement des fichiers " + countFile + "/" + infoProgress.getMaximum());
        infoProgress.setValue(countFile);


        downloadText.setText( downloadedByte + " Mb/" + downloadProgress.getMaximum() + " Mb");
        downloadProgress.setValue(downloadedByte);

    }

    // Load properties from the file, create the file if it doesn't exist
    private Properties loadProperties() {
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(DownloadUtility.getInstance().getDir().getPath() + "/launcher.properties"))) {
            prop.load(input);
        } catch (IOException ex) {
            // If the file doesn't exist, create it
            createPropertiesFile();
            // Load properties again
            try (InputStream input = Files.newInputStream(Paths.get(DownloadUtility.getInstance().getDir().getPath() + "/launcher.properties"))) {
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

    private void createPropertiesFile() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("ram", "4");
        saveProperties(defaultProperties);
    }

    // Save properties to the file
    public static void saveProperties(Properties prop) {
        DownloadUtility.getInstance().checkLauncherDir();

        try (OutputStream output = Files.newOutputStream(Paths.get(DownloadUtility.getInstance().getDir().getPath() + "/launcher.properties"))) {
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}