package com.ericsson.cifwk.taf.ui.automation.demo;

import com.ericsson.cifwk.taf.ui.automation.AutomationLocation;
import com.ericsson.cifwk.taf.ui.automation.Automator;
import com.ericsson.cifwk.taf.ui.automation.impl.DefaultAutomator;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class AutomatorDemo {

    public static void main(String[] args) throws Exception {
        URL imageFolderUrl = AutomatorDemo.class.getClassLoader().getResource("automation");
        assertThat(imageFolderUrl).isNotNull();
        File imageFolder = new File(imageFolderUrl.toURI());

        String brushImagePath = getImagePath(imageFolder, "mspaint-brush.png");
        String canvasImagePath = getImagePath(imageFolder, "mspaint-canvas.png");

        File desktopFolder = new File(System.getProperty("user.home"), "Desktop");
        String desktopPath = desktopFolder.getAbsolutePath();

        Automator automator = new DefaultAutomator();
        automator.run("mspaint", brushImagePath, 5000);
        automator.sendKey(KeyEvent.VK_E, KeyEvent.VK_CONTROL);
        automator.sendKeys("100");
        automator.sendKey(KeyEvent.VK_TAB);
        automator.sendKeys("100");
        automator.sendKey(KeyEvent.VK_ENTER);

        AutomationLocation center = automator.automateRegion(canvasImagePath).topLeft();
        drawArc(automator, center, 25, 0, 360, 30);
        drawArc(automator, center, 15, 15, 165, 30);
        drawArc(automator, center.relativeLocation(-9, -9), 2, 0, 360, 60);
        drawArc(automator, center.relativeLocation(9, -9), 2, 0, 360, 60);

        automator.sendKey(KeyEvent.VK_A, KeyEvent.VK_CONTROL);
        automator.sendKey(KeyEvent.VK_C, KeyEvent.VK_CONTROL);
        automator.saveClipboard(desktopPath);
    }

    private static String getImagePath(File imageFolder, String name) throws Exception {
        File imageFile = new File(imageFolder, name);
        return imageFile.getAbsolutePath();
    }

    private static void drawArc(Automator automator,
                                AutomationLocation center,
                                int radius,
                                int from,
                                int to,
                                int step) {
        for (int i = from; i <= to; i += step) {
            double angle = i * Math.PI / 180;
            int dx = (int) Math.round(radius * Math.cos(angle));
            int dy = (int) Math.round(radius * Math.sin(angle));
            AutomationLocation pt = center.relativeLocation(dx, dy);
            automator.drag(pt);
        }
        automator.drop();
    }

}
