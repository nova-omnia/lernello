# Use Oracle JDK 23.0.2 base image
FROM container-registry.oracle.com/java/oraclelinux:23-slim

# Install necessary packages
RUN microdnf install -y unzip tar gzip git && \
    microdnf clean all

# Set working directory inside container
WORKDIR /app

# Copy gradle wrapper and config
COPY gradlew .
COPY gradle gradle
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle

# Copy only the backend folder (the Spring Boot project)
COPY backend/ backend/

# Give execution permissions to gradlew
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew :backend:bootJar --no-daemon

# Use a minimal image for running the app
FROM container-registry.oracle.com/java/oraclelinux:23-slim

WORKDIR /app

# Copy the built JAR from the previous build stage
COPY --from=0 /app/backend/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
