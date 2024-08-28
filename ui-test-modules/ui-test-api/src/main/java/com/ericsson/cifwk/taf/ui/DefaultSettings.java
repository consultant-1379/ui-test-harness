package com.ericsson.cifwk.taf.ui;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.ericsson.cifwk.meta.API.Since;
import com.ericsson.cifwk.taf.configuration.TafConfiguration;
import com.ericsson.cifwk.taf.configuration.TafConfigurationProvider;
import com.ericsson.cifwk.taf.ui.spi.PropertiesKeyMapper;

/**
 * Default UI settings
 * <p>
 * Loads from the first <code>taf_ui.properties</code> config file it finds in the classpath.
 */
public final class DefaultSettings {

    /*
     * Default values
    */
    static final BrowserOS DEFAULT_OS = BrowserOS.WINDOWS;
    static final BrowserType DEFAULT_BROWSER = BrowserType.CHROME;
    static final long TIMEOUT = 7000;
    static final long[] RETRY_SCHEMA = new long[]{100, 200, 500, 1000, 2000, 4000}; // NOSONAR

    /*
     * Properties keys
     */
    public static final String UI_DEFAULT_GRID_IP_PROPERTY = "ui_toolkit.default_grid_ip";
    public static final String UI_DEFAULT_GRID_PORT_PROPERTY = "ui_toolkit.default_grid_port";
    public static final String UI_DEFAULT_BROWSER_PROPERTY = "ui_toolkit.default_browser";
    public static final String UI_DEFAULT_OS_PROPERTY = "ui_toolkit.default_OS";

    /**
     * Retry Schema - how many times and how long retry UI operation.
     * Please look at next setting to see what cases can be handled and retried.
     * <p>
     * Default value is '100,200,500,1000,2000,4000' in millis.
     */
    public static final String UI_RETRY_SCHEMA_PROPERTY = "ui_toolkit.implicit_wait.retry_schema";

    /**
     * How long to wait until retry schema give up and throws exception.
     * <p>
     * Depending on original exception following exceptions could be thrown:
     * - UiComponentNotFoundException
     * - UiComponentNotVisibleException
     * - StaleElementReferenceException
     * <p>
     * Default value is 7 seconds.
     */
    public static final String UI_RETRY_SCHEMA_TIMEOUT_PROPERTY = "ui_toolkit.implicit_wait.total_timeout_millis";


    private static final TafConfiguration configuration = TafConfigurationProvider.provide();

    private DefaultSettings() {
    }

    public static String getDefaultGridIp() {
        return getProperty(UI_DEFAULT_GRID_IP_PROPERTY);
    }

    public static int getDefaultGridPort() {
        String gridPort = getProperty(UI_DEFAULT_GRID_PORT_PROPERTY);
        if (isNullOrEmpty(gridPort)) {
            return 0;
        } else {
            return Integer.parseInt(gridPort);
        }
    }

    public static BrowserType getDefaultBrowserType() {
        String defaultBrowser = getProperty(UI_DEFAULT_BROWSER_PROPERTY);
        return (defaultBrowser == null) ? DEFAULT_BROWSER : BrowserType.forName(defaultBrowser);
    }

    public static BrowserOS getDefaultBrowserOS() {
        String defaultOS = getProperty(UI_DEFAULT_OS_PROPERTY);
        return (defaultOS == null) ? DEFAULT_OS : BrowserOS.forName(defaultOS);
    }

    public static long getImplicitWaitTimeout() {
        return getLongProperty(UI_RETRY_SCHEMA_TIMEOUT_PROPERTY, TIMEOUT);
    }

    public static long[] getImplicitWaitRetrySchema() {
        return getProperty(UI_RETRY_SCHEMA_PROPERTY, new RetrySchemaProvider(), RETRY_SCHEMA);
    }

    protected static String getProperty(String key) {
        return getProperty(key, new PropertyProvider(), null);
    }

    protected static Long getLongProperty(String key, Long defaultValue) {
        return getProperty(key, new LongPropertyProvider(), defaultValue);
    }

    private static <T> T getProperty(String key, Provider<T> provider, T defaultValue) {
        T result = provider.provide(key);
        Iterator<String> alternativePropertyKeys = getAlternativePropertyKeys(key).iterator();
        while (result == null && alternativePropertyKeys.hasNext()) {
            result = provider.provide(alternativePropertyKeys.next());
        }
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    private static List<String> getAlternativePropertyKeys(String propertyKey) {
        List<String> alternativePropertyKeys = new ArrayList<>();
        for (PropertiesKeyMapper propertiesKeyMapper : ServiceLoader.load(PropertiesKeyMapper.class)) {
            String alternativePropertyKey = propertiesKeyMapper.map(propertyKey);
            if (alternativePropertyKey != null) {
                alternativePropertyKeys.add(alternativePropertyKey);
            }
        }
        return alternativePropertyKeys;
    }

    private interface Provider<T> {
        T provide(String key);
    }

    private static class PropertyProvider implements Provider<String>{
        @Override
        public String provide(String key) {
            return configuration.getProperty(key, null, String.class);
        }
    }

    private static class LongPropertyProvider implements Provider<Long> {

        @Override
        public Long provide(String key) {
            return configuration.getProperty(key, null, Long.class);
        }
    }

    private static class RetrySchemaProvider implements Provider<long[]> {
        @Override
        public long[] provide(String key) {
            return configuration.getProperty(key, null, long[].class);
        }
    }

}
