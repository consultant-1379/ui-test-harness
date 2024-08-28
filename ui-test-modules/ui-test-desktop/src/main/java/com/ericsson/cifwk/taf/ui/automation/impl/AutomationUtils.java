package com.ericsson.cifwk.taf.ui.automation.impl;

import org.sikuli.api.ImageTarget;
import org.sikuli.api.Target;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class AutomationUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd 'at' HH.mm.ss";

    private AutomationUtils() {
    }

    /**
     * Creates a <code>File</code> instance named with the current timestamp.
     *
     * @param directory directory to hold the file
     * @param extension file extension
     * @return file with a timestamp in its name
     */
    public static File timestampedFile(String directory, String extension) {
        return timestampedFile(directory, null, extension);
    }

    /**
     * Creates a <code>File</code> instance named with the current timestamp.
     *
     * @param directory directory to hold the file
     * @param prefix    file name prefix to put before the timestamp
     * @param extension file extension
     * @return file with a timestamp in its name
     */
    public static File timestampedFile(String directory, String prefix, String extension) {
        String name = (prefix != null ? prefix + " " : "")
                + new SimpleDateFormat(DATE_FORMAT).format(new Date());
        return new File(directory, name + "." + extension);
    }

    /**
     * Makes a Sikuli image target from a file location.
     *
     * @param targetLocation location of image file
     * @return image targets
     */
    public static Target makeTarget(String targetLocation) {
        File file = new File(targetLocation);
        return new ImageTarget(file);
    }

    /**
     * Returns clipboard contents in a given flavor.
     *
     * @param clipboard  target clipboard
     * @param dataFlavor data flavor, e.g. text or image
     * @return clipboard contents
     */
    public static Object getClipboard(Clipboard clipboard, DataFlavor dataFlavor) {
        try {
            return clipboard.getData(dataFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    /**
     * Converts a given <code>Image</code> to <code>BufferedImage</code>.
     *
     * @param image image to convert to <code>BufferedImage</code>
     * @return <code>BufferedImage</code> instance
     */
    public static BufferedImage toBufferedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int type = BufferedImage.TYPE_INT_ARGB;
        BufferedImage bufferedImage = new BufferedImage(width, height, type);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }
}
