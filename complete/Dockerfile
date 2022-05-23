FROM eclipse-temurin:11-jdk-alpine AS build
COPY src /home/producing-web-service/src
USER root

FROM openjdk:11-ea-11-jdk-slim
COPY --from=build /home/producing-web-service/target/producing-web-service-0.0.1-SNAPSHOT.jar /usr/local/lib/producing-web-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/producing-web-service-0.0.1-SNAPSHOT.jar"]
