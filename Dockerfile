# Étape 1 : Build du projet avec Gradle
FROM gradle:9.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# Étape 2 : Image finale légère
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]