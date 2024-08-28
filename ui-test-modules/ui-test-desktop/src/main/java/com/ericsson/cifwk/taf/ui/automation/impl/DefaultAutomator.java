package com.ericsson.cifwk.taf.ui.automation.impl;

import com.ericsson.cifwk.taf.ui.automation.AutomationLocation;
import com.ericsson.cifwk.taf.ui.automation.AutomationRegion;
import com.ericsson.cifwk.taf.ui.automation.Automator;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static com.ericsson.cifwk.taf.ui.automation.impl.AutomationUtils.getClipboard;
import static com.ericsson.cifwk.taf.ui.automation.impl.AutomationUtils.timestampedFile;
import static com.ericsson.cifwk.taf.ui.automation.impl.AutomationUtils.toBufferedImage;

public final class DefaultAutomator implements Automator {

    private final AutomationContext context;

    public DefaultAutomator() {
        context = new AutomationContext();
    }

    @Override
    public AutomationRegion automateDesktop() {
        ScreenRegion region = new DesktopScreenRegion();
        return new DefaultAutomationRegion(region);
    }

    @Override
    public AutomationRegion automateRegion(String imageFile) {
        AutomationRegion region = automateDesktop();
        return region.find(imageFile);
    }

    @Override
    public AutomationRegion automateRegion(String imageFile, int waitMillis) {
        AutomationRegion region = automateDesktop();
        return region.find(imageFile, waitMillis);
    }

    @Override
    public AutomationRegion automateRegion(int x, int y, int width, int height) {
        ScreenRegion region = new DesktopScreenRegion(x, y, width, height);
        return new DefaultAutomationRegion(region);
    }

    @Override
    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run(String command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    @Override
    public void run(String command, String imageFile, int waitMillis) {
        run(command);
        automateRegion(imageFile, waitMillis);
    }

    @Override
    public void click(AutomationLocation location) {
        DesktopMouse mouse = context.getMouse();
        mouse.click(getScreenLocation(location));
    }

    @Override
    public void rightClick(AutomationLocation location) {
        DesktopMouse mouse = context.getMouse();
        mouse.rightClick(getScreenLocation(location));
    }

    @Override
    public void doubleClick(AutomationLocation location) {
        DesktopMouse mouse = context.getMouse();
        mouse.doubleClick(getScreenLocation(location));
    }

    @Override
    public void drag() {
        DesktopMouse mouse = context.getMouse();
        mouse.mouseDown(InputEvent.BUTTON1_MASK);
    }

    @Override
    public void drag(AutomationLocation location) {
        DesktopMouse mouse = context.getMouse();
        mouse.drag(getScreenLocation(location));
    }

    @Override
    public void drop() {
        pause(1000);
        DesktopMouse mouse = context.getMouse();
        mouse.mouseUp(InputEvent.BUTTON1_MASK);
    }

    @Override
    public void drop(AutomationLocation location) {
        DesktopMouse mouse = context.getMouse();
        mouse.drop(getScreenLocation(location));
    }

    private static ScreenLocation getScreenLocation(AutomationLocation location) {
        return ((DefaultAutomationLocation) location).getLocation();
    }

    @Override
    public void sendKey(int keyCode, int... modifierKeyCodes) {
        DesktopKeyboard keyboard = context.getKeyboard();
        pressKeys(modifierKeyCodes);
        keyboard.keyDown(keyCode);
        keyboard.keyUp(keyCode);
        releaseKeys(modifierKeyCodes);
    }

    @Override
    public void sendKeys(String keys, int... modifierKeyCodes) {
        DesktopKeyboard keyboard = context.getKeyboard();
        pressKeys(modifierKeyCodes);
        keyboard.type(keys);
        releaseKeys(modifierKeyCodes);
    }

    private void pressKeys(int... keyCodes) {
        DesktopKeyboard keyboard = context.getKeyboard();
        for (int keyCode : keyCodes) {
            keyboard.keyDown(keyCode);
        }
    }

    private void releaseKeys(int... keyCodes) {
        DesktopKeyboard keyboard = context.getKeyboard();
        for (int keyCode : keyCodes) {
            keyboard.keyUp(keyCode);
        }
    }

    @Override
    public void paste(String text) {
        DesktopKeyboard keyboard = context.getKeyboard();
        keyboard.paste(text);
    }

    @Override
    public String clipboardAsString() {
        Clipboard clipboard = context.getClipboard();
        return (String) getClipboard(clipboard, DataFlavor.stringFlavor);
    }

    @Override
    public void saveClipboard(String directory) {
        Clipboard clipboard = context.getClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            Image image = (Image) getClipboard(clipboard, DataFlavor.imageFlavor);
            File file = timestampedFile(directory, "Clipboard", "png");
            try {
                ImageIO.write(toBufferedImage(image), "png", file);
            } catch (IOException e) {
                throw new RuntimeException(e); // NOSONAR
            }
        }
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            String text = (String) getClipboard(clipboard, DataFlavor.stringFlavor);
            File file = timestampedFile(directory, "Clipboard", "txt");
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file);
                writer.println(text);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e); // NOSONAR
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
}
