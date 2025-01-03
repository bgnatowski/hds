# Etap 1: Budowanie aplikacji
FROM maven:3.8.3-openjdk-17-slim as build

WORKDIR /app

COPY ./src ./src
COPY /pom.xml .

RUN mvn clean package -DskipTests

# Etap 2: Uruchamianie aplikacji
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/proxy-api-1.0.jar ./proxy-api.jar

CMD ["java", "-jar", "proxy-api.jar"]
