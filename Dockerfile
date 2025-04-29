# 1. Сборка WAR через Gradle
FROM gradle:8.7-jdk21 AS builder

WORKDIR /build
COPY . .
RUN gradle build -x test

# 2. Лёгкий Open Liberty с поддержкой Java 21
FROM quay.io/wildfly/wildfly:latest

HEALTHCHECK CMD curl --fail http://localhost:8080/

# 3. Деплой приложения
COPY --from=builder /build/build/libs/*.war /opt/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080
