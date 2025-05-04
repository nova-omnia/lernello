# Use Oracle JDK 23.0.2 as the build environment
FROM openjdk:23-jdk-oracle AS builder

# Install xargs (part of findutils) so gradlew will run
RUN microdnf install -y findutils \
    && microdnf clean all

WORKDIR /app

# Copy Gradle wrapper and its files
COPY gradlew .
COPY gradle/ gradle/

# Copy only the Spring Boot project
COPY backend/ backend/

# Make the wrapper executable
RUN chmod +x gradlew

# Build the application (skip tests to speed up; remove -x test if you want them)
RUN ./gradlew  :backend:build

# -----

# Use a fresh Oracle JDK image for runtime
FROM openjdk:23-jdk-oracle

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/backend/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
