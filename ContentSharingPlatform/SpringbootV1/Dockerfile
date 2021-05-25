FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} SpringbootV1-0.0.1-SNAPSHOT.jar.jar
ENTRYPOINT ["java","-jar","/SpringbootV1-0.0.1-SNAPSHOT.jar.jar"]