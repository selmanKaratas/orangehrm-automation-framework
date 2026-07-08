package com.selman.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.selman.automation.config.ConfigManager;

public class LoginPage {

    private final Page page;

    // Locators — defined once, used everywhere
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameInput = page.locator("input[name='username']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.errorMessage = page.locator(".oxd-alert-content-text");
    }

    public void navigate() {
        page.navigate(ConfigManager.get().baseUrl());
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void loginAs(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }
}