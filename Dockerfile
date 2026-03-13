# Use Amazon Corretto 21 as base image
FROM amazoncorretto:21

# Set working directory inside the container
WORKDIR /app

# Copy the built jar from target directory
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Optional: health check for Render
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the jar
ENTRYPOINT ["java", "-jardocker build -t demo-app .", "app.jar"]