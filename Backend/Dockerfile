# Stage 1: Build the JAR with Maven
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
# Copy pom first → better caching
COPY pom.xml .
# Download dependencies (cache layer)
RUN mvn dependency:go-offline -B
# Copy source code
COPY src ./src
# Build (skip tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Lightweight runtime image
FROM openjdk:26-ea-30-jdk-oraclelinux9
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# (use *.jar to match whatever Maven names it, e.g. todoList-0.0.1-SNAPSHOT.jar)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]