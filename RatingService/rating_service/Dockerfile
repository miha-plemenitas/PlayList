# Use a Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /work/

# Copy the Quarkus runner files
COPY target/quarkus-app/lib/ /work/lib/
COPY target/quarkus-app/app/ /work/app/
COPY target/quarkus-app/quarkus/ /work/quarkus/
COPY target/quarkus-app/quarkus-run.jar /work/

# Expose port (your service listens on 8081)
EXPOSE 8081

# Correct command: run the correct JAR
CMD ["java", "-jar", "quarkus-run.jar"]
