FROM eclipse-temurin:21

WORKDIR /app

COPY target/ms-auth.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]