FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY build/libs/*.war app.war
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.war"]