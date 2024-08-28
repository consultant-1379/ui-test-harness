package com.ericsson.cifwk.taf.ui.automation.impl;

import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

import java.awt.*;
import java.awt.datatransfer.Clipboard;

public class AutomationContext {

    private final DesktopMouse mouse;
    private final DesktopKeyboard keyboard;
    private final Clipboard clipboard;

    AutomationContext() {
        mouse = new DesktopMouse();
        keyboard = new DesktopKeyboard();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public DesktopMouse getMouse() {
        return mouse;
    }

    public DesktopKeyboard getKeyboard() {
        return keyboard;
    }

    public Clipboard getClipboard() {
        return clipboard;
    }
}
