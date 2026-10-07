FROM maven:3.9-eclipse-temurin-21-alpine
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Executa diretamente o comando do Spring Boot para desenvolvimento
CMD ["mvn", "spring-boot:run"]
