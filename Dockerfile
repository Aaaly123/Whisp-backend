# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port (Render will assign a port via PORT environment variable)
EXPOSE 8080

# Run the Spring Boot JAR
CMD java -jar target/demo-0.0.1-SNAPSHOT.jar
