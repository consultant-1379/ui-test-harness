package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.UiKeyCodes;
import com.ericsson.cifwk.taf.ui.spi.AbstractUiEventProcessor;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.utils.InternalFileFinder;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Env;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

class SikuliUiMediator extends AbstractUiEventProcessor implements UiMediator {

    private static final Logger LOG = LoggerFactory.getLogger(SikuliUiMediator.class);

    private static final Map<Integer, String> KEY_MAP;
    private final ScreenRegion region;
    private final boolean highlightOn;

    static {
        KEY_MAP = new HashMap<>();
        KEY_MAP.put(UiKeyCodes.BACKSPACE, Key.BACKSPACE);
        KEY_MAP.put(UiKeyCodes.DELETE, Key.DELETE);
        KEY_MAP.put(UiKeyCodes.END, Key.END);
        KEY_MAP.put(UiKeyCodes.HOME, Key.HOME);
        KEY_MAP.put(UiKeyCodes.SHIFT, Key.SHIFT);
    }

    SikuliUiMediator(ScreenRegion region, boolean highlightOn) {
        this.region = region;
        this.highlightOn = highlightOn;
    }

    @Override
    public List<UiComponentStateManager> retrieve(final UiComponentMappingDetails details) {
        List<UiComponentStateManager> stateManagers = new ArrayList<>();
        if(!Strings.isNullOrEmpty(details.getSelector())){
            stateManagers.addAll(retrieveSelector(details));
        }
        if(stateManagers.isEmpty()){
            throw new UiComponentNotFoundException(details.toString());
        }
        return stateManagers;
    }

    private List<UiComponentStateManager> retrieveImage(UiComponentMappingDetails details, final String path) {

        Target target;
        URL resource = getClasspathResource(path);
        if (resource != null) {
            target = new ImageTarget(resource);
        } else {
            target = new ImageTarget(new File(path));
        }

        ScreenRegion subRegion = region.find(target);
        if (subRegion == null) {
            throw new UiComponentNotFoundException(details.toString());
        }

        if (highlightOn) {
            highlightRegion(subRegion);
        }

        List<UiComponentStateManager> result = Lists.newArrayList();
        result.add(new SikuliUiComponentStateManager(this, subRegion, details.toString()));

        return result;
    }

    private void highlightRegion(ScreenRegion subRegion) {
        Canvas canvas = new DesktopCanvas();
        canvas.addBox(subRegion).display(1);
    }

    private URL getClasspathResource(String imagePath) {
        try {
            return Resources.getResource(imagePath);
        } catch (IllegalArgumentException e) {
            // <-- Thrown if file wasn't found
            LOG.debug(format("File '%s' wasn't found", imagePath), e);
            return null;
        }
    }

    private List<UiComponentStateManager> retrieveSelector(UiComponentMappingDetails details) {
        String imagePath = details.getSelector();
        SelectorType selectorType = details.getSelectorType();
        switch (selectorType) {
            case XPATH:
            case CSS:
                throw new UnsupportedOperationException();
            case FILE_PATH:
            case DEFAULT:
                String fullImagePath = findFile(imagePath);
                return retrieveImage(details, fullImagePath);
            default:
                throw new UnsupportedOperationException("'" + imagePath + "' is not supported yet");
        }
    }

    private String findFile(final String imagePath) {
        String fullImagePath = InternalFileFinder.findFile(imagePath);
        if (fullImagePath == null) {
            throw new IllegalArgumentException(format("Cannot find image file '%s'", imagePath));
        }
        return fullImagePath;
    }

    @Override
    public String copy() {
        DesktopKeyboard keyboard = new DesktopKeyboard();
        keyboard.keyDown(Env.getHotkeyModifier());
        keyboard.type("c");
        keyboard.keyUp();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        sendKeys(sikuliKey(key), modifiers);
    }

    private static String sikuliKey(int key) {
        String keyString = KEY_MAP.get(key);
        if (keyString == null) {
            throw new IllegalArgumentException();
        }
        return keyString;
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        DesktopKeyboard keyboard = new DesktopKeyboard();
        if (modifiers != null) {
            for (int modifier : modifiers) {
                keyboard.keyDown(modifier);
            }
        }
        if (keys != null) {
            keyboard.type(keys);
        }
        if (modifiers != null) {
            for (int modifier : modifiers) {
                keyboard.keyUp(modifier);
            }
        }
    }

    @Override
    public void close() {
        throw unsupported();
    }

    @Override
    public void maximize() {
        throw unsupported();
    }

    @Override
    public void minimize() {
        throw unsupported();
    }

    @Override
    public String getCurrentUrl() {
        throw unsupported();
    }

    @Override
    public String getTitle() {
        throw unsupported();
    }

    @Override
    public void back() {
        throw unsupported();
    }

    @Override
    public void forward() {
        throw unsupported();
    }

    @Override
    public void refresh() {
        throw unsupported();
    }

    @Override
    public MessageBox getMessageBox() {
        throw unsupported();
    }

    @Override
    public UiComponentSize getWindowSize() {
        throw unsupported();
    }

    @Override
    public Object evaluate(String expression) {
        throw unsupported();
    }

    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        throw unsupported();
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments, UiComponentCallback callback) {
        throw unsupported();
    }

    @Override
    public Optional<InputStream> takeScreenshot() {
        return Optional.absent();
    }

    @Override
    public void setWindowSize(int width, int height) {
        throw unsupported();
    }

    @Override
    public UiActions newActionChain() {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException("Method is not supported");
    }

}
