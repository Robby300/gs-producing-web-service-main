FROM gradle:7.0-jdk11 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/home/producing-web-service/complete/
WORKDIR $APP_HOME

COPY build.gradle settings.gradle $APP_HOME
COPY gradle $APP_HOME/gradle/
COPY --chown=gradle:gradle . producing-web-service/complete/

USER root
RUN chown -R gradle /home/producing-web-service/

RUN gradle build || return 0
COPY . .
RUN gradle clean build

# actual container
FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=gs-producing-web-service-0.1.0.jar
ENV APP_HOME=/home/producing-web-service/

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}




