# Use official Java 17 runtime image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/Loyalty_Program-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app listens on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
