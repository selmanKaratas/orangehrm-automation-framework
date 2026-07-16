# OrangeHRM Test Automation Framework

[![CI](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/selmanKaratas/orangehrm-automation-framework/actions/workflows/ci.yml)

A portfolio test automation framework for the
[OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/).

The framework combines browser automation, BDD scenarios, API testing,
reporting, continuous integration, and containerized test execution.

## Technology Stack

- Java 21
- Maven
- Playwright 1.49.0
- Cucumber 7
- JUnit 5 and JUnit Platform
- PicoContainer
- REST Assured
- AssertJ
- Owner
- DataFaker
- Allure Report
- GitHub Actions
- Docker

## Test Coverage

The project currently contains 10 automated tests:

### UI and BDD Tests

Seven executable Cucumber test cases covering:

- Successful login with valid credentials
- Invalid username
- Invalid password
- Invalid username and password
- Required-field validation
- Adding an employee with generated test data
- Searching for an existing employee

### API Tests

Three REST Assured tests covering:

- Employee-list endpoint status and response structure
- Employee-list limit parameter
- Rejection of unauthenticated requests

The employee scenarios use API-assisted authentication to avoid repeating
the login flow through the user interface.

## Framework Design

The framework uses the Page Object Model to separate page interactions from
test scenarios.

PicoContainer provides scenario-level dependency injection through a shared
test context. Hooks manage browser setup, cleanup, and screenshots for failed
scenarios.

Owner is used for configuration management, while DataFaker generates dynamic
employee test data.

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

## Run Tests Locally

Run the complete test suite:

```bash
mvn clean test
```

Run the tests in headless mode:

```bash
mvn clean test -Dheadless=true
```

Headless mode runs the browser without displaying its graphical window. It is intended for CI and container environments.

## Test Reports

### Cucumber HTML Report

After test execution, the report is generated at:

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

Screenshots from failed Cucumber scenarios are attached to the report.

## Docker Execution

Build the Docker image:

```bash
docker build -t orangehrm-tests .
```

Run the complete test suite inside a container:

```bash
docker run --rm orangehrm-tests
```

The Docker image uses the official Playwright Java image with the required browser and Linux dependencies.

## Continuous Integration

GitHub Actions runs automatically on:

- Pushes to `main`
- Pull requests targeting `main`
- Manual workflow dispatches

The pipeline configures Java 21, installs Chromium, runs the tests in headless mode, and uploads the Allure and Cucumber reports as artifacts.

Reports are uploaded even when tests fail, making CI failures easier to investigate.

## Configuration

Default settings are located in:

```text
src/test/resources/config.properties
```

Headless mode can be overridden from the command line:

```bash
mvn clean test -Dheadless=true
```

## Current Test Result

The framework has been verified locally and inside Docker:

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Disclaimer

This project is intended for learning and portfolio demonstration purposes. The OrangeHRM public demo is a shared external system, so its data and availability may change.
