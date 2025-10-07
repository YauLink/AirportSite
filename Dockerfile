FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.war app.war
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.war"]