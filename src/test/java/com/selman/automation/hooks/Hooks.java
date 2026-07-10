package com.selman.automation.hooks;

import com.selman.automation.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

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
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = testContext.getPage().screenshot();
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        testContext.closeBrowser();
    }
}