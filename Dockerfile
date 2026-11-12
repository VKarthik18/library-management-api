
FROM maven:3.9.9-eclipse-temurin-22 AS build

WORKDIR /app

COPY . .

RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:22-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
