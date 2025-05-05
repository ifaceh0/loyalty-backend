# Use official Java 17 runtime image
#FROM eclipse-temurin:17-jdk

# Set the working directory in the container
#WORKDIR /app

# Copy the JAR file into the container
#COPY target/Loyalty_Program-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app listens on
#EXPOSE 8080

# Command to run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]


# Stage 1: Build the JAR file
FROM maven:3.8.4-openjdk-17-slim AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the src directory to the container
COPY pom.xml /app/
COPY src /app/src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with the JAR
FROM openjdk:17-jdk-slim

# Set the working directory in the final image
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/Loyalty_Program-0.0.1-SNAPSHOT.jar /app/Loyalty_Program-0.0.1-SNAPSHOT.jar

# Expose the port your Spring Boot app will use
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/Loyalty_Program-0.0.1-SNAPSHOT.jar"]

