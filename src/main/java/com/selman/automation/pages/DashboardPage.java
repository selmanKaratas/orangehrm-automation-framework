
package com.selman.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashboardPage {

    private final Locator pimMenuLink;

    public DashboardPage(Page page) {
        this.pimMenuLink = page.locator("a[href*='pim/viewPimModule']");
    }

    public void goToPim() {
        pimMenuLink.click();
    }
}