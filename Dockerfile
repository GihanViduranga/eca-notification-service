FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8083
EXPOSE 8083

ENTRYPOINT ["java", \
  "-Dserver.port=${PORT}", \
  "-Dspring.cloud.config.fail-fast=false", \
  "-Dspring.cloud.config.enabled=false", \
  "-Dspring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}", \
  "-Deureka.client.service-url.defaultZone=${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE}", \
  "-Deureka.instance.prefer-ip-address=true", \
  "-Dspring.config.import=", \
  "-jar", "app.jar"]
