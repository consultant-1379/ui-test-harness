<head>
   <title>UI Test Toolkit - Advanced browser options</title>
</head>

# Advanced browser options

<!-- MACRO{toc} -->
<!-- Adding a comment line for E2C FEM testing -->
## Remote browsers

UI Test Toolkit SDK provides an opportunity to open browsers remotely. This opportunity is based on [Selenium Grid 2](https://code.google.com/p/selenium/wiki/Grid2) technology.

![Cloud tests](images/cloud_tests.png)

This provides a possibility to automate the UI tests. CI servers are not normally integrated with X windows or virtual frame buffer, and rarely
run on Windows - so it’s usually a pain to set up the environment for automated UI tests. Besides, one CI server covers only one OS. So it makes
much more sense to use different browsers, set up on the remote hosts. Any amount of OS’s, browser types and versions.

To create and use an instance of a remote browser, you have to:

1. Define HTTP connection details in a property file available in classpath:

```xml
ui_toolkit.default_grid_ip=<IP or DNS address of the grid>
ui_toolkit.default_grid_port=<grid port, usually 4444>
```

3. Create an instance of the browser:

<!-- MACRO{snippet|id=INSTANTIATE_BROWSERS_WITH_PARAMS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

When the browser is initialized, it’s used in the very same way as the ordinary local `Browser` is. But the results can
be seen on appropriate grid node, not on the machine where you run your test.

----------- | -------------
**NOTE**    | In order to use the particular version of the browser this browser version has to be registered in the grid. Otherwise an exception will be thrown. See [com.ericsson.cifwk.taf.ui](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/ui/package-frame.html) Javadoc for more details.

## When will a remote or local browser be instantiated?

The methods to initialize a remote browser are the same as the methods to initialize a local browser. This is implemented in order to support an easier
test creation - so that all parameters of the browser (type, desired OS, desired browser version) could be defined outside of the test.

The following are the `newBrowser()` method signatures:

* Browser newBrowser()

* Browser newBrowser(BrowserType browserType)

* Browser newBrowser(BrowserType browserType, BrowserOS os)

* Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)

If properties `ui_toolkit.default_grid_ip` and `ui_toolkit.default_grid_port` are found, all the browsers will be remote and will be run via this grid hub.
If they are not found, all the browsers will be run locally.

## Default browser settings

`Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)` is an ultimate signature. If one of the parameters is omitted (other signatures are used), UI will look for the default settings:

* `ui_toolkit.default_browser` - default browser type. Should be set to one of the values from `com.ericsson.cifwk.taf.ui.BrowserType`

* `ui_toolkit.default_OS` - default OS where the browser should be launched. Should be set to one of the values from `com.ericsson.cifwk.taf.ui.BrowserOS`

You can define these settings in a property file or pass via command line when starting UI tests.

* if `newBrowser()` is called, all browser parameters will have the default values.

* if `newBrowser(BrowserType browserType)` is called, browser OS will have the default value.

* if `newBrowser(BrowserType browserType, BrowserOS os)` is called, no parameters will have the default values. Version should always be defined explicitly.

* if `newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)` is called, no parameters will have the default values.

## Custom browser capabilities

Desired Capabilities are key-value pairs that may be passed to the Web browser instance as a part of `BrowserSetup`.

<!-- MACRO{snippet|id=DEFINE_PROXY|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

Selenium browser capabilities are supported. Please refer to [CapabilityType](http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/remote/CapabilityType.html) for full details.

:----------   | :--------------
**IMPORTANT** | Please note that these capabilities are DESIRED - therefore, if they are inapplicable or unsupported, they will not work for you.

It is possible to set a proxy on some browsers programmatically (all except Internet Explorer). For IE this functionality temporarily
changes the system's proxy settings and this may affect the tests run in parallel. This means a high risk of getting incorrect result with local tests, and
simply not acceptable with Grid tests.

Please refer to [Proxy](http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/Proxy.html) for full details

This functionality works in both types of UI test execution: local test execution and on the Selenium Grid.

## How to disable a Proxy?

Use the following capability:

<!-- MACRO{snippet|id=DISABLE_PROXY|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->