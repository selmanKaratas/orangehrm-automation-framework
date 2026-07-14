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
import com.selman.automation.pages.EmployeeListPage;
import com.selman.automation.api.ApiClient;

import java.util.List;

public class EmployeeSteps {

    private final TestContext context;
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;
    private final PimPage pimPage;
    private Employee employee;
    private final EmployeeListPage employeeListPage;

    public EmployeeSteps(TestContext context) {
        this.context = context;
        this.loginPage = new LoginPage(context.getPage());
        this.dashboardPage = new DashboardPage(context.getPage());
        this.pimPage = new PimPage(context.getPage());
        this.employeeListPage = new EmployeeListPage(context.getPage());
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

    @Then("the employee should appear in the search results")
    public void theEmployeeShouldAppearInTheSearchResults() {
        List<String> displayedNames =
                employeeListPage.getDisplayedNames();

        Assertions.assertThat(displayedNames)
                .as(
                        "Search results should contain employee '%s'. Actual names: %s",
                        employee.firstName(),
                        displayedNames
                )
                .anyMatch(displayedName ->
                        displayedName.contains(employee.firstName())
                );
    }

    @Given("an employee with random data exists")
    public void anEmployeeWithRandomDataExists() {
        dashboardPage.goToPim();
        pimPage.clickAddEmployee();

        this.employee = Employee.random();
        pimPage.createEmployee(employee);

        dashboardPage.goToPim();
    }

    @When("the user searches for that employee by name")
    public void theUserSearchesForThatEmployeeByName() {
        Assertions.assertThat(employee)
                .as("An employee must exist before searching")
                .isNotNull();

        employeeListPage.searchByName(employee.firstName());
    }


    @Given("the user is logged in as admin via API")
    public void theUserIsLoggedInAsAdminViaApi() {
        String session = new ApiClient().loginAndGetSessionCookie(
                ConfigManager.get().defaultUsername(),
                ConfigManager.get().defaultPassword()
        );
        context.startAuthenticatedSession(session);
        context.getPage().navigate(ConfigManager.get().baseUrl() + "/web/index.php/dashboard/index");
    }


}