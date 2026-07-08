package com.selman.automation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.selman.automation.config.ConfigManager;
import com.selman.automation.pages.LoginPage;

public class LoginTest {



    public static void main(String[] args) {

        System.out.println("Config check → baseUrl: " + com.selman.automation.config.ConfigManager.get().baseUrl());


        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(ConfigManager.get().headless())
            );
            Page page = browser.newPage();
            page.setDefaultTimeout(ConfigManager.get().timeoutMs());


            LoginPage loginPage = new LoginPage(page);
            loginPage.navigate();
            loginPage.loginAs(

                    ConfigManager.get().defaultUsername(),
                    ConfigManager.get().defaultPassword()
            );

            browser.close();
        }
    }
}