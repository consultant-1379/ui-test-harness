package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.selenium.pages.AppListViewModel;
import com.ericsson.cifwk.taf.ui.selenium.pages.LoginViewModel;

public class LoginTestOperator {
    private final Browser browser;
    private final BrowserTab browserTab;
    private AppListViewModel appListView;
    private LoginViewModel loginView;

    public LoginTestOperator(String url) {
        this.browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        this.browserTab = browser.open(url);
        this.loginView = browserTab.getView(LoginViewModel.class);
    }

    public boolean isOnLoginPage() {
        return loginView != null && loginView.isCurrentView();
    }

    public boolean isOnAppListPage() {
        return appListView != null && appListView.isCurrentView();
    }

    public void login(String username, String password) {
        loginView = browserTab.getView(LoginViewModel.class);
        loginView.login(username, password);

        appListView = browserTab.getView(AppListViewModel.class);
        browserTab.waitUntilComponentIsDisplayed(appListView.getSignOutLink(), 5000);
    }

    public void openCEXCitrixApp() {
        if (appListView.isCurrentView()) {
            appListView.clickCEXLink();
        }
    }

    public boolean loginFailed() {
        return loginView.loginFailed();
    }


    public String getHTMLTitle() {
        return browserTab.getTitle();
    }

    public void closeResources() {
        browser.close();
    }

    public boolean isCEXLinkAvailable() {
        return appListView.isCEXLinkAvailable();
    }

    public void logout() {
        if (appListView != null) {
            appListView.clickSignOut();
        }
        UiToolkit.pause(2000);
    }

    protected LoginViewModel getLoginViewModel() {
        return loginView;
    }
}
