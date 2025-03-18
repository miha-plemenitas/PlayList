# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code into the container
COPY pom.xml ./
COPY src ./src

# Copy the Maven wrapper files (.mvn and mvnw)
COPY mvnw ./
COPY mvnw.cmd ./
COPY .mvn ./.mvn

# Ensure the mvnw file is executable
RUN chmod +x mvnw

# Build the project using the Maven wrapper (skip tests)
RUN ./mvnw clean install -DskipTests

# Expose the port the app will run on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "target/user-service-0.0.1-SNAPSHOT.jar"]
