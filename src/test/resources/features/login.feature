Feature: Login
  As a registered user
  I want to log in to OrangeHRM
  So that I can access my dashboard

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with valid credentials
    Then the user should see the dashboard

  Scenario: Login fails with invalid credentials
    Given the user is on the login page
    When the user logs in with username "WrongUser" and password "wrongpass"
    Then an error message "Invalid credentials" should be displayed