# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy build configuration first (Docker cache optimization)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code for all modules
COPY api api
COPY core core
COPY infra infra

# Make gradlew executable and build the executable jar
# Note: gradle.properties is intentionally NOT copied to avoid Windows-specific java.home path
RUN chmod +x gradlew && \
    ./gradlew api:bootJar -x test --no-daemon

# Find and copy the executable jar
RUN find api/build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} app.jar \;

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
