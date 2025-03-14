FROM eclipse-temurin:21-jdk AS build

# Set work directory inside the container
WORKDIR /app

# Copy project files
COPY ../Coach/.mvn .mvn
COPY ../Coach/mvnw pom.xml ./
COPY ../Coach/src ./src

# Grant execute permissions to Maven Wrapper
RUN chmod +x mvnw

# Build the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# ====== Step 2: Run Stage ======
FROM eclipse-temurin:21-jre

# Set work directory
WORKDIR /app

# Copy the built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

# Run as a non-root user for security
RUN useradd -m spring
USER spring

# Expose application port (default Spring Boot port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
