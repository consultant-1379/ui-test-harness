package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static java.lang.String.format;

public class SwtUiNavigator {

    private static final String WINDOW_PREFIX = "window:";

    private static final String SHELL_PREFIX = "shell-for-window:";

    private RestClient restClient;

    private WidgetProxyFactory proxyFactory;

    public SwtUiNavigator(String host, int port, String startupScriptPath, String configPath,
                          String display, Long timeout) {
        OsgiHost osgiHost = startContainerOnDemand(host, port, startupScriptPath, configPath, display, timeout);
        this.restClient = new RestClient(osgiHost.getHostname(), osgiHost.getPort());
        this.proxyFactory = new WidgetProxyFactory(new RemoteInvoker(restClient));
    }

    public SwtUiNavigator(String host, int port) {
        this.restClient = new RestClient(host, port);
        this.proxyFactory = new WidgetProxyFactory(new RemoteInvoker(restClient));
    }

    protected OsgiHost startContainerOnDemand(String host, int port, String startUpScriptPath, String configPath, String display, Long timeout) {
        return new OsgiHost(host, port);
    }

    public ViewModel getView(String windowName) {
        return createProxy(ViewModel.class, WINDOW_PREFIX + windowName);
    }

    public UiComponent getShell(String windowName) {
        return createProxy(UiComponent.class, SHELL_PREFIX + windowName);
    }

    public List<String> getViews() {
        try {
            return restClient.getViews();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public <T> T as(Object widget, Class<T> clazz) {
        return proxyFactory.castWidgetTo(widget, clazz);
    }

    public void reset() {
        try {
            proxyFactory.reset();
            restClient.reset();
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    public <T> T createProxy(Class<T> clazz, String widgetId) {
        return proxyFactory.create(clazz, widgetId);
    }

    public byte[] getScreenshot(String windowName) {
        try {
            String windowScreenshotUrl = format("%s%s.png", RestClient.VIEWS_PATH, URLEncoder.encode(windowName, "utf-8"));
            return restClient.getAsBytes(windowScreenshotUrl);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    void setProxyFactory(WidgetProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    static class OsgiHost {

        private String hostname;

        private int port;

        OsgiHost(String hostname, int port) {
            this.port = port;
            this.hostname = hostname;
        }

        String getHostname() {
            return hostname;
        }

        int getPort() {
            return port;
        }
    }

}
