Feature: Employee Management
  As an HR admin
  I want to manage employee records
  So that the organization data stays up to date

  Scenario: Add a new employee with random data
    Given the user is logged in as admin
    When the user adds a new employee with random data
    Then the employee's personal details page should display the correct name