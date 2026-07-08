package com.selman.automation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.selman.automation.pages.LoginPage;

public class LoginTest {

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page page = browser.newPage();

            LoginPage loginPage = new LoginPage(page);
            loginPage.navigate();
            loginPage.loginAs("Admin", "admin123");

            System.out.println("URL after login: " + page.url());

            browser.close();
        }
    }
}