package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.core.UiComponentAdapter;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import io.sterodium.extensions.client.SikuliExtensionClient;
import io.sterodium.rmi.protocol.server.TargetServerException;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
class SikuliUiComponent extends UiComponentAdapter {

    private final Keyboard keyboard;

    private ImageTarget imageTarget;

    private SikuliExtensionClient sikuliClient;

    private final DesktopScreenRegion desktop;

    public SikuliUiComponent(ImageTarget imageTarget, SikuliExtensionClient sikuliClient) {
        this.imageTarget = imageTarget;
        this.sikuliClient = sikuliClient;
        this.desktop = sikuliClient.getDesktop();
        this.keyboard = sikuliClient.getKeyboard();
    }

    @Override
    public void click() {
        ScreenRegion screenRegion = findComponent();
        if (screenRegion != null) {
            ScreenLocation center = screenRegion.getCenter();
            sikuliClient.getMouse().click(center);
        } else {
            throw new UiComponentNotFoundException(imageTarget.toString());
        }
    }

    @Override
    public boolean exists() {
        return findComponent() != null;
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        click();
        for (CharSequence keys : keysToSend) {
            pressKeysCombination(keys);
        }
    }

    private void pressKeysCombination(CharSequence keys) {
        sendKey(getKeyCode(keys), getKeyModifierCodes(keys));
    }

    private static int getKeyCode(CharSequence keys) {
        return keys.charAt(0);
    }

    private static int[] getKeyModifierCodes(CharSequence keys) {
        int[] keyCodes = new int[keys.length() - 1];
        for (int i = 1; i < keys.length(); i++) {
            // copied from SWTBot?
            keyCodes[i++] = keys.charAt(i); // NOSONAR
        }
        return keyCodes;
    }

    protected ScreenRegion findComponent() {
        try {
            return desktop.find(imageTarget);
        }
        catch(TargetServerException t){
            throw new RuntimeException(String.format("desktop invocation failed while looking for component '%s' '%s' ",
                    imageTarget.toString(), t.getMessage()));
        }
        catch (Exception e){
             return null;
        }
    }

    protected void sendKey(int keyCode, int... modifierKeyCodes) {
        lockKeys(modifierKeyCodes);
        keyboard.keyDown(keyCode);
        keyboard.keyUp(keyCode);
        releaseKeys(modifierKeyCodes);
    }

    private void lockKeys(int... keyCodes) {
        for (int keyCode : keyCodes) {
            keyboard.keyDown(keyCode);
        }
    }

    private void releaseKeys(int... keyCodes) {
        for (int keyCode : keyCodes) {
            keyboard.keyUp(keyCode);
        }
    }

    protected Keyboard getKeyboard() {
        return keyboard;
    }
}
