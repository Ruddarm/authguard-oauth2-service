FROM maven:3.9.4-eclipse-temurin-21-alpine

WORKDIR /oauth2service
COPY .mvn/  .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src  ./src

CMD ["./mvnw", "spring-boot:run"]

