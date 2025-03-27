# Usa a imagem base do Gradle com JDK 17
FROM gradle:8.2-jdk17-alpine AS builder

# Define o diretório de trabalho para o projeto
WORKDIR /app

# Copia o arquivo build.gradle, settings.gradle e o diretório src
COPY build.gradle settings.gradle ./
COPY src ./src/

# Compila o projeto usando o Gradle
RUN gradle clean build --no-daemon --stacktrace

# Lista os arquivos gerados para verificar o nome do JAR
RUN ls -l /app/build/libs/

# Usa a imagem base do OpenJDK 17 para a execução do projeto
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Copia o JAR gerado pelo Gradle da imagem builder
COPY --from=builder /app/build/libs/application-0.0.1-SNAPSHOT.jar ./backend.jar

# Expõe a porta onde a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "backend.jar"]