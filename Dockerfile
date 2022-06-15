FROM gradle:latest AS GRADLE_BUILD
COPY ./ /usr/local/app
WORKDIR /usr/local/app
RUN sh gradlew clean build

FROM openjdk:11-jre-slim
RUN mkdir -p /app/logs && \
    chgrp -R 0 /app/logs && \
    chmod -R g=u /app/logs

COPY --from=GRADLE_BUILD /usr/local/app/build/libs/gs-producing-web-service-0.1.0.jar /app

EXPOSE 8080

WORKDIR /app/

CMD ["java","-jar","/app/gs-producing-web-service-0.1.0.jar"]

