package com.selman.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.selman.automation.models.Employee;

public class PimPage {
    private final Page page;
    private final Locator firstNameInput;
    private final Locator middleNameInput;
    private final Locator lastNameInput;
    private final Locator saveButton;
    private final Locator employeeHeaderName;
    private final Locator addButton;

    public PimPage(Page page) {
        this.page = page;

        this.firstNameInput = page.getByPlaceholder("First Name");
        this.middleNameInput = page.getByPlaceholder("Middle Name");
        this.lastNameInput = page.getByPlaceholder("Last Name");

        this.saveButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save").setExact(true)
        );

        this.addButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Add"));


        this.employeeHeaderName = page.locator(".orangehrm-edit-employee-name h6");
    }

    public void clickAddEmployee() {
        addButton.click();
    }

    public void createEmployee(Employee employee) {
        firstNameInput.fill(employee.firstName());
        middleNameInput.fill(employee.middleName());
        lastNameInput.fill(employee.lastName());
        saveButton.click();
    }


    public String getDisplayedEmployeeName() {
        return employeeHeaderName.innerText();
    }
}