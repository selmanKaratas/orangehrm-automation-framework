Feature: Login
  As a registered user
  I want to log in to OrangeHRM
  So that I can access my dashboard

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with valid credentials
    Then the user should see the dashboard

  Scenario Outline: Login fails with invalid credentials
    Given the user is on the login page
    When the user logs in with username "<username>" and password "<password>"
    Then an error message "<errorMessage>" should be displayed

    Examples:
      | username  | password  | errorMessage        |
      | WrongUser | admin123  | Invalid credentials |
      | Admin     | wrongpass | Invalid credentials |
      | WrongUser | wrongpass | Invalid credentials |

  Scenario: Login with empty username shows required field error
    Given the user is on the login page
    When the user logs in with username "" and password "admin123"
    Then a required field error should be displayed