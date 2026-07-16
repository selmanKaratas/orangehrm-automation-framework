# Playwright Java image with browsers and Linux dependencies
FROM mcr.microsoft.com/playwright/java:v1.49.0-noble

WORKDIR /app

# Copy pom.xml first to use Docker layer caching
COPY pom.xml .

# Download Maven dependencies
RUN mvn dependency:go-offline -B

# Copy the test source code
COPY src ./src

# Run tests in headless mode
CMD ["mvn", "clean", "test", "-Dheadless=true"]
