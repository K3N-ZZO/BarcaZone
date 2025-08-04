# === build stage ===
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle


COPY build.gradle settings.gradle ./
RUN chmod +x gradlew \
 && ./gradlew clean build -x test --no-daemon


FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

# uruchom
ENTRYPOINT ["java","-jar","app.jar"]
