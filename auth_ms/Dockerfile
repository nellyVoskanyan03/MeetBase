# Stage 1: Build the JAR
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy pom and fetch dependencies (improves build speed via caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final small image
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENV SPRING_PROFILES_ACTIVE=stand

ENTRYPOINT ["java", "-jar", "app.jar"]