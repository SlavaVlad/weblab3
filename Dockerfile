# 1. Билдим Java EE приложение через Gradle
FROM gradle:8.7-jdk21 AS builder

WORKDIR /build
COPY . .
RUN gradle build -x test

# 2. Берем Open Liberty
FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi

# 3. Копируем собранный WAR как приложение по умолчанию
COPY --from=builder /build/build/libs/*.war /config/dropins/ROOT.war

# 4. Healthcheck для проверки доступности
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:9080/ || exit 1

# 5. Открываем нужный порт
EXPOSE 9080
