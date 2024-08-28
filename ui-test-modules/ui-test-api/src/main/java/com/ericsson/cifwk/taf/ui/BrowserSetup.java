package com.ericsson.cifwk.taf.ui;


import java.util.HashMap;
import java.util.Map;

public final class BrowserSetup {

    protected static final String IMAGE_RECOGNITION_CAPABILITY = "extension.sikuliCapability";

    /**
     * Predefined screen resolutions
     * <ul>
     * <li>{@link #RESOLUTION_800x600  800x600}</li>
     * <li>{@link #RESOLUTION_1024x768 1024x768 }</li>
     * <li>{@link #RESOLUTION_1280x960 1280x960 }</li>
     * <li>{@link #RESOLUTION_1280x1024 1280x1024 }</li>
     * <li>{@link #RESOLUTION_1600x1200 1600x1200 }</li>
     * <li>{@link #RESOLUTION_1920x1200 1920x1200 }</li>
     * <li>{@link #RESOLUTION_2560x1440 2560x1440 }</li>
     * </ul>
     */
    public enum Resolution {

        RESOLUTION_800x600(800, 600),
        RESOLUTION_1024x768(1024, 768),
        RESOLUTION_1280x960(1280, 960),
        RESOLUTION_1280x1024(1280, 1024),
        RESOLUTION_1600x1200(1600, 1200),
        RESOLUTION_1920x1200(1920, 1200),
        RESOLUTION_2560x1440(2560, 1440);

        int width, height;

        Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private BrowserType browserType;
    private BrowserOS os;
    private String browserVersion;
    private int width, height;
    private final Map<String, Object> capabilities = new HashMap<>();

    public BrowserSetup() {
        // empty
    }

    public BrowserSetup(BrowserType browserType, BrowserOS os, String browserVersion) {
        this.browserType = browserType;
        this.os = os;
        this.browserVersion = browserVersion;
    }

    public BrowserSetup(BrowserType browserType, BrowserOS os, String browserVersion,
                        int width, int height,
                        Map<String, Object> capabilites) {
        this.browserType = browserType;
        this.os = os;
        this.browserVersion = browserVersion;
        this.width = width;
        this.height = height;
        this.capabilities.putAll(capabilites);
    }


    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public BrowserOS getOs() {
        return os;
    }

    public void setOs(BrowserOS os) {
        this.os = os;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setSize(Resolution resolution) {
        this.width = resolution.width;
        this.height = resolution.height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    /**
     * Checks if setup contains all necessary information
     */
    public boolean isValid() {
        return browserType != null && os != null;
    }

    @Override
    public String toString() {
        return "BrowserSetup [browserType=" + browserType + ", os=" + os + ", browserVersion=" + browserVersion + "]";
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {

        private BrowserType browserType;
        private BrowserOS os;
        private String browserVersion;
        private int width, height;
        private final Map<String, Object> capabilities = new HashMap<>();

        public Builder withType(BrowserType browserType) {
            this.browserType = browserType;
            return this;
        }

        public Builder withOS(BrowserOS os) {
            this.os = os;
            return this;
        }

        public Builder withVersion(String browserVersion) {
            this.browserVersion = browserVersion;
            return this;
        }

        public Builder withSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder withSize(Resolution resolution) {
            this.width = resolution.width;
            this.height = resolution.height;
            return this;
        }

        public Builder withCapability(String name, Object capability) {
            capabilities.put(name, capability);
            return this;
        }

        public Builder withCapability(Map<String, Object> capabilities) {
            this.capabilities.putAll(capabilities);
            return this;
        }

        public Builder withImageRecognitionCapability() {
            this.capabilities.put(IMAGE_RECOGNITION_CAPABILITY, true);
            return this;
        }

        public BrowserSetup setup() {
            return new BrowserSetup(browserType, os, browserVersion, width, height, capabilities);
        }

    }

}
