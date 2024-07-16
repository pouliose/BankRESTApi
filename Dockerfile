# syntax=docker/dockerfile:1
FROM maven:3.9.6-amazoncorretto-21-al2023

# Copy the pom.xml file first and run mvn dependency:go-offline
# This helps with Docker caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy the rest of the project
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/fintech-0.0.1-SNAPSHOT.jar"]
