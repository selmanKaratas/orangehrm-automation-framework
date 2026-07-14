Feature: Employee Management
  As an HR admin
  I want to manage employee records
  So that the organization data stays up to date

  Scenario: Add a new employee with random data
    Given the user is logged in as admin via API
    When the user adds a new employee with random data
    Then the employee's personal details page should display the correct name


  Scenario: Search for an existing employee by name
    Given the user is logged in as admin via API
    And an employee with random data exists
    When the user searches for that employee by name
    Then the employee should appear in the search results