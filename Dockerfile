FROM maven:3.9.1-eclipse-temurin-17 AS build
COPY core/src /app/core/src
COPY model/src /app/model/src
COPY /pom.xml /app
COPY /core/pom.xml /app/core
COPY /model/pom.xml /app/model
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
COPY --from=build /app/core/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
