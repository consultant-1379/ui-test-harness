package com.ericsson.cifwk.taf.ui.spi;

import static com.ericsson.cifwk.taf.ui.DefaultSettings.UI_RETRY_SCHEMA_PROPERTY;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 24.04.2017
 */
public class TestPropertiesKeyMapper implements PropertiesKeyMapper {

    public static final String TAF_KEY = "taf.key";
    public static final String TAF_LONG_KEY = "taf.long.key";
    public static final String KEY = "key";
    public static final String LONG_KEY = "long.key";

    // real keys to be used in mapper in TAF adapter
    public static final String TAF_UI_DEFAULT_GRID_IP_PROPERTY = "taf_ui.default_grid_ip";
    public static final String TAF_UI_DEFAULT_GRID_PORT_PROPERTY = "taf_ui.default_grid_port";
    public static final String TAF_UI_DEFAULT_BROWSER_PROPERTY = "taf_ui.default_browser";
    public static final String TAF_UI_DEFAULT_OS_PROPERTY = "taf_ui.default_OS";

    /**
     * Retry Schema - how many times and how long retry UI operation.
     * Please look at next setting to see what cases can be handled and retried.
     * <p>
     * Default value is '100,200,500,1000,2000,4000' in millis.
     */
    public static final String TAF_UI_RETRY_SCHEMA_PROPERTY = "taf_ui.implicit_wait.retry_schema";

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
    public static final String TAF_UI_RETRY_SCHEMA_TIMEOUT_PROPERTY = "taf_ui.implicit_wait.total_timeout_millis";

    private static Map<String, String> keysMapping = new HashMap<>();
    static {
        keysMapping.put(KEY, TAF_KEY);
        keysMapping.put(LONG_KEY, TAF_LONG_KEY);
        keysMapping.put(UI_RETRY_SCHEMA_PROPERTY, TAF_UI_RETRY_SCHEMA_PROPERTY);
    }

    @Override
    public String map(String propertyKey) {
        return keysMapping.get(propertyKey);
    }

}
