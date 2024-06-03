FROM openjdk:17-alpine
VOLUME /tmp
EXPOSE 8000
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar
LABEL authors="sjsk3232"
ENTRYPOINT ["java", "-jar", "/app.jar"]