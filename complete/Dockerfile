FROM maven:3.8.4-jdk-8-slim AS build
COPY src /home/jNotes/src
COPY pom.xml /home/jNotes/
USER root
RUN --mount=type=cache,target=/root/.m2 mvn -DskipTests=true -f /home/jNotes/pom.xml clean package

FROM openjdk:8-jre-alpine
COPY --from=build /home/jNotes/target/jNotes-0.0.1-SNAPSHOT.jar /usr/local/lib/jNotes-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/jNotes-0.0.1-SNAPSHOT.jar"]
