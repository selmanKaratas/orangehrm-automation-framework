package com.selman.automation.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.selman.automation.config.ConfigManager;

public class TestContext {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    public void startBrowser() {
        playwright = Playwright.create();
        browser = switch (ConfigManager.get().browser()) {
            case "firefox" -> playwright.firefox().launch(launchOptions());
            case "webkit" -> playwright.webkit().launch(launchOptions());
            default -> playwright.chromium().launch(launchOptions());
        };
        browserContext = browser.newContext();
        page = browserContext.newPage();
        page.setDefaultTimeout(ConfigManager.get().timeoutMs());
    }

    private com.microsoft.playwright.BrowserType.LaunchOptions launchOptions() {
        return new com.microsoft.playwright.BrowserType.LaunchOptions()
                .setHeadless(ConfigManager.get().headless());
    }

    public void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    public Page getPage() {
        return page;
    }
}