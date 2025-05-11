# Use the official OpenJDK image from Docker Hub
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY build/libs/simplapi-0.0.1.jar app.jar

# Expose port 8080 (default for Ktor)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]