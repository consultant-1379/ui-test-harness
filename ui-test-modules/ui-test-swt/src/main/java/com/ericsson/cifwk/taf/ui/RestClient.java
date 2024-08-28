/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

class RestClient {

    public static final String AGENT_PATH = "/swt-agent/";

    public static final String VIEWS_PATH = AGENT_PATH + "windows/";

    public static final String WIDGETS_PATH = AGENT_PATH + "widgets/";

    private static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Class.class, new ClassAdapter());
        gson = gsonBuilder.create();
    }

    private HttpClient httpClient = HttpClientBuilder.create().build();

    private String host;

    private int port;

    public RestClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String invoke(String widgetId, SwtMethodInvocation swtMethodInvocation) throws IOException {
        String jsonString = gson.toJson(swtMethodInvocation);
        HttpPost request = new HttpPost(WIDGETS_PATH + URLEncoder.encode(widgetId, "UTF-8"));
        request.setEntity(new StringEntity(jsonString));
        HttpResponse httpResponse = httpClient.execute(new HttpHost(host, port), request);
        int statusCode = getStatusCode(httpResponse);
        if (statusCode == 404) {
            String message = String.format("Widget %s is not available at %s is not available", widgetId, getUrl(request));
            throw new RuntimeException(message); // NOSONAR
        } else if (statusCode != 200) {
            String messageFromServer = EntityUtils.toString(httpResponse.getEntity());
            String message = String.format("Widget %s invocation failed (expected 200, but was %s): %s", widgetId, statusCode, messageFromServer);
            throw new RuntimeException(message); // NOSONAR
        }
        return EntityUtils.toString(httpResponse.getEntity());
    }

    @SuppressWarnings("unchecked")
    public List<String> getViews() throws IOException {
        HttpGet request = new HttpGet(VIEWS_PATH);
        HttpResponse httpResponse = httpClient.execute(new HttpHost(host, port), request);
        int statusCode = getStatusCode(httpResponse);
        if (statusCode != 200) {
            String message = String.format("Windows list at %s is not available (expected 200, but was %s)", getUrl(request), statusCode);
            throw new RuntimeException(message); // NOSONAR
        }

        String json = EntityUtils.toString(httpResponse.getEntity());
        return gson.fromJson(json, List.class);
    }

    public void reset() throws IOException {
        HttpDelete request = new HttpDelete(WIDGETS_PATH);
        HttpResponse httpResponse = httpClient.execute(new HttpHost(host, port), request);
        int statusCode = getStatusCode(httpResponse);
        if (statusCode != 200) {
            String message = String.format("Widgets reset operation failed (expected 200, but was %s)", statusCode);
            throw new RuntimeException(message); // NOSONAR
        }
    }

    public byte[] getAsBytes(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(new HttpHost(host, port), request);
        int statusCode = getStatusCode(httpResponse);
        if (statusCode != 200) {
            String message = String.format("Page at %s is not available (expected 200, but was %s)", getUrl(request), statusCode);
            throw new RuntimeException(message); // NOSONAR
        }

        return EntityUtils.toByteArray(httpResponse.getEntity());
    }

    private int getStatusCode(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode();
    }

    private String getUrl(HttpRequestBase request) {
        return String.format("(%s) http://%s:%s%s", request.getMethod(), host, port, request.getURI());
    }

}
