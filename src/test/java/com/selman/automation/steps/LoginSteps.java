package com.selman.automation.steps;

import com.selman.automation.config.ConfigManager;
import com.selman.automation.context.TestContext;
import com.selman.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final TestContext testContext;
    private LoginPage loginPage;

    public LoginSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage = new LoginPage(testContext.getPage());
        loginPage.navigate();
    }

    @When("the user logs in with valid credentials")
    public void theUserLogsInWithValidCredentials() {
        loginPage.loginAs(
                ConfigManager.get().defaultUsername(),
                ConfigManager.get().defaultPassword()
        );
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWith(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Then("the user should see the dashboard")
    public void theUserShouldSeeTheDashboard() {
        assertThat(testContext.getPage().url()).contains("/dashboard");
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedMessage) {
        assertThat(loginPage.getErrorMessage()).isEqualTo(expectedMessage);
    }

    @Then("a required field error should be displayed")
    public void aRequiredFieldErrorShouldBeDisplayed() {
        assertThat(loginPage.getRequiredFieldError())
                .isEqualTo("Required");
    }




}