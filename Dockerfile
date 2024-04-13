#FROM maven:3.8.7-openjdk-18-slim AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src/ /app/src/
#RUN mvn package -DskipTests
#
#FROM openjdk:17-alpine
#RUN apk --no-cache add curl
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
##EXPOSE 5000
#ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]