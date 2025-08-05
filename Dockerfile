# === etap budowania ===
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 1. Skopiuj wrapper Gradle i pliki konfiguracyjne
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 2. Skopiuj kod źródłowy
COPY src src

# 3. Ustaw prawa i zbuduj fat‐JAR
RUN chmod +x gradlew \
 && ./gradlew clean bootJar -x test --no-daemon

# === etap uruchomieniowy ===
FROM eclipse-temurin:21-jre
WORKDIR /app

# 4. Pobierz gotowy JAR
COPY --from=builder /app/build/libs/*.jar app.jar

# 5. Port, na którym nasłuchuje Spring (domyślnie 8080)
EXPOSE 8080

# 6. Komenda startowa
ENTRYPOINT ["java","-jar","app.jar"]
