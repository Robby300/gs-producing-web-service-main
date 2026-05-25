FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app
COPY gradlew gradlew.bat ./
COPY gradle gradle
COPY settings.gradle ./
COPY employee employee
RUN ./gradlew :employee:build -x test

FROM eclipse-temurin:25-jre-alpine
RUN mkdir -p /app/logs && \
    addgroup -S app && \
    adduser -S app -G app
COPY --from=builder /app/employee/build/libs/employee-0.0.1.jar /app/app.jar
EXPOSE 8081
USER app
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
