# 1. Сборка WAR через Gradle
FROM gradle:8.7-jdk21 AS builder

WORKDIR /build
COPY . .
RUN gradle build -x test

# 2. Лёгкий Open Liberty с поддержкой Java 21
FROM openliberty/open-liberty:kernel-slim-java21-openj9

# 3. Деплой приложения
COPY --from=builder /build/build/libs/*.war /config/dropins/ROOT.war

# 4. Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:9080/ || exit 1

EXPOSE 9080
