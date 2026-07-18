# OrangeHRM Test Automation Framework

[![CI](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml)

This is a portfolio test automation framework for the
[OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/).

The project combines browser automation, BDD scenarios, API testing, reporting,
continuous integration, and containerized test execution.

## Technology Stack

- **Java 21** — The main programming language of the framework.
- **Maven** — Manages dependencies and the build lifecycle of the project.
- **Playwright 1.49.0** — Automates browser actions and reduces timing problems with its auto-waiting feature.
- **Cucumber 7** — Defines UI scenarios in a readable Given-When-Then format.
- **JUnit 5 and JUnit Platform** — Discover and run both Cucumber and API tests.
- **PicoContainer** — Provides dependency injection and shares test state inside one scenario.
- **REST Assured** — Tests REST endpoints and prepares some test preconditions through API calls.
- **AssertJ** — Provides readable and clear assertions.
- **Owner** — Keeps the test configuration in one place and supports configuration overrides.
- **DataFaker** — Generates dynamic employee data and reduces conflicts on the shared demo site.
- **Allure Report** — Creates detailed test reports and includes screenshots for failed scenarios.
- **GitHub Actions** — Runs the tests automatically after pushes and pull requests.
- **Docker** — Provides an isolated and repeatable Linux environment for test execution.
- **Docker Compose** — Stores the container settings, environment variables, and volume configuration in one file.

## Test Coverage

The project currently contains 10 automated tests.

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
contain test assertions. Assertions are kept in the step-definition and test
layers.

PicoContainer provides scenario-level dependency injection through a shared
test context. This allows related steps to share data while keeping different
scenarios isolated from each other.

Hooks manage browser setup, browser cleanup, and screenshots for failed
scenarios. Owner manages configuration, while DataFaker creates dynamic
employee data.

The project uses a hybrid UI and API approach. API calls are used for fast and
reliable test setup where possible, while user-facing behaviour is checked
through the browser.

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
```

## Prerequisites

For local execution:

- Java 21
- Maven
- Chromium installed through Playwright

For containerized execution:

- Docker Desktop
- Docker Compose

Allure Commandline is also required if the Allure report will be opened
locally.

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

The Docker image uses the official Playwright Java image. This image contains
the browser and Linux dependencies required by Playwright.

### Run with Docker Compose

Run the complete test suite:

```bash
docker compose up --build --abort-on-container-exit --exit-code-from tests
```

Remove the Compose container and network after execution:

```bash
docker compose down
```

Docker Compose runs the browser in headless mode. It also mounts the Allure
results directory from the container to the host machine. Because of this
volume, the test results remain available after the container stops.

## Test Reports

### Cucumber HTML Report

After test execution, the Cucumber report is generated at:

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

Open the Allure report with:

```bash
allure serve target/allure-results
```

Screenshots from failed Cucumber scenarios are attached to the Allure report.

In GitHub Actions, the Allure results and Cucumber report can be downloaded
from the artifacts section of the workflow run. Reports are uploaded even when
the tests fail.

## Continuous Integration

GitHub Actions runs automatically on:

- Pushes to `main`
- Pull requests targeting `main`
- Manual workflow runs

The pipeline configures Java 21, installs Chromium, runs the tests in headless
mode, and uploads the test reports as artifacts.

The reporting step uses an `always` condition. This is useful because the
reports are still uploaded when a test fails and needs investigation.

## Configuration

Default test settings are located in:

```text
src/test/resources/config.properties
```

Configuration values can be changed without editing the test code. For
example, headless mode can be enabled with a Maven system property:

```bash
mvn clean test -Dheadless=true
```

Docker Compose also passes the headless setting to the container.

## Key Design Decisions

- **API-assisted setup** — API calls are used for suitable preconditions because they are faster and less fragile than repeating every setup action through the UI.
- **Page Object Model** — Browser locators and actions are kept away from the test scenarios. This makes the test code easier to maintain when the UI changes.
- **Assertions outside page objects** — Page objects describe what a user can do, while test and step classes decide what should be verified.
- **Scenario-level dependency injection** — PicoContainer shares state between steps in the same scenario without using global test data.
- **Dynamic test data** — DataFaker creates different employee data for each execution and lowers the risk of conflicts on the shared demo system.
- **Stable assertions** — Tests verify important business results instead of depending on unstable details such as record order or fixed data.
- **Selective BDD usage** — Cucumber is used for user-facing workflows. Direct API checks use JUnit because Given-When-Then scenarios would add little value there.
- **Screenshot on failure** — Failed UI scenarios include a screenshot to make debugging easier.
- **Configuration overrides** — Environment-specific values can be changed without modifying or rebuilding the test code.
- **Containerized execution** — Docker keeps the browser, Java, and Linux dependencies consistent across different machines.

## Load Testing

A small k6 script is included in:

```text
load-test/login-load.js
```

Run it with:

```bash
k6 run load-test/login-load.js
```

The script sends HTTP requests to the OrangeHRM login page with five virtual
users for 30 seconds. It checks the HTTP status and response time.

This test is only a small technical example. It is not intended to create
heavy traffic on the public demo application.

## Current Test Result

The framework has been verified locally and with Docker Compose:

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The Allure results were also saved successfully on the host machine through a
Docker bind mount.

## Known Limitations

- The OrangeHRM demo application is a shared external system. Its test data, response time, and availability are outside the control of this project.
- Other users can create or remove data while the tests are running.
- Network latency may sometimes affect UI execution. Playwright auto-waiting and reasonable timeouts are used to reduce this risk.
- The framework currently focuses on Chromium and has not been fully tested with all supported Playwright browsers.
- The included k6 script uses only five virtual users because the target is a public demo site.
- A real load test should only be performed against an approved and isolated test environment.
