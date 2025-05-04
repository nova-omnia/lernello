# Use Oracle JDK 23 base image
FROM ghcr.io/graalvm/oraclelinux:23.0.2

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and settings
COPY gradlew settings.gradle.kts gradle/ ./

# Copy only the backend project (contains build.gradle etc.)
COPY backend ./backend

# Give gradlew execute permission
RUN chmod +x ./gradlew

# Build the backend project
RUN ./gradlew -p backend build --no-daemon

# Use a lightweight base image for runtime
FROM eclipse-temurin:23-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=0 /app/backend/build/libs/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
