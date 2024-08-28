package com.ericsson.cifwk.taf.ui.core;

public class UiComponentSize {
    private final int width;
    private final int height;

    public UiComponentSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "UiComponentSize [width=" + width + ", height=" + height + "]";
    }
}
