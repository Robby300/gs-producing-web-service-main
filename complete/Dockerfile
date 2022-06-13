FROM openjdk:11-jre-slim
RUN mkdir -p /app/logs && \
    chgrp -R 0 /app/logs && \
    chmod -R g=u /app/logs

RUN set -eux; \
	apt-get update; \
	apt-get install -y --no-install-recommends \
		procps curl \
	; \
	rm -rf /var/lib/apt/lists/*


COPY /build/libs/gs-producing-web-service-0.1.0.jar /app

EXPOSE 8080

WORKDIR /app/

CMD ["java","-jar","/app/gs-producing-web-service-0.1.0.jar"]