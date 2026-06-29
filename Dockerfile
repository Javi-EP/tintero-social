FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY user-service/pom.xml user-service/pom.xml
COPY book-service/pom.xml book-service/pom.xml
COPY review-service/pom.xml review-service/pom.xml
COPY reading-list-service/pom.xml reading-list-service/pom.xml
COPY social-service/pom.xml social-service/pom.xml
COPY recommendation-service/pom.xml recommendation-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY search-service/pom.xml search-service/pom.xml
COPY stats-service/pom.xml stats-service/pom.xml
COPY audit-service/pom.xml audit-service/pom.xml
COPY gateway-service/pom.xml gateway-service/pom.xml

RUN mvn dependency:resolve -B 2>/dev/null || true

COPY . .
RUN mvn clean install -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /build/user-service/target/user-service-*.jar user-service.jar
COPY --from=builder /build/book-service/target/book-service-*.jar book-service.jar
COPY --from=builder /build/review-service/target/review-service-*.jar review-service.jar
COPY --from=builder /build/reading-list-service/target/reading-list-service-*.jar reading-list-service.jar
COPY --from=builder /build/social-service/target/social-service-*.jar social-service.jar
COPY --from=builder /build/recommendation-service/target/recommendation-service-*.jar recommendation-service.jar
COPY --from=builder /build/notification-service/target/notification-service-*.jar notification-service.jar
COPY --from=builder /build/search-service/target/search-service-*.jar search-service.jar
COPY --from=builder /build/stats-service/target/stats-service-*.jar stats-service.jar
COPY --from=builder /build/audit-service/target/audit-service-*.jar audit-service.jar
COPY --from=builder /build/gateway-service/target/gateway-service-*.jar gateway-service.jar

COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["docker-entrypoint.sh"]
