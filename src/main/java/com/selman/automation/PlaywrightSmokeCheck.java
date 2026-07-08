package com.selman.automation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

public class PlaywrightSmokeCheck {

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();
            page.navigate("https://opensource-demo.orangehrmlive.com");

            System.out.println("Page title: " + page.title());

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/first-run.png")));

            browser.close();
        }
    }
}