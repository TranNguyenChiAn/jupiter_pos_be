FROM maven:3-amazoncorretto-17 AS build

# Copy Maven files
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# -------- RUN STAGE --------
FROM amazoncorretto:17-jdk
WORKDIR /app

COPY --from=build /target/jupiter_pos_be-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]