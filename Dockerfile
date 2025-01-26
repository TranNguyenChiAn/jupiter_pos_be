FROM maven:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /build/libs/demo-1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "jupiter_store.jar"]