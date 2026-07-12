package com.selman.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class EmployeeListPage {

    private final Page page;
    private final Locator employeeNameInput;
    private final Locator searchButton;
    private final Locator displayedNameCells;

    public EmployeeListPage(Page page) {
        this.page = page;

        Locator employeeNameGroup = page.locator(".oxd-input-group")
                .filter(
                        new Locator.FilterOptions()
                                .setHasText("Employee Name")
                );

        this.employeeNameInput = employeeNameGroup.locator("input");

        this.searchButton = page.locator(".oxd-table-filter")
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Search")
                                .setExact(true)
                );

        this.displayedNameCells = page.locator(
                ".oxd-table-card .oxd-table-cell:nth-child(3)"
        );
    }

    public void searchByName(String name) {
        employeeNameInput.fill(name);

        Locator suggestion = page.locator(
                        ".oxd-autocomplete-dropdown [role='option']"
                )
                .filter(
                        new Locator.FilterOptions()
                                .setHasText(name)
                )
                .first();

        suggestion.waitFor();
        suggestion.click();

        page.waitForResponse(
                response ->
                        response.url().contains("/api/v2/pim/employees")
                                && response.request().method().equals("GET"),
                searchButton::click
        );
    }

    public List<String> getDisplayedNames() {
        return displayedNameCells.allInnerTexts();
    }
}