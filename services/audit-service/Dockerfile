# Use a lightweight OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/audit-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your service runs on
EXPOSE 8082

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]