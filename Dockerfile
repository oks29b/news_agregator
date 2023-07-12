FROM eclipse-temurin:11-jdk-alpine
COPY build/libs/news_agregator-0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
