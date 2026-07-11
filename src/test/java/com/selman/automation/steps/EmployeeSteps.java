package com.selman.automation.steps;

import com.selman.automation.config.ConfigManager;
import com.selman.automation.context.TestContext;
import com.selman.automation.models.Employee;
import com.selman.automation.pages.DashboardPage;
import com.selman.automation.pages.LoginPage;
import com.selman.automation.pages.PimPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

public class EmployeeSteps {

    private final TestContext context;
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;
    private final PimPage pimPage;
    private Employee employee;

    public EmployeeSteps(TestContext context) {
        this.context = context;
        this.loginPage = new LoginPage(context.getPage());
        this.dashboardPage = new DashboardPage(context.getPage());
        this.pimPage = new PimPage(context.getPage());
    }

    @Given("the user is logged in as admin")
    public void theUserIsLoggedInAsAdmin() {
        loginPage.navigate();
        loginPage.loginAs(
                ConfigManager.get().defaultUsername(),
                ConfigManager.get().defaultPassword()
        );
    }


    @When("the user adds a new employee with random data")
    public void theUserAddsANewEmployeeWithRandomData() {
        dashboardPage.goToPim();
        pimPage.clickAddEmployee();

        this.employee = Employee.random();

        pimPage.createEmployee(employee);
    }

    @Then("the employee's personal details page should display the correct name")
    public void theEmployeeSPersonalDetailsPageShouldDisplayTheCorrectName() {
        String expectedFullName = employee.fullName();
        String actualHeaderName = pimPage.getDisplayedEmployeeName();

        Assertions.assertThat(actualHeaderName)
                .as("The profile header name should match the expected full name")
                .isEqualTo(expectedFullName);
    }
}