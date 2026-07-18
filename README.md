# OrangeHRM Test Automation Framework

[![CI](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml)

This is a portfolio test automation framework for the
[OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/).

The project combines browser automation, BDD scenarios, API testing, reporting,
continuous integration, containerized execution, and a small load test example.

## Technology Stack

- **Java 21** — The main programming language of the framework.
- **Maven** — Manages project dependencies and the build lifecycle.
- **Playwright 1.49.0** — Automates browser actions and reduces timing problems with auto-waiting.
- **Cucumber 7** — Defines UI scenarios in a readable Given-When-Then format.
- **JUnit 5 and JUnit Platform** — Discover and run both Cucumber and API tests.
- **PicoContainer** — Provides dependency injection and shares state inside one scenario.
- **REST Assured** — Tests REST endpoints and prepares suitable test preconditions through API calls.
- **AssertJ** — Provides clear and readable assertions.
- **Owner** — Keeps configuration in one place and supports values from different sources.
- **DataFaker** — Generates dynamic employee data and reduces conflicts on the shared demo site.
- **Allure Report** — Creates detailed test results and includes screenshots from failed UI scenarios.
- **GitHub Actions** — Runs the test suite automatically after pushes and pull requests.
- **Docker** — Provides an isolated and repeatable Linux environment.
- **Docker Compose** — Keeps the container command, environment settings, and volume configuration in one file.
- **k6** — Creates lightweight HTTP-level load for a small performance test example.

## Test Coverage

The project currently includes UI, BDD, and API tests.

### UI and BDD Tests

Seven executable Cucumber test cases cover:

- Successful login with valid credentials
- Invalid username
- Invalid password
- Invalid username and password
- Required-field validation
- Adding an employee with generated test data
- Searching for an existing employee

### API Tests

Three REST Assured tests cover:

- Employee-list endpoint status and response structure
- Employee-list limit parameter
- Rejection of unauthenticated requests

The employee scenarios use API-assisted authentication to avoid repeating the
login flow through the user interface.

## Framework Design

The framework uses the Page Object Model to separate browser interactions from
test scenarios. Page objects contain locators and user actions, but they do not
contain test assertions. Assertions remain in the step-definition and test
layers.

PicoContainer provides scenario-level dependency injection through a shared
test context. Related steps can share data inside the same scenario, while
different scenarios remain isolated from each other.

Hooks manage browser setup, cleanup, and screenshots for failed scenarios.
Owner manages configuration, while DataFaker creates dynamic employee data.

The project uses a hybrid UI and API approach. API calls prepare suitable
preconditions quickly, while user-facing behaviour is verified through the
browser.

## Project Structure

```text
src
├── main
│   └── java/com/selman/automation
│       ├── config
│       ├── models
│       └── pages
└── test
    ├── java/com/selman/automation
    │   ├── api
    │   ├── context
    │   ├── hooks
    │   └── steps
    └── resources
        ├── features
        ├── allure.properties
        ├── config.properties
        └── junit-platform.properties

load-test
└── login-load.js
```

## Prerequisites

For local test execution:

- Java 21
- Maven
- Playwright Chromium browser

For containerized execution:

- Docker Desktop
- Docker Compose

For viewing Allure reports locally:

- Allure Commandline

For running the load test:

- k6

## Run Tests Locally

Run the complete test suite:

```bash
mvn clean test
```

Run the tests in headless mode:

```bash
mvn clean test -Dheadless=true
```

Headless mode runs the browser without opening a graphical window. It is mainly
used in CI and container environments.

## Docker Execution

Build the Docker image:

```bash
docker build -t orangehrm-tests .
```

Run the test suite inside a container:

```bash
docker run --rm orangehrm-tests
```

The Docker image uses the official Playwright Java image. It includes the
browser and Linux dependencies required by Playwright.

### Run with Docker Compose

Run the complete test suite:

```bash
docker compose up --build --abort-on-container-exit --exit-code-from tests
```

Remove the Compose container and network after execution:

```bash
docker compose down
```

Docker Compose runs the tests in headless mode and mounts the Allure results
directory to the host machine. The results remain available after the
container stops.

## Test Reports

### Cucumber HTML Report

After test execution, the Cucumber HTML report is generated at:

```text
target/cucumber-report.html
```

Open it on macOS:

```bash
open target/cucumber-report.html
```

### Allure Report

Allure results are generated at:

```text
target/allure-results
```

Open the report locally with:

```bash
allure serve target/allure-results
```

Screenshots from failed Cucumber scenarios are attached to the Allure results.

In GitHub Actions, the Allure results and Cucumber report can be downloaded
from the artifacts section of each workflow run. The reports are uploaded even
when tests fail.

## Continuous Integration

GitHub Actions runs automatically on:

- Pushes to `main`
- Pull requests targeting `main`
- Manual workflow runs

The pipeline configures Java 21, installs Chromium, runs the tests in headless
mode, and uploads the reports as workflow artifacts.

The report upload step uses an `always` condition. This keeps the reports
available when a failed test needs investigation.

## Configuration

Default settings are located in:

```text
src/test/resources/config.properties
```

Values are resolved in this order: Java system properties, environment
variables, and then `config.properties`. Owner uses the `MERGE` load policy,
so higher-priority sources can override default values without changing the
test code.

For example, headless mode can be enabled with a Maven system property:

```bash
mvn clean test -Dheadless=true
```

Docker Compose also passes the headless setting to the test container.

## Key Design Decisions

- **API-assisted setup** — Preconditions such as authentication are prepared through API calls. In local measurements, a pure API test completed in around 0.2–0.5 seconds, while a UI journey took 13 seconds or more. This approach keeps the suite faster and less fragile.
- **Page Object Model** — Browser locators and actions are kept separate from test scenarios. This makes maintenance easier when the UI changes.
- **Assertions outside page objects** — Page objects describe available user actions, while step and test classes decide what must be verified.
- **Scenario-level dependency injection** — PicoContainer shares state between steps in the same scenario without using global test data.
- **Dynamic test data** — DataFaker creates different employee data for each execution and lowers the risk of conflicts on the shared demo system.
- **Stable assertions** — Tests check important business results instead of depending on unstable details such as record order or fixed shared data.
- **Selective BDD usage** — Cucumber is used for user-facing workflows. Direct API checks use JUnit because Given-When-Then scenarios would add little value there.
- **Screenshot on failure** — Failed UI scenarios include a screenshot to make investigation easier.
- **Configuration overrides** — Environment-specific values can be changed without modifying or rebuilding the test code.
- **Containerized execution** — Docker keeps Java, browser, and Linux dependencies consistent across different machines.

## Load Testing

A small HTTP-level k6 test is included at:

```text
load-test/login-load.js
```

Run it with:

```bash
k6 run load-test/login-load.js
```

The script uses five virtual users for 30 seconds. It sends requests to the
OrangeHRM login page and checks that:

- The response status is `200`
- The response time is below two seconds

This is a small technical example, not a full performance test. It uses HTTP
requests instead of browser sessions, so it can create concurrent traffic with
fewer local resources than Playwright.

## CI Status

The current status of the test suite is shown by the CI badge at the top of
this page. Allure results and the Cucumber HTML report for each CI run are
available as GitHub Actions artifacts.

## Future Improvements

- Add structured logging with Log4j2 instead of using the current no-operation SLF4J logger.
- Add controlled parallel execution to reduce the total suite duration.
- Add a cross-browser CI matrix for Chromium, Firefox, and WebKit.

## Known Limitations

- The OrangeHRM demo application is a shared external system. Its test data, response time, and availability are outside the control of this project.
- Other users can create or remove data while the tests are running.
- Network latency may sometimes affect UI execution. Playwright auto-waiting and reasonable timeouts are used to reduce this risk.
- API authentication requires extracting a CSRF token from the login page before sending the login request.
- The framework currently focuses on Chromium and has not been fully tested with all Playwright browsers.
- The included k6 script uses only five virtual users because the target is a public demo site.
- A real load test should only be performed against an approved and isolated test environment.
