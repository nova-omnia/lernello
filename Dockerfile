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
RUN ./gradlew -p backend clean build -x test

# -----

# Use a fresh Oracle JDK image for runtime
FROM openjdk:23-jdk-oracle

WORKDIR /app

# Locate the one Spring-Boot “fat” JAR (exclude any *plain.jar) and rename it to app.jar
RUN find backend/build/libs -maxdepth 1 -name '*.jar' ! -name '*plain.jar' -print -quit \
    | xargs -I {} cp {} app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
