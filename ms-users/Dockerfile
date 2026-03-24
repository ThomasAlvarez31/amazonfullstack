FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Usamos un asterisco para que encuentre el jar sin importar la versión exacta
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]