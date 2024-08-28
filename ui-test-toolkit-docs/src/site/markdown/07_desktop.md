<head>
   <title>UI Test Toolkit - How to get started with desktop testing</title>
</head>

# How to get started with desktop testing

UI Test Toolkit provides a possibility to use the desktop navigation utility, which works with all windows, not only browsers. Using this utility
you can automate even Microsoft Word document creation. This is a handy tool when your test covers more than just web browsing. It has
limited functionality, but works with any graphical interface.

<!-- MACRO{toc} -->

## 1. Create a test Maven project:

Make sure you have the following artifacts in your POM:

```xml
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-api</artifactId>
</dependency>
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-desktop</artifactId>
</dependency>
```
## 2. Create an instance of desktop navigator:

<!-- MACRO{snippet|id=INIT_DESKTOP_NAVIGATOR|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

The boolean parameter for this method defines whether activated UI component will be highlighted (true) or not (false). Set to "true" if you
will demonstrate or record the test - this will provide an observer a better understanding of what’s currently happening. If you call the
method without this parameter, highlighting will be switched off.

## 3. Find and use the window elements

Desktop navigator has a significant limitation: it works with the currently focused window only (get its reference - `DesktopWindow` - via `DesktopNavigator.getCurrentWindow()`),
and it cannot manage the windows. In all other means it uses the same classes as Web SDK.

![UI_Test_toolkit_Main_Classes](images/ui_test_toolkit_main_classes.png)

Desktop navigation SDK uses the same idea of component mapping, but the only way to map the components here is by providing a file path to
the appropriate image (GIF, JPEG, PNG) of the control you are mapping. This is the default mapping type if you get an instance of `ViewModel` component via `DesktopNavigator`
(like the CSS mapping is default for `ViewModel` component, retrieved from the `BrowserTab`).
Therefore, the two following mappings are identical:

<!-- MACRO{snippet|id=DESKTOP_VM_MAPPING_FULL|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->
<!-- MACRO{snippet|id=DESKTOP_VM_MAPPING_DEFAULT|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

Image paths can be relative to your project (for example, if you keep them in main/resources - which is the best way to go) or absolute.

At the moment you can use only generic view model:

<!-- MACRO{snippet|id=USE_GENERIC_VIEW|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

# Sikuli grid

UI Test Toolkit extends Selenium grid to support Sikuli commands (like searching for components by image, clicking, typing, etc.).

## Sikuli grid use case

Dependencies used:

```xml
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-web</artifactId>
</dependency>
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-desktop</artifactId>
</dependency>
```

Imports in your testware:

```java
    import com.ericsson.cifwk.taf.ui.Browser;
    import com.ericsson.cifwk.taf.ui.BrowserSetup;
    import com.ericsson.cifwk.taf.ui.BrowserTab;
    import com.ericsson.cifwk.taf.ui.BrowserType;
    import com.ericsson.cifwk.taf.ui.DesktopNavigator;
    import com.ericsson.cifwk.taf.ui.UiToolkit;
    import com.ericsson.cifwk.taf.ui.core.UiComponent;
    import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
```

Getting browser with Sikuli capability (you can use standard capabilities you need for web testing)

<!-- MACRO{snippet|id=INIT_SIKULI_BROWSER|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

Driving browser for (regular web testing or) opening VNC client:

<!-- MACRO{snippet|id=OPEN_SIKULI_BROWSER_WINDOW|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

When VNC client is displayed you can shift to Sikuli to proceed UI testing. Sikuli needs images as UI component selectors. Let’s upload
images to remote node Sikuli is executed at:

<!-- MACRO{snippet|id=OPEN_SIKULI_BROWSER_DESKTOP_NAV|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

:------------ | :-------------
**CAUTION**   | Note that `UiToolkit.newDesktopNavigator` is uploading images to Selenium grid when constructed. Make sure to re-use this object for each selenium session.

:--------- | :---------
**NOTE**   | Custom view models are not currently supported in UI Test Desktop Toolkit. Use generic model.

UI Test Desktop Toolkit implements part of UI Test Toolkit API:

<!-- MACRO{snippet|id=DESKTOP_NAV_COMMON_API|file=ui-test-modules/ui-test-desktop/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiDesktopCodeExamples.java} -->

## How to set up Selenium grid with Sikuli capability

Refer to [Selenium grid setup](http://confluence-nam.lmera.ericsson.se/display/TAF/Selenium+Grid+setup) instructions on Confluence. 
Then setup UI Test Toolkit extensions for Selenium grid hub and node(s).

<span style="color:#ba3925;">Hub</span>

1. Download Selenium hub extensions JAR from [Nexus](https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/com/ericsson/cifwk/all-hub-extensions/) and place it in <span style="color:#ba3925;">/opt/selenium_server/extensions</span>

2. Start the hub with extension JAR in class path, specify custom servlet and capability matcher:

```
    nohup java -server -cp /opt/selenium_server/selenium-server-standalone-2.47.1.jar:/opt/
    selenium_server/extensions/* org.openqa.grid.selenium.GridLauncher \
    -role hub \
    -servlets com.ericsson.taf.selenium.hub.proxy.HubRequestsProxyingServlet \
    -capabilityMatcher com.ericsson.taf.selenium.capability.CustomCapabilityMatcher &
```

<span style="color:#ba3925;">Nodes</span>

1.Download Selenium node extensions JAR from [Nexus](https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/com/ericsson/cifwk/all-node-extensions/) and place it in <span style="color:#ba3925;">/opt/selenium_server/extensions</span>

2.Configure node by JSON file <span style="color:#ba3925;">/opt/selenium_server/nodeConfig.json</span>. Define <span style="color:#ba3925;">extension.sikuliCapability</span> and servlets in configuration. Each node supports one concurrent session.

```
    {
      "capabilities": [
        {
          "browserName": "firefox",
          "maxInstances": 1,
          "extension.sikuliCapability": true
        }
      ],
      "configuration": {
        "proxy": "org.openqa.grid.selenium.proxy.DefaultRemoteProxy",
        "maxSession": 1,
        "port": 5555,
        "host": localhost,
        "register": true,
        "registerCycle": 5000,
        "hubPort": 4444,
        "hubHost": localhost,
        "servlets": "com.ericsson.taf.selenium.node.sikuli.SikuliExtensionServlet,com.ericsson.
        taf.selenium.node.upload.FileUploadServlet,com.ericsson.taf.selenium.node.download.
        FileDownloadServlet"
      }
    }
```

3.Set distinct <span style="color:#ba3925;">DISPLAY</span> environment variable for each node running on the same Linux host, e.g. ":0" for first node, <span style="color:#ba3925;">":1"</span> for second, etc.

```
    export DISPLAY=:0
```

4.Start node with extensions and corresponding configuration file.

```
    java -server -cp /opt/selenium_server/selenium-server-standalone-2.47.1.jar:/opt/selenium_
    server/extension/* org.openqa.grid.selenium.GridLauncher \
    -role node \
    -nodeConfig /opt/selenium_server/nodeConfig.json &
```

# Best use of UI Test Toolkit Desktop SDK

<span style="color:#ba3925;">Main guidelines are:</span>

* Keep the image locations in constants or in property files.
* Desktop SDK works quite slowly - so use it only when it’s absolutely necessary
* Use Desktop SDK to work with a Web page element that has a different implementation in different site versions, but keeps the same look. In this case you can write a part of your test with Web SDK, and another part that works with this element - with Desktop SDK.
* As a mapping image use the part of the control that is unlikely to change in future.
* Keep in mind that Desktop SDK currently works only with the currently focused window. If you will try to locate a component that is on the background window, this attempt will fail.
