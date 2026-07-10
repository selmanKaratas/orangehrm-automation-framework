package com.selman.automation.hooks;

import com.selman.automation.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void beforeScenario() {
        testContext.startBrowser();
    }

    @After
    public void afterScenario() {
        testContext.closeBrowser();
    }
}