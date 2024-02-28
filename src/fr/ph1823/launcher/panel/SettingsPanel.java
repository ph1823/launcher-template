package fr.ph1823.launcher.panel;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.sun.management.OperatingSystemMXBean;

public class SettingsPanel extends JPanel {
    private JSlider ramSlider;
    private JLabel selectedRamLabel;

    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> saveRam = null;
    public SettingsPanel(Properties properties) {
        setLayout(new GridLayout(2, 1));

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        int minMemoryGB = 2; // Minimum RAM in gigabytes
        long maxMemoryGB = osBean.getTotalPhysicalMemorySize() / (1024 * 1024 * 1024); // Maximum RAM in gigabytes

        int defaultRam = Integer.parseInt(properties.getProperty("ram", "4"));
        // Create a JSlider for selecting RAM
        System.out.println(Runtime.getRuntime().maxMemory());
        ramSlider = new JSlider(minMemoryGB, (int) maxMemoryGB);
        ramSlider.setMajorTickSpacing(1); // Set major tick every 1GB
        ramSlider.setPaintTicks(true);
        ramSlider.setPaintLabels(true);
        ramSlider.setValue(defaultRam);

        // Create a JLabel to display the selected RAM value
        selectedRamLabel = new JLabel("Min RAM: 2Go Max RAM: " + ramSlider.getValue() + " Go");

        // Add components to the panel
        add(ramSlider);
        add(selectedRamLabel);

        // Add a ChangeListener to the slider to update the label
        ramSlider.addChangeListener(e -> {
            selectedRamLabel.setText("Min RAM: 2Go Max RAM: " + ramSlider.getValue() + " Go");
            if(saveRam != null) saveRam.cancel(false);

            saveRam = ses.schedule(() -> {
                properties.setProperty("ram", ramSlider.getValue()+"");
                MainPanel.saveProperties(properties);
            }, 2, TimeUnit.SECONDS);
        });
    }
}
