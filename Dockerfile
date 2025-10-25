# Multi-stage Dockerfile for Test Automation Framework
FROM openjdk:17-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean compile test-compile

# Runtime stage
FROM openjdk:17-jdk-slim

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Install ChromeDriver
RUN CHROME_VERSION=$(google-chrome --version | awk '{print $3}' | cut -d'.' -f1-3) \
    && CHROMEDRIVER_VERSION=$(curl -s "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${CHROME_VERSION}") \
    && wget -O /tmp/chromedriver.zip "https://chromedriver.storage.googleapis.com/${CHROMEDRIVER_VERSION}/chromedriver_linux64.zip" \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip \
    && chmod +x /usr/local/bin/chromedriver

# Set working directory
WORKDIR /app

# Copy built application
COPY --from=builder /app/target ./target
COPY --from=builder /app/src ./src

# Set environment variables
ENV JAVA_OPTS="-Xmx2g -Xms1g"
ENV HEADLESS=true
ENV BROWSER=chrome

# Expose port for Allure reports
EXPOSE 8080

# Create entrypoint script
RUN echo '#!/bin/bash\n\
set -e\n\
echo "Starting Test Automation Framework..."\n\
echo "Java version: $(java -version)"\n\
echo "Chrome version: $(google-chrome --version)"\n\
echo "ChromeDriver version: $(chromedriver --version)"\n\
\n\
# Run tests\n\
./mvnw test $@\n\
\n\
# Generate Allure report\n\
if [ -d "target/allure-results" ]; then\n\
    echo "Generating Allure report..."\n\
    allure generate target/allure-results -o target/allure-report --clean\n\
    echo "Allure report generated in target/allure-report"\n\
fi\n\
\n\
echo "Test execution completed!"' > /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

# Set entrypoint
ENTRYPOINT ["/app/entrypoint.sh"]

# Default command
CMD ["-Dtest=api.SimpleApiTest"]