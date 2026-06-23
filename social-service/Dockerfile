FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

RUN addgroup -S authgroup && adduser -S authuser -G authgroup
USER authuser

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]