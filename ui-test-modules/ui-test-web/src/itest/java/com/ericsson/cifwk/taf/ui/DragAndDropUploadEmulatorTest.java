package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.selenium.AbstractEmbeddedJettyITest;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;

@Ignore("Temporarily ignored, PhantomJS doesn't wait for JQuery to load, and fails the test. Fix or use grid browsers")
public class DragAndDropUploadEmulatorTest extends AbstractEmbeddedJettyITest {
    private EmbeddedJetty jetty;
    private Browser browser;
    private BrowserTab browserTab;
    private WebDriver webDriver;
    private static final String FILE_TO_UPLOAD = "upload_image.png";
    private static final String PATH_TO_FILE_TO_UPLOAD = "html_pages/" + FILE_TO_UPLOAD;

    @Before
    public void setUp() {
        jetty = EmbeddedJetty.build()
                .withResourceBase("./target/itest-classes/html_pages")
                .withServlet(getUploadServletInstance(), "/upload/*")
                .start();
    }

    private AjaxFileUploadServlet getUploadServletInstance() {
        return new AjaxFileUploadServlet();
    }

    private BrowserType getBrowserType() {
        return BrowserType.PHANTOMJS;
    }

    @Test
    public void uploadLocalFile() throws IOException {
        this.browser = UiToolkit.newBrowser(getBrowserType());
        String testPage = findHtmlPage("drag_and_drop_upload.htm");
        this.browserTab = browser.open(testPage);

        String filePath = Resources.getResource(PATH_TO_FILE_TO_UPLOAD).getFile();
        File uploadableFile = new File(filePath);
        ViewModel view = browserTab.getGenericView();
        UiComponent dropTarget = view.getViewComponent("#holder");

        browserTab.dragAndDropForUpload(uploadableFile, dropTarget);

        verifyUploadedFile(FILE_TO_UPLOAD, view.getViewComponent("#responseBox").getText());
    }

    @Test
    public void uploadFileFromInputStream() throws IOException {
        String htmlPage = findHtmlPage(FILE_TO_UPLOAD);
        URL url = new URL(htmlPage);
        InputStream inputStream = url.openStream();

        this.browser = UiToolkit.newBrowser(getBrowserType());
        String testPage = findHtmlPage("drag_and_drop_upload.htm");
        this.browserTab = browser.open(testPage);

        ViewModel view = browserTab.getGenericView();
        UiComponent dropTarget = view.getViewComponent("#holder");

        String targetFileName = String.format("my.%d.png", System.currentTimeMillis());
        browserTab.dragAndDropForUpload(inputStream, dropTarget, targetFileName);

        verifyUploadedFile(targetFileName, view.getViewComponent("#responseBox").getText());
    }

    @Test
    public void uploadFileViaSelenium() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, System.getProperty("taf_ui.phantom_js.executable_path"));
        webDriver = new PhantomJSDriver(DesiredCapabilities.phantomjs().merge(caps));
        webDriver.get(findHtmlPage("drag_and_drop_upload.htm"));

        String dropBoxId = "holder";
        String inputId = dropBoxId + "FileUpload";

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;

        if (!isJQuerySupported()) {
            addJQuerySupport();
        }

        // append input to HTML to add file path
        javascriptExecutor.executeScript(inputId + " = $('<input id=\"" + inputId + "\"/>').attr({type:'file'}).appendTo('body');");
        String filePath = Resources.getResource(PATH_TO_FILE_TO_UPLOAD).getFile();
        File uploadableFile = new File(filePath);
        Assert.assertTrue(uploadableFile.exists());
        webDriver.findElement(By.id(inputId)).sendKeys(uploadableFile.getAbsolutePath());

        // fire mock event pointing to inserted file path
        javascriptExecutor.executeScript("e = $.Event('drop'); e.originalEvent = {dataTransfer : { files : " + inputId + ".get(0).files } }; $('#" + dropBoxId + "').trigger(e);");

        verifyUploadedFile(FILE_TO_UPLOAD, webDriver.findElement(By.id("responseBox")).getText());

        webDriver.quit();
    }

    private void verifyUploadedFile(String fileName, String uploadedFileLocation) throws IOException {
        int dotIdx = fileName.indexOf(".");
        String name = (dotIdx == -1) ? fileName : fileName.substring(0, dotIdx);

        Assert.assertThat(uploadedFileLocation, containsString(name));
        File uploadedFile = new File(uploadedFileLocation);
        Assert.assertTrue(uploadedFile.exists());

        Path path = uploadedFile.toPath();
        Assert.assertEquals("image/png", Files.probeContentType(path));

        Files.delete(path);
    }

    private void addJQuerySupport() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("var jq = document.createElement('script');jq.src = '//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js';document.getElementsByTagName('head')[0].appendChild(jq);");
        try {
            // small delay needed for jQuery injection to kick in
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isJQuerySupported() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        Object result = javascriptExecutor.executeScript("return typeof jQuery == 'undefined'");
        return !(Boolean) result;
    }

    protected String findHtmlPage(String htmlPage) {
        return "http://localhost:" + jetty.getPort() + "/" + htmlPage;
    }

    @After
    public void tearDown() throws Exception {
        if (browser != null) {
            browser.close();
        }
        jetty.stop();
    }

}
