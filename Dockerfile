# Etapa 1: construir o JAR usando uma imagem com Java e Maven
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: usar uma imagem Java mais leve apenas para rodar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/emprestimodelivro-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
